import { Component } from '@angular/core';
import { Router, NavigationStart } from '@angular/router';
import { SocketService } from '../../service/socket.service';
import { Game, Player, Minion, Spell } from '../../app.models';
import 'rxjs/add/operator/pairwise';
import 'rxjs/add/operator/filter';

@Component({
	selector: 'game-component',
	templateUrl: './game.component.html',
	styleUrls: ['./game.component.css']
})
export class GameComponent {

	private game: Game;
	private player: Player;
	private otherPlayer: Player;
	private yourTurn: boolean = false;
	private error: String;

	private showLoader: boolean = true;

	constructor(private socketService: SocketService, private router: Router) {
		this.socketService.gameObservable.subscribe((value: Game) => {
			this.game = value;

			if(this.game.currentPlayer.uuid == this.socketService.getPlayer().uuid){
				this.player = this.game.currentPlayer;
				this.otherPlayer = this.game.otherPlayer;
				this.yourTurn = true;
			} else {
				this.player = this.game.otherPlayer;
				this.otherPlayer = this.game.currentPlayer;
				this.yourTurn = false;
			}

			if(this.showLoader){
				this.showLoader = false;
			}
		});

		this.socketService.errorObservable.subscribe((value: String) => {
			this.error = value;
			console.log(this.error);
		});

		this.game = this.socketService.getGame();
		this.error = this.socketService.getError();

		if(this.game && this.showLoader){
			this.showLoader = false;
		}
	}

	playCard(idCard: number){
		this.socketService.playCard(this.game.idGame, idCard, this.player.uuid, 1);
	}

	endTurn() {
		this.socketService.endTurn(this.game.idGame);
	}

	heroPower() {
		if(this.player.hero.type=="mage") {
			this.socketService.heroPower(this.game.idGame, this.otherPlayer.uuid, 1);
		} else {
			this.socketService.heroPower(this.game.idGame, this.otherPlayer.uuid, 1);
		}
	}

	isMinion(card): boolean {
		return (card as Minion).taunt != undefined;
	}

	isSpell(card): boolean {
		return (card as Spell).polymorph != undefined;
	}
}