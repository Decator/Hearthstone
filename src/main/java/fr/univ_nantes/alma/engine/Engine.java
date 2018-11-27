package fr.univ_nantes.alma.engine;

import java.util.ArrayList;
import java.util.UUID;

/**
 * 
 * @author Alexis Claveau, Martin Ars, Maud Van Dorssen, Alexis Loret
 * @version 0.0.1
 */
public class Engine implements EngineBridge {
	private ArrayList<Game> games = new ArrayList<Game>();
	public ArrayList<Hero> getHeros() {
		// TODO Auto-generated method stub
		return null;
	}

	public Player createPlayer(int idHero, String username) {
		UUID idPlayer = new UUID(0, 10000);
		Player player = new Player(idPlayer, username, this.getHeros().get(idHero));
		return player;
	}
	
	public Game createGame(UUID uuidGame, Player player1, Player player2) {
		Game game = new Game(uuidGame, player1, player2);
		this.games.add(game);
		return game;
	}

	public void endTurn() {
		Game game = // la game du joueur
		game.endTurn();
	}

	public void playCard(int idCard) {
		Game game = // la game du joueur
		game.playCard(idCard);

	}

	public void heroPower(int idTarget) {
		// TODO Auto-generated method stub

	}

	public void attack(int idAttack, int idTarget) {
		// TODO Auto-generated method stub

	}

}
