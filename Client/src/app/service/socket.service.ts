import { Injectable } from '@angular/core';
import * as Stomp from 'stompjs';
import * as SockJS from 'sockjs-client';
import { Observable, Subject, Subscription } from 'rxjs';
import { Player, Game, Hero } from '../app.models';

@Injectable()
export class SocketService {
  private serverUrl = 'http://localhost:8080/hearthstone-websocket';
  private stompClient: Stomp.CompatClient;

  private isRedirect: boolean = false;

  private userSubscription: Subscription;
  private playerSubscription: Subscription;
  private gameSubscription: Subscription;

  private player: Player;
  private playerSubject: Subject<Player>;
  playerObservable: Observable<Player>;

  private game: Game;
  private gameSubject: Subject<Game>;
  gameObservable: Observable<Game>;

  private heros: Array<Hero>;
  private herosSubject: Subject<Array<Hero>>;
  herosObservable: Observable<Array<Hero>>;

  private error: string;
  private errorSubject: Subject<string>;
  errorObservable: Observable<string>;

  constructor() {
    this.heros = new Array<Hero>();
    this.herosSubject = new Subject<Array<Hero>>();
    this.herosObservable = this.herosSubject.asObservable();

    this.initService();
    this.initializeWebSocketConnection();
  }

  initService() {
    this.player = null;
    this.playerSubject = new Subject<Player>();
    this.playerObservable = this.playerSubject.asObservable();

    this.game = null;
    this.gameSubject = new Subject<Game>();
    this.gameObservable = this.gameSubject.asObservable();

    this.error = "";
    this.errorSubject = new Subject<string>();
    this.errorObservable = this.errorSubject.asObservable();

    if(this.userSubscription){
      this.userSubscription.unsubscribe();
      this.userSubscription = null;
    }

    if(this.playerSubscription){
      this.playerSubscription.unsubscribe();
      this.playerSubscription = null;
    }

    if(this.gameSubscription) {
      this.gameSubscription.unsubscribe();
      this.gameSubscription = null;
    }
  }

  initializeWebSocketConnection() {
    let ws = new SockJS(this.serverUrl);
    this.stompClient = Stomp.over(ws);
    this.stompClient.connect({}, (frame) => {
      let heroSubscription = this.stompClient.subscribe(`/heros`, (message) => {
        for (let hero of JSON.parse(message.body)) {
          this.heros.push(new Hero(hero));
        }
        this.herosSubject.next(this.heros);
        heroSubscription.unsubscribe();
      });
      this.stompClient.send("/app/getHeros");
    });
  }

  play(username: string, idHero: number) {
    if (this.userSubscription) {
      this.userSubscription.unsubscribe();
      this.userSubscription = null;
    }
    this.userSubscription = this.stompClient.subscribe(`/player/${username}`, (message) => {
      try {
        this.player = new Player(JSON.parse(message.body));
        this.playerSubject.next(this.player);
        this.createGame();
        this.userSubscription.unsubscribe();
        this.userSubscription = null;
      } catch (err) {
        this.error = message.body;
        this.errorSubject.next(this.error);
      }
    });
    this.stompClient.send("/app/createPlayer", {}, `${username}_${idHero}`);
  }

  createGame() {
    if (this.playerSubscription) {
      this.playerSubscription.unsubscribe();
      this.playerSubscription = null;
    }
    this.playerSubscription = this.stompClient.subscribe(`/game/${this.player.uuid}`, (message) => {
      try {
        this.game = new Game(JSON.parse(message.body));
        this.gameSubject.next(this.game);
        if(this.gameSubscription){
          this.gameSubscription.unsubscribe();
          this.gameSubscription = null;
        }
        this.gameSubscription = this.stompClient.subscribe(`/game/${this.game.idGame}`, (message) => {
          try {
            this.game = new Game(JSON.parse(message.body));
            this.gameSubject.next(this.game);
            this.errorSubject.next("");
          } catch (err) {
            this.error = message.body;
            this.errorSubject.next(this.error);
          }
        });
      } catch (err) {
        this.error = message.body;
        this.errorSubject.next(this.error);
      }
    });
    this.stompClient.send("/app/createGame", {}, this.player.uuid);
  }

  playCard(uuidGame: String, uuidPlayer: string, idCard: number, uuidTargetPlayer: string, idTarget: number) {
    this.stompClient.send("/app/playCard", {}, `${uuidGame}_${uuidPlayer}_${idCard}_${uuidTargetPlayer}_${idTarget}`);
  }

  attack(uuidGame: string, uuidPlayer: string, idCard: number, idTarget: number) {
    this.stompClient.send("/app/attack", {}, `${uuidGame}_${uuidPlayer}_${idCard}_${idTarget}`);
  }

  endTurn(uuidGame: String, uuidPlayer: string) {
    this.stompClient.send("/app/endTurn", {}, `${uuidGame}_${uuidPlayer}`);
  }

  heroPower(uuidGame: String, uuidPlayer: string, uuidTargetPlayer: string, idTarget: number) {
    this.stompClient.send("/app/heroPower", {}, `${uuidGame}_${uuidPlayer}_${uuidTargetPlayer}_${idTarget}`);
  }

  endGame() {
    this.initService();
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

  getError(): string {
    return this.error;
  }

  getIsRedirect(): boolean {
    return this.isRedirect;
  }

  setIsRedirect() {
    this.isRedirect = true;
  }
}
