package fr.univ_nantes.alma.engine;

import java.util.ArrayList;
import java.util.HashMap;
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
	public ArrayList<Hero> getHeros();
	
	/**
	 * Create a player with a hero id and a username. 
	 * @param idHero the id of the hero
	 * @param username the username of the player
	 * @return the player
	 */
	public Player createPlayer(int idHero, String username);
	
	/**
	 * Create a game with an uuid and two players. 
	 * @param uuidGame the uuid of the game
	 * @param player1 the first player
	 * @param player2 the second player
	 * @return the game
	 */
	public Game createGame(Player player1, Player player2);
	
	/**
	 * End the turn.
	 * @param uuidGame the id of the game 
	 */
	public void endTurn(UUID uuidGame);
	
	/**
	 * Play a card. Either put a minion card on the board or activate a spell card.
	 * @param uuidGame the id of the game
	 * @param idCard the id of the card
	 * @param idTarget the id of the target
	 */
	public void playCard(UUID uuidGame, int idCard, Player targetPlayer, int idTarget);
	
	/**
	 * Activate the hero's power.
	 * @param uuidGame the id of the game
	 * @param idTarget the id of the target, if the power doesn't need a target then set idTarget to -1
	 */
	public void heroPower(UUID uuidGame, Player player, int idTarget);
	
	/**
	 * Attack a card or a hero.
	 * @param uuidGame the id of the game
	 * @param idAttack the id of the attacker
	 * @param idTarget the id of the target
	 */
	public void attack(UUID uuidGame, int idAttack, int idTarget);
}
