package fr.univ_nantes.alma.engine;

import java.util.Vector;

/**
 * Manage all the game's rule.
 *
 * @author Alexis Claveau, Martin Ars, Maud Van Dorssen, Alexis Loret
 * @version 0.0.1
 */
class Rule {

    static final int MANA_MAX = 10;
    static final int MANA_COST_HERO_POWER = 2;
    static final int MAX_HAND_SIZE = 10;
    static final int MAX_BOARD_SIZE = 7;
    static final int MAX_HERO_HEALTH_POINTS = 30;

    /**
     * Check if the player has reached the maximum mana.
     * @param manaMaxTurn the mana of the player
     * @return true of false
     */
    static boolean checkManaTurn(int manaMaxTurn) {
		return manaMaxTurn < MANA_MAX;
	}

    /**
     * Check if the player has reached the maximum hand size.
     * @param handSize the hand's size
     * @return true of false
     */
	static boolean checkHandSize(Vector<Card> hand) {
        return hand.size() < MAX_HAND_SIZE;
    }

    /**
     * Check if the player has reached the maximum board size.
     * @param boardSize the board's size
     * @return true of false
     */
    static boolean checkBoardSize(Vector<Minion> board) {
        return board.size() < MAX_BOARD_SIZE;
    }

    /**
     * Check if the player has reached the maximum health points.
     * @param healthPoints the player's healthPoints
     * @return true of false
     */
    static boolean checkHealthPoints(int healthPoints) {
        return healthPoints < MAX_HERO_HEALTH_POINTS;
    }

    /**
     * Check if the player has enough manaPoints to cast his spell
     * @param manaCost the spell's cost
     * @param manaPool the player's manaPool
     * @return true of false
     */
   static boolean checkManaPool(int manaPool, int manaCost) {
        return manaPool - manaCost >= 0;
    }
   
   /**
    * Check if the minion or Hero is alive
    * @param healthPoints minion's/hero's health points
    * @return true or false
    */
   
   static boolean checkAlive(int healthPoints) {
	   return healthPoints > 0;
   }
}
