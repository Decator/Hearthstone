import { Injectable } from '@angular/core';
import * as Stomp from 'stompjs';
import * as SockJS from 'sockjs-client';
import { Observable, Subject, Subscription } from 'rxjs';
import { Player, Game, Hero } from '../app.models';

@Injectable()
/**
 * The service that handles the link between this Angular Client and the server via websockets.
 */
export class SocketService {
  private serverUrl = 'http://localhost:8080/hearthstone-websocket';
  private stompClient: Stomp.CompatClient;

  /**
   * A boolean that is set to true when we get to the Game component from the Hero component.
   * This is used to avoid people going to the game page without them having created a player.
   */
  private isRedirect: boolean = false;

  /**
   * The subscriptions to server websocket channels. 
   */
  private userSubscription: Subscription;
  private playerSubscription: Subscription;
  private gameSubscription: Subscription;

  /**
   * Observables that the components will observe. 
   */
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

  /**
   * Initialize the service's attibutes.
   */
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

  /**
   * Establish the connection between the client and the server via websockets.
   * Once the connection has been established, get the different Heros from the server.
   */
  initializeWebSocketConnection() {
    let ws = new SockJS(this.serverUrl);
    this.stompClient = Stomp.over(ws);
    // Connect to the server's websocket
    this.stompClient.connect({}, (frame) => {
      // Connect to the server's websocket's hero channel
      let heroSubscription = this.stompClient.subscribe(`/heros`, (message) => {
        for (let hero of JSON.parse(message.body)) {
          this.heros.push(new Hero(hero));
        }
        this.herosSubject.next(this.heros);
        // Once we have the Heros, unsubscribe from the hero channel
        heroSubscription.unsubscribe();
      });
      // Ask the server for the Heros
      this.stompClient.send("/app/getHeros");
    });
  }

  /**
   * Create a Player with a username and a Hero. 
   * @param username the chosen username
   * @param idHero the id of the chosen Hero
   */
  play(username: string, idHero: number) {
    if (this.userSubscription) {
      this.userSubscription.unsubscribe();
      this.userSubscription = null;
    }
    // Connect to the server's websocket's user channel
    this.userSubscription = this.stompClient.subscribe(`/player/${username}`, (message) => {
      try {
        // Receive the player
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
    // Ask the server to create a Player
    this.stompClient.send("/app/createPlayer", {}, `${username}_${idHero}`);
  }

  /**
   * Create a Game. 
   */
  createGame() {
    if (this.playerSubscription) {
      this.playerSubscription.unsubscribe();
      this.playerSubscription = null;
    }
    // Connect to the server's websocket's player channel
    this.playerSubscription = this.stompClient.subscribe(`/game/${this.player.uuid}`, (message) => {
      try {
        // Receive a Game
        this.game = new Game(JSON.parse(message.body));
        this.gameSubject.next(this.game);
        if(this.gameSubscription){
          this.gameSubscription.unsubscribe();
          this.gameSubscription = null;
        }
        // Connect to the server's websocket's game channel
        this.gameSubscription = this.stompClient.subscribe(`/game/${this.game.idGame}`, (message) => {
          try {
            // Receive the Game
            this.game = new Game(JSON.parse(message.body));
            this.gameSubject.next(this.game);
            this.errorSubject.next("");
          } catch (err) {
            // Receive error messages from the server
            this.error = message.body;
            this.errorSubject.next(this.error);
          }
        });
      } catch (err) {
        // Receive error messages from the server
        this.error = message.body;
        this.errorSubject.next(this.error);
      }
    });
    // Ask the server to join a Game
    this.stompClient.send("/app/createGame", {}, this.player.uuid);
  }

  /**
   * Play a card.
   * @param uuidGame the game's id
   * @param uuidPlayer the id of the player who plays the card
   * @param idCard the id of the played card
   * @param uuidTargetPlayer the id of the player to whom the targeted card belongs
   * @param idTarget the id of the targeted card
   */
  playCard(uuidGame: String, uuidPlayer: string, idCard: number, uuidTargetPlayer: string, idTarget: number) {
    this.stompClient.send("/app/playCard", {}, `${uuidGame}_${uuidPlayer}_${idCard}_${uuidTargetPlayer}_${idTarget}`);
  }

  /**
   * Attack a card. 
   * @param uuidGame the game's id
   * @param uuidPlayer the id of the attacking player
   * @param idCard the id of the attacking card
   * @param uuidTarget this id of the targeted player
   * @param idTarget the id of the targeted card
   */
  attack(uuidGame: string, uuidPlayer: string, idCard: number, uuidTarget: string, idTarget: number) {
    this.stompClient.send("/app/attack", {}, `${uuidGame}_${uuidPlayer}_${idCard}_${uuidTarget}_${idTarget}`);
  }

  /**
   * End the turn. 
   * @param uuidGame the game's id
   * @param uuidPlayer the id of the player ending the turn
   */
  endTurn(uuidGame: String, uuidPlayer: string) {
    this.stompClient.send("/app/endTurn", {}, `${uuidGame}_${uuidPlayer}`);
  }

  /**
   * Player a hero's power. 
   * @param uuidGame the game's id
   * @param uuidPlayer the id of the player playing the power
   * @param uuidTargetPlayer the id of the targeted player
   * @param idTarget the id of the targeted card
   */
  heroPower(uuidGame: String, uuidPlayer: string, uuidTargetPlayer: string, idTarget: number) {
    this.stompClient.send("/app/heroPower", {}, `${uuidGame}_${uuidPlayer}_${uuidTargetPlayer}_${idTarget}`);
  }

  /**
   * When the Game has ended, reinitialize the service's attributes. 
   */
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
