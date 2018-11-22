package engine;

/**
 * 
 * @author Alexis Claveau, Martin Ars, Maud Van Dorssen, Alexis Loret
 * @version 0.0.1
 */
class Rule {

    final int MANA_MAX = 10;
    final int MANA_COST_HERO_POWER = 2;
    final int MAX_HAND_SIZE = 10;
    final int MAX_BOARD_SIZE = 7;
    final int MAX_HERO_HEALTH_POINTS = 30;

    boolean checkManaTurn(int manaMaxTurn) {
		if(manaMaxTurn >= MANA_MAX) {
		    return true;
        }
		else return false;
	}

	boolean checkHandSize(int handSize) {
        if(handSize >= MAX_HAND_SIZE) {
            return true;
        }
        else return false;
    }

    boolean checkBoardSize(int boardSize) {
        if(boardSize >= MAX_BOARD_SIZE) {
            return true;
        }
        else return false;
    }

    boolean checkHealthPoints(int healthPoints) {
        if(healthPoints >= MAX_HERO_HEALTH_POINTS) {
            return true;
        }
        else return false;
    }

    boolean checkManaPool(int manaPool, int manaCost) {
        // Je vois pas ce que c'est
    }
}
