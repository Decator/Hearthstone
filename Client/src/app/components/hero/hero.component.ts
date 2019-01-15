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
		// Try getting the Heros
		this.heros = this.socketService.getHeros();
		if(this.heros.length != 0){
			this.chosenHero = this.heros[0].id;
		} else {
			// If there are no heros
			this.heros = new Array<Hero>();
			this.chosenHero = 1;
			// Listen to the hero observable until we have the Heros
			this.socketService.herosObservable.subscribe((value: Array<Hero>) => {
				this.heros = value;
				this.chosenHero = this.heros[0].id;
			});
		}
	}

	/**
	 * Start playing, redirect to the game component. 
	 * @param username the chosen username
	 * @param heroId the id of the chosen Hero
	 */
	play(username: string, heroId: number) {
		this.socketService.play(username, heroId);
		this.socketService.setIsRedirect();
		this.router.navigate(['/game']);
	}

	/**
	 * Submit the form.
	 */
	submit(){
		this.play(this.username, this.chosenHero);
	}

	/**
	 * Choose a Hero
	 * @param id the id of the chosen Hero
	 */
	chooseHero(id){
		this.chosenHero = id;
	}
}