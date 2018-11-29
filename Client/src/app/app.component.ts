import { Component } from '@angular/core';
import * as Stomp from 'stompjs';
import * as SockJS from 'sockjs-client';
import $ from 'jquery';

@Component({
	selector: 'app-root',
	templateUrl: './app.component.html',
	styleUrls: ['./app.component.css']
})
export class AppComponent {
	private serverUrl = 'http://localhost:8091/hearthstone-websocket';
	private title = 'HearthStone';
	private stompClient;

	constructor() {
		this.initializeWebSocketConnection();
	}

	initializeWebSocketConnection() {
		let ws = new SockJS(this.serverUrl);
		this.stompClient = Stomp.over(ws);
		let that = this;
		this.stompClient.connect({}, function (frame) {
			that.stompClient.subscribe("/topic", (message) => {
				if (message.body) {
					$(".topic").append("<div class='message'>" + message.body + "</div>")
					console.log(message.body);
				}
			});
		});
	}

	sendMessage(message) {
		this.stompClient.send("/app/send/message", {}, message);
		$('#input').val('');
	}
}