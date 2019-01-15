import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { SocketService } from '../../service/socket.service';

@Component({
	selector: 'end-component',
	templateUrl: './end.component.html',
	styleUrls: ['./end.component.css']
})
export class EndComponent {

	private result = "other";
	private sub: any;

	constructor(private route: ActivatedRoute, private socketService: SocketService, private router: Router) {
		// Redirect to the hero component when not allowed
		if (!this.socketService.getIsRedirect()) {
			this.router.navigate(['/hero']);
		}
	}

	ngOnInit() {
		this.route.params.subscribe(params => {
			this.result = params['result'];
		});
	}
}
