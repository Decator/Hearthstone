package fr.univ.nantes.alma.engine;

import static org.assertj.core.api.Assertions.assertThat;
import static pl.pojo.tester.api.assertion.Assertions.assertPojoMethodsFor;
import static pl.pojo.tester.api.assertion.Method.CONSTRUCTOR;
import static pl.pojo.tester.api.assertion.Method.GETTER;
import static pl.pojo.tester.api.assertion.Method.SETTER;
import static pl.pojo.tester.api.assertion.Method.TO_STRING;

import fr.univ.nantes.alma.engine.HeroCard;

import org.junit.Test;

public class TestHeroCard {

  @Test
  public void testPojoHeroCard() {
    final Class<?> classUnderTest = HeroCard.class;

    assertPojoMethodsFor(classUnderTest)
    .testing(GETTER)
    .testing(SETTER)
    .testing(CONSTRUCTOR)
    .testing(TO_STRING)
      .areWellImplemented();
  }

  @Test
  public void testReceiveDamage() {
    HeroCard hero3 = new HeroCard(3, "warrior", "Garrosh", 0, 0, 0, 2, 
        "Gain d'armure ! : Confère 2 points d'armure.", null);

    hero3.setArmorPoints(2);
    hero3.receiveDamage(-1);
    assertThat(hero3.getArmorPoints()).isEqualTo(2);
    assertThat(hero3.getHealthPoints()).isEqualTo(30);
    hero3.receiveDamage(0);
    assertThat(hero3.getArmorPoints()).isEqualTo(2);
    assertThat(hero3.getHealthPoints()).isEqualTo(30);
    hero3.receiveDamage(1);
    assertThat(hero3.getArmorPoints()).isEqualTo(1);
    assertThat(hero3.getHealthPoints()).isEqualTo(30);
    hero3.receiveDamage(2);
    assertThat(hero3.getArmorPoints()).isEqualTo(0);
    assertThat(hero3.getHealthPoints()).isEqualTo(29);
    hero3.receiveDamage(2);
    assertThat(hero3.getArmorPoints()).isEqualTo(0);
    assertThat(hero3.getHealthPoints()).isEqualTo(27);
  }
}
