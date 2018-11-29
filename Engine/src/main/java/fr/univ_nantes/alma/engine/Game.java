package fr.univ_nantes.alma.engine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
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
	
	LinkedHashMap<String, Card> targetsFromTargetString(Player player, Player playerEnemy, Player targetPlayer, int idTarget, String spellTarget){
		String[] splitString = spellTarget.split("_");
		Hero hero = player.getHero();
		Hero heroEnemy = player.getHero();
		LinkedHashMap<String, Card> targets = new LinkedHashMap();
		switch (splitString[0]) {
		case "minion" :
			switch (splitString[1]) {
			case "all" :
				switch (splitString[2]) {
				case "enemy" :
					for (int i = 0; i < playerEnemy.getBoard().size(); i++) {
						targets.put("1_" + String.valueOf(i), playerEnemy.getBoard().get(i));
					}
				case "ally" :
					for (int i = 0; i < player.getBoard().size(); i++) {
						targets.put("0_" + String.valueOf(i), player.getBoard().get(i));
					}
				case "all" :
					for (int i = 0; i < playerEnemy.getBoard().size(); i++) {
						targets.put("1_" + String.valueOf(i),playerEnemy.getBoard().get(i));
					}
					for (int i = 0; i < player.getBoard().size(); i++) {
						targets.put("0_" + String.valueOf(i), player.getBoard().get(i));
					}
				}
			case "1" :
				switch (splitString[2]) {
				case "enemy" :
					targets.put("1_" + String.valueOf(idTarget), playerEnemy.getBoard().get(idTarget));
				case "ally" :
					targets.put("0_" + String.valueOf(idTarget), player.getBoard().get(idTarget));
				case "all" :
					if (targetPlayer.equals(player)) {
						targets.put("0_" + String.valueOf(idTarget), player.getBoard().get(idTarget));
					} else {
						targets.put("1_" + String.valueOf(idTarget), playerEnemy.getBoard().get(idTarget));
					}
					
				}
			}
		case "all" :
			switch (splitString[1]) {
			case "all" :
				switch (splitString[2]) {
				case "enemy" :
					targets.put("1",heroEnemy);
					for (int i = 0; i < playerEnemy.getBoard().size(); i++) {
						targets.put("1_" + String.valueOf(i),playerEnemy.getBoard().get(i));
					}
				case "ally" :
					targets.put("0", hero);
					for (int i = 0; i < player.getBoard().size(); i++) {
						targets.put("0_" + String.valueOf(i), player.getBoard().get(i));
					}
				case "all" :
					targets.put("0",hero);
					targets.put("1", heroEnemy);
					for (int i = 0; i < player.getBoard().size(); i++) {
						targets.put("0_" + String.valueOf(i), player.getBoard().get(i));
					}
					for (int i = 0; i < playerEnemy.getBoard().size(); i++) {
						targets.put("1_" + String.valueOf(i), playerEnemy.getBoard().get(i));
					}
				}
			case "1" :
				switch (splitString[2]) {
				case "enemy" :
					if(idTarget == -1) {
						targets.put("1", heroEnemy);
					} else {
						targets.put("1_" + String.valueOf(idTarget), playerEnemy.getBoard().get(idTarget));
					}
				case "ally" :
					if(idTarget == -1) {
						targets.put("0", hero);
					} else {
						targets.put("0_" + String.valueOf(idTarget), player.getBoard().get(idTarget));
					}
				case "all" :
					if (targetPlayer.equals(player)) {
						if(idTarget == -1) {
							targets.put("0", hero);
						} else {
							targets.put("0_" + String.valueOf(idTarget), player.getBoard().get(idTarget));
						}
					} else {
						if(idTarget == -1) {
							targets.put("1", heroEnemy);
						} else {
							targets.put("1_" + String.valueOf(idTarget), playerEnemy.getBoard().get(idTarget));
						}
					}
				}
			}
		}
		return targets;
	}
	
	/**
	 * Draw a card from the player's deck and place it into his hand.
	 * @return message
	 */
	String drawCard() {
		Player player = this.players[this.idCurrentPlayer];
		int random = (int)(player.getDeck().length * Math.random());
		String message = null;
		if(Rule.checkHandSize(player.getHand())) {
			player.addCardToHand(player.getDeck()[random]);
			return message;
		} else {
			message = "Votre main est pleine !";
			return message;
		}
	}
	
	/**
	 * Initializes manaMaxTurn, manaPool, heroPowerUsed and minionAttacked for the beginning of the turn
	 * @return 
	 */
	String initTurn() {
		Player player = this.players[this.idCurrentPlayer];
		Hero hero = this.players[this.idCurrentPlayer].getHero();
		String message = drawCard();
		if(Rule.checkManaTurn(player.getManaMaxTurn())) {
			player.setManaMaxTurn();
		}
		player.setManaPoolForNewTurn();
		hero.setHeroPowerUsed(false);
		for (Minion minion : player.getBoard()) {
			minion.setAttacked(false);
		}
		return message;
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
	 * Check if a minion in the board of interest has at least one minion with taunt
	 * @param board the board that needs to be checked for taunt minions
	 * @return true if a minion as taunt, else false
	 */
	boolean Taunt(Vector<Minion> board) {
		for (Minion minionEnemy : board) { // Check if an enemy minion has taunt
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
	 * @return message
	 */
	String attack(int idAttack, int idTarget) {
		Minion minion = this.players[this.idCurrentPlayer].getBoard().get(idAttack);
		Hero hero = this.players[this.idCurrentPlayer].getHero();
		Hero heroEnemy = this.players[this.idCurrentPlayer ^ 1].getHero();
		Player playerEnemy = this.players[this.idCurrentPlayer ^ 1];
		Player currentPlayer = this.players[this.idCurrentPlayer];
		String message = null;
		
		//Checks if the minion already attacked or not
		if(minion != null && !Rule.checkMinionAttacked(minion)) {
			if (!Taunt(playerEnemy.getBoard()) && idTarget == -1) { // If no enemy minion has taunt and enemy Hero is target, then attacking is possible
				int damage = minion.getDamage();
				heroEnemy.receiveDamage(damage); //attacks the enemy Hero
				minion.setAttacked(true);
				LifeSteal(hero, minion);
				if(!Rule.checkAlive(heroEnemy.getHealthPoints())) {
					return endGame();
				}
					return message;
				} else if (!Taunt(playerEnemy.getBoard()) || playerEnemy.getBoard().get(idTarget).getTaunt()) { // If no enemy minion has taunt, or target minion has taunt, attacking is possible
					Minion victim = playerEnemy.getBoard().get(idTarget);
					if(victim != null) {
						minion.receiveDamage(victim.getDamage()); // minion takes victim's damage
						LifeSteal(hero, minion);
						victim.receiveDamage(minion.getDamage()); //attacks the minion
						LifeSteal(heroEnemy, victim);
						minion.setAttacked(true);
						if(!Rule.checkAlive(minion.getHealthPoints())) {
							removeAttackAuraFromMinions(currentPlayer.getBoard(), minion); //removes attack buff from other minions if relevant
							currentPlayer.removeCardFromBoard(idAttack);
						}
						if(!Rule.checkAlive(victim.getHealthPoints())) {
							removeAttackAuraFromMinions(playerEnemy.getBoard(), victim); //removes attack buff from other minions if relevant
							playerEnemy.removeCardFromBoard(idTarget);
						}
						return message;
					} else {
						message = "Le serviteur que vous cherchez à attaquer n'existe pas !";
						return message;
					}
				} else {
					message = "Cible incorrecte, un serviteur adverse a provocation !";
					return message;
				}
			} else {
				message = "Ce serviteur a déjà attaqué durant ce tour !";
				return message;
		}
	}
	
	/**
     * Play the card with the specified id.
     * @param idCard the id of the card the player wants to play
     * @param idTarget the id of the target
     * @throws EngineException 
     */
	String playCard(int idCard, Player targetPlayer, int idTarget)  {
        Card card = this.players[this.idCurrentPlayer].getHand().get(idCard); // Create the card according to the id given on parameters
        Player player = this.players[this.idCurrentPlayer];
        Hero hero = this.players[this.idCurrentPlayer].getHero();
        Player playerEnemy = this.players[this.idCurrentPlayer ^ 1];
        Hero heroEnemy = this.players[this.idCurrentPlayer ^ 1].getHero();
        String message = null;
        
        if(Rule.checkManaPool(player.getManaPool(), card.getManaCost())) { // If the player has enough mana, play the card
        	if(card instanceof Minion) { // If it's a Minion
            	if(Rule.checkBoardSize(player.getBoard())) {
            		player.removeCardFromHand(idCard);
            		player.addCardToBoard((Minion)player.getHand().get(idCard));
            		Minion lastMinionPlayed = player.getBoard().lastElement();
            		Charge(lastMinionPlayed);
            		giveAttackAuraToOtherMinions(player.getBoard(), lastMinionPlayed); //give attack aura buff to other minions if it exists
            		getAttackAuraFromOtherMinions(player.getBoard(), lastMinionPlayed); // get attack auras buffs from other minions if they exist
            		return message;
            	} else {
            		message = "Vous avez atteint le nombre maximum de serviteurs sur le plateau !";
            		return message;
            	}
        	} else if (card instanceof Spell) {
        		player.removeCardFromHand(idCard);
        		Spell spell = (Spell) card;
        		
        		//Drawing effect
        		if (spell.getNbDraw() != 0) { // Add cards to hand if relevant
        			for (int i =0; i < spell.getNbDraw(); i++) { // Draw as many cards as needed
        					message = drawCard();
        			}
        		}
        		
        		//Armor buff effect
        		if (spell.getArmorBuff() != 0) { // Adds Armor points if relevant
        			hero.setArmorPoints(hero.getArmorPoints() + spell.getArmorBuff());
        		}
        		
        		//Summoning effect
        		if (spell.getNbSummon() != 0) { // Summon specific minions on the board
        			for (int i = 0; i < spell.getNbSummon(); i++) { // Summon as many minions as needed
	        			if (Rule.checkBoardSize(player.getBoard())) { // if board not full
							Minion minion = null;
							for(Minion invoc : this.invocations) { //get specific minion
								if (invoc.getId() == spell.getIdInvocation()) {
									minion = invoc;
								}
							}
							if (minion != null) {
								player.addCardToBoard(minion); 
								giveAttackAuraToOtherMinions(player.getBoard(), player.getBoard().lastElement());
								getAttackAuraFromOtherMinions(player.getBoard(), player.getBoard().lastElement()); //get attack aura buff from other minions if relevant
							} else {
								message = "Le minion n'a pas pu être invoqué";
							}
							return message;
	        			}
        			}
        		}
        		
        		//Attack buff effect
        		if (spell.getAttackBuff() !=0) { // Adds an attack buff to the target minions 
        			LinkedHashMap<String, Card> targets = targetsFromTargetString(player, playerEnemy, targetPlayer, idTarget, spell.getTarget());
        			for (Map.Entry<String, Card> entry : targets.entrySet()) {
        				if(entry.getValue() instanceof Minion) {
        					((Minion)entry.getValue()).setDamage(((Minion)entry.getValue()).getDamage() + spell.getAttackBuff());
        				}
        			}
        		}
        		
        		//Polymorph effect
        		if(spell.getPolymorph()) {
        			LinkedHashMap<String, Card> targets = targetsFromTargetString(player, playerEnemy, targetPlayer, idTarget, spell.getTarget());
        			for (Map.Entry<String, Card> entry : targets.entrySet()) {
        				if(entry.getValue() instanceof Minion) {
        					String keys[] = entry.getKey().split("_");
        					if (keys[0] == "0") {
        						removeAttackAuraFromMinions(player.getBoard(), (Minion) entry.getValue());
        						player.removeCardFromBoard(Integer.parseInt(keys[1]));
        						Minion minion = null;
        						for(Minion invoc : this.invocations) { //get specific minion
        							if (invoc.getId() == spell.getIdInvocation()) {
        								minion = invoc;
        							}
        						}
        						if (minion != null) {
        							player.addCardToBoard(minion); 
        							giveAttackAuraToOtherMinions(player.getBoard(), player.getBoard().lastElement());
        							getAttackAuraFromOtherMinions(player.getBoard(), player.getBoard().lastElement()); //get attack aura buff from other minions if relevant
        						} else {
        							message = "Le minion n'a pas pu être invoqué";
        						}
        						return message;
        						
        					} else if (keys[0] == "1") {
        						removeAttackAuraFromMinions(playerEnemy.getBoard(), (Minion) entry.getValue());
        						playerEnemy.removeCardFromBoard(Integer.parseInt(keys[1]));
        						Minion minion = null;
        						for(Minion invoc : this.invocations) { //get specific minion
        							if (invoc.getId() == spell.getIdInvocation()) {
        								minion = invoc;
        							}
        						}
        						if (minion != null) {
        							playerEnemy.addCardToBoard(minion); 
        							giveAttackAuraToOtherMinions(playerEnemy.getBoard(), playerEnemy.getBoard().lastElement());
        							getAttackAuraFromOtherMinions(playerEnemy.getBoard(), playerEnemy.getBoard().lastElement()); //get attack aura buff from other minions if relevant
        						} else {
        							message = "Le minion n'a pas pu être invoqué";
        						}
        						return message;
        					}
        				}
        			}
        		}
        		
        		//Damage effect
                if (spell.getDamage() != 0) {
                	LinkedHashMap<String, Card> targets = targetsFromTargetString(player, playerEnemy, targetPlayer, idTarget, spell.getTarget());
                	for (Map.Entry<String, Card> entry : targets.entrySet()) {
                		if (entry.getValue() instanceof Hero) {
                			((Hero) entry.getValue()).receiveDamage(spell.getDamage());
	                		if(!Rule.checkAlive(((Hero)entry.getValue()).getHealthPoints())){
	    						message = endGame();
	    						return message;
	                		}
                		} else if (entry.getValue() instanceof Minion) {
                			((Minion) entry.getValue()).receiveDamage(spell.getDamage());
                			if(!Rule.checkAlive(((Minion) entry.getValue()).getHealthPoints())) {
                				String keys[] = entry.getKey().split("_");
                				if (keys[0] == "0") {
	                				removeAttackAuraFromMinions(player.getBoard(), (Minion) entry.getValue()); //remove attack buff from other minions if relevant
	    							player.removeCardFromBoard(Integer.parseInt(keys[1])); //If minion healthPoints <= 0, remove minion from board
                				} else if (keys[1] == "1") {
                					removeAttackAuraFromMinions(playerEnemy.getBoard(), (Minion) entry.getValue()); //remove attack buff from other minions if relevant
	    							playerEnemy.removeCardFromBoard(Integer.parseInt(keys[1])); //If minion healthPoints <= 0, remove minion from board
                				}
                			}
                		}
                	}
                }
            }
            player.setManaPoolAfterPlay(card.getManaCost()); // Decrements manaCost from player's manaPool
            return message;
        } else {
        	message = "Vous n'avez pas assez de mana !";
        	return message;
        }
    }
	
	String heroPower(Player playerTarget, int idTarget) {
		Player currentPlayer = this.players[this.idCurrentPlayer];
		Hero heroCurrentPlayer = this.players[this.idCurrentPlayer].getHero();
		String message = null;
		
		if (!heroCurrentPlayer.getHeroPowerUsed()) { // If the hero has already used his power
			if (Rule.checkManaPool(currentPlayer.getManaPool(), Rule.MANA_COST_HERO_POWER)) {
				switch (heroCurrentPlayer.getType()) {//check class of the hero
				case "Warrior": //gives armor buff
					heroCurrentPlayer.setArmorPoints(heroCurrentPlayer.getArmorPoints() + heroCurrentPlayer.getArmorBuff());
					heroCurrentPlayer.setHeroPowerUsed(true);
					currentPlayer.setManaPoolAfterPlay(Rule.MANA_COST_HERO_POWER);
					return message;
				case "Mage":
					if (idTarget == -1){ //if target is a hero
						playerTarget.getHero().receiveDamage(heroCurrentPlayer.getDamage());
						heroCurrentPlayer.setHeroPowerUsed(true);
						currentPlayer.setManaPoolAfterPlay(Rule.MANA_COST_HERO_POWER);
						if(!Rule.checkAlive(playerTarget.getHero().getHealthPoints())){
							message = endGame();
							return message;
						}
						return message;
					} else { // if target is a minion
						playerTarget.getBoard().get(idTarget).receiveDamage(heroCurrentPlayer.getDamage()); //Inflicts damage to minion
						heroCurrentPlayer.setHeroPowerUsed(true);
						currentPlayer.setManaPoolAfterPlay(Rule.MANA_COST_HERO_POWER); //Decrement manaCost from manaPool
						if(!Rule.checkAlive(playerTarget.getBoard().get(idTarget).getHealthPoints())){
							removeAttackAuraFromMinions(playerTarget.getBoard(), playerTarget.getBoard().get(idTarget)); //remove attack buff from other minions if relevant
							playerTarget.removeCardFromBoard(idTarget); //If minion healthPoints <= 0, remove minion from board
						}
						return message;
					}
				case "Paladin":
					if (Rule.checkBoardSize(currentPlayer.getBoard())) { // if board is not full
						Minion minion = null;
						for(Minion invoc : this.invocations) { //get specific minion
							if (invoc.getId() == heroCurrentPlayer.getIdInvocation()) {
								minion = invoc;
							}
						}
						if (minion != null) {
							currentPlayer.addCardToBoard(minion); 
							giveAttackAuraToOtherMinions(currentPlayer.getBoard(), currentPlayer.getBoard().lastElement());
							getAttackAuraFromOtherMinions(currentPlayer.getBoard(), currentPlayer.getBoard().lastElement()); //get attack aura buff from other minions if relevant
						} else {
							message = "Le minion n'a pas pu être invoqué";
						}
						heroCurrentPlayer.setHeroPowerUsed(true);
						currentPlayer.setManaPoolAfterPlay(Rule.MANA_COST_HERO_POWER);
						return message;
					} else {
						message = "Vous avez atteint le nombre maximum de serviteurs sur le plateau !";
						return message;
					}
				default :
					message = "Impossible de récupérer la classe du héros !";
					heroCurrentPlayer.setHeroPowerUsed(true);
					currentPlayer.setManaPoolAfterPlay(Rule.MANA_COST_HERO_POWER);
					return message;
				}
			} else {
				message = "Vous n'avez pas assez de mana !";
				return message;
			}
		} else {
			message = "Vous avez déjà utilisé votre pouvoir héroïque durant ce tour !";
			return message;
		}
	}
	
	/**
	 * End the turn and switch the current player.
	 */
	void endTurn() {
		this.idCurrentPlayer ^= 1;
	}
	
	/**
	 * Ends the game
	 * @return message
	 */
	String endGame() {
		String message = "Game Over";
		return message;
	}
}

