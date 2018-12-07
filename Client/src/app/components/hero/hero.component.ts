import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators, FormControl } from '@angular/forms';
import { Router } from '@angular/router';
import { SocketService } from '../../service/socket.service';
import { Hero } from '../../app.models';

@Component({
	selector: 'hero-component',
	templateUrl: './hero.component.html'
})
export class HeroComponent {

	private heros: Array<Hero>;
	private playerDetailsForm: FormGroup;

	constructor(private socketService: SocketService, private formBuilder: FormBuilder, private router: Router) {
		this.socketService.herosObservable.subscribe((value: Array<Hero>) => {
			this.heros = value;
		});

		this.heros = this.socketService.getHeros();
		if(this.heros.length != 0){
			this.playerDetailsForm = this.formBuilder.group({
				username: ['', Validators.required],
				chosenHero: [this.heros[0].id, Validators.required]
			});
		} else {
			this.heros = new Array<Hero>();
			this.playerDetailsForm = this.formBuilder.group({
				username: ['', Validators.required],
				chosenHero: [1, Validators.required]
			});
			this.socketService.herosObservable.subscribe((value: Array<Hero>) => {
				this.heros = value;

				this.playerDetailsForm = this.formBuilder.group({
					username: ['', Validators.required],
					chosenHero: [this.heros[0].id, Validators.required]
				});
			});
		}
	}

	play(username: string, heroId: number) {
		this.socketService.play(username, heroId);
		this.socketService.setIsRedirect();
		this.router.navigate(['/game']);
	}

	onFormSubmit(value: any){
		this.play(value.username, value.chosenHero);
	}
}