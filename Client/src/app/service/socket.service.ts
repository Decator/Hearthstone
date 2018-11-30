import { Injectable } from '@angular/core';
import * as Stomp from 'stompjs';
import * as SockJS from 'sockjs-client';
import { Player, Game } from '../app.models';
import { Subscription } from 'rxjs/Subscription';

@Injectable()
export class SocketService {
  private serverUrl = 'http://localhost:8080/hearthstone-websocket';
  private stompClient: any;

  private playerSubscription: Subscription;

  private player: Player;
  private game: Game;

  constructor() {
    this.initializeWebSocketConnection();
  }

  initializeWebSocketConnection() {
    let ws = new SockJS(this.serverUrl);
    this.stompClient = Stomp.over(ws);
    this.stompClient.connect({}, (frame) => { });
  }

  play(username: string){
    this.playerSubscription = this.stompClient.subscribe(`/greeting/player/${username}`, (message) => {
      this.player = new Player(JSON.parse(message.body));
      this.playerSubscription.unsubscribe();
      this.createGame();
    });
    this.stompClient.send("/app/createPlayer", {}, username);
  }

  createGame() {
    this.stompClient.subscribe(`/greeting/game/${this.player.uuid}`, (message) => {
      try{
        this.game = new Game(JSON.parse(message.body));
      } catch(err){
        console.log(message.body);
      }
    });
    this.stompClient.send("/app/createGame", {}, this.player.uuid);
  }
}
