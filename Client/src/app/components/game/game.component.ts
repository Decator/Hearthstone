import { Component } from '@angular/core';
import { Router, NavigationStart } from '@angular/router';
import { SocketService } from '../../service/socket.service';
import { Game, Player, Minion, Spell } from '../../app.models';
import { MatSnackBar } from '@angular/material/snack-bar';

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
	private error: string;
	private manaPoolArray = new Array(0);
	private manaMaxTurnArray = new Array(0);
	private manaPoolArrayAdv = new Array(0);
	private manaMaxTurnArrayAdv = new Array(0);

	private showLoader: boolean = true;
	private showHand: boolean = false;
	private showAllyMinions: boolean = false;
	private showEnemyMinions: boolean = false;
	private showAllyHero: boolean = false;
	private showEnemyHero: boolean = false;
	private showAllyHeroPower: boolean = false;
	private showOnlyTaunt: boolean = false;

	private waitingForPlayCardTarget = false;
	private waitingForAttackTarget = false;
	private waitingForHeroPowerTarget = false;
	private idWaitingCard: number;

	private timeLeft: number = 120;
	private interval: any;
	private lastYourTurn = null;

	constructor(private socketService: SocketService, private router: Router, private snackBar: MatSnackBar) {
		if (this.socketService.getIsRedirect()) {
			this.socketService.gameObservable.subscribe((value: Game) => {
				this.game = value;
				if (this.game.gameOver) {
					this.socketService.endGame();
					if (this.game.currentPlayer.uuid === this.socketService.getPlayer().uuid) {
						if (this.game.currentPlayer.hero.healthPoints <= 0) {
							this.router.navigate(['/end', "lose"]);
						} else if (this.game.otherPlayer.hero.healthPoints <= 0) {
							this.router.navigate(['/end', "win"]);
						} else {
							this.router.navigate(['/end', "other"]);
						}
					} else {
						if (this.game.currentPlayer.hero.healthPoints <= 0) {
							this.router.navigate(['/end', "win"]);
						} else if (this.game.otherPlayer.hero.healthPoints <= 0) {
							this.router.navigate(['/end', "lose"]);
						} else {
							this.router.navigate(['/end', "other"]);
						}
					}
				}
				this.resetBooleans();

				if (this.showLoader) {
					this.showLoader = false;
				}
			});

			this.socketService.errorObservable.subscribe((value: string) => {
				this.error = value;

				if (this.game) {
					if(this.error != ""){
						this.snackBar.open(this.error, '', {
							duration: 2000,
						});
					}
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

	playCard(idCard: number, uuidPlayer: string, uuidTarget?: string, idTarget?: number) {
		if (uuidTarget && idTarget) {
			this.socketService.playCard(this.game.idGame, uuidPlayer, idCard, uuidTarget, idTarget);
		} else {
			this.socketService.playCard(this.game.idGame, uuidPlayer, idCard, uuidPlayer, 0);
		}
	}

	attack(idCard: number, uuidPlayer: string, idTarget: number) {
		this.socketService.attack(this.game.idGame, uuidPlayer, idCard, idTarget);
	}

	heroPower(uuidPlayer: string, uuidTarget: string, idTarget: number) {
		this.socketService.heroPower(this.game.idGame, uuidPlayer, uuidTarget, idTarget);
	}

	onClickHand(idCard: number) {
		const card = this.player.hand[idCard];

		if (card.manaCost <= this.player.manaPool && this.isSpell(card) && (card as Spell).target != undefined && (card as Spell).target.includes('1')) {
			const spell = (card as Spell);
			const splitTarget = spell.target.split('_');

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
			this.playCard(this.idWaitingCard, this.player.uuid, uuidTarget, idCard);
		} else if (this.waitingForAttackTarget) {
			this.attack(this.idWaitingCard, this.player.uuid, idCard);
		} else if (this.waitingForHeroPowerTarget) {
			this.heroPower(this.player.uuid, uuidTarget, idCard);
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
			for (const card of this.otherPlayer.board) {
				if (card.taunt) {
					this.showOnlyTaunt = true;
					break;
				}
			}
		}
	}

	onClickHero(uuidTarget: string) {
		if (this.waitingForPlayCardTarget) {
			this.playCard(this.idWaitingCard, this.player.uuid, uuidTarget, -1);
		} else if (this.waitingForAttackTarget) {
			this.attack(this.idWaitingCard, this.player.uuid, -1);
		} else if (this.waitingForHeroPowerTarget) {
			this.heroPower(this.player.uuid, uuidTarget, -1);
		}
	}

	onClickHeroPower() {
		if(this.player.manaPool >= 2 && this.player.hero.target && this.player.hero.target.includes('1')) {
			const splitTarget = this.player.hero.target.split('_');

			this.showHand = true;
			this.showAllyHeroPower = false;
			this.showOnlyTaunt = false;

			this.waitingForPlayCardTarget = false;
			this.waitingForAttackTarget = false;
			this.waitingForHeroPowerTarget = true;

			this.showTargets(splitTarget);
		} else {
			this.socketService.heroPower(this.game.idGame, this.player.uuid, this.otherPlayer.uuid, 1);
		}
	}

	endTurn() {
		this.socketService.endTurn(this.game.idGame, this.player.uuid);
	}

	resetBooleans() {
		if (this.game.currentPlayer.uuid === this.socketService.getPlayer().uuid) {
			this.player = this.game.currentPlayer;
			this.otherPlayer = this.game.otherPlayer;

			this.manaPoolArray = new Array(this.player.manaPool);
			this.manaMaxTurnArray = new Array(this.player.manaMaxTurn - this.player.manaPool);
			this.manaPoolArrayAdv = new Array(this.otherPlayer.manaPool);
			this.manaMaxTurnArrayAdv = new Array(this.otherPlayer.manaMaxTurn - this.otherPlayer.manaPool);

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

			this.manaPoolArray = new Array(this.player.manaPool);
			this.manaMaxTurnArray = new Array(this.player.manaMaxTurn - this.player.manaPool);
			this.manaPoolArrayAdv = new Array(this.otherPlayer.manaPool);
			this.manaMaxTurnArrayAdv = new Array(this.otherPlayer.manaMaxTurn - this.otherPlayer.manaPool);

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
		if(this.yourTurn != this.lastYourTurn){
			this.lastYourTurn = this.yourTurn;
			this.resetTimer();
			this.startTimer();
		}
	}

	showTargets(splitTarget: Array<String>) {
		if (splitTarget[0] === 'minion') {
			if (splitTarget[2] === 'ally') {
				this.showAllyMinions = true;
				this.showEnemyMinions = false;
				this.showAllyHero = false;
				this.showEnemyHero = false;
			} else if (splitTarget[2] === 'enemy') {
				this.showAllyMinions = false;
				this.showEnemyMinions = true;
				this.showAllyHero = false;
				this.showEnemyHero = false;
			} else if (splitTarget[2] === 'all') {
				this.showAllyMinions = true;
				this.showEnemyMinions = true;
				this.showAllyHero = false;
				this.showEnemyHero = false;
			}
		} else if (splitTarget[0] === 'all') {
			if (splitTarget[2] === 'ally') {
				this.showAllyMinions = true;
				this.showEnemyMinions = false;
				this.showAllyHero = true;
				this.showEnemyHero = false;
			} else if (splitTarget[2] === 'enemy') {
				this.showAllyMinions = false;
				this.showEnemyMinions = true;
				this.showAllyHero = false;
				this.showEnemyHero = true;
			} else if (splitTarget[2] === 'all') {
				this.showAllyMinions = true;
				this.showEnemyMinions = true;
				this.showAllyHero = true;
				this.showEnemyHero = true;
			}
		}
	}

	isMinion(card): boolean {
		return (card as Minion).taunt !== undefined;
	}

	isSpell(card): boolean {
		return (card as Spell).polymorph !== undefined;
	}

	startTimer() {
		if(!this.interval){
			this.interval = setInterval(() => {
				if (this.timeLeft > 0) {
					this.timeLeft--;
				} else {
					this.stopTimer();
					this.resetTimer();
					if (this.yourTurn) {
						this.endTurn();
					}
				}
			}, 1000)
		}
	}

	resetTimer() {
		this.timeLeft = 120;
	}

	stopTimer() {
		clearInterval(this.interval);
		this.interval = null;
	}
}

