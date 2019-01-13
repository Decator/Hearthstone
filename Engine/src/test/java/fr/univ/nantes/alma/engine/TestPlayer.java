package fr.univ.nantes.alma.engine;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static pl.pojo.tester.api.assertion.Assertions.assertPojoMethodsFor;
import static pl.pojo.tester.api.assertion.Method.CONSTRUCTOR;
import static pl.pojo.tester.api.assertion.Method.GETTER;
import static pl.pojo.tester.api.assertion.Method.SETTER;
import static pl.pojo.tester.api.FieldPredicate.exclude;

import java.util.UUID;
import java.util.Vector;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TestPlayer {
  
  UUID uuid = UUID.randomUUID();
  HeroCard heroDummy = mock(HeroCard.class);
  MinionCard minion1 = new MinionCard(6, "common", "Chevaucheur de loup", 3, 2, 
      "Charge", 1, false, true, false, 0);
  MinionCard minion2 = new MinionCard(4, "common", "Sanglier brocheroc", 1, 1, 
      null, 1, false, false, false, 0);
  AbstractCard[] deck = new AbstractCard[2];
  Vector<AbstractCard> hand = new Vector<AbstractCard>();
  Player player = new Player();
  
  @BeforeEach
  public void init() {
    deck[0] = minion1;
    hand.add(minion1);
  }
  
  @Test
  public void testPojoPlayer() {
    final Class<?> classUnderTest = Player.class;

    assertPojoMethodsFor(classUnderTest, exclude("deck"))
    .testing(GETTER)
    .testing(CONSTRUCTOR)
    .testing(SETTER)
      .areWellImplemented();
  }
  
  @Test
  public void testSetManaMaxTurn() {
    Player player = new Player(uuid, "pouet", heroDummy, deck, hand);
    player.setManaMaxTurn();
    assertThat(1).isEqualTo(player.getManaMaxTurn());
  }
  
  @Test
  public void testSetManaPoolForNewTurn() {
    Player player = new Player(uuid, "pouet", heroDummy, deck, hand);
    player.setManaMaxTurn();
    player.setManaPoolForNewTurn();
    assertThat(1).isEqualTo(player.getManaPool());
  }
  
  @Test
  public void testSetManaAfterPlay() {
    Player player = new Player(uuid, "pouet", heroDummy, deck, hand);
    player.setManaMaxTurn();
    player.setManaMaxTurn();
    player.setManaPoolForNewTurn();
    player.setManaPoolAfterPlay(2);
    assertThat(2).isEqualTo(player.getManaMaxTurn());
    assertThat(0).isEqualTo(player.getManaPool());
  }
  
  @Test
  public void testAddCardToHand() {
    Player player = new Player(uuid, "pouet", heroDummy, deck, hand);
    player.addCardToHand(minion2);
    
    assertThat(player.getHand()).hasSize(2).contains(minion1).contains(minion2);
    assertThat(player.getHand().lastElement()).isEqualTo(minion2);
  }
  
  @Test
  public void testAddCardToBoard() {
    Player player = new Player(uuid, "pouet", heroDummy, deck, hand);
    player.addCardToBoard(minion1);
    assertThat(player.getBoard()).hasSize(1);
    assertThat(player.getBoard().lastElement()).isEqualTo(minion1);
  }
  
  @Test
  public void testAddCardToBoardWithIndex() {
    Player player = new Player(uuid, "pouet", heroDummy, deck, hand);
    player.addCardToBoard(minion1);
    player.addCardToBoard(minion2, 0);
    assertThat(player.getBoard()).hasSize(1).contains(minion2).doesNotContain(minion1);
  }
  
  @Test
  public void testRemoveCardFromBoard() {
    Player player = new Player(uuid, "pouet", heroDummy, deck, hand);
    player.addCardToBoard(minion1);
    player.removeCardFromBoard(0);
    assertThat(player.getBoard()).hasSize(0).isEmpty();
  }
  
  @Test 
  public void testRemoveCardFromHand() {
    Player player = new Player(uuid, "pouet", heroDummy, deck, hand);
    player.addCardToHand(minion2);
    player.removeCardFromHand(0);
    assertThat(player.getHand()).hasSize(1).doesNotContain(minion1).contains(minion2);
  }

}
