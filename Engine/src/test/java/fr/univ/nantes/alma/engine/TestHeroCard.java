package fr.univ.nantes.alma.engine;

import static org.assertj.core.api.Assertions.assertThat;
import static pl.pojo.tester.api.assertion.Assertions.assertPojoMethodsFor;

import fr.univ.nantes.alma.engine.HeroCard;

import org.junit.jupiter.api.Test;

public class TestHeroCard {

  @Test
  public void testPojoHeroCard() {
    final Class<?> classUnderTest = HeroCard.class;

    assertPojoMethodsFor(classUnderTest)
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
  
  @Test
  public void testReceiveHealing() {
    HeroCard hero3 = new HeroCard(3, "warrior", "Garrosh", 0, 0, 0, 2, 
        "Gain d'armure ! : Confère 2 points d'armure.", null);
    
    hero3.receiveDamage(3);
    hero3.receiveHealing(-1);
    assertThat(27).isEqualTo(hero3.getHealthPoints());
    hero3.receiveHealing(1);
    assertThat(28).isEqualTo(hero3.getHealthPoints());
    hero3.receiveHealing(3);
    assertThat(30).isEqualTo(hero3.getHealthPoints());
    hero3.receiveHealing(3);
    assertThat(30).isEqualTo(hero3.getHealthPoints());
  }
  
}
