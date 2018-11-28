import { Component } from '@angular/core';
import { GameService } from './services/game.service';

@Component({
	selector: 'app-root',
	templateUrl: './app.component.html',
	styleUrls: ['./app.component.css']
})
export class AppComponent {
	title = 'HearthStone';

	constructor(private gameService: GameService) {
		gameService.message.subscribe(msg => {
			console.log("Response from Websocket Server : " + msg);
		});
	}

	private message = {
		author: "maud",
		message: "wesh"
	}

	sendMessage() {
		console.log("New Message Sent From Client");
		this.gameService.message.next(this.message);
	}
}