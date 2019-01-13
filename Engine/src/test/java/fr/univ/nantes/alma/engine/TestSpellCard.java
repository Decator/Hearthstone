package fr.univ.nantes.alma.engine;

import static pl.pojo.tester.api.assertion.Assertions.assertPojoMethodsFor;
import static pl.pojo.tester.api.assertion.Method.CONSTRUCTOR;
import static pl.pojo.tester.api.assertion.Method.GETTER;
import static pl.pojo.tester.api.assertion.Method.SETTER;
import static pl.pojo.tester.api.assertion.Method.TO_STRING;

import fr.univ.nantes.alma.engine.SpellCard;

import org.junit.jupiter.api.Test;

public class TestSpellCard {

  @Test
  public void testPojoSpellCard() {
    final Class<?> classUnderTest = SpellCard.class;

    assertPojoMethodsFor(classUnderTest).areWellImplemented();
  }
}
