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
	private showHand: boolean = false;
	private showAllyMinions: boolean = false;
	private showEnemyMinions: boolean = false;
	private showAllyHero: boolean = false;
	private showEnemyHero: boolean = false;

	private waitingForTarget: boolean = false;
	private idWaitingCard: number;

	constructor(private socketService: SocketService, private router: Router) {
		if(this.socketService.getIsRedirect()){
			this.socketService.gameObservable.subscribe((value: Game) => {
				this.game = value;
	
				if(this.game.currentPlayer.uuid == this.socketService.getPlayer().uuid){
					this.player = this.game.currentPlayer;
					this.otherPlayer = this.game.otherPlayer;

					this.yourTurn = true;
					this.showHand = true;
					this.showAllyMinions = true;
					this.showEnemyMinions = false;
					this.showAllyHero = true;
					this.showEnemyHero = false;
					this.waitingForTarget = false;
				} else {
					this.player = this.game.otherPlayer;
					this.otherPlayer = this.game.currentPlayer;

					this.yourTurn = false;
					this.showHand = false;
					this.showAllyMinions = false;
					this.showEnemyMinions = false;
					this.showAllyHero = false;
					this.showEnemyHero = false;
					this.waitingForTarget = false;
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
		} else {
			this.router.navigate(['/hero']);
		}
	}

	playCard(idCard: number, uuidTarget: string, idTarget?: number){
		console.log(uuidTarget);
		if(idTarget){
			this.socketService.playCard(this.game.idGame, idCard, uuidTarget, idTarget);
		} else {
			this.socketService.playCard(this.game.idGame, idCard, uuidTarget, 0);
		}
	}

	onClickHand(idCard: number){
		let card = this.player.hand[idCard];

		if(this.isSpell(card) && (card as Spell).target != undefined && (card as Spell).target.includes("1")){
			let spell = (card as Spell);
			let splitTarget = spell.target.split('_');

			if(splitTarget[0] == "minion"){
				if(splitTarget[2] == "ally"){
					this.showHand = false;
					this.showAllyMinions = true;
					this.showEnemyMinions = false;
					this.showAllyHero = false;
					this.showEnemyHero = false;
					this.waitingForTarget = true;
					this.idWaitingCard = idCard;
				} else if(splitTarget[2] == "enemy"){
					this.showHand = false;
					this.showAllyMinions = false;
					this.showEnemyMinions = true;
					this.showAllyHero = false;
					this.showEnemyHero = false;
					this.waitingForTarget = true;
					this.idWaitingCard = idCard;
				}
			} else {

			}
		} else {
			console.log(this.player.uuid);
			this.playCard(idCard, this.player.uuid);
		}
	}

	onClickMinion(uuidTarget: string, idCard: number){
		if(this.waitingForTarget){
			console.log(uuidTarget);
			console.log(idCard);
			this.playCard(this.idWaitingCard, uuidTarget, idCard);
		} else {
			
		}
	}

	onClickHero(){
		if(this.waitingForTarget){

		}
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