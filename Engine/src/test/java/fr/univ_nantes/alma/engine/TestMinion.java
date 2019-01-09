package fr.univ_nantes.alma.engine;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TestMinion {
	
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
		
		// Methods
		assertEquals(minion.getHealthPoints(), -1);
	}

}
