package fr.univ_nantes.alma.engine;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.UUID;
import java.util.Vector;

import org.junit.Test;

import fr.univ_nantes.alma.engine.Game;
import fr.univ_nantes.alma.engine.Hero;
import fr.univ_nantes.alma.engine.Minion;
import fr.univ_nantes.alma.engine.Player;
import fr.univ_nantes.alma.engine.Spell;

public class EngineTest {

	@Test
	public void testMinion() {
		Minion minion = new Minion(1, "common", "Chevaucheur de loup", 3, 2, "Charge", 1, false, true, false, 0);
		
		// Getters
		assertEquals(minion.getId(), 1);
		assertEquals(minion.getType(), "common");
		assertEquals(minion.getName(), "Chevaucheur de loup");
		assertEquals(minion.getManaCost(), 3);
		assertEquals(minion.getDamage(), 2);
		assertEquals(minion.getDescription(), "Charge");
		assertEquals(minion.getHealthPoints(), 1);
		assertFalse(minion.getTaunt());
		assertTrue(minion.getLifesteal());
		assertFalse(minion.getCharge());
		assertEquals(minion.getAttackBuffAura(), 0);
		assertTrue(minion.getAttacked());
		
		// Setters
		minion.setAttacked(false);
		assertFalse(minion.getAttacked());
		minion.setDamage(3);
		assertEquals(minion.getDamage(), 3);
		minion.receiveDamage(2);
		assertEquals(minion.getHealthPoints(), -1);
	}
	
	@Test
	public void testSpell() {
		Spell spell = new Spell(16, "mage", "Métamorphose", 4, 0, "Transforme un serviteur en mouton 1/1.", 1, 13, 0, 0, 0, true, "minion_1_enemy");
		
		// Getters
		assertEquals(spell.getId(), 16);
		assertEquals(spell.getType(), "mage");
		assertEquals(spell.getName(), "Métamorphose");
		assertEquals(spell.getManaCost(), 4);
		assertEquals(spell.getDamage(), 0);
		assertEquals(spell.getDescription(), "Transforme un serviteur en mouton 1/1.");
		assertEquals(spell.getNbSummon(), 1);
		assertEquals(spell.getIdInvocation(), 13);
		assertEquals(spell.getAttackBuff(), 0);
		assertEquals(spell.getArmorBuff(), 0);
		assertEquals(spell.getNbDraw(), 0);
		assertTrue(spell.getPolymorph());
		assertEquals(spell.getTarget(), "minion_1_enemy");
	}
	
	@Test
	public void testHero() {
		Hero hero = new Hero(1, "mage", "Jaina", 1, 0, 0, 0, "Boule de feu : Inflige 1 point de dégâts.");
		
		// Getters
		assertEquals(hero.getId(), 1);
		assertEquals(hero.getType(), "mage");
		assertEquals(hero.getName(), "Jaina");
		assertEquals(hero.getHealthPoints(), 30);
		assertEquals(hero.getArmorPoints(), 0);
		assertFalse(hero.getHeroPowerUsed());
		assertEquals(hero.getDamage(), 1);
		assertEquals(hero.getNbSummon(), 0);
		assertEquals(hero.getIdInvocation(), 0);
		assertEquals(hero.getArmorBuff(), 0);
		assertEquals(hero.getDescription(), "Boule de feu : Inflige 1 point de dégâts.");
		
		// Setters
		hero.setHealthPoints(25);
		assertEquals(hero.getHealthPoints(), 25);
		hero.setArmorPoints(2);
		assertEquals(hero.getArmorPoints(), 2);
		hero.setHeroPowerUsed(true);
		assertTrue(hero.getHeroPowerUsed());
		hero.receiveDamage(2);
		assertEquals(hero.getHealthPoints(), 23);
		hero.receiveHealing(2);
		assertEquals(hero.getHealthPoints(), 25);
	}
	
	@Test
	public void testPlayer() {
		// Create hero
		Hero hero = new Hero(1, "mage", "Jaina", 1, 0, 0, 0, "Boule de feu : Inflige 1 point de dégâts.");
		
		// Create deck
		Card[] deck = new Card[4];
		deck[0] = new Minion(4, "common", "Sanglier brocheroc", 1, 1, null, 1, false, false, false, 0);
		deck[1] = new Minion(5, "common", "Soldat du compté-de-l'or", 1, 1, "Provocation", 2, true, false, false, 0);
		deck[2] = new Spell(14, "mage", "Image miroir", 1, 0, "Invoque deux serviteurs 0/2 avec provocation.", 2, 11, 0, 0, 0, false, null);
		deck[3] = new Spell(15, "mage", "Explosion des arcanes", 2, 1, "Inflige 1 point de dégâts à tous les serviteurs adverses.", 0, 0, 0, 0, 0, false, "minion_all_enemy");
		
		// Create hand
		Vector<Card> hand = new Vector<Card>();
		hand.add(deck[(int)(Math.random() * deck.length)]);
		hand.add(deck[(int)(Math.random() * deck.length)]);
		hand.add(deck[(int)(Math.random() * deck.length)]);
		
		UUID uuid = UUID.randomUUID();
		
		Player player = new Player(uuid, "Bob", hero, deck, hand);
		
		// Getters
		assertEquals(player.getUUID(), uuid);
		assertEquals(player.getUsername(), "Bob");
		assertEquals(player.getHero(), hero);
		assertEquals(player.getManaPool(), 0);
		assertEquals(player.getManaMaxTurn(), 1);
		assertEquals(player.getDeck(), deck);
		assertEquals(player.getHand(), hand);
		assertEquals(player.getBoard(), new Vector<Minion>());
		assertEquals(player.getHero(), hero);
		
		// Setters
		player.setManaPoolForNewTurn();
		assertEquals(player.getManaPool(), 1);
		player.setManaPoolAfterPlay(1);
		assertEquals(player.getManaPool(), 0);
		player.setManaMaxTurn();
		assertEquals(player.getManaMaxTurn(), 2);
		Minion minion = new Minion(5, "common", "Soldat du compté-de-l'or", 1, 1, "Provocation", 2, true, false, false, 0);
		player.addCardToHand(minion);
		assertTrue(player.getHand().contains(minion));
		player.addCardToBoard(minion);
		assertTrue(player.getBoard().contains(minion));
		player.addCardToBoard(minion, 1);
		assertEquals(player.getBoard().get(1), minion);
		player.removeCardFromHand(3);
		assertEquals(player.getHand().size(), 3);
		player.removeCardFromBoard(1);
		assertEquals(player.getBoard().size(), 1);
	}
	
	@Test
	public void testGame() {
		Game game = new Game(UUID.randomUUID(), null, null, null);
		
		
	}
}
