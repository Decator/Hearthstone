import { Component } from '@angular/core';
import { SocketService } from '../../service/socket.service';

@Component({
	selector: 'game-component',
	templateUrl: './game.component.html'
})
export class GameComponent {

	constructor(private socketService: SocketService) {}
}