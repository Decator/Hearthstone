import { Component } from '@angular/core';
import { SocketService } from '../../service/socket.service';

@Component({
	selector: 'start-component',
	templateUrl: './start.component.html',
	styleUrls: ['./start.component.css']
})
export class StartComponent {

	constructor(private socketService: SocketService) {}
}