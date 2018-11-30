import { Injectable } from '@angular/core';
import * as Stomp from 'stompjs';
import * as SockJS from 'sockjs-client';
import { Subscription } from 'rxjs/Subscription';
import { Player, Game, Hero } from '../app.models';

@Injectable()
export class SocketService {
  private serverUrl = 'http://localhost:8080/hearthstone-websocket';
  private stompClient: any;

  private playerSubscription: Subscription;

  private player: Player;
  private game: Game;
  private heros: Array<Hero>;

  constructor() {
    this.heros = new Array<Hero>();
    this.initializeWebSocketConnection();
  }

  initializeWebSocketConnection() {
    let ws = new SockJS(this.serverUrl);
    this.stompClient = Stomp.over(ws);
    this.stompClient.connect({}, (frame) => {
      let heroSubscription = this.stompClient.subscribe(`/heros`, (message) => {
        for(let hero of JSON.parse(message.body)){
          this.heros.push(new Hero(hero));
        }
        heroSubscription.unsubscribe();
      });
      this.stompClient.send("/app/getHeros");
    });
  }

  play(username: string, idHero: number){
    this.playerSubscription = this.stompClient.subscribe(`/player/${username}`, (message) => {
      this.player = new Player(JSON.parse(message.body));
      this.playerSubscription.unsubscribe();
      this.createGame();
    });
    this.stompClient.send("/app/createPlayer", {}, `${username}_${idHero}`);
  }

  createGame() {
    this.stompClient.subscribe(`/game/${this.player.uuid}`, (message) => {
      try{
        this.game = new Game(JSON.parse(message.body));
      } catch(err){
        console.log(message.body);
      }
    });
    this.stompClient.send("/app/createGame", {}, this.player.uuid);
  }

  getPlayer(): Player {
    return this.player;
  }

  getGame(): Game {
    return this.game;
  }

  getHeros(): Array<Hero> {
    return this.heros;
  }
}
