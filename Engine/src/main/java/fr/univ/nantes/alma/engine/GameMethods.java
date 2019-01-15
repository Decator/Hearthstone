package fr.univ.nantes.alma.engine;

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
public class GameMethods {
  private UUID idGame;
  private Player currentPlayer;
  private Player otherPlayer;
  private boolean gameOver;
  private ArrayList<MinionCard> invocations;

  /**
   * Initialize the attributes of this class.
   * @param idGame the id of the game
   * @param player1 the first player of the game
   * @param player2 the second player of the game
   * @param invocations List of potential invocations
   */
  GameMethods(UUID idGame, Player player1, Player player2, ArrayList<MinionCard> invocations) {
    this.idGame = idGame;
    this.currentPlayer = player1;
    this.otherPlayer = player2;
    this.invocations = invocations;
    this.gameOver = false;
    initTurn();
  }
  
  /**
   * Empty Constructor for GameMethods.
   */
  GameMethods() {}
  
  /**
   * Get the id of the game.
   * @return the id of the game
   */
  public UUID getIdGame() {
    return this.idGame;
  }

  /**
   * Get the current player.
   * @return the player
   */
  public Player getCurrentPlayer() {
    return this.currentPlayer;
  }
  
  /**
   * Sets the current player.
   * @param currentPlayer the current player
   */
  public void setCurrentPlayer(Player currentPlayer) {
    this.currentPlayer = currentPlayer;
  }

  /**
   * Get the other player.
   * @return the player
   */
  public Player getOtherPlayer() {
    return this.otherPlayer;
  }
  
  /**
   * Sets the other player.
   * @param otherPlayer the other player
   */
  public void setOtherPlayer(Player otherPlayer) {
    this.otherPlayer = otherPlayer;
  }
  
  /**
   * Get the game over status of the game.
   * @return true if the game is over, else false
   */
  public boolean isGameOver() {
    return this.gameOver;
  }
  
  /**
   * Sets the gameOver status of the game.
   * @param gameOver true if the game is over, else false
   */
  public void setGameOver(boolean gameOver) {
    this.gameOver = gameOver;
  }
  
  /**
   * Get the list of potential invocations.
   * @return the list of potential invocations
   */
  public ArrayList<MinionCard> getInvocations() {
    return this.invocations;
  }
  
  /**
   * Sets the list of potential invocations.
   * @param invocations the list of potential invocations
   */
  public void setInvocations(ArrayList<MinionCard> invocations) {
    this.invocations = invocations;
  }
  
  /**
   * Play the card with the specified id.
   * @param uuidPlayer the uuid of the player who uses it
   * @param idCard the id of the card the player wants to play
   * @param targetPlayer the player that might be the target of the played card
   * @param idTarget the id of the target
   * @throws EngineException custom exception
   */
  void playCard(UUID uuidPlayer, int idCard, Player targetPlayer, int idTarget) 
      throws EngineException  {
    if (this.currentPlayer.getUuid().equals(uuidPlayer)) {
      AbstractCard card = null; // Create the card according to the id given on parameters
      HeroCard hero = this.currentPlayer.getHero();
      if (idCard >= 0 && idCard < this.currentPlayer.getHand().size()) {
        card = this.currentPlayer.getHand().get(idCard);
        // If the player has enough mana, play the card
        if (GameRuleUtil.checkManaPool(this.currentPlayer.getManaPool(), card.getManaCost())) { 
          if (card instanceof MinionCard) { // If it's a Minion
            summonMinionFromHand(idCard);
          } else if (card instanceof SpellCard) {
            this.currentPlayer.removeCardFromHand(idCard);
            SpellCard spell = (SpellCard) card;
            spellDrawingEffect(spell);
            spellArmorBuffing(hero, spell);
            spellSummoningEffect(spell);
            spellAttackBuffingEffect(targetPlayer, idTarget, spell);
            spellPolymorphingEffect(targetPlayer, idTarget, spell);
            spellDamageEffect(targetPlayer, idTarget, spell);
          } else {
            throw new EngineException("Ceci n'est pas une carte valide !");
          }
          // Decrements manaCost from player's manaPool
          this.currentPlayer.setManaPoolAfterPlay(card.getManaCost()); 
        } else {
          throw new EngineException("Vous n'avez pas assez de mana !");
        }
      } else {
        throw new EngineException("Votre carte est inexistante !");
      }
    } else {
      throw new EngineException("Ce n'est pas votre tour !");
    }
  }

