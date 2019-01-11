package fr.univ.nantes.alma.engine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.util.Vector;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * The game engine performing the Game Methods.
 * 
 * @author Alexis Claveau, Martin Ars, Maud Van Dorssen, Alexis Loret
 * @version 0.0.1
 */
public class Engine implements EngineBridge {
  private HashMap<UUID, GameMethods> games;
  private HashMap<UUID, Player> players;
  private Player waitingPlayer;

  @Autowired
  private MinionCardRepository minionRepository;
  @Autowired
  private SpellCardRepository spellRepository;
  @Autowired
  private HeroCardRepository heroRepository;
  
  public Engine(MinionCardRepository minion, SpellCardRepository spell, HeroCardRepository hero) {
    this.minionRepository = minion;
    this.spellRepository = spell;
    this.heroRepository = hero;
    this.games = new HashMap<UUID, GameMethods>();
    this.players = new HashMap<UUID, Player>();
  }
    
  @Override
  public ArrayList<HeroCard> getHeros() {
    return this.heroRepository.findAll();
  }

  @Override
  public Player createPlayer(int idHero, String username) {
    HeroCard hero = this.retrieveHero(idHero);
    ArrayList<MinionCard> minion = retrieveMinions(hero.getType());
    ArrayList<SpellCard> spell = retrieveSpells(hero.getType());

    // Create deck
    ArrayList<AbstractCard> card = new ArrayList<AbstractCard>();
    card.addAll(minion);
    card.addAll(spell);
    AbstractCard[] deck = card.toArray(new AbstractCard[card.size()]);

    // Create hand
    Vector<AbstractCard> hand = new Vector<AbstractCard>();
    AbstractCard card1 = deck[(int)(Math.random() * deck.length)];
    AbstractCard card2 = deck[(int)(Math.random() * deck.length)];
    AbstractCard card3 = deck[(int)(Math.random() * deck.length)];
    try {
      hand.add((AbstractCard)card1.clone());
      hand.add((AbstractCard)card2.clone());
      hand.add((AbstractCard)card3.clone());
    } catch (CloneNotSupportedException e) {
      e.printStackTrace();
    }

    Player player = new Player(UUID.randomUUID(), username, hero, deck, hand);
    this.players.put(player.getUuid(), player); // Put in hashmap
    return player;
  }

  @Override
  public GameMethods createGame(UUID uuidPlayer) {
    GameMethods game = null;
    if (this.waitingPlayer == null) {
      if (this.players.containsKey(uuidPlayer)) {
        this.waitingPlayer = this.players.get(uuidPlayer);
      }
    } else {
      if (this.players.containsKey(uuidPlayer)) {
        game = new GameMethods(UUID.randomUUID(), this.waitingPlayer, 
            this.players.get(uuidPlayer), retrieveInvocations());
        this.players.remove(uuidPlayer);
        if(this.players.containsKey(this.waitingPlayer.getUuid())) {
            this.players.remove(this.waitingPlayer.getUuid());
        }
        this.waitingPlayer = null;
        this.games.put(game.getIdGame(), game);
      }
    }
    return game;
}
    
  @Override
  public GameMethods endTurn(UUID uuidGame) {
    games.get(uuidGame).endTurn();
    return games.get(uuidGame);
  }

  @Override
  public GameMethods playCard(UUID uuidGame, int idCard, UUID uuidPlayer, int idTarget) throws EngineException {
    try {
      if (games.get(uuidGame).getOtherPlayer().getUuid() == uuidPlayer) {
        games.get(uuidGame).playCard(idCard, games.get(uuidGame).getOtherPlayer(), idTarget);
      } else {
        games.get(uuidGame).playCard(idCard, games.get(uuidGame).getCurrentPlayer(), idTarget);
      }
    } catch (EngineException e) {
      throw e;
    }
    return games.get(uuidGame);
  }

  @Override
  public GameMethods heroPower(UUID uuidGame, UUID uuidPlayer, int idTarget) throws EngineException {
    try {
      if (games.get(uuidGame).getCurrentPlayer().getUuid() == uuidPlayer) {
        games.get(uuidGame).heroPower(games.get(uuidGame).getCurrentPlayer(), idTarget);
      } else {
        games.get(uuidGame).heroPower(games.get(uuidGame).getOtherPlayer(), idTarget);
      }
    } catch (EngineException e) {
      throw e;
    }
    return games.get(uuidGame);
  }

  @Override
  public GameMethods attack(UUID uuidGame, int idAttack, int idTarget) throws EngineException {
    try {
      games.get(uuidGame).attack(idAttack, idTarget);
    } catch (EngineException e) {
      throw e;
    }
    return games.get(uuidGame);
  }

  /**
   * Get all the Minions a Hero can use. 
   * @param heroType the type of Hero
   * @return the list of Minions
   */
  public ArrayList<MinionCard> retrieveMinions(String heroType) {
    ArrayList<MinionCard> commonList = this.minionRepository.findByType("common");
    ArrayList<MinionCard> heroList = this.minionRepository.findByType(heroType);
    heroList.addAll(commonList);
    return heroList;
  }

  /**
   * Get all Spells a Hero can use. 
   * @param heroType the type of Hero
   * @return the list of Spells
   */
  public ArrayList<SpellCard> retrieveSpells(String heroType) {
    ArrayList<SpellCard> commonList = this.spellRepository.findByType("common");
    ArrayList<SpellCard> heroList = this.spellRepository.findByType(heroType);
    heroList.addAll(commonList);
    return heroList;
  }

  /**
   * Get all the invocations. 
   * @return the list of invocations
   */
  public ArrayList<MinionCard> retrieveInvocations() {
    return this.minionRepository.findByType("invocation");
  }
  
  /**
   * Get the hero. 
   * @return the hero
   */
  public HeroCard retrieveHero(int hero) {
    if (this.heroRepository.findById(hero) != null) {
      return this.heroRepository.findById(hero);
    }
    return this.heroRepository.findById(1);
  }
}
