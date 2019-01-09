package fr.univ_nantes.alma.engine;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TestSpell {
	
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

}
