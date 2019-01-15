import { Component } from '@angular/core';
import { Router } from '@angular/router';
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
	private waitingCardSource: string;

	private timeLeft: number = 120;
	private interval: any;
	private lastYourTurn = null;

	constructor(private socketService: SocketService, private router: Router, private snackBar: MatSnackBar) {
		if (this.socketService.getIsRedirect()) {
			this.socketService.gameObservable.subscribe((value: Game) => {
				this.game = value;
				if (this.game.gameOver) {
					if (this.game.currentPlayer.uuid === this.socketService.getPlayer().uuid) {
						if (this.game.currentPlayer.hero.healthPoints <= 0) {
							this.socketService.endGame();
							this.router.navigate(['/end', "lose"]);
						} else if (this.game.otherPlayer.hero.healthPoints <= 0) {
							this.socketService.endGame();
							this.router.navigate(['/end', "win"]);
						} else {
							this.socketService.endGame();
							this.router.navigate(['/end', "other"]);
						}
					} else {
						if (this.game.currentPlayer.hero.healthPoints <= 0) {
							this.socketService.endGame();
							this.router.navigate(['/end', "win"]);
						} else if (this.game.otherPlayer.hero.healthPoints <= 0) {
							this.socketService.endGame();
							this.router.navigate(['/end', "lose"]);
						} else {
							this.socketService.endGame();
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
							duration: 2000
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

	/**
	 * Play a card.
	 * @param idCard the id of the played card
	 * @param uuidPlayer the id of the player who plays the card
	 * @param uuidTarget the id of the player to whom the targeted card belongs
	 * @param idTarget the id of the targeted card
	 */
	playCard(idCard: number, uuidPlayer: string, uuidTarget?: string, idTarget?: number) {
		if (idTarget != null) {
			this.socketService.playCard(this.game.idGame, uuidPlayer, idCard, uuidTarget, idTarget);
		} else {
			this.socketService.playCard(this.game.idGame, uuidPlayer, idCard, uuidPlayer, 0);
		}
	}

	/**
	 * Attack a card. 
	 * @param idCard the id of the attacking card
	 * @param uuidPlayer the id of the attacking player
	 * @param uuidTarget this id of the targeted player
	 * @param idTarget the id of the targeted card
	 */
	attack(idCard: number, uuidPlayer: string, uuidTarget: string, idTarget: number) {
		this.socketService.attack(this.game.idGame, uuidPlayer, idCard, uuidTarget, idTarget);
	}

	/**
	 * Player a hero's power. 
	 * @param uuidPlayer the id of the player playing the power
	 * @param uuidTarget the id of the targeted player
	 * @param idTarget the id of the targeted card
	 */
	heroPower(uuidPlayer: string, uuidTarget: string, idTarget: number) {
		this.socketService.heroPower(this.game.idGame, uuidPlayer, uuidTarget, idTarget);
	}

	/**
	 * When clicking on a Card from the hand, if it needs a target, wait for the target. 
	 * Else, play the card.
	 * @param idCard 
	 */
	onClickHand(idCard: number) {
		const card = this.player.hand[idCard];

		if (this.yourTurn && card.manaCost <= this.player.manaPool && this.isSpell(card) && (card as Spell).target != undefined && (card as Spell).target.includes('1')) {
			const spell = (card as Spell);
			const splitTarget = spell.target.split('_');

			this.showHand = true;
			this.showAllyHeroPower = true;
			this.showOnlyTaunt = false;

			this.waitingForPlayCardTarget = true;
			this.waitingForAttackTarget = false;
			this.waitingForHeroPowerTarget = false;
			this.idWaitingCard = idCard;
			this.waitingCardSource = "hand";

			this.showTargets(splitTarget);
		} else {
			this.playCard(idCard, this.player.uuid);
		}
	}

	/**
	 * When clicking on a Minion, find out whether its a target of if it's attacking.
	 * Apply the given methods.
	 * @param uuidTarget the id of the player of the Minion
	 * @param idCard the id of the Minion Card
	 */
	onClickMinion(uuidTarget: string, idCard: number) {
		if (this.waitingForPlayCardTarget) {
			this.playCard(this.idWaitingCard, this.player.uuid, uuidTarget, idCard);
		} else if (this.waitingForAttackTarget) {
			this.attack(this.idWaitingCard, this.player.uuid, uuidTarget, idCard);
		} else if (this.waitingForHeroPowerTarget) {
			this.heroPower(this.player.uuid, uuidTarget, idCard);
		} else {
			if(uuidTarget == this.player.uuid){
				if(this.player.board[idCard].damage > 0 && !this.player.board[idCard].attacked){
					this.showHand = true;
					this.showAllyHeroPower = true;
		
					this.waitingForPlayCardTarget = false;
					this.waitingForAttackTarget = true;
					this.waitingForHeroPowerTarget = false;

					this.idWaitingCard = idCard;
					this.waitingCardSource = "board";
		
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
				} else {
					this.attack(idCard, this.player.uuid, this.otherPlayer.uuid, 1);
				}
			}
		}
	}

	/**
	 * When clicking on a hero, apply the action for which the Hero is a target.
	 * @param uuidTarget the id of the player of the Hero
	 */
	onClickHero(uuidTarget: string) {
		if (this.waitingForPlayCardTarget) {
			this.playCard(this.idWaitingCard, this.player.uuid, uuidTarget, -1);
		} else if (this.waitingForAttackTarget) {
			this.attack(this.idWaitingCard, this.player.uuid, uuidTarget, -1);
		} else if (this.waitingForHeroPowerTarget) {
			this.heroPower(this.player.uuid, uuidTarget, -1);
		}
	}

	/**
	 * Activate a Hero power or wait for a target.
	 */
	onClickHeroPower() {
		if(this.player.manaPool >= 2 && !this.player.hero.heroPowerUsed && this.player.hero.target && this.player.hero.target.includes('1')) {
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

	/**
	 * End the turn.
	 */
	endTurn() {
		this.socketService.endTurn(this.game.idGame, this.player.uuid);
	}

	/**
	 * Reset the booleans. 
	 * These booleans say which elements are enabled or disabled in the html.
	 * It also resets the timer if needed.
	 */
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
		this.waitingCardSource = "none";
	}

	/**
	 * Check which targets to enable or disable.
	 * @param splitTarget the target string to analyze
	 */
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

	/**
	 * Return true if the card is a Minion Card. 
	 * @param card the card
	 */
	isMinion(card): boolean {
		return (card as Minion).taunt !== undefined;
	}

	/**
	 * Return true if the card is a Spell Card. 
	 * @param card the card
	 */
	isSpell(card): boolean {
		return (card as Spell).polymorph !== undefined;
	}

	/**
	 * Start the timer if it hasn't already started. 
	 */
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

	/**
	 * Reset the timer. 
	 */
	resetTimer() {
		this.timeLeft = 120;
	}

	/**
	 * Stop the timer. 
	 */
	stopTimer() {
		clearInterval(this.interval);
		this.interval = null;
	}
}

