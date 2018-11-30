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
	private stompClient;

	private player: Player;
	private game: Game;

	constructor() {
		this.initializeWebSocketConnection();
	}

	initializeWebSocketConnection() {
		let ws = new SockJS(this.serverUrl);
		this.stompClient = Stomp.over(ws);
		this.stompClient.connect({}, (frame) => {
			this.stompClient.subscribe("/greeting/player", (message) => {
				this.player = new Player(JSON.parse(message.body));
				console.log(this.player);
			});
			this.stompClient.subscribe("/greeting/game", (message) => {
				console.log(JSON.parse(message.body));
				this.game = new Game(JSON.parse(message.body));
				console.log(this.game);
			});
		});
	}

	createPlayer(message) {
		this.stompClient.send("/app/createPlayer");
	}

	createGame(){
		this.stompClient.send("/app/createGame", {}, this.player.uuid);
	}
}