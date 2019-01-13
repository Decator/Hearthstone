package fr.univ.nantes.alma.engine;

import java.util.Arrays;
import java.util.UUID;
import java.util.Vector;

/**
 * The complete description of a player.
 *
 * @author Alexis Claveau, Martin Ars, Maud Van Dorssen, Alexis Loret
 * @version 0.0.1
 */
public class Player {
  private UUID uuid;
  private String username;
  private HeroCard hero;
  private int manaPool;
  private int manaMaxTurn;
  private AbstractCard[] deck;
  private Vector<AbstractCard> hand;
  private Vector<MinionCard> board;

  /**
   * Initialize the attributes of this class.
   * @param uuid the id of the game
   * @param username the username of the user
   * @param hero the hero of the player
   */

  Player(UUID uuid, String username, HeroCard hero, AbstractCard[] deck, Vector<AbstractCard> hand) {
    this.uuid = uuid;
    this.username = username;
    this.hero = hero;
    this.manaPool = 0;
    this.manaMaxTurn = 0;
    this.deck = Arrays.copyOf(deck, deck.length);
    this.hand = hand;
    this.board = new Vector<MinionCard>();
  }
  
  /**
   * Empty Constructor for Player.
   */
  public Player() {} 

  /**
   * Return the id of the game.
   * @return the id of the game
   */
  public UUID getUuid() {
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
  public HeroCard getHero() {
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
  public AbstractCard[] getDeck() {
    return Arrays.copyOf(this.deck, this.deck.length);
  }

  /**
   * Return the hand of the player.
   * @return he hand of the player
   */
  public Vector<AbstractCard> getHand() {
    return this.hand;
  }

  /**
   * Return the board of the player.
   * @return the board of the player
   */
  public Vector<MinionCard> getBoard() {
    return this.board;
  }

  public void setUuid(UUID uuid) {
    this.uuid = uuid;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public void setHero(HeroCard hero) {
    this.hero = hero;
  }

  public void setManaPool(int manaPool) {
    this.manaPool = manaPool;
  }

  public void setManaMaxTurn(int manaMaxTurn) {
    this.manaMaxTurn = manaMaxTurn;
  }

  public void setDeck(AbstractCard[] deck) {
    this.deck = deck;
  }

  public void setHand(Vector<AbstractCard> hand) {
    this.hand = hand;
  }

  public void setBoard(Vector<MinionCard> board) {
    this.board = board;
  }

  /**
   * Set the mana pool at manaMaxTurn.
   */
  void setManaPoolForNewTurn() {
    this.manaPool = manaMaxTurn;
  }
    
  /**
   * Set the mana pool at manaMaxTurn.
   */
  void setManaPoolAfterPlay(int mana) {
    this.manaPool -= mana;
  }

  /**
   * Increase the mana pool each turn until it reaches 10.
   */
  void setManaMaxTurn() {
    this.manaMaxTurn++;
  }

  /**
   * Add a card to the player's hand.
   * @param card the card to add
   */
  void addCardToHand(AbstractCard card) {
    try {
      this.getHand().add((AbstractCard)card.clone());
    } catch (CloneNotSupportedException e) {
      e.printStackTrace();
    } 
  }

  /**
   * Add a minion to the player's board.
   * @param minion the minion to add
   */
  void addCardToBoard(MinionCard minion) {
    this.getBoard().add(minion);
  }

  /**
   * Add a minion at a specific index to the player's board.
   * @param minion the minion to add
   */
  void addCardToBoard(MinionCard minion, int boardCardIndex) {
    this.getBoard().set(boardCardIndex, minion);
  }

  /**
   * Remove a card from the player's hand.
   * @param handCardIndex the id of the card to remove
   */
  void removeCardFromHand(int handCardIndex) {
    this.getHand().remove(handCardIndex);
  }

  /**
   * Remove a minion from the player's board.
   * @param boardCardIndex the id of the minion to remove
   */
  void removeCardFromBoard(int boardCardIndex) {
    this.getBoard().remove(boardCardIndex);
  }
}