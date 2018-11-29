package fr.univ_nantes.alma.engine;

import java.util.ArrayList;
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
	private ArrayList<Minion> invocations;

	/**
	 * Initialize the attributes of this class.
	 * @param idGame the id of the game
	 * @param player1 the first player of the game
	 * @param player2 the second player of the game
	 */
	public Game(UUID idGame, Player player1, Player player2, ArrayList<Minion> invocations) {
		this.idGame = idGame;
		this.players = new Player[2];
		this.players[0] = player1;
		this.players[1] = player2;
		this.idCurrentPlayer = 0;
		this.invocations = invocations;
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
	public Player[] getPlayers() {
		return this.players;
	}
	
	/**
	 * Get the index of the current player.
	 * @return the index of the current player
	 */
	public int getIdCurrentPlayer() {
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
	 * Gives attack aura buff from played minion to other minions on the board
	 * @param board
	 * @param lastMinionPlayed
	 */
	void giveAttackAuraToOtherMinions(Vector<Minion> board, Minion lastMinionPlayed) {
		if (lastMinionPlayed.getAttackBuffAura() != 0) { //if minion has attack aura
			int attackBuffAura = lastMinionPlayed.getAttackBuffAura();
			for (int i = 0; i < board.size() - 1; i++) { // gives attack aura to all allies minions but itself
				Minion minion = board.get(i);
				minion.setDamage(minion.getDamage() + attackBuffAura);
			}
		}
	}
	
	/**
	 * Gets the cumulated attack aura buffs of other minions and add them to the played minion
	 * @param board the board of the player playing the minion
	 * @param lastMinionPlayed
	 */
	void getAttackAuraFromOtherMinions(Vector<Minion> board, Minion lastMinionPlayed) {
		int attackBuffAura = 0;
		for (int i = 0; i < board.size() - 1; i++) { //if there are minions with attack auras on the board
			Minion minion = board.get(i);
			if (minion.getAttackBuffAura()!= 0) {
				attackBuffAura += minion.getAttackBuffAura(); //add auras up except aura generated by played minion
			}
		}
		lastMinionPlayed.setDamage(lastMinionPlayed.getDamage() + attackBuffAura); // gives attack aura to played minion
	}
	/**
	 * Removes the attack aura buff from all minions of the board upon the death of the minion generating the buff
	 * @param board the board containing the dying minion
	 * @param dyingMinion
	 */
	void removeAttackAuraFromMinions(Vector<Minion> board, Minion dyingMinion) {
		if (dyingMinion.getAttackBuffAura() != 0) { //if minion has attack aura
			for (int i = 0; i < board.size(); i++) {
				board.get(i).setDamage(board.get(i).getDamage() - dyingMinion.getAttackBuffAura());
			}
		}
	}
	/**
	 * Heals the hero for the damage inflicted by its lifestealing minion
	 * @param hero the hero healed by lifesteal
	 * @param minion the minion having lifesteal
	 */
	void LifeSteal(Hero hero, Minion minion) {
		if (minion.getLifesteal()) { //check for lifesteal
			if (!Rule.checkHealthPoints(hero.getHealthPoints() + minion.damage)) {
				hero.setHealthPoints(Rule.MAX_HERO_HEALTH_POINTS); // if going over max HP, then HP = max
			} else {
				hero.receiveHealing(minion.damage); // if not, heal for the damage inflicted by the minion
			}
		}
	}
	/**
	 * If played minion has charge, it can attack right away
	 * @param lastMinionPlayed
	 */
	void Charge(Minion lastMinionPlayed){
		if (lastMinionPlayed.getCharge()) { // If played minion has charge
			lastMinionPlayed.setAttacked(false); //can attack right away
		} else  {
			lastMinionPlayed.setAttacked(true); // else has to wait a turn
		}
	}
	/**
	 * 
	 * @param board the board that needs to be checked for taunt minions
	 * @return true if a minion as taunt, else return false
	 */
	boolean Taunt(Vector<Minion> board) {
		for (Minion minionEnemy : board) // Check if an enemy minion has taunt
		{
			if (minionEnemy.getTaunt()) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Attacks a target with an attacker given in parameter
	 * @param idAttack the id of the attacker
	 * @param idTarget the id of the target
	 * @throws EngineException 
	 */
	void attack(int idAttack, int idTarget) throws EngineException {
		Minion minion = this.players[this.idCurrentPlayer].getBoard().get(idAttack);
		Hero hero = this.players[this.idCurrentPlayer].getHero();
		Hero heroEnemy = this.players[this.idCurrentPlayer ^ 1].getHero();
		Player playerEnemy = this.players[this.idCurrentPlayer ^ 1];
		Player currentPlayer = this.players[this.idCurrentPlayer];
	
		if (!Taunt(playerEnemy.getBoard()) && idTarget == -1) { // If no enemy minion has taunt and enemy Hero is target, then attacking is possible
			//Check if the minion already attacked or not
			if(minion != null && !Rule.checkMinionAttacked(minion)) {
				int damage = minion.getDamage();
				if(idTarget == -1) { // If it's the hero
					heroEnemy.receiveDamage(damage); //attack the enemy Hero
					minion.setAttacked(true);
					LifeSteal(hero, minion);
					if(!Rule.checkAlive(heroEnemy.getHealthPoints())) {
						//end game
					}
				} else if (!Taunt(playerEnemy.getBoard()) || playerEnemy.getBoard().get(idTarget).getTaunt()) {
					Minion victim = playerEnemy.getBoard().get(idTarget);
					if(victim != null) {
						minion.receiveDamage(victim.getDamage()); // minion takes victim's damage
						LifeSteal(hero, minion);
						victim.receiveDamage(damage); //attack the minion
						LifeSteal(heroEnemy, victim);
						minion.setAttacked(true);
						if(!Rule.checkAlive(minion.getHealthPoints())) {
							removeAttackAuraFromMinions(currentPlayer.getBoard(), minion); //removes attack buff from other minions if relevant
							currentPlayer.getBoard().remove(idAttack);
						}
						if(!Rule.checkAlive(victim.getHealthPoints())) {
							removeAttackAuraFromMinions(playerEnemy.getBoard(), victim); //removes attack buff from other minions if relevant
							playerEnemy.getBoard().remove(idTarget);
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
            		Minion lastMinionPlayed = player.getBoard().lastElement();
            		Charge(lastMinionPlayed);
            		giveAttackAuraToOtherMinions(player.getBoard(), lastMinionPlayed); //give attack aura buff to other minions if it exists
            		getAttackAuraFromOtherMinions(player.getBoard(), lastMinionPlayed); // get attack auras buffs from other minions if they exist
            	} else {
            		throw new EngineException("Vous avez atteint le nombre maximum de serviteurs sur le plateau !");
            	}
        	} else if (card instanceof Spell) {
        		
                // Cast spell
            }
            player.setManaPoolAfterPlay(card.getManaCost()); // Decrements manaCost from player's manaPool
        } else {
        	throw new EngineException("Vous n'avez pas assez de mana !");
        }
        
    }
	
	void heroPower(Player playerTarget, int idTarget) throws EngineException {
		Player currentPlayer = this.players[this.idCurrentPlayer];
		Hero heroCurrentPlayer = this.players[this.idCurrentPlayer].getHero();
		
		if (!heroCurrentPlayer.getHeroPowerUsed()) { // If the hero has already used his power
			if (Rule.checkManaPool(currentPlayer.getManaPool(), Rule.MANA_COST_HERO_POWER)) {
				switch (heroCurrentPlayer.getType()) //check class of the hero
				{
				case "Warrior": //gives armor buff
					heroCurrentPlayer.setArmorPoints(heroCurrentPlayer.getArmorPoints() + heroCurrentPlayer.getArmorBuff());
					heroCurrentPlayer.setHeroPowerUsed(true);
					currentPlayer.setManaPoolAfterPlay(Rule.MANA_COST_HERO_POWER);
					break;
				case "Mage":
					if (idTarget == -1){ //if target is a hero
						playerTarget.getHero().receiveDamage(heroCurrentPlayer.getDamage());
						heroCurrentPlayer.setHeroPowerUsed(true);
						currentPlayer.setManaPoolAfterPlay(Rule.MANA_COST_HERO_POWER);
						if(!Rule.checkAlive(playerTarget.getHero().getHealthPoints())){
							//end game
						}
					}
					else { // if target is a minion
						playerTarget.getBoard().get(idTarget).receiveDamage(heroCurrentPlayer.getDamage()); //Inflicts damage to minion
						heroCurrentPlayer.setHeroPowerUsed(true);
						currentPlayer.setManaPoolAfterPlay(Rule.MANA_COST_HERO_POWER); //Decrement manaCost from manaPool
						if(!Rule.checkAlive(playerTarget.getBoard().get(idTarget).getHealthPoints())){
							removeAttackAuraFromMinions(playerTarget.getBoard(), playerTarget.getBoard().get(idTarget)); //remove attack buff from other minions if relevant
							playerTarget.getBoard().remove(idTarget); //If minion healthPoints <= 0, remove minion from board
						}
					}
					break;
				case "Paladin":
					if (Rule.checkBoardSize(currentPlayer.getBoard())) { // if board is not full
						Minion minion = null;
						for(Minion invoc : this.invocations) { //get specific minion
							if (invoc.getId() == 9) {
								minion = invoc;
							}
						}
						if (minion != null) {
							currentPlayer.addCardToBoard(minion); 
							getAttackAuraFromOtherMinions(currentPlayer.getBoard(), currentPlayer.getBoard().lastElement()); //get attack aura buff from other minions if relevant
						} else {
							throw new EngineException("Le minion n'a pas pu être invoqué");
						}
						heroCurrentPlayer.setHeroPowerUsed(true);
						currentPlayer.setManaPoolAfterPlay(Rule.MANA_COST_HERO_POWER);
					} else {
						throw new EngineException("Vous avez atteint le nombre maximum de serviteurs sur le plateau !");
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
