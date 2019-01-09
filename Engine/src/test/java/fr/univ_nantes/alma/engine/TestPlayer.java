package fr.univ_nantes.alma.engine;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.UUID;
import java.util.Vector;

import org.junit.Test;

public class TestPlayer {
	
	@Test
	public void testPlayer() {
		// Create hero
		Hero hero = new Hero(1, "mage", "Jaina", 1, 0, 0, 0, "Boule de feu : Inflige 1 point de dégâts.", "all_1_all");
		
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
		assertEquals(player.getManaMaxTurn(), 0);
		assertEquals(player.getDeck(), deck);
		assertEquals(player.getHand(), hand);
		assertEquals(player.getBoard(), new Vector<Minion>());
		
		// Setters
		player.setManaPoolForNewTurn();
		assertEquals(player.getManaPool(), 0);
		assertEquals(player.getManaMaxTurn(), 0);
		player.setManaMaxTurn();
		assertEquals(player.getManaMaxTurn(), 1);
		assertEquals(player.getManaPool(), 0);
		player.setManaPoolForNewTurn();
		assertEquals(player.getManaMaxTurn(), 1);
		assertEquals(player.getManaPool(), 1);
		player.setManaPoolAfterPlay(1);
		assertEquals(player.getManaPool(), 0);
		assertEquals(player.getManaMaxTurn(), 1);
		Minion minion1 = new Minion(5, "common", "Soldat du compté-de-l'or", 1, 1, "Provocation", 2, true, false, false, 0);
		Minion minion2 = new Minion(4, "common", "Sanglier brocheroc", 1, 1, null, 1, false, false, false, 0);
		player.addCardToHand(minion1);
		assertEquals(player.getHand().size(), 4);
		assertEquals(player.getHand().lastElement(), minion1);
		player.addCardToBoard(minion1);
		assertEquals(player.getBoard().lastElement(), minion1);
		assertEquals(player.getBoard().size(), 1);
		player.removeCardFromHand(3);
		assertEquals(player.getHand().size(), 3);
		player.addCardToHand(minion2);
		assertEquals(player.getHand().size(), 4);
		player.addCardToBoard(minion2, 0);
		assertEquals(player.getBoard().lastElement(), minion2);
		player.removeCardFromBoard(0);
		assertEquals(player.getBoard().size(), 0);
		

	}

}
