package engine;

import java.util.ArrayList;
import java.util.UUID;

/**
 * The interface of the package engine.
 * The Client component can use it to run the game
 *
 * @author Alexis Claveau, Martin Ars, Maud Van Dorssen, Alexis Loret
 * @version 0.0.1
 */
public interface EngineBridge {
	
	/**
	 * Get a list of heros available 
	 * @return the list of heros
	 */
	public ArrayList<Hero> getHeros();
	
	/**
	 * Create a player with an hero's id and a pseudo
	 * @param idHero the id of the hero
	 * @param pseudo the pseudo of the player
	 * @return the player
	 */
	public Player createPlayer(int idHero, String pseudo);
	
	/**
	 * Create a instance of game with an uuid and two players
	 * @param uuidGame the uuid of the game
	 * @param player1 the player 1
	 * @param player2 the player 2
	 * @return the game
	 */
	public Game createGame(UUID uuidGame, Player player1, Player player2);
	
	/**
	 * Finish the turn
	 */
	public void endTurn();
	
	/**
	 * Play a card it's either put a card on the board (minion) or activate a spell
	 * @param idCard the id of the card
	 */
	public void playCard(int idCard);
	
	/**
	 * Active the hero's power and give the target, if the power doesn't need a target then idTarget is to -1
	 * @param idTarget the id of the target
	 */
	public void heroPower(int idTarget);
	
	/**
	 * Attack a card or a hero
	 * @param idAttack the id of the attacker
	 * @param idTarget the id of the target
	 */
	public void attack(int idAttack, int idTarget);
}
