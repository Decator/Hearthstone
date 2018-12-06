package fr.univ_nantes.alma.engine;

import java.util.ArrayList;
import java.util.LinkedHashMap;
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
	private Player currentPlayer;
	private Player otherPlayer;
	private ArrayList<Minion> invocations;

	/**
	 * Initialize the attributes of this class.
	 * @param idGame the id of the game
	 * @param player1 the first player of the game
	 * @param player2 the second player of the game
	 */
	Game(UUID idGame, Player player1, Player player2, ArrayList<Minion> invocations) {
		this.idGame = idGame;
		this.currentPlayer = player1;
		this.otherPlayer = player2;
		this.invocations = invocations;
		initTurn();
	}
	
	/**
     * Play the card with the specified id.
     * @param idCard the id of the card the player wants to play
     * @param targetPlayer the player that might be the target of the played card
     * @param idTarget the id of the target
	 * @throws EngineException 
     */
	void playCard(int idCard, Player targetPlayer, int idTarget) throws EngineException  {
        Card card = this.currentPlayer.getHand().get(idCard); // Create the card according to the id given on parameters
        Hero hero = this.currentPlayer.getHero();
        
        if(Rule.checkManaPool(this.currentPlayer.getManaPool(), card.getManaCost())) { // If the player has enough mana, play the card
        	if(idCard >= 0 && idCard < this.currentPlayer.getHand().size()) { // A REVOIIIIR
	        	if(card instanceof Minion) { // If it's a Minion
	            	if(Rule.checkBoardSize(this.currentPlayer.getBoard())) {
	            		this.currentPlayer.addCardToBoard((Minion)this.currentPlayer.getHand().get(idCard));
	            		this.currentPlayer.removeCardFromHand(idCard);
	            		Minion lastMinionPlayed = this.currentPlayer.getBoard().lastElement();
	            		Charge(lastMinionPlayed);
	            		giveAttackAuraToOtherMinions(this.currentPlayer.getBoard(), lastMinionPlayed); //give attack aura buff to other minions if it exists
	            		getAttackAuraFromOtherMinions(this.currentPlayer.getBoard(), lastMinionPlayed); // get attack auras buffs from other minions if they exist
	            	} else {
	            		throw new EngineException("Vous avez atteint le nombre maximum de serviteurs sur le plateau !");
	            	}
	        	} else if (card instanceof Spell) {
	        		this.currentPlayer.removeCardFromHand(idCard);
	        		Spell spell = (Spell) card;
	        		
	        		//Drawing effect
	        		if (spell.getNbDraw() > 0) { // Add cards to hand if relevant
	        			for (int i =0; i < spell.getNbDraw(); i++) { // Draw as many cards as needed
	        					drawCard();
	        			}
	        		}
	        		
	        		//Armor buff effect
	        		if (spell.getArmorBuff() > 0) { // Adds Armor points if relevant
	        			hero.setArmorPoints(hero.getArmorPoints() + spell.getArmorBuff());
	        		}
	        		
	        		//Summoning effect
	        		if (spell.getNbSummon() > 0) { // Summon specific minions on the board
	        			for (int i = 0; i < spell.getNbSummon(); i++) { // Summon as many minions as needed
		        			summonMinion(this.currentPlayer, spell.getIdInvocation());
	        			}
	        		}
	        		
	        		//Attack buff effect
	        		if (spell.getAttackBuff() > 0) { // Adds an attack buff to the target minions 
	        			LinkedHashMap<String, Card> targets = targetsFromTargetString(this.currentPlayer, this.otherPlayer, targetPlayer, idTarget, spell.getTarget());
	        			for (Map.Entry<String, Card> entry : targets.entrySet()) {
	        				if(entry.getValue() instanceof Minion) {
	        					((Minion)entry.getValue()).setDamage(((Minion)entry.getValue()).getDamage() + spell.getAttackBuff());
	        				}
	        			}
	        		}
	        		
	        		//Polymorph effect
	        		if(spell.getPolymorph()) {
	        			LinkedHashMap<String, Card> targets = targetsFromTargetString(this.currentPlayer, this.otherPlayer, targetPlayer, idTarget, spell.getTarget());
	        			for (Map.Entry<String, Card> entry : targets.entrySet()) {
	        				if(entry.getValue() instanceof Minion) {
	        					String keys[] = entry.getKey().split("_");
	        					if (keys[0] == "0") {
	        						removeAttackAuraFromMinions(this.currentPlayer.getBoard(), (Minion) entry.getValue());
	        						this.currentPlayer.removeCardFromBoard(Integer.parseInt(keys[1]));
	        						polymorph(this.currentPlayer, spell.getIdInvocation(), keys[1]);
	        						
	        					} else if (keys[0] == "1") {
	        						removeAttackAuraFromMinions(this.otherPlayer.getBoard(), (Minion) entry.getValue());
	        						this.otherPlayer.removeCardFromBoard(Integer.parseInt(keys[1]));
	        						polymorph(this.otherPlayer, spell.getIdInvocation(), keys[1]);
	        					}
	        				}
	        			}
	        		}
	        		
	        		//Damage effect
	                if (spell.getDamage() > 0) {
	                	LinkedHashMap<String, Card> targets = targetsFromTargetString(this.currentPlayer, this.otherPlayer, targetPlayer, idTarget, spell.getTarget());
	                	for (Map.Entry<String, Card> entry : targets.entrySet()) {
	                		if (entry.getValue() instanceof Hero) {
	                			((Hero) entry.getValue()).receiveDamage(spell.getDamage());
		                		if(!Rule.checkAlive(((Hero)entry.getValue()).getHealthPoints())){
		    						endGame();
		                		}
	                		} else if (entry.getValue() instanceof Minion) {
	                			((Minion) entry.getValue()).receiveDamage(spell.getDamage());
	                			if(!Rule.checkAlive(((Minion) entry.getValue()).getHealthPoints())) {
	                				String keys[] = entry.getKey().split("_");
	                				if (keys[0] == "0") {
		                				removeAttackAuraFromMinions(this.currentPlayer.getBoard(), (Minion) entry.getValue()); //remove attack buff from other minions if relevant
		                				this.currentPlayer.removeCardFromBoard(Integer.parseInt(keys[1])); //If minion healthPoints <= 0, remove minion from board
	                				} else if (keys[1] == "1") {
	                					removeAttackAuraFromMinions(this.otherPlayer.getBoard(), (Minion) entry.getValue()); //remove attack buff from other minions if relevant
	                					this.otherPlayer.removeCardFromBoard(Integer.parseInt(keys[1])); //If minion healthPoints <= 0, remove minion from board
	                				}
	                			}
	                		}
	                	}
	                }
	            }
	        	this.currentPlayer.setManaPoolAfterPlay(card.getManaCost()); // Decrements manaCost from player's manaPool
        	}
        	else {
        		throw new EngineException("Votre carte est inexistante");
        	}
        } else {
        	throw new EngineException("Vous n'avez pas assez de mana !");
        }
    }
	
	/**
	 * Triggers the hero power of the current player based on its class
	 * @param playerTarget the player that might be the target of the hero power
	 * @param idTarget the specific target of the hero power
	 * @throws EngineException 
	 */
	void heroPower(Player playerTarget, int idTarget) throws EngineException {
		Hero heroCurrentPlayer = this.currentPlayer.getHero();
		
		if (!heroCurrentPlayer.getHeroPowerUsed()) { // If the hero has already used his power
			if (Rule.checkManaPool(this.currentPlayer.getManaPool(), Rule.MANA_COST_HERO_POWER)) {
				switch (heroCurrentPlayer.getType()) {//check class of the hero
				case "warrior": //gives armor buff
					heroCurrentPlayer.setArmorPoints(heroCurrentPlayer.getArmorPoints() + heroCurrentPlayer.getArmorBuff());
					heroCurrentPlayer.setHeroPowerUsed(true);
					this.currentPlayer.setManaPoolAfterPlay(Rule.MANA_COST_HERO_POWER);
					break;
				case "mage":
					if (idTarget == -1){ //if target is a hero
						playerTarget.getHero().receiveDamage(heroCurrentPlayer.getDamage());
						heroCurrentPlayer.setHeroPowerUsed(true);
						this.currentPlayer.setManaPoolAfterPlay(Rule.MANA_COST_HERO_POWER);
						if(!Rule.checkAlive(playerTarget.getHero().getHealthPoints())){
							endGame();
						}
					} else { // if target is a minion
						playerTarget.getBoard().get(idTarget).receiveDamage(heroCurrentPlayer.getDamage()); //Inflicts damage to minion
						heroCurrentPlayer.setHeroPowerUsed(true);
						currentPlayer.setManaPoolAfterPlay(Rule.MANA_COST_HERO_POWER); //Decrement manaCost from manaPool
						if(!Rule.checkAlive(playerTarget.getBoard().get(idTarget).getHealthPoints())){
							removeAttackAuraFromMinions(playerTarget.getBoard(), playerTarget.getBoard().get(idTarget)); //remove attack buff from other minions if relevant
							playerTarget.removeCardFromBoard(idTarget); //If minion healthPoints <= 0, remove minion from board
						}
					}
					break;
				case "paladin":
					summonMinion(currentPlayer, heroCurrentPlayer.getIdInvocation());
					heroCurrentPlayer.setHeroPowerUsed(true);
					currentPlayer.setManaPoolAfterPlay(Rule.MANA_COST_HERO_POWER);
					break;
				default :
					heroCurrentPlayer.setHeroPowerUsed(true);
					currentPlayer.setManaPoolAfterPlay(Rule.MANA_COST_HERO_POWER);
					throw new EngineException("Impossible de récupérer la classe du héros !");
				}
			} else {
				throw new EngineException("Vous n'avez pas assez de mana !");
			}
		} else {
			throw new EngineException("Vous avez déjà utilisé votre pouvoir héroïque durant ce tour !");
		}
	}
	
	/**
	 * Attacks a target with an attacker given in parameter
	 * @param idAttack the id of the attacker
	 * @param idTarget the id of the target
	 * @throws EngineException 
	 */
	void attack(int idAttack, int idTarget) throws EngineException {
		Minion minion = this.currentPlayer.getBoard().get(idAttack);
		Hero hero = this.currentPlayer.getHero();
		Hero heroEnemy = this.otherPlayer.getHero();
		
		//Checks if the minion already attacked or not
		if(minion != null && !Rule.checkMinionAttacked(minion)) {
			if (!Taunt(this.otherPlayer.getBoard()) && idTarget == -1) { // If no enemy minion has taunt and enemy Hero is target, then attacking is possible
				int damage = minion.getDamage();
				heroEnemy.receiveDamage(damage); //attacks the enemy Hero
				minion.setAttacked(true);
				LifeSteal(hero, minion);
				if(!Rule.checkAlive(heroEnemy.getHealthPoints())) {
					endGame();
				}
			} else if (!Taunt(this.otherPlayer.getBoard()) || this.otherPlayer.getBoard().get(idTarget).getTaunt()) { // If no enemy minion has taunt, or target minion has taunt, attacking is possible
				Minion victim = this.otherPlayer.getBoard().get(idTarget);
				if(victim != null) {
					minion.receiveDamage(victim.getDamage()); // minion takes victim's damage
					LifeSteal(hero, minion);
					victim.receiveDamage(minion.getDamage()); //attacks the minion
					LifeSteal(heroEnemy, victim);
					minion.setAttacked(true);
					if(!Rule.checkAlive(minion.getHealthPoints())) {
						removeAttackAuraFromMinions(this.currentPlayer.getBoard(), minion); //removes attack buff from other minions if relevant
						this.currentPlayer.removeCardFromBoard(idAttack);
					}
					if(!Rule.checkAlive(victim.getHealthPoints())) {
						removeAttackAuraFromMinions(this.otherPlayer.getBoard(), victim); //removes attack buff from other minions if relevant
						this.otherPlayer.removeCardFromBoard(idTarget);
					}
				} else {
					throw new EngineException("Le serviteur que vous cherchez à attaquer n'existe pas !");
				}
			} else {
				throw new EngineException("Cible incorrecte, un serviteur adverse a provocation !");
			}
		} else {
			throw new EngineException("Ce serviteur a déjà attaqué durant ce tour !");
		}
	}
	
	/**
	 * Initializes manaMaxTurn, manaPool, heroPowerUsed and minionAttacked for the beginning of the turn
	 */
	void initTurn() {
		Player player = this.currentPlayer;
		Hero hero = this.currentPlayer.getHero();
		try {
			drawCard();
		} catch (EngineException e) {
			System.out.println(e.getMessage());
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
	 * End the turn of the current player and switches to the other player
	 */
	void endTurn(){
		Player tmp = this.currentPlayer;
		this.currentPlayer = this.otherPlayer;
		this.otherPlayer = tmp;
		initTurn();
	}
	
	/**
	 * Ends the game
	 * @throws EngineException 
	 */
	void endGame() throws EngineException {
		throw new EngineException("Game Over");
	}
	
	/**
	 * Draws a card from the player's deck and place it into his hand.
	 * @throws EngineException 
	 */
	void drawCard() throws EngineException {
		Player player = this.currentPlayer;
		int random = (int)(player.getDeck().length * Math.random());
		
		if(Rule.checkHandSize(player.getHand())) {
			player.addCardToHand(player.getDeck()[random]);
		} else {
			throw new EngineException("Votre main est pleine !");
		}
	}
	
	/**
	 * Returns a list of targets based on the String of a specific spell
	 * @param player the player playing the spell
	 * @param playerEnemy the enemy player that may be affected by the spell
	 * @param targetPlayer the specific player impacted by the spell if specified
	 * @param idTarget the specific target of the spell if specified
	 * @param spellTarget the string detailing the potential targets of a given spell
	 * @return LinkedHashMap<String, Card> containing the targets of the spell
	 */
	LinkedHashMap<String, Card> targetsFromTargetString(Player player, Player playerEnemy, Player targetPlayer, int idTarget, String spellTarget){
		String[] splitString = spellTarget.split("_");
		Hero hero = player.getHero();
		Hero heroEnemy = playerEnemy.getHero();
		LinkedHashMap<String, Card> targets = new LinkedHashMap<String, Card>();
		switch (splitString[0]) {
		case "minion" :
			switch (splitString[1]) {
			case "all" :
				switch (splitString[2]) {
				case "enemy" :
					for (int i = playerEnemy.getBoard().size() -1 ; i >= 0; i--) {
						targets.put("1_" + String.valueOf(i), playerEnemy.getBoard().get(i));
					}
				case "ally" :
					for (int i = player.getBoard().size() -1 ; i >= 0; i--) {
						targets.put("0_" + String.valueOf(i), player.getBoard().get(i));
					}
				case "all" :
					for (int i = playerEnemy.getBoard().size() -1 ; i >= 0; i--) {
						targets.put("1_" + String.valueOf(i),playerEnemy.getBoard().get(i));
					}
					for (int i = player.getBoard().size() -1 ; i >= 0; i--) {
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
					for (int i = playerEnemy.getBoard().size() -1 ; i >= 0; i--) {
						targets.put("1_" + String.valueOf(i),playerEnemy.getBoard().get(i));
					}
					targets.put("1",heroEnemy);
				case "ally" :
					for (int i = player.getBoard().size() -1 ; i >= 0; i--) {
						targets.put("0_" + String.valueOf(i), player.getBoard().get(i));
					}
					targets.put("0", hero);
				case "all" :
					for (int i = player.getBoard().size() -1 ; i >= 0; i--) {
						targets.put("0_" + String.valueOf(i), player.getBoard().get(i));
					}
					for (int i = playerEnemy.getBoard().size() -1 ; i >= 0; i--) {
						targets.put("1_" + String.valueOf(i), playerEnemy.getBoard().get(i));
					}
					targets.put("0",hero);
					targets.put("1", heroEnemy);
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
	 * Checks if a minion in the board of interest has at least one minion with taunt
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
	 * Summons a minion on the plaer's board based on idSummon
	 * @param player the player getting the minion on his board
	 * @param idSummon the id of the summoned minion
	 * @throws EngineException 
	 */
	void summonMinion(Player player, int idSummon) throws EngineException {
		if (Rule.checkBoardSize(player.getBoard())) { // if board is not full
			Minion minion = null;
			for(Minion invoc : this.invocations) { //get specific minion
				if (invoc.getId() == idSummon) {
					minion = invoc;
				}
			}
			if (minion != null) {
				player.addCardToBoard(minion); 
				giveAttackAuraToOtherMinions(player.getBoard(), player.getBoard().lastElement());
				getAttackAuraFromOtherMinions(player.getBoard(), player.getBoard().lastElement()); //get attack aura buff from other minions if relevant
			} else {
				throw new EngineException("Le minion n'a pas pu être invoqué");
			}
		} else {
			throw new EngineException("Vous avez atteint le nombre maximum de serviteurs sur le plateau !");
		}
	}
	
	/**
	 * Summons the minion replacing the polymorphed minion at a specified location on the board of the affected player 
	 * @param player the player affected by the spell
	 * @param idSummon the id of the minion replacing the polymorphed minion
	 * @param indexBoard the board index where the minion needs to be summoned
	 * @throws EngineException 
	 */
	void polymorph(Player player, int idSummon, String indexBoard) throws EngineException {
		Minion minion = null;
		for(Minion invoc : this.invocations) { //get specific minion
			if (invoc.getId() == idSummon) {
				minion = invoc;
			}
		}
		if (minion != null) {
			player.addCardToBoard(minion, Integer.parseInt(indexBoard)); 
			giveAttackAuraToOtherMinions(player.getBoard(), player.getBoard().get(Integer.parseInt(indexBoard)));
			getAttackAuraFromOtherMinions(player.getBoard(), player.getBoard().get(Integer.parseInt(indexBoard))); //get attack aura buff from other minions if relevant
		} else {
			throw new EngineException("Le minion n'a pas pu être invoqué");
		}
	}
	
	/**
	 * Get the id of the game
	 * @return the id of the game
	 */
	public UUID getIdGame() {
		return this.idGame;
	}
	
	/**
	 * Get the current player
	 * @return the player
	 */
	public Player getCurrentPlayer() {
		return this.currentPlayer;
	}
	
	/**
	 * Get the other player
	 * @return the player
	 */
	public Player getOtherPlayer() {
		return this.otherPlayer;
	}
}

