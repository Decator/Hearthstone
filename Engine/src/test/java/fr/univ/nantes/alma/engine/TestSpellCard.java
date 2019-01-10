package fr.univ.nantes.alma.engine;


import static pl.pojo.tester.api.assertion.Assertions.assertPojoMethodsFor;
import static pl.pojo.tester.api.assertion.Method.*;

import org.junit.Test;

import fr.univ.nantes.alma.engine.SpellCard;

public class TestSpellCard {

	@Test
	public void testPojoSpellCard() {
		final Class<?> classUnderTest = SpellCard.class;
		
		assertPojoMethodsFor(classUnderTest)
		.testing(GETTER)
		.testing(SETTER)
		.testing(CONSTRUCTOR)
		.testing(TO_STRING)
		.areWellImplemented();	
	}

}
