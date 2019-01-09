package fr.univ_nantes.alma.engine;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TestHero {
	
	@Test
	public void testHero() {
		Hero hero = new Hero(1, "mage", "Jaina", 1, 0, 0, 0, "Boule de feu : Inflige 1 point de dégâts.", "all_1_all");
		
		// Getters
		assertEquals(hero.getId(), 1);
		assertEquals(hero.getType(), "mage");
		assertEquals(hero.getName(), "Jaina");
		assertEquals(hero.getDamage(), 1);
		assertEquals(hero.getNbSummon(), 0);
		assertEquals(hero.getIdInvocation(), 0);
		assertEquals(hero.getArmorBuff(), 0);
		assertEquals(hero.getDescription(), "Boule de feu : Inflige 1 point de dégâts.");
		assertEquals(hero.getTarget(), "all_1_all");
		assertFalse(hero.getHeroPowerUsed());
		assertEquals(hero.getHealthPoints(), 30);
		assertEquals(hero.getArmorPoints(), 0);
		assertEquals(hero.getManaCost(), 0);
		
		// Setters
		hero.setHealthPoints(25);
		assertEquals(hero.getHealthPoints(), 25);
		hero.setArmorPoints(2);
		assertEquals(hero.getArmorPoints(), 2);
		hero.setHeroPowerUsed(true);
		assertTrue(hero.getHeroPowerUsed());
		hero.setDamage(2);
		assertEquals(hero.getDamage(), 2);
		
		// Methods
		hero.receiveDamage(1);
		assertEquals(hero.getHealthPoints(), 25);
		assertEquals(hero.getArmorPoints(), 1);
		hero.receiveDamage(3);
		assertEquals(hero.getArmorPoints(), 0);
		assertEquals(hero.getHealthPoints(), 23);
		hero.receiveDamage(2);
		assertEquals(hero.getHealthPoints(), 21);
		assertEquals(hero.getArmorPoints(), 0);
		hero.receiveHealing(2);
		assertEquals(hero.getHealthPoints(), 23);
		assertEquals(hero.getArmorBuff(), 0);
	}

}