  /**
   * Summons a minion from the nad of the player onto the board.
   * @param idCard Hand index of the minion to summon
   * @throws EngineException custom exception
   */
  void summonMinionFromHand(int idCard) throws EngineException {
    if (GameRuleUtil.checkBoardSize(this.currentPlayer.getBoard())) {
      this.currentPlayer.addCardToBoard((MinionCard)this.currentPlayer.getHand().get(idCard));
      this.currentPlayer.removeCardFromHand(idCard);
      MinionCard lastMinionPlayed = this.currentPlayer.getBoard().lastElement();
      charge(lastMinionPlayed);
      //give attack aura buff to other minions if it exists
      giveAttackAuraToOtherMinions(this.currentPlayer.getBoard(), lastMinionPlayed);
      // get attack auras buffs from other minions if they exist
      getAttackAuraFromOtherMinions(this.currentPlayer.getBoard(), lastMinionPlayed);
    } else {
      throw new EngineException("Vous avez atteint "
          + "le nombre maximum de serviteurs sur le plateau !");
    }
  }

  /**
   * Deals damage to targets depending on the spell.
   * @param targetPlayer player targeted by the spell
   * @param idTarget board in dex of the target of the spell
   * @param spell spell being cast
   */
  void spellDamageEffect(Player targetPlayer, int idTarget, SpellCard spell) {
    if (spell.getDamage() > 0) {
      LinkedHashMap<String, AbstractCard> targets = targetsFromTargetString(this.currentPlayer, 
          this.otherPlayer, targetPlayer, idTarget, spell.getTarget());
      for (Map.Entry<String, AbstractCard> entry : targets.entrySet()) {
        if (entry.getValue() instanceof HeroCard) {
          ((HeroCard) entry.getValue()).receiveDamage(spell.getDamage());
          if (!GameRuleUtil.checkAlive(((HeroCard)entry.getValue()).getHealthPoints())) {
            this.gameOver = true;
          }
        } else if (entry.getValue() instanceof MinionCard) {
          ((MinionCard) entry.getValue()).receiveDamage(spell.getDamage());
          if (!GameRuleUtil.checkAlive(((MinionCard) entry.getValue()).getHealthPoints())) {
            String[] keys = entry.getKey().split("_");
            if (keys[0].equals("0")) {
              //remove attack buff from other minions if relevant
              removeAttackAuraFromMinions(this.currentPlayer.getBoard(), 
                  (MinionCard) entry.getValue());
              //If minion healthPoints <= 0, remove minion from board
              this.currentPlayer.removeCardFromBoard(Integer.parseInt(keys[1]));
            } else if (keys[0].equals("1")) {
              //remove attack buff from other minions if relevant
              removeAttackAuraFromMinions(this.otherPlayer.getBoard(), 
                  (MinionCard) entry.getValue());
              //If minion healthPoints <= 0, remove minion from board
              this.otherPlayer.removeCardFromBoard(Integer.parseInt(keys[1]));
            }
          }
        }
      }
    }
  }

  /**
   * Polymorphs a specific minion.
   * @param targetPlayer player controlling the minion being polymorphed
   * @param idTarget board index of the minion being polymorphed
   * @param spell spell being cast
   * @throws EngineException custom exception
   */
  void spellPolymorphingEffect(Player targetPlayer, int idTarget, 
      SpellCard spell) throws EngineException {
    if (spell.isPolymorph()) {
      LinkedHashMap<String, AbstractCard> targets = targetsFromTargetString(this.currentPlayer, 
          this.otherPlayer, targetPlayer, idTarget, spell.getTarget());
      for (Map.Entry<String, AbstractCard> entry : targets.entrySet()) {
        if (entry.getValue() instanceof MinionCard) {
          String[] keys = entry.getKey().split("_");
          if (keys[0].equals("0")) {
            removeAttackAuraFromMinions(this.currentPlayer.getBoard(), 
                (MinionCard) entry.getValue());
            polymorph(this.currentPlayer, spell.getIdInvocation(), keys[1]);    
          } else if (keys[0].equals("1")) {
            removeAttackAuraFromMinions(this.otherPlayer.getBoard(), (MinionCard) entry.getValue());
            polymorph(this.otherPlayer, spell.getIdInvocation(), keys[1]);
          }
        }
      }
    }
  }

