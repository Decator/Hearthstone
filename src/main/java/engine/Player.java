package engine;

import java.util.UUID;
import java.util.Vector;

/**
 * 
 * @author Alexis Claveau, Martin Ars, Maud Van Dorssen, Alexis Loret
 * @version 0.0.1
 */
public class Player {
	private UUID uuid;
	private String username;
	private Hero hero;
	private int manaPool;
	private int manaMaxTurn;
	private Card[] deck;
	private Vector<Card> hand;
	private Vector<Card> board;
	
	Player(UUID uuid, String username, Hero hero) {
		this.uuid = uuid;
		this.username = username;
		this.hero = hero;
		this.manaPool = 0;
		this.manaMaxTurn = 1;
		this.deck = null; //TODO
		this.hand = new Vector<Card>();
		this.board = new Vector<Card>();
	}
	
	public UUID getUUID() {
		return this.uuid;
	}
	
	public String getUsrname() {
		return this.username;
	}
	
	public Hero getHero() {
		return this.hero;
	}
	
	public int getManaPool() {
		return this.manaPool;
	}
	
	public int getManaMaxTurn() {
		return this.manaMaxTurn;
	}
	
	public Card[] getDeck() {
		return this.deck;
	}
	
	public Vector<Card> getHand() {
		return this.hand;
	}
	
	public Vector<Card> getBoard() {
		return this.board;
	}
	
	void setManaPoll(int mana) {
		this.manaPool = mana;
	}
	
	void setManaMaxTurn() {
		if(Rule.checkManaTurn(this.manaMaxTurn))
			this.manaMaxTurn++;
		else
			throws new EngineException();
	}
}
