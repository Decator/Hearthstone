package fr.univ.nantes.alma.engine;

import java.util.ArrayList;
import java.util.UUID;

/**
 * The interface of the package engine. 
 * The Client component can use it to run the game. 
 *
 * @author Alexis Claveau, Martin Ars, Maud Van Dorssen, Alexis Loret
 * @version 0.0.1
 */
public interface EngineBridge {

  /**
   * Get the list of available heros.
   * @return the list of available heros
   */
  public ArrayList<HeroCard> getHeros();

  /**
   * Create a player with a hero id and a username. 
   * @param idHero the id of the hero
   * @param username the username of the player
   * @return the player
   */
  public Player createPlayer(int idHero, String username);
  
  /**
   * Create a game with an uuid and two players. 
   * @param uuidPlayer the uuid of the player
   * @return the game
   */
  public GameMethods createGame(UUID uuidPlayer);

  /**
   * End the turn.
   * @param uuidGame the id of the game 
   * @param uuidPlayer the uuid of the player who uses it
   * @return the game
   */
  public GameMethods endTurn(UUID uuidGame, UUID uuidPlayer) throws EngineException;

  /**
   * Play a card. Either put a minion card on the board or activate a spell card.
   * @param uuidGame the id of the game
   * @param uuidPlayer the uuid of the player who uses it
   * @param idCard the id of the card
   * @param uuidTargetPlayer the player choose (for some spells)
   * @param idTarget the id of the target
   * @return the game
   * @throws EngineException a custom exception
   */
  public GameMethods playCard(UUID uuidGame, UUID uuidPlayer, int idCard, UUID uuidTargetPlayer, 
      int idTarget) throws EngineException;

  /**
   * Activate the hero's power.
   * @param uuidGame the id of the game
   * @param uuidPlayer the uuid of the player who uses it
   * @param uuidTargetPlayer the chosen player (for some power)
   * @param idTarget the id of the target, if the power doesn't need a target then set idTarget to 1
   * @return the game
   * @throws EngineException a custom exception
   */
  public GameMethods heroPower(UUID uuidGame, UUID uuidPlayer, UUID uuidTargetPlayer, int idTarget) 
      throws EngineException;

  /**
   * Attack a card or a hero.
   * @param uuidGame the id of the game
   * @param uuidPlayer the uuid of the player who uses it
   * @param idAttack the id of the attacker
   * @param uuidTarget uuid of the targeted player
   * @param idTarget the id of the target
   * @return the game
   * @throws EngineException a custom exception
   */
  public GameMethods attack(UUID uuidGame, UUID uuidPlayer, int idAttack, 
      UUID uuidTarget, int idTarget) 
      throws EngineException;
  
  /**
   * Remove the game when it is finish.
   * @param uuidGame the uuid of the game
   * @param uuidPlayer1 the uuid of the first player
   * @param uuidPlayer2 the uuid of the second player
   */
  public void endGame(UUID uuidGame, UUID uuidPlayer1, UUID uuidPlayer2);
}
