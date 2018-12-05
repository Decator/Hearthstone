import { Component } from '@angular/core';
import { Router, NavigationStart } from '@angular/router';
import { SocketService } from '../../service/socket.service';
import { Game, Player } from '../../app.models';
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
	private playerNumber: number;
	private error: String;

	private showLoader: boolean = true;

	constructor(private socketService: SocketService, private router: Router) {
		this.socketService.gameObservable.subscribe((value: Game) => {
			this.game = value;
			console.log(this.game);

			if(this.game.players[0].uuid == this.socketService.getPlayer().uuid){
				this.player = this.game.players[0];
				this.otherPlayer = this.game.players[1];
				this.playerNumber = 0;
			} else {
				this.player = this.game.players[1];
				this.otherPlayer = this.game.players[0];
				this.playerNumber = 1;
			}

			console.log(this.game);

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

		if(this.game){
			if(this.game.players[0].uuid == this.socketService.getPlayer().uuid){
				this.player = this.game.players[0];
				this.otherPlayer = this.game.players[1];
				this.playerNumber = 0;
			} else {
				this.player = this.game.players[1];
				this.otherPlayer = this.game.players[0];
				this.playerNumber = 1;
			}
		}

		if(this.game && this.showLoader){
			this.showLoader = false;
		}
	}

	playCard(){
		this.socketService.playCard(this.game.idGame, 0, this.player.uuid, 1);
	}
}