import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

@Component({
	selector: 'end-component',
	templateUrl: './end.component.html',
	styleUrls: ['./end.component.css']
})
export class EndComponent {

	private result = "other";
	private sub: any;

	constructor(private route: ActivatedRoute) {

	}

	ngOnInit() {
		this.sub = this.route.params.subscribe(params => {
			this.result = params['result'];
		});
	}

	ngOnDestroy() {
		this.sub.unsubscribe();
	}
}