  /**
   * Buffs the attack of a minion according to the spell.
   * @param targetPlayer player controlling the minion
   * @param idTarget board index of the minion
   * @param spell the spell that is cast
   */
  void spellAttackBuffingEffect(Player targetPlayer, int idTarget, SpellCard spell) {
    if (spell.getAttackBuff() > 0) { // Adds an attack buff to the target minions 
      LinkedHashMap<String, AbstractCard> targets = targetsFromTargetString(this.currentPlayer, 
          this.otherPlayer, targetPlayer, idTarget, spell.getTarget());
      for (Map.Entry<String, AbstractCard> entry : targets.entrySet()) {
        if (entry.getValue() instanceof MinionCard) {
          ((MinionCard)entry.getValue()).setDamage(((MinionCard)entry.getValue()).getDamage() 
              + spell.getAttackBuff());
        }
      }
    }
  }

  /**
   * Summons minions according to the spell.
   * @param spell the spell that is cast
   * @throws EngineException custom exception
   */
  void spellSummoningEffect(SpellCard spell) throws EngineException {
    if (spell.getNbSummon() > 0 && !spell.isPolymorph()) { // Summon specific minions on the board
      for (int i = 0; i < spell.getNbSummon(); i++) { // Summon as many minions as needed
        summonMinion(this.currentPlayer, spell.getIdInvocation());
      }
    }
  }

  /**
   * Draws card according to the spell.
   * @param spell the spell that is cast
   * @throws EngineException custom exception
   */
  void spellDrawingEffect(SpellCard spell) throws EngineException {
    if (spell.getNbDraw() > 0) { // Add cards to hand if relevant
      for (int i = 0; i < spell.getNbDraw(); i++) { // Draw as many cards as needed
        drawCard();
      }
    }
  }

  /**
   * Gives armorPoints to the hero using the spell.
   * @param hero the hero using the spell.
   * @param spell the spell being used.
   */
  void spellArmorBuffing(HeroCard hero, SpellCard spell) {
    if (spell.getArmorBuff() > 0) { // Adds Armor points if relevant
      hero.setArmorPoints(hero.getArmorPoints() + spell.getArmorBuff());
    }
  }
  
  /**
   * Triggers the hero power of the current player based on its class.
   * @param uuidPlayer the uuid of the player who uses it
   * @param playerTarget the player that might be the target of the hero power
   * @param idTarget the specific target of the hero power
   * @throws EngineException custom exception
   */
  void heroPower(UUID uuidPlayer, Player playerTarget, int idTarget) throws EngineException {
    if (this.currentPlayer.getUuid().equals(uuidPlayer)) {
      HeroCard heroCurrentPlayer = this.currentPlayer.getHero();
      // If the hero has already used his power
      if (!heroCurrentPlayer.isHeroPowerUsed()) { 
        if (GameRuleUtil.checkManaPool(this.currentPlayer.getManaPool(), 
            GameRuleUtil.MANA_HERO_POWER)) {
          switch (heroCurrentPlayer.getType()) {
            case "warrior":
              heroCurrentPlayer.setArmorPoints(heroCurrentPlayer.getArmorPoints() 
                  + heroCurrentPlayer.getArmorBuff());
              heroCurrentPlayer.setHeroPowerUsed(true);
              this.currentPlayer.setManaPoolAfterPlay(GameRuleUtil.MANA_HERO_POWER);
              break;
            case "mage":
              mageHeroPower(playerTarget, idTarget, heroCurrentPlayer);
              heroCurrentPlayer.setHeroPowerUsed(true);
              this.currentPlayer.setManaPoolAfterPlay(GameRuleUtil.MANA_HERO_POWER);
              break;
            case "paladin":
              summonMinion(currentPlayer, heroCurrentPlayer.getIdInvocation());
              heroCurrentPlayer.setHeroPowerUsed(true);
              currentPlayer.setManaPoolAfterPlay(GameRuleUtil.MANA_HERO_POWER);
              break;
            default :
              heroCurrentPlayer.setHeroPowerUsed(true);
              currentPlayer.setManaPoolAfterPlay(GameRuleUtil.MANA_HERO_POWER);
              throw new EngineException("Impossible de récupérer la classe du héros !");
          }
        } else {
          throw new EngineException("Vous n'avez pas assez de mana !");
        }
      } else {
        throw new EngineException("Vous avez déjà utilisé votre pouvoir héroïque durant ce tour !");
      }
    } else {
      throw new EngineException("Ce n'est pas votre tour !");
    }
  }

