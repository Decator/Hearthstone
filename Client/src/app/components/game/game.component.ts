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
	private gameOver: boolean;
	private yourTurn: boolean = false;
	private error: String;

	private showLoader: boolean = true;
	private showHand: boolean = false;
	private showAllyMinions: boolean = false;
	private showEnemyMinions: boolean = false;
	private showAllyHero: boolean = false;
	private showEnemyHero: boolean = false;
	private showAllyHeroPower: boolean = false;
	private showOnlyTaunt: boolean = false;

	private waitingForPlayCardTarget: boolean = false;
	private waitingForAttackTarget: boolean = false;
	private waitingForHeroPowerTarget: boolean = false;
	private idWaitingCard: number;

	constructor(private socketService: SocketService, private router: Router) {
		if (this.socketService.getIsRedirect()) {
			this.socketService.gameObservable.subscribe((value: Game) => {
				this.game = value;

				this.resetBooleans();

				if (this.showLoader) {
					this.showLoader = false;
				}
			});

			this.socketService.errorObservable.subscribe((value: String) => {
				this.error = value;
				console.log(this.error);

				if(this.game){
					console.log("error if");
					this.resetBooleans();
				}
			});

			this.game = this.socketService.getGame();
			this.error = this.socketService.getError();

			if (this.game && this.showLoader) {
				this.showLoader = false;
			}
		} else {
			this.router.navigate(['/hero']);
		}
	}

	playCard(idCard: number, uuidTarget: string, idTarget?: number) {
		if (idTarget) {
			this.socketService.playCard(this.game.idGame, idCard, uuidTarget, idTarget);
		} else {
			this.socketService.playCard(this.game.idGame, idCard, uuidTarget, 0);
		}
	}

	attack(idCard: number, idTarget: number) {
		this.socketService.attack(this.game.idGame, idCard, idTarget);
	}

	heroPower(uuidTarget: string, idTarget: number){
		this.socketService.heroPower(this.game.idGame, uuidTarget, idTarget);
	}

	onClickHand(idCard: number) {
		let card = this.player.hand[idCard];

		if (this.isSpell(card) && (card as Spell).target != undefined && (card as Spell).target.includes("1")) {
			let spell = (card as Spell);
			let splitTarget = spell.target.split('_');

			this.showHand = true;
			this.showAllyHeroPower = true;
			this.showOnlyTaunt = false;
			
			this.waitingForPlayCardTarget = true;
			this.waitingForAttackTarget = false;
			this.waitingForHeroPowerTarget = false;
			this.idWaitingCard = idCard;

			this.showTargets(splitTarget);
		} else {
			this.playCard(idCard, this.player.uuid);
		}
	}

	onClickMinion(uuidTarget: string, idCard: number) {
		if (this.waitingForPlayCardTarget) {
			this.playCard(this.idWaitingCard, uuidTarget, idCard);
		} else if (this.waitingForAttackTarget) {
			this.attack(this.idWaitingCard, idCard);
		} else if(this.waitingForHeroPowerTarget) {
			this.heroPower(uuidTarget, idCard);
		} else {
			this.showHand = true;
			this.showAllyHeroPower = true;

			this.waitingForPlayCardTarget = false;
			this.waitingForAttackTarget = true;
			this.waitingForHeroPowerTarget = false;

			this.idWaitingCard = idCard;

			this.showAllyMinions = false;
			this.showEnemyMinions = true;
			this.showAllyHero = false;
			this.showEnemyHero = true;

			this.showOnlyTaunt = false;
			for(let card of this.otherPlayer.board){
				if (card.taunt) {
					this.showOnlyTaunt = true;
					break;
				}
			}
		}
	}

	onClickHero(uuidTarget: string) {
		if (this.waitingForPlayCardTarget) {
			this.playCard(this.idWaitingCard, uuidTarget, -1);
		} else if (this.waitingForAttackTarget) {
			this.attack(this.idWaitingCard, -1);
		} else if(this.waitingForHeroPowerTarget){
			this.heroPower(uuidTarget, -1);
		}
		if (this.gameOver) {
		  this.router.navigate(['/end']);
		}
	}

	onClickHeroPower() {
		if(this.player.hero.target && this.player.hero.target.includes("1")){
			let splitTarget = this.player.hero.target.split('_');

			this.showHand = true;
			this.showAllyHeroPower = false;
			this.showOnlyTaunt = false;
			
			this.waitingForPlayCardTarget = false;
			this.waitingForAttackTarget = false;
			this.waitingForHeroPowerTarget = true;

			this.showTargets(splitTarget);
		} else {
			this.socketService.heroPower(this.game.idGame, this.otherPlayer.uuid, 1);
		}
	}

	endTurn() {
		this.socketService.endTurn(this.game.idGame);
	}

	resetBooleans(){
		if (this.game.currentPlayer.uuid == this.socketService.getPlayer().uuid) {
			this.player = this.game.currentPlayer;
			this.otherPlayer = this.game.otherPlayer;

			this.yourTurn = true;
			this.showHand = true;
			this.showAllyMinions = true;
			this.showEnemyMinions = false;
			this.showAllyHero = false;
			this.showEnemyHero = false;
			this.showAllyHeroPower = true;
			this.showOnlyTaunt = false;

			this.waitingForPlayCardTarget = false;
			this.waitingForAttackTarget = false;
			this.waitingForHeroPowerTarget = false;
		} else {
			this.player = this.game.otherPlayer;
			this.otherPlayer = this.game.currentPlayer;

			this.yourTurn = false;
			this.showHand = false;
			this.showAllyMinions = false;
			this.showEnemyMinions = false;
			this.showAllyHero = false;
			this.showEnemyHero = false;
			this.showAllyHeroPower = false;
			this.showOnlyTaunt = false;

			this.waitingForPlayCardTarget = false;
			this.waitingForAttackTarget = false;
			this.waitingForHeroPowerTarget = false;
		}
	}

	showTargets(splitTarget: Array<String>){
		if (splitTarget[0] == "minion") {
			if (splitTarget[2] == "ally") {
				this.showAllyMinions = true;
				this.showEnemyMinions = false;
				this.showAllyHero = false;
				this.showEnemyHero = false;
			} else if (splitTarget[2] == "enemy") {
				this.showAllyMinions = false;
				this.showEnemyMinions = true;
				this.showAllyHero = false;
				this.showEnemyHero = false;
			} else if (splitTarget[2] == "all") {
				this.showAllyMinions = true;
				this.showEnemyMinions = true;
				this.showAllyHero = false;
				this.showEnemyHero = false;
			}
		} else if (splitTarget[0] == "all") {
			if (splitTarget[2] == "ally") {
				this.showAllyMinions = true;
				this.showEnemyMinions = false;
				this.showAllyHero = true;
				this.showEnemyHero = false;
			} else if (splitTarget[2] == "enemy") {
				this.showAllyMinions = false;
				this.showEnemyMinions = true;
				this.showAllyHero = false;
				this.showEnemyHero = true;
			} else if (splitTarget[2] == "all") {
				this.showAllyMinions = true;
				this.showEnemyMinions = true;
				this.showAllyHero = true;
				this.showEnemyHero = true;
			}
		}
	}

	isMinion(card): boolean {
		return (card as Minion).taunt != undefined;
	}

	isSpell(card): boolean {
		return (card as Spell).polymorph != undefined;
	}
}
