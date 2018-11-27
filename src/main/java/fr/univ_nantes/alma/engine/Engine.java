package fr.univ_nantes.alma.engine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

/**
 * 
 * @author Alexis Claveau, Martin Ars, Maud Van Dorssen, Alexis Loret
 * @version 0.0.1
 */
public class Engine implements EngineBridge {
	private HashMap<UUID, Game> games = new HashMap<UUID, Game>();
	public ArrayList<Hero> getHeros() { // get to the bdd
		ArrayList<Hero> heros = null;
		return heros;
	}

	public Player createPlayer(int idHero, String username) {
		UUID idPlayer = new UUID(0, 10000);
		Player player = new Player(idPlayer, username, this.getHeros().get(idHero));
		return player;
	}
	
	public Game createGame(UUID uuidGame, Player player1, Player player2) {
		Game game = new Game(uuidGame, player1, player2);
		this.games.put(uuidGame, game);
		return game;
	}

	public void endTurn(UUID uuidGame) {
		games.get(uuidGame).endTurn();
	}

	public void playCard(UUID uuidGame, int idCard) {
		games.get(uuidGame).playCard(idCard);

	}


	public void heroPower(UUID uuidGame, Player player, int idTarget) {
		games.get(uuidGame).heroPower(player, idTarget);

	}

	public void attack(UUID uuidGame, int idAttack, int idTarget) {
		games.get(uuidGame).attack(idAttack, idTarget);

	}

}