  /**
   * Activates the hero power of the mage, thus dealing some damage to a target.
   * @param playerTarget player target of the hero power
   * @param idTarget board index of the actual target of the hero power
   * @param heroCurrentPlayer hero of the player using the hero power
   */
  void mageHeroPower(Player playerTarget, int idTarget, HeroCard heroCurrentPlayer) {
    LinkedHashMap<String, AbstractCard> targets = targetsFromTargetString(this.currentPlayer, 
        this.otherPlayer, playerTarget, idTarget, heroCurrentPlayer.getTarget());
    for (Map.Entry<String, AbstractCard> entry : targets.entrySet()) {
      if (entry.getValue() instanceof HeroCard) {
        ((HeroCard) entry.getValue()).receiveDamage(heroCurrentPlayer.getDamage());
        if (!GameRuleUtil.checkAlive(((HeroCard)entry.getValue()).getHealthPoints())) {
          this.gameOver = true;
        }
      } else if (entry.getValue() instanceof MinionCard) {
        ((MinionCard) entry.getValue()).receiveDamage(heroCurrentPlayer.getDamage());
        if (!GameRuleUtil.checkAlive(((MinionCard) entry.getValue()).getHealthPoints())) {
          String[] keys = entry.getKey().split("_");
          if (keys[0].equals("0")) { 
            removeAttackAuraFromMinions(this.currentPlayer.getBoard(), 
                (MinionCard) entry.getValue());
            this.currentPlayer.removeCardFromBoard(Integer.parseInt(keys[1]));
          } else if (keys[0].equals("1")) {
            removeAttackAuraFromMinions(this.otherPlayer.getBoard(), (MinionCard) entry.getValue());
            this.otherPlayer.removeCardFromBoard(Integer.parseInt(keys[1]));
          }
        }
      }
    }
  }
  
  /**
   * Attacks a target with an attacker given in parameter.
   * @param uuidPlayer the uuid of the player who uses it
   * @param idAttack the id of the attacker
   * @param idTarget the id of the target
   * @throws EngineException custom exception
   */
  void attack(UUID uuidPlayer, int idAttack, int idTarget) throws EngineException {
    if (this.currentPlayer.getUuid().equals(uuidPlayer)) {
      MinionCard minion = this.currentPlayer.getBoard().get(idAttack);
      HeroCard hero = this.currentPlayer.getHero();
      HeroCard heroEnemy = this.otherPlayer.getHero();
      if (minion.getDamage() > 0) {
        //Checks if the minion already attacked or not
        if (minion != null && !GameRuleUtil.checkMinionAttacked(minion)) {
          /* If no enemy minion has taunt and enemy Hero is target, 
          then attacking is possible*/
          if (!taunt(this.otherPlayer.getBoard()) && idTarget == -1) { 
            int damage = minion.getDamage();
            heroEnemy.receiveDamage(damage); //attacks the enemy Hero
            minion.setAttacked(true);
            lifesteal(hero, minion);
            if (!GameRuleUtil.checkAlive(heroEnemy.getHealthPoints())) {
              this.gameOver = true;
            }
            /* If no enemy minion has taunt, 
            or target minion has taunt, attacking is possible*/
          } else if (!taunt(this.otherPlayer.getBoard()) 
              || this.otherPlayer.getBoard().get(idTarget).isTaunt()) { 
            MinionCard victim = this.otherPlayer.getBoard().get(idTarget);
            if (victim != null) {
              minion.receiveDamage(victim.getDamage()); // minion takes victim's damage
              lifesteal(hero, minion);
              victim.receiveDamage(minion.getDamage()); //attacks the minion
              lifesteal(heroEnemy, victim);
              minion.setAttacked(true);
              if (!GameRuleUtil.checkAlive(minion.getHealthPoints())) {
                //removes attack buff from other minions if relevant
                removeAttackAuraFromMinions(this.currentPlayer.getBoard(), minion);
                this.currentPlayer.removeCardFromBoard(idAttack);
              }
              if (!GameRuleUtil.checkAlive(victim.getHealthPoints())) {
                //removes attack buff from other minions if relevant
                removeAttackAuraFromMinions(this.otherPlayer.getBoard(), victim);
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
      } else {
        throw new EngineException("Ce serviteur ne peut pas attaquer !");
      }
    } else {
      throw new EngineException("Ce n'est pas votre tour !");
    }
  }
  
  /**
   * Initializes manaMaxTurn, manaPool, heroPowerUsed and minionAttacked for 
   * the beginning of the turn.
   */
  void initTurn() {
    Player player = this.currentPlayer;
    HeroCard hero = this.currentPlayer.getHero();
    try {
      drawCard();
    } catch (EngineException e) {
      System.out.println(e.getMessage());
    } finally {
      if (GameRuleUtil.checkManaTurn(player.getManaMaxTurn())) {
        player.setManaMaxTurn();
      }
      player.setManaPoolForNewTurn();
      hero.setHeroPowerUsed(false);
      for (MinionCard minion : player.getBoard()) {
        minion.setAttacked(false);
      }
    }
  }
  
  /**
   * End the turn of the current player and switches to the other player.
   * @param uuidPlayer the uuid of the player who uses it
   * @throws EngineException custom exception
   */
  void endTurn(UUID uuidPlayer) throws EngineException {
    if (this.currentPlayer.getUuid().equals(uuidPlayer)) {
      Player tmp = this.currentPlayer;
      this.currentPlayer = this.otherPlayer;
      this.otherPlayer = tmp;
      initTurn();
    } else {
      throw new EngineException("Ce n'est pas votre tour !");
    }
  }
  
  /**
   * Draws a card from the player's deck and place it into his hand.
   * @throws EngineException custom exception
   */
  void drawCard() throws EngineException {
    Player player = this.currentPlayer;
    int random = (int)(player.getDeck().length * Math.random());
    if (GameRuleUtil.checkHandSize(player.getHand())) {
      player.addCardToHand(player.getDeck()[random]);
    } else {
      throw new EngineException("Votre main est pleine !");
    }
  }
  
  

  /**
   * Gives attack aura buff from played minion to other minions on the board.
   * @param board board where the minion is summoned
   * @param lastMinionPlayed minion that was just summoned
   */
  void giveAttackAuraToOtherMinions(Vector<MinionCard> board, MinionCard lastMinionPlayed) {
    if (lastMinionPlayed.getAttackBuffAura() > 0) { //if minion has attack aura
      int attackBuffAura = lastMinionPlayed.getAttackBuffAura();
      // gives attack aura to all allies minions but itself
      for (int i = 0; i < board.size() - 1; i++) {
        MinionCard minion = board.get(i);
        minion.setDamage(minion.getDamage() + attackBuffAura);
      }
    }
  }
  
  /**
   * Gets the cumulated attack aura buffs of other minions and add them to the played minion.
   * @param board the board of the player playing the minion
   * @param lastMinionPlayed minion that was just summoned
   */
  void getAttackAuraFromOtherMinions(Vector<MinionCard> board, MinionCard lastMinionPlayed) {
    int attackBuffAura = 0;
    //if there are minions with attack auras on the board
    for (int i = 0; i < board.size() - 1; i++) {
      MinionCard minion = board.get(i);
      if (minion.getAttackBuffAura() > 0) {
        //add auras up except aura generated by played minion
        attackBuffAura += minion.getAttackBuffAura();
      }
    }
    // gives attack aura to played minion
    lastMinionPlayed.setDamage(lastMinionPlayed.getDamage() + attackBuffAura); 
  }
  
  /**
   * Removes the attack aura buff from all minions of the board 
   * upon the death of the minion generating the buff.
   * @param board the board containing the dying minion
   * @param dyingMinion minion that just died
   */
  void removeAttackAuraFromMinions(Vector<MinionCard> board, MinionCard dyingMinion) {
    if (dyingMinion.getAttackBuffAura() > 0) { //if minion has attack aura
      for (int i = 0; i < board.size(); i++) {
        board.get(i).setDamage(board.get(i).getDamage() - dyingMinion.getAttackBuffAura());
      }
    }
  }
  
  /**
   * Heals the hero for the damage inflicted by its lifestealing minion.
   * @param hero the hero healed by lifesteal
   * @param minion the minion having lifesteal
   */
  void lifesteal(HeroCard hero, MinionCard minion) {
    if (minion.isLifesteal()) { //check for lifesteal
      //heals for the damage inflicted by the minion
      hero.receiveHealing(minion.getDamage()); 
    }
  }
  
  /**
   * If played minion has charge, it can attack right away.
   * @param lastMinionPlayed minion that was last summoned
   */
  void charge(MinionCard lastMinionPlayed) {
    if (lastMinionPlayed.isCharge()) { // If played minion has charge
      lastMinionPlayed.setAttacked(false); //can attack right away
    } else  {
      lastMinionPlayed.setAttacked(true); // else has to wait a turn
    }
  }
  
  /**
   * Checks if a minion in the board of interest has at least one minion with taunt.
   * @param board the board that needs to be checked for taunt minions
   * @return true if a minion as taunt, else false
   */
  boolean taunt(Vector<MinionCard> board) {
    for (MinionCard minionEnemy : board) { // Check if an enemy minion has taunt
      if (minionEnemy.isTaunt()) {
        return true;
      }
    }
    return false;
  }
  
  /**
   * Summons a minion on the player's board based on idSummon.
   * @param player the player getting the minion on his board
   * @param idSummon the id of the summoned minion
   * @throws EngineException custom exception
   */
  void summonMinion(Player player, int idSummon) throws EngineException {
    if (GameRuleUtil.checkBoardSize(player.getBoard())) { // if board is not full
      MinionCard minion = null;
      for (MinionCard invoc : this.invocations) { //get specific minion
        if (invoc.getId() == idSummon) {
          try {
            minion = (MinionCard) invoc.clone();
          } catch (CloneNotSupportedException e) {
            e.printStackTrace();
          }
        }
      }
      if (minion != null) {
        player.addCardToBoard(minion);
        giveAttackAuraToOtherMinions(player.getBoard(), player.getBoard().lastElement());
        getAttackAuraFromOtherMinions(player.getBoard(), player.getBoard().lastElement());
      } else {
        throw new EngineException("Le minion n'a pas pu être invoqué !");
      }
    }
  }

  /**
   * Summons the minion replacing the polymorphed minion 
   * at a specified location on the board of the affected player.
   * @param player the player affected by the spell
   * @param idSummon the id of the minion replacing the polymorphed minion
   * @param indexBoard the board index where the minion needs to be summoned
   * @throws EngineException custom exception
   */
  void polymorph(Player player, int idSummon, String indexBoard) throws EngineException {
    MinionCard minion = null;
    for (MinionCard invoc : this.invocations) { //get specific minion
      if (invoc.getId() == idSummon) {
        try {
          minion = (MinionCard) invoc.clone();
        } catch (CloneNotSupportedException e) {
          e.printStackTrace();
        }
      }
    }
    if (minion != null) {
      player.addCardToBoard(minion, Integer.parseInt(indexBoard)); 
      giveAttackAuraToOtherMinions(player.getBoard(), 
          player.getBoard().get(Integer.parseInt(indexBoard)));
      getAttackAuraFromOtherMinions(player.getBoard(), 
          player.getBoard().get(Integer.parseInt(indexBoard)));
    } else {
      throw new EngineException("Le minion n'a pas pu être invoqué !");
    }
  }
  
  /**
   * Returns a list of targets based on the String of a specific spell.
   * @param player the player playing the spell
   * @param playerEnemy the enemy player that may be affected by the spell
   * @param targetPlayer the specific player impacted by the spell if specified
   * @param idTarget the specific target of the spell if specified
   * @param spellTarget the string detailing the potential targets of a given spell
   * @return LinkedHashMap containing the targets of the spell
   */
  public LinkedHashMap<String, AbstractCard> targetsFromTargetString(Player player, 
      Player playerEnemy, Player targetPlayer, int idTarget, String spellTarget) {
    String[] splitString = spellTarget.split("_");
    HeroCard hero = player.getHero();
    HeroCard heroEnemy = playerEnemy.getHero();
    LinkedHashMap<String, AbstractCard> targets = new LinkedHashMap<String, AbstractCard>();
    switch (splitString[0]) {
      case "minion" :
        switch (splitString[1]) {
          case "all" :
            switch (splitString[2]) {
              case "enemy" :
                for (int i = playerEnemy.getBoard().size() - 1; i >= 0; i--) {
                  targets.put("1_" + String.valueOf(i), playerEnemy.getBoard().get(i));
                }
                break;
              case "ally" :
                for (int i = player.getBoard().size() - 1; i >= 0; i--) {
                  targets.put("0_" + String.valueOf(i), player.getBoard().get(i));
                }
                break;
              case "all" :
                for (int i = playerEnemy.getBoard().size() - 1; i >= 0; i--) {
                  targets.put("1_" + String.valueOf(i),playerEnemy.getBoard().get(i));
                }
                for (int i = player.getBoard().size() - 1; i >= 0; i--) {
                  targets.put("0_" + String.valueOf(i), player.getBoard().get(i));
                }
                break;
              default:
                break;
            }
            break;
          case "1" :
            switch (splitString[2]) {
              case "enemy" :
                targets.put("1_" + String.valueOf(idTarget), playerEnemy.getBoard().get(idTarget));
                break;
              case "ally" :
                targets.put("0_" + String.valueOf(idTarget), player.getBoard().get(idTarget));
                break;
              case "all" :
                if (targetPlayer.getUuid().equals(player.getUuid())) {
                  targets.put("0_" + String.valueOf(idTarget), player.getBoard().get(idTarget));
                } else if (targetPlayer.getUuid().equals(playerEnemy.getUuid())) {
                  targets.put("1_" + String.valueOf(idTarget), 
                      playerEnemy.getBoard().get(idTarget));
                }
                break;
              default:
                break;
            }
            break;
          default:
            break;
        }
        break;
      case "all" :
        switch (splitString[1]) {
          case "all" :
            switch (splitString[2]) {
              case "enemy" :
                for (int i = playerEnemy.getBoard().size() - 1; i >= 0; i--) {
                  targets.put("1_" + String.valueOf(i),playerEnemy.getBoard().get(i));
                }
                targets.put("1",heroEnemy);
                break;
              case "ally" :
                for (int i = player.getBoard().size() - 1; i >= 0; i--) {
                  targets.put("0_" + String.valueOf(i), player.getBoard().get(i));
                }
                targets.put("0", hero);
                break;
              case "all" :
                for (int i = player.getBoard().size() - 1; i >= 0; i--) {
                  targets.put("0_" + String.valueOf(i), player.getBoard().get(i));
                }
                for (int i = playerEnemy.getBoard().size() - 1; i >= 0; i--) {
                  targets.put("1_" + String.valueOf(i), playerEnemy.getBoard().get(i));
                }
                targets.put("0",hero);
                targets.put("1", heroEnemy);
                break;
              default:
                break;
            }
            break;
          case "1" :
            switch (splitString[2]) {
              case "enemy" :
                if (idTarget == -1) {
                  targets.put("1", heroEnemy);
                } else {
                  targets.put("1_" + String.valueOf(idTarget), 
                      playerEnemy.getBoard().get(idTarget));
                }
                break;
              case "ally" :
                if (idTarget == -1) {
                  targets.put("0", hero);
                } else {
                  targets.put("0_" + String.valueOf(idTarget), player.getBoard().get(idTarget));
                }
                break;
              case "all" :
                if (targetPlayer.getUuid().equals(player.getUuid())) {
                  if (idTarget == -1) {
                    targets.put("0", hero);
                  } else {
                    targets.put("0_" + String.valueOf(idTarget), player.getBoard().get(idTarget));
                  }
                } else if (targetPlayer.getUuid().equals(playerEnemy.getUuid())) {
                  if (idTarget == -1) {
                    targets.put("1", heroEnemy);
                  } else {
                    targets.put("1_" + String.valueOf(idTarget), 
                        playerEnemy.getBoard().get(idTarget));
                  }
                }
                break;
              default:
                break;
            }
            break;
          default:
            break;
        }
        break;
      default:
        break;
    }
    return targets;
  }
}

