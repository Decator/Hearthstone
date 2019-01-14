import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators, FormControl } from '@angular/forms';
import { Router } from '@angular/router';
import { SocketService } from '../../service/socket.service';
import { Hero } from '../../app.models';

@Component({
	selector: 'hero-component',
	templateUrl: './hero.component.html',
	styleUrls: ['./hero.component.css']
})
export class HeroComponent {

	private heros: Array<Hero>;
	private playerDetailsForm: FormGroup;
	private username = "";
	private chosenHero = 1;

	constructor(private socketService: SocketService, private formBuilder: FormBuilder, private router: Router) {
		this.socketService.herosObservable.subscribe((value: Array<Hero>) => {
			this.heros = value;
		});

		this.heros = this.socketService.getHeros();
		if(this.heros.length != 0){
			this.chosenHero = this.heros[0].id;
		} else {
			this.heros = new Array<Hero>();
			this.chosenHero = 1;
			this.socketService.herosObservable.subscribe((value: Array<Hero>) => {
				this.heros = value;
				this.chosenHero = this.heros[0].id;
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

	submit(){
		this.play(this.username, this.chosenHero);
	}

	chooseHero(id){
		this.chosenHero = id;
	}
}