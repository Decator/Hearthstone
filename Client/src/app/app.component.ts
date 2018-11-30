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
		let that = this;
		this.stompClient.connect({}, function (frame) {
			that.stompClient.subscribe("/greeting", (message) => {
				console.log(JSON.parse(message.body));
				this.player = new Player(JSON.parse(message.body));
				console.log(this.player);
			});
			that.stompClient.subscribe("/game", (message) => {
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