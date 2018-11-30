import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators, FormControl } from '@angular/forms';
import { SocketService } from '../../service/socket.service';
import { Hero } from '../../app.models';

@Component({
	selector: 'hero-component',
	templateUrl: './hero.component.html'
})
export class HeroComponent {

	private heros: Array<Hero>;
	private playerDetailsForm: FormGroup;

	constructor(private socketService: SocketService, private formBuilder: FormBuilder) {
		this.heros = this.socketService.getHeros();
		
		this.playerDetailsForm = this.formBuilder.group({
			username: ['', Validators.required],
			chosenHero: [this.heros[0].id, Validators.required]
		});
		console.log(this.playerDetailsForm);
	}

	play(username: string, heroId: number) {
		this.socketService.play(username, heroId);
	}

	onFormSubmit(value: any){
		this.play(value.username, value.chosenHero);
	}
}