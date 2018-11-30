import { Component } from '@angular/core';
import { SocketService } from './service/socket.service';

@Component({
	selector: 'app-root',
	templateUrl: './app.component.html',
	styleUrls: ['./app.component.css']
})
export class AppComponent {
	private title = 'HearthStone';

	constructor(private socketService: SocketService) {}

	createPlayer(username: string) {
		this.socketService.createPlayer(username);
	}

	createGame(){
		this.socketService.createGame();
	}
}