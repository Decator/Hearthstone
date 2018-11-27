package fr.univ_nantes.alma.engine;

import java.util.UUID;


/**
 * Manage all the actions that can happen in the game.
 *
 * @author Alexis Claveau, Martin Ars, Maud Van Dorssen, Alexis Loret
 * @version 0.0.1
 */
public class Game {
	private UUID idGame;
	private Player player1;
	private Player player2;
	private Player currentPlayer;

	/**
	 * Initialize the attributes of this class.
	 * @param idGame the id of the game
	 * @param player1 the first player of the game
	 * @param player2 the second player of the game
	 */
	public Game(UUID idGame, Player player1, Player player2) {
		this.idGame = idGame;
		this.player1 = player1;
		this.player2 = player2;
		this.currentPlayer = player1;
	}

	/**
	 * Get the id of the game
	 * @return the id of the game
	 */
	public UUID getIdGame() {
		return this.idGame;
	}
	
	/**
	 * Get the player1.
	 * @return the player1
	 */
	Player getPlayer1() {
		return this.player1;
	}
	
	/**
	 * Get the player2.
	 * @return the player2
	 */
	Player getPlayer2() {
		return this.player2;
	}
	
	/**
	 * Get the current player.
	 * @return the current player
	 */
	Player getCurrentPlayer() {
		return this.currentPlayer;
	}

	/**
	 * Draw a card from the player's deck and place it into his hand.
	 */
	void drawCard() {
		try {
			int random = (int)(this.currentPlayer.getDeck().length * Math.random());
			this.currentPlayer.addCardToHand(this.currentPlayer.getDeck()[random]);
		} catch (EngineException e) {
			e.printStackTrace();
		}
	}
	
	void attack() {
		
	}

	/**
	 * End the turn and switch the current player.
	 */
	void endTurn() {
		if(this.currentPlayer == player1) {
			this.currentPlayer = player2;
		}
		else this.currentPlayer = player1;
	}

	/**
	 * Play the card with the specified id.
	 * @param idCard the id of the card
	 * @throws EngineException 
	 */
	void playCard(int idCard) throws EngineException {
		Card card = this.currentPlayer.getHand().get(idCard); // Create the card accroding to the id given on parameters
		
		if(Rule.checkManaPool(this.currentPlayer.getManaPool(), card.getManaCost())) { // Check manaCost
			if(card instanceof Minion) {
				try {
					this.currentPlayer.addCardToBoard((Minion)this.currentPlayer.getHand().get(idCard));
				} catch (EngineException e) {
					e.printStackTrace();
				}
			}
			else {
				// Cast spell
			}
		} else {
			throw new EngineException("Error adding card to board : player has reached the maximum board size.");
		}
	}
	
	void heroPower() {
		
	}
}
