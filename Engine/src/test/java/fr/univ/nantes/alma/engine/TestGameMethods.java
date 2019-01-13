package fr.univ.nantes.alma.engine;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.UUID;
import java.util.Vector;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class TestGameMethods {
  GameMethods gameMethods = new GameMethods();
  Vector<MinionCard> board = new Vector<MinionCard>();
  MinionCard minion1 = new MinionCard();
  MinionCard minion2 = new MinionCard();
  HeroCard hero1 = new HeroCard();
  HeroCard hero2 = new HeroCard();
  AbstractCard[] deck = new AbstractCard[1];
  Vector<AbstractCard> hand = new Vector<AbstractCard>();
  Player player1 = new Player();
  Player player2 = new Player();
  
  @BeforeEach
  public void init() {
    board = new Vector<MinionCard>();
    minion1 = new MinionCard();
    minion2 = new MinionCard();
    hero1 = new HeroCard();
    hero2 = new HeroCard();
    gameMethods  = new GameMethods();
    deck = new AbstractCard[10];
    hand = new Vector<AbstractCard>();
    player1 = new Player();
    player2 = new Player();
  }
  
  @ParameterizedTest
  @CsvSource({"true, true, false", "true, false, true", "false, true, false", "false, false, true" })
  public void testCharge(boolean attackedBegin, boolean charge, boolean attackedEnd) {
    minion1.setAttacked(attackedBegin);
    minion1.setCharge(charge);
    gameMethods.charge(minion1);
    assertThat(attackedEnd).isEqualTo(minion1.isAttacked());
  }
  
  @ParameterizedTest
  @CsvSource({"1,true,28,29","2,true,29,30", "2,true,30,30", "2,false,28,28"})
  public void testLifesteal(int damageMinion, boolean lifestealMinion, int heroHealthPointsBegin, int heroHealthPointsEnd) {
    minion1.setDamage(damageMinion);
    minion1.setLifesteal(lifestealMinion);

    hero1.setHealthPoints(heroHealthPointsBegin);
    gameMethods.lifesteal(hero1, minion1);
    assertThat(heroHealthPointsEnd).isEqualTo(hero1.getHealthPoints());
  }
  
  @ParameterizedTest
  @CsvSource({"true, false, true, true", "false, true, false, true", "true, true, true, true", "false, false, false, false"})
  public void testTaunt(boolean tauntMinion1, boolean tauntMinion2, boolean tauntStatus1, boolean tauntStatus2) {
    assertThat(false).isEqualTo(gameMethods.taunt(board));
    minion1.setTaunt(tauntMinion1);
    board.add(minion1);
    assertThat(tauntStatus1).isEqualTo(gameMethods.taunt(board));
    minion2.setTaunt(tauntMinion2);
    board.add(minion2);
    assertThat(tauntStatus2).isEqualTo(gameMethods.taunt(board));
  }
  
  @ParameterizedTest
  @CsvSource({"1,1,2,1","1,0,1,1"})
  public void testGiveAttackAuraToOtherMinions(int buffMinion1, int buffMinion2, int finalDamageMinion1, int finalDamageMinion2) {
    minion1.setDamage(1);
    minion1.setAttackBuffAura(buffMinion1);
    minion2.setAttackBuffAura(buffMinion2);
    minion2.setDamage(1);
    board.add(minion1);
    board.add(minion2);
    gameMethods.giveAttackAuraToOtherMinions(board, minion2);
    assertThat(finalDamageMinion1).isEqualTo(board.get(0).getDamage());
    assertThat(finalDamageMinion2).isEqualTo(board.get(1).getDamage());
  }
  
  @ParameterizedTest
  @CsvSource({"2,1,1,2","0,1,1,0"})
  public void testGetAttackAuraFromOtherMinions(int buffMinion1, int buffMinion2, int finalDamageMinion1, int finalDamageMinion2) {
    minion1.setAttackBuffAura(buffMinion1);
    minion1.setDamage(1);
    minion2.setAttackBuffAura(buffMinion2);
    minion2.setDamage(0);
    board.add(minion1);
    board.addElement(minion2);
    gameMethods.getAttackAuraFromOtherMinions(board, minion2);
    assertThat(finalDamageMinion1).isEqualTo(board.get(0).getDamage());
    assertThat(finalDamageMinion2).isEqualTo(board.get(1).getDamage());
    
  }
  
  @ParameterizedTest
  @CsvSource({"2,1,1,2","2,0,2,3"})
  public void testRemoveAttackAuraFromMinions(int buffMinion1, int buffMinion2, int finalDamageMinion1, int finalDamageMinion2) {
    minion1.setAttackBuffAura(buffMinion1);
    minion1.setDamage(2);
    minion2.setAttackBuffAura(buffMinion2);
    minion2.setDamage(3);
    board.add(minion1);
    board.add(minion2);
    gameMethods.removeAttackAuraFromMinions(board, minion2);
    assertThat(finalDamageMinion1).isEqualTo(board.get(0).getDamage());
    assertThat(finalDamageMinion2).isEqualTo(board.get(1).getDamage());
  }
  
  /*@Test
  public void testDrawCard() {
    deck[0] = minion1;
    player1.setDeck(deck);
    player1.setHand(hand);
    gameMethods.setCurrentPlayer(player1);
    try {
      gameMethods.drawCard();
    } catch (EngineException e) {
      
    } finally {
      assertThat(player1.getHand()).hasSize(1).contains(minion1);
    }
    
  }*/

}
