package fr.univ.nantes.alma.engine;

import static org.assertj.core.api.Assertions.assertThat;
import static pl.pojo.tester.api.assertion.Assertions.assertPojoMethodsFor;
import static pl.pojo.tester.api.assertion.Method.CONSTRUCTOR;
import static pl.pojo.tester.api.assertion.Method.GETTER;

import org.junit.Test;

public class TestPlayer {
  
  @Test
  public void testPojoPlayer() {
    final Class<?> classUnderTest = Player.class;

    assertPojoMethodsFor(classUnderTest)
    .testing(GETTER)
    .testing(CONSTRUCTOR)
      .areWellImplemented();
  }
  
  /*@Test
  public void testSetManaMaxTurn() {
    Player player = new Player(, "Bonjour", null, null, null);
    player.setManaMaxTurn();
    assertThat("1").isEqualTo(player.getManaMaxTurn());
  }
  
  @Test
  public void testSetManaPoolForNewTurn() {
    Player player = new Player(null, null, null, null, null);
    player.setManaPoolForNewTurn();
    assertThat("1").isEqualTo(player.getManaPool());
  }*/
  

}
