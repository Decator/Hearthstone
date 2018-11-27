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
	private Player[] players;
//	private Player player1;
//	private Player player2;
	private int idCurrentPlayer;

	/**
	 * Initialize the attributes of this class.
	 * @param idGame the id of the game
	 * @param player1 the first player of the game
	 * @param player2 the second player of the game
	 */
	public Game(UUID idGame, Player player1, Player player2) {
		this.idGame = idGame;
		this.players = new Player[2];
		this.players[0] = player1;
		this.players[1] = player2;
		this.idCurrentPlayer = 0;
//		this.player1 = player1;
//		this.player2 = player2;
//		this.currentPlayer = player1;
	}

	/**
	 * Get the id of the game
	 * @return the id of the game
	 */
	public UUID getIdGame() {
		return this.idGame;
	}
	
	/**
	 * Get an array containing the two players
	 * @return the players
	 */
	Player[] getPlayers() {
		return this.players;
	}
	
	/**
	 * Get the index of the current player.
	 * @return the index of the current player
	 */
	int getIdCurrentPlayer() {
		return this.idCurrentPlayer;
	}

	/**
	 * Draw a card from the player's deck and place it into his hand.
	 */
	void drawCard() {
		try {
			int random = (int)(this.players[this.idCurrentPlayer].getDeck().length * Math.random());
			this.players[this.idCurrentPlayer].addCardToHand(this.players[this.idCurrentPlayer].getDeck()[random]);
		} catch (EngineException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @param idAttack
	 * @param idTarget
	 */
	void attack(int idAttack, int idTarget) {
		//Check if the minion already attacked or not
		Minion minion = this.players[this.idCurrentPlayer].getBoard().get(idAttack);
		if(minion != null && !Rule.checkMinionAttacked(minion)) {
			int damage = minion.getDamage();
			
			if(idTarget == 0) {
				Hero hero = this.players[this.idCurrentPlayer ^ 1].getHero();
				hero.recieveDamage(damage); //attack the hero
				if(hero.getHealthPoints() <= 0) {
					//end game
				}
			} else {
				Minion victim = this.players[this.idCurrentPlayer ^ 1].getBoard().get(idTarget);
				if(victim != null) {
					victim.recieveDamage(damage); //attack the minion
					if(victim.getHealthPoints() <= 0) {
						this.players[this.idCurrentPlayer ^ 1].getBoard().remove(idTarget);
					}
				} else {
					//exception
				}
			}
		} else {
			//exception
		}
	}

	/**
	 * End the turn and switch the current player.
	 */
	void endTurn() {
		this.idCurrentPlayer ^= 1;
	}

	/**
     * Play the card with the specified id.
     * @param idCard the id of the card
     * @throws EngineException 
     */
	void playCard(int idCard) throws EngineException {
        Card card = this.players[this.idCurrentPlayer].getHand().get(idCard); // Create the card according to the id given on parameters

        this.players[this.idCurrentPlayer].setManaPoolAfterPlayCard(card.getManaCost()); // Check manaCost

        if(card instanceof Minion) {
            try {
                this.players[this.idCurrentPlayer].addCardToBoard((Minion)this.players[this.idCurrentPlayer].getHand().get(idCard));
            } catch (EngineException e) {
                e.printStackTrace();
            }
        }
        else {
            // Cast spell
        }
    }
	
	void heroPower() {
		
	}
}
