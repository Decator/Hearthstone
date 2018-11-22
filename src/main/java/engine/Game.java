package engine;

import java.util.UUID;

/**
 * 
 * @author Alexis Claveau, Martin Ars, Maud Van Dorssen, Alexis Loret
 * @version 0.0.1
 */
class Game {
	private UUID idGame;
	private Player player1;
	private Player player2;
	
	Game(UUID idGame, Player player1, Player player2) {
		this.idGame = idGame;
		this.player1 = player1;
		this.player2 = player2;
	}
	
	public UUID getIdGame() {
		return this.idGame;
	}
	
	void drawCard() {
		
	}
	
	void attack() {
		
	}
	
	void endTurn() {
		
	}
	
	void playCard() {
		
	}
	
	void heroPower() {
		
	}
}
