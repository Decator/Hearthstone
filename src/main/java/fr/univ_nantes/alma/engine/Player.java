package fr.univ_nantes.alma.engine;

import java.util.UUID;
import java.util.Vector;

/**
 * The copmlete description of a player.
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

	/**
	 * Initialize the attributes of this class.
	 * @param uuid the id of the game
	 * @param username the username of the user
	 * @param hero the hero of the player
	 */
	
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

	/**
	 * Return the id of the game.
	 * @return the id of the game
	 */
	public UUID getUUID() {
		return this.uuid;
	}

	/**
	 * Return the username.
	 * @return the username
	 */
	public String getUsername() {
		return this.username;
	}

	/**
	 * Return the Hero the user has chosen.
	 * @return the hero
	 */
	public Hero getHero() {
		return this.hero;
	}

	/**
	 * Return the manaPool of the player.
	 * @return the manaPool of the player
	 */
	public int getManaPool() {
		return this.manaPool;
	}

	/**
	 * Return the ManaMaxTurn of the player.
	 * @return the ManaMaxTurn of the player
	 */
	public int getManaMaxTurn() {
		return this.manaMaxTurn;
	}

	/**
	 * Return the deck of the player.
	 * @return the deck of the player
	 */
	public Card[] getDeck() {
		return this.deck;
	}

	/**
	 * Return the hand of the player.
	 * @return he hand of the player
	 */
	public Vector<Card> getHand() {
		return this.hand;
	}

	/**
	 * Return the board of the player.
	 * @return the board of the player
	 */
	public Vector<Card> getBoard() {
		return this.board;
	}

	/**
	 * Set the mana pool at manaMaxTurn.
	 */
	void setManaPool() {
		this.manaPool = this.manaMaxTurn;
	}

	/**
	 * Increase the mana pool each turn until it reaches 10
	 */
	void setManaMaxTurn() {
		if(Rule.checkManaTurn(this.manaMaxTurn))
			this.manaMaxTurn++;
	}

	/**
	 * Add a card to the player's hand
	 * @param card the card to add
	 * @throws EngineException 
	 */
	void addCardToHand(Card card) throws EngineException {
		if(Rule.checkHandSize(this.getHand())) {
			this.getHand().add(card);
		} else {
		 	throw new EngineException("Error adding the card to hand : player has reached the maximum hand size.");
		}
	}

	/**
	 * Add a minion to the player's board
	 * @param minion the minion to add
	 * @throws EngineException 
	 */
	void addCardToBoard(Minion minion) throws EngineException {
		if(Rule.checkBoardSize(this.getBoard())) {
			this.getBoard().add(minion);
		} else {
	 		throw new EngineException("Error adding card to board : player has reached the maximum board size.");
		}
	}

	/**
	 * Remove a card from the player's hand
	 * @param id the card to remove
	 * @throws EngineException 
	 */
	void removeCardFromHand(Card card) throws EngineException {
		if(!this.getHand().remove(card)) {
	 		throw new EngineException("Error removing the card from the hand.");
		}
	}

	/**
	 * Remove a minion from the player's board
	 * @param id the minion to remove
	 * @throws EngineException 
	 */
	void removeCardFromBoard(Minion minion) throws EngineException {
		if(this.getBoard().remove(minion)) {
			throw new EngineException("Error removing the minion from the board.");
		}
	}
}
