import { Component } from '@angular/core';
import * as Stomp from 'stompjs';
import * as SockJS from 'sockjs-client';
import $ from 'jquery';
import { Player, Game } from './app.models';

@Component({
	selector: 'app-root',
	templateUrl: './app.component.html',
	styleUrls: ['./app.component.css']
})
export class AppComponent {
	private serverUrl = 'http://localhost:8080/hearthstone-websocket';
	private title = 'HearthStone';
	private stompClient: any;

	private player: Player;
	private game: Game;

	private playerSubscription: any;

	constructor() {
		this.initializeWebSocketConnection();
	}

	initializeWebSocketConnection() {
		let ws = new SockJS(this.serverUrl);
		this.stompClient = Stomp.over(ws);
		this.stompClient.connect({}, (frame) => {});
	}

	createPlayer(userName: string) {
		this.stompClient.send("/app/createPlayer", {}, userName);
		this.playerSubscription = this.stompClient.subscribe(`/greeting/player/${userName}`, (message) => {
			this.player = new Player(JSON.parse(message.body));
			this.playerSubscription.unsubscribe();
		});
	}

	createGame(){
		this.stompClient.send("/app/createGame", {}, this.player.uuid);
		this.stompClient.subscribe(`/greeting/game/${this.player.uuid}`, (message) => {
			console.log(JSON.parse(message.body));
			this.game = new Game(JSON.parse(message.body));
			console.log(this.game);
		});
	}
}