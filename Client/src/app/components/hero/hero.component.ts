import { Component } from '@angular/core';
import { SocketService } from '../../service/socket.service';

@Component({
	selector: 'hero-component',
	templateUrl: './hero.component.html'
})
export class HeroComponent {

	constructor(private socketService: SocketService) {}

	play(username: string) {
		this.socketService.play(username);
	}
}