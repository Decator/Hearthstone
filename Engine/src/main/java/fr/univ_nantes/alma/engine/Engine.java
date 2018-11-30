package fr.univ_nantes.alma.engine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.UUID;
import java.util.Vector;

/**
 * 
 * 
 * @author Alexis Claveau, Martin Ars, Maud Van Dorssen, Alexis Loret
 * @version 0.0.1
 */
public class Engine implements EngineBridge {
	private HashMap<UUID, Game> games;
	private HashMap<UUID, Player> players;
	private Player waitingPlayer;
	
	private MinionRepository minionRepository;
	private SpellRepository spellRepository;
	private HeroRepository heroRepository;
	
	public Engine(MinionRepository minion, SpellRepository spell, HeroRepository hero) {
		this.minionRepository = minion;
		this.spellRepository = spell;
		this.heroRepository = hero;
		this.games = new HashMap<UUID, Game>();
		this.players = new HashMap<UUID, Player>();
	}
    
    @Override
	public ArrayList<Hero> getHeros() {
    	return this.heroRepository.findAll();
	}
    
    @Override
    public Player createPlayer(int idHero, String username) {
    	Hero hero = this.retrieveHero(idHero);
    	ArrayList<Minion> minion = retrieveMinions(hero.getType());
    	ArrayList<Spell> spell = retrieveSpells(hero.getType());
    	
    	// Create deck
    	ArrayList<Card> card = new ArrayList<Card>();
    	card.addAll(minion);
    	card.addAll(spell);
		Card[] deck = card.toArray(new Card[card.size()]);
		
		// Create hand
		Vector<Card> hand = new Vector<Card>();
		hand.add(deck[(int)(Math.random() * deck.length)]);
		hand.add(deck[(int)(Math.random() * deck.length)]);
		hand.add(deck[(int)(Math.random() * deck.length)]);
		
		Player player = new Player(UUID.randomUUID(), username, hero, deck, hand);
		this.players.put(player.getUUID(), player); // Put in hashmap
		return player;
	}
	
    @Override
    public Game createGame(UUID uuidPlayer) {
    	Game game = null;
    	if(this.waitingPlayer == null) {
    		if(this.players.containsKey(uuidPlayer)) {
    			this.waitingPlayer = this.players.get(uuidPlayer);
    		}
    	} else {
    		if(this.players.containsKey(uuidPlayer)) {
    			game = new Game(UUID.randomUUID(), this.waitingPlayer, this.players.get(uuidPlayer), retrieveInvocations());
    			this.players.remove(uuidPlayer);
    			this.players.remove(this.waitingPlayer.getUUID());
    			this.waitingPlayer = null;
    			this.games.put(game.getIdGame(), game);
    		}
    	}
		return game;
	}
    
    @Override
	public void endTurn(UUID uuidGame) {
		games.get(uuidGame).endTurn();
	}
	
	@Override
	public void playCard(UUID uuidGame, int idCard, Player targetPlayer, int idTarget) {
		games.get(uuidGame).playCard(idCard, targetPlayer, idTarget);
	}
	
	@Override
	public void heroPower(UUID uuidGame, Player player, int idTarget) {
		games.get(uuidGame).heroPower(player, idTarget);
	}
	
	@Override
	public void attack(UUID uuidGame, int idAttack, int idTarget) {
		games.get(uuidGame).attack(idAttack, idTarget);
	}
	
	/**
	 * Get all the Minions a Hero can use. 
	 * @param heroType the type of Hero
	 * @return the list of Minions
	 */
	public ArrayList<Minion> retrieveMinions(String heroType){
		ArrayList<Minion> commonList = this.minionRepository.findByType("common");
		ArrayList<Minion> heroList = this.minionRepository.findByType(heroType);
		heroList.addAll(commonList);
		return heroList;
	}
	
	/**
	 * Get all Spells a Hero can use. 
	 * @param heroType the type of Hero
	 * @return the list of Spells
	 */
	public ArrayList<Spell> retrieveSpells(String heroType){
		ArrayList<Spell> commonList = this.spellRepository.findByType("common");
		ArrayList<Spell> heroList = this.spellRepository.findByType(heroType);
		heroList.addAll(commonList);
		return heroList;
	}
	
	/**
	 * Get all the invocations. 
	 * @return the list of invocations
	 */
	public ArrayList<Minion> retrieveInvocations(){
		return this.minionRepository.findByType("invocation");
	}
	
	/**
	 * Get the hero. 
	 * @return the hero
	 */
    public Hero retrieveHero(int hero) {
    	if(this.heroRepository.findById(hero) != null ) {
    		this.heroRepository.findById(hero);
    	}
    	return this.heroRepository.findById(1);
	}
}
