import { Component } from '@angular/core';
import { SocketService } from '../../service/socket.service';

@Component({
	selector: 'end-component',
	templateUrl: './end.component.html',
	styleUrls: ['./end.component.css']
})
export class EndComponent {

	constructor(private socketService: SocketService) {}
}
