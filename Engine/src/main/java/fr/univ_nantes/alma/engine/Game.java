package fr.univ_nantes.alma.engine;

import java.util.UUID;
import java.util.Vector;


/**
 * Manage all the actions that can happen in the game.
 *
 * @author Alexis Claveau, Martin Ars, Maud Van Dorssen, Alexis Loret
 * @version 0.0.1
 */
public class Game {
	private UUID idGame;
	private Player[] players;
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
	 * @throws EngineException
	 */
	void drawCard() throws EngineException {
		Player player = this.players[this.idCurrentPlayer];
		int random = (int)(player.getDeck().length * Math.random());
		if(Rule.checkHandSize(player.getHand()))
			player.addCardToHand(player.getDeck()[random]);
		else 
			throw new EngineException("Votre main est pleine !");
	}
	
	/**
	 * Attack a target with a attacker given in parameter
	 * @param idAttack the id of the attacker
	 * @param idTarget the id of the target
	 * @throws EngineException 
	 */
	void attack(int idAttack, int idTarget) throws EngineException {
		Minion minion = this.players[this.idCurrentPlayer].getBoard().get(idAttack);
		Minion victim = this.players[this.idCurrentPlayer ^ 1].getBoard().get(idTarget);
		Hero hero = this.players[this.idCurrentPlayer].getHero();
		Hero heroEnemy = this.players[this.idCurrentPlayer ^ 1].getHero();
		Player playerEnemy = this.players[this.idCurrentPlayer ^ 1];
		
		boolean taunt = false;
		
		for (Minion minionEnemy : playerEnemy.getBoard()) // Check if an enemy minion has taunt
		{
			if (minionEnemy.getTaunt()) {
				taunt = true;
			}
		}
		if (!taunt || victim.getTaunt()) { // If no minion has taunt or the target victim minion has taunt, then attacking is possible
			//Check if the minion already attacked or not
			if(minion != null && !Rule.checkMinionAttacked(minion)) {
				int damage = minion.getDamage();
				if(idTarget == -1) { // If it's the hero
					heroEnemy.receiveDamage(damage); //attack the enemy Hero
					minion.setAttacked(true);
					if (minion.getLifesteal()) { //check for lifesteal
						if (!Rule.checkHealthPoints(hero.getHealthPoints() + minion.damage)) {
							hero.setHealthPoints(Rule.MAX_HERO_HEALTH_POINTS); // if going over max HP, then HP = max
						} else {
							hero.receiveHealing(minion.damage); // if not, heal for the damage inflicted by the minion
						}
					}
					if(!Rule.checkAlive(heroEnemy.getHealthPoints())) {
						//end game
					}
				} else {
					if(victim != null) {
						minion.receiveDamage(victim.getDamage()); // minion takes victim's damage
						if (minion.getLifesteal()) { //check for lifesteal
							if (!Rule.checkHealthPoints(hero.getHealthPoints() + minion.damage)) {
								hero.setHealthPoints(Rule.MAX_HERO_HEALTH_POINTS); // if going over max HP, then HP = max
							} else {
								hero.receiveHealing(minion.damage); // if not, heal for the damage inflicted by the minion
							}
						}
						victim.receiveDamage(damage); //attack the minion
						if (victim.getLifesteal()) { //check for lifesteal
							if (!Rule.checkHealthPoints(heroEnemy.getHealthPoints() + victim.damage)) {
								heroEnemy.setHealthPoints(Rule.MAX_HERO_HEALTH_POINTS); // if going over max HP, then HP = max
							} else {
								heroEnemy.receiveHealing(victim.damage); // if not, heal for the damage inflicted by the victim
							}
						}
						minion.setAttacked(true);
						if(!Rule.checkAlive(minion.getHealthPoints())) {
							this.players[this.idCurrentPlayer].getBoard().remove(idAttack);
						}
						if(!Rule.checkAlive(victim.getHealthPoints())) {
							this.players[this.idCurrentPlayer ^ 1].getBoard().remove(idTarget);
						}
					} else {
						throw new EngineException("Le serviteur que vous cherchez à attaquer n'existe pas !");
					}
				}
			} else {
				throw new EngineException("Ce serviteur a déjà attaqué durant ce tour !");
			}
		} else {
			throw new EngineException("Cible incorrecte, un serviteur adverse a provocation !");
		}
	}
	/**
	 * Initializes manaMaxTurn, manaPool, heroPowerUsed and minionAttacked for the beginning of the turn
	 * @throws EngineException
	 */
	void initTurn() throws EngineException {
		Player player = this.players[this.idCurrentPlayer];
		Hero hero = this.players[this.idCurrentPlayer].getHero();
		try {
			drawCard();
		} catch(EngineException e) {
			throw e;
		} finally {
			if(Rule.checkManaTurn(player.getManaMaxTurn())) {
				player.setManaMaxTurn();
			}
			player.setManaPoolForNewTurn();
			hero.setHeroPowerUsed(false);
			for (Minion minion : player.getBoard()) {
				minion.setAttacked(false);
			}
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
     * @param idCard the id of the card the player wants to play
     * @throws EngineException 
     */
	void playCard(int idCard) throws EngineException {
        Card card = this.players[this.idCurrentPlayer].getHand().get(idCard); // Create the card according to the id given on parameters
        Player player = this.players[this.idCurrentPlayer];
        if(Rule.checkManaPool(player.getManaPool(), card.getManaCost())) { // If the player has enough mana, play the card
        	if(card instanceof Minion) { // If it's a Minion
            	if(Rule.checkBoardSize(player.getBoard())) {
            		player.addCardToBoard((Minion)player.getHand().get(idCard));
            		if (player.getBoard().lastElement().getCharge()) {
            			player.getBoard().lastElement().setAttacked(false);
            		} else  {
            			player.getBoard().lastElement().setAttacked(true);
            		}
            	} else {
            		throw new EngineException("Vous avez atteint le nombre maximum de serviteurs sur le plateau !");
            	}
        	} else {
                // Cast spell
            }
            player.setManaPoolAfterPlay(card.getManaCost()); // Check manaCost is lower than player's manaPool
        } else {
        	throw new EngineException("Vous n'avez pas assez de mana !");
        }
        
    }
	
	void heroPower(Player playerTarget, int idTarget) throws EngineException {
		Player currentPlayer = this.players[this.idCurrentPlayer];
		Hero heroCurrentPlayer = this.players[this.idCurrentPlayer].getHero();
		
		if (!heroCurrentPlayer.getHeroPowerUsed()) { // If the hero has already used his power
			if (Rule.checkManaPool(currentPlayer.getManaPool(), Rule.MANA_COST_HERO_POWER)) {
				switch (heroCurrentPlayer.getType())
				{
				case "Warrior":
					heroCurrentPlayer.setArmorPoints(heroCurrentPlayer.getArmorPoints() + heroCurrentPlayer.getArmorBuff());
					heroCurrentPlayer.setHeroPowerUsed(true);
					currentPlayer.setManaPoolAfterPlay(Rule.MANA_COST_HERO_POWER);
					break;
				case "Mage":
					if (idTarget == -1){
						playerTarget.getHero().receiveDamage(heroCurrentPlayer.getDamage());
						heroCurrentPlayer.setHeroPowerUsed(true);
						currentPlayer.setManaPoolAfterPlay(Rule.MANA_COST_HERO_POWER);
						if(!Rule.checkAlive(playerTarget.getHero().getHealthPoints())){
							//end game
						}
					}
					else {
						playerTarget.getBoard().get(idTarget).receiveDamage(heroCurrentPlayer.getDamage()); //Inflicts damage to minion
						heroCurrentPlayer.setHeroPowerUsed(true);
						currentPlayer.setManaPoolAfterPlay(Rule.MANA_COST_HERO_POWER); //Decrement manaCost from manaPool
						if(!Rule.checkAlive(playerTarget.getBoard().get(idTarget).getHealthPoints())){
							playerTarget.getBoard().remove(idTarget); //If minion healthPoints <= 0, remove minion from board
						}
					}
					break;
				case "Paladin":
					if (Rule.checkBoardSize(this.players[this.idCurrentPlayer].getBoard())) {
						/*ArrayList<Minion> = Engine.retrieveMinion
						Minion minion = this.players[this.idCurrentPlayer].[9];
						this.players[this.idCurrentPlayer].addCardToBoard((minion) card);*/
						heroCurrentPlayer.setHeroPowerUsed(true);
						currentPlayer.setManaPoolAfterPlay(Rule.MANA_COST_HERO_POWER);
					}
					break;
				}
			} else {
				throw new EngineException("Vous n'avez pas assez de mana !");
			}
		} else {
			throw new EngineException("Vous avez déjà utilisé votre pouvoir héroïque durant ce tour !");
		}
	}
}
