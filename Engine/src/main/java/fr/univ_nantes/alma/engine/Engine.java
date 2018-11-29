package fr.univ_nantes.alma.engine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;
import java.util.Vector;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * 
 * 
 * @author Alexis Claveau, Martin Ars, Maud Van Dorssen, Alexis Loret
 * @version 0.0.1
 */
@SpringBootApplication
public class Engine implements EngineBridge {
	private HashMap<UUID, Game> games = new HashMap<UUID, Game>();
	
	private MinionRepository minionRepository;
	private SpellRepository spellRepository;
	private HeroRepository heroRepository;
	
	public static void main(String[] args) {
		System.out.println("Run SpringApplication");
        SpringApplication.run(Engine.class, args);
    }
	
	@Bean
	public CommandLineRunner retrieveMinionRepository(MinionRepository repository) {
		return (args) -> {
			System.out.println("MinionRepository saved");
			this.minionRepository = repository;
		};
	}
	
	@Bean
	public CommandLineRunner retrieveSpellRepository(SpellRepository repository) {
		return (args) -> {
			System.out.println("SpellRepository saved");
			this.spellRepository = repository;
		};
	}
    
    @Bean
	public CommandLineRunner retrieveHeoRepository(HeroRepository repository) {
    	return (args) -> {
        	System.out.println("HeroRepository saved");
    		this.heroRepository = repository;
		};
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
		int random = (int)(Math.random() * deck.length);
		hand.add(deck[random]);
		
		return new Player(UUID.randomUUID(), username, hero, deck, hand);
	}
	
    @Override
    public Game createGame(Player player1, Player player2) {
		Game game = new Game(UUID.randomUUID(), player1, player2, retrieveInvocations());
		this.games.put(game.getIdGame(), game);
		return game;
	}
    
    @Override
	public void endTurn(UUID uuidGame) {
		games.get(uuidGame).endTurn();
	}
	
	@Override
	public void playCard(UUID uuidGame, int idCard) {
		try {
			games.get(uuidGame).playCard(idCard);
		} catch(EngineException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void heroPower(UUID uuidGame, Player player, int idTarget) {
		try {
			games.get(uuidGame).heroPower(player, idTarget);
		} catch (EngineException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void attack(UUID uuidGame, int idAttack, int idTarget) {
		try {
			games.get(uuidGame).attack(idAttack, idTarget);
		} catch (EngineException e) {
			e.printStackTrace();
		}
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
		return this.heroRepository.findById(hero);
	}
}
