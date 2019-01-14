package fr.univ.nantes.alma.engine;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.UUID;
import java.util.Vector;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class TestGameMethods {
  GameMethods gameMethods = spy(new GameMethods());
  Vector<MinionCard> board = new Vector<MinionCard>();
  MinionCard minion1 = new MinionCard();
  MinionCard minion2 = new MinionCard();
  HeroCard hero1 = new HeroCard();
  HeroCard hero2 = new HeroCard();
  AbstractCard[] deck = new AbstractCard[1];
  Vector<AbstractCard> hand = new Vector<AbstractCard>();
  Player player1 = new Player();
  Player player2 = new Player();
  SpellCard spell1 = new SpellCard();
  LinkedHashMap<String, AbstractCard> targets = new LinkedHashMap<String, AbstractCard>();
  
  @BeforeEach
  public void init() {
    board = new Vector<MinionCard>();
    minion1 = new MinionCard();
    minion2 = new MinionCard();
    hero1 = new HeroCard();
    hero2 = new HeroCard();
    gameMethods  = spy(new GameMethods());
    deck = new AbstractCard[1];
    hand = new Vector<AbstractCard>();
    player1 = new Player();
    player2 = new Player();
    spell1 = new SpellCard();
    targets = new LinkedHashMap<String, AbstractCard>();
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
  
  @Test
  public void testDrawCardException() {
    deck[0] = minion1;
    player1.setDeck(deck);
    hand = mock(Vector.class);
    when(hand.size()).thenReturn(10);
    player1.setHand(hand);
    gameMethods.setCurrentPlayer(player1);
    Throwable exception = assertThrows(
        EngineException.class, () -> {
          gameMethods.drawCard();
        }
    );
    
    assertThat("Votre main est pleine !").isEqualTo(exception.getMessage());
  }
  
  @Test
  public void testDrawCard() throws EngineException {
    deck[0] = minion1;
    player1.setDeck(deck);
    player1.setHand(hand);
    gameMethods.setCurrentPlayer(player1);
    try {
      gameMethods.drawCard();
    } catch (EngineException e) {
      throw e;
    }
    assertThat(player1.getHand()).hasSize(1).contains(minion1);
  }

  @ParameterizedTest
  @CsvSource({"0,0,1,1","10,10,10,10"})
  public void testInitTurn(int manaMaxInit, int manaPoolInit, int manaMaxEnd, int manaPoolEnd) {
    hero1.setHeroPowerUsed(true);
    minion1.setAttacked(true);
    board.add(minion1);
    player1.setBoard(board);
    player1.setHero(hero1);
    player1.setManaMaxTurn(manaMaxInit);
    player1.setManaPool(manaPoolInit);
    player1.setHand(hand);
    deck[0] = minion1;
    player1.setDeck(deck);
    gameMethods.setCurrentPlayer(player1);
    gameMethods.initTurn();
    assertThat(manaMaxEnd).isEqualTo(player1.getManaMaxTurn());
    assertThat(manaPoolEnd).isEqualTo(player1.getManaPool());
    assertThat(false).isEqualTo(player1.getHero().isHeroPowerUsed());
    assertThat(false).isEqualTo(player1.getBoard().get(0).isAttacked());
  }
  
  @Test
  public void testInitTurnException() {
    hero1.setHeroPowerUsed(true);
    minion1.setAttacked(true);
    board.add(minion1);
    player1.setBoard(board);
    player1.setHero(hero1);
    player1.setManaMaxTurn(0);
    player1.setManaPool(0);
    hand = mock(Vector.class);
    when(hand.size()).thenReturn(10);
    player1.setHand(hand);
    player1.setDeck(deck);
    gameMethods.setCurrentPlayer(player1);
    gameMethods.initTurn();
    assertThat(1).isEqualTo(player1.getManaMaxTurn());
    assertThat(1).isEqualTo(player1.getManaPool());
    assertThat(false).isEqualTo(player1.getHero().isHeroPowerUsed());
    assertThat(false).isEqualTo(player1.getBoard().get(0).isAttacked());
  }
  
  @Test
  public void testEndTurn() {
    doNothing().when(gameMethods).initTurn();
    gameMethods.setCurrentPlayer(player1);
    gameMethods.setOtherPlayer(player2);
    gameMethods.endTurn();
    assertThat(player2).isEqualTo(gameMethods.getCurrentPlayer());
    assertThat(player1).isEqualTo(gameMethods.getOtherPlayer());
  }
  
  @ParameterizedTest
  @CsvSource({"-1,0","0,0","1,1", "3,3"})
  public void testSpellArmorBuffing(int armorBuff, int armorHeroEnd) {
    spell1.setArmorBuff(armorBuff);
    hero1.setArmorPoints(0);
    gameMethods.spellArmorBuffing(hero1, spell1);
    assertThat(armorHeroEnd).isEqualTo(hero1.getArmorPoints());
  }
  
  @ParameterizedTest
  @CsvSource({"-1,0", "0,0", "1,1", "3,3"})
  public void testSpellDrawingEffect(int nbDraw, int callDrawCard) throws EngineException {
    spell1.setNbDraw(nbDraw);
    doNothing().when(gameMethods).drawCard();
    gameMethods.spellDrawingEffect(spell1);
    verify(gameMethods, times(callDrawCard)).drawCard();
  }
  
  @ParameterizedTest
  @CsvSource({"-1,0, false", "0,0, false", "1,1, false", "3,3, false", "1,0,true"})
  public void testSpellSummoningEffect(int nbSummon, int actualSummon, boolean polymorph) throws EngineException {
    spell1.setNbSummon(nbSummon);
    spell1.setIdInvocation(1);
    spell1.setPolymorph(polymorph);
    gameMethods.setCurrentPlayer(player1);
    doNothing().when(gameMethods).summonMinion(player1,spell1.getIdInvocation());
    gameMethods.spellSummoningEffect(spell1);
    verify(gameMethods, times(actualSummon)).summonMinion(player1,spell1.getIdInvocation());
  }
  
  @ParameterizedTest
  @CsvSource({"-1,1", "0,1","1, 2","3,4"})
  public void testSpellAttackBuffingEffect(int attackBuff, int attackEnd) {
    spell1.setAttackBuff(attackBuff);
    spell1.setTarget("");
    minion1.setDamage(1);
    gameMethods.setCurrentPlayer(player1);
    gameMethods.setOtherPlayer(player2);
    LinkedHashMap<String, AbstractCard> targets = new LinkedHashMap<String, AbstractCard>();
    targets.put("0_0", minion1);
    doReturn(targets).when(gameMethods).targetsFromTargetString(player1, player2, player1, 1, spell1.getTarget());
    gameMethods.spellAttackBuffingEffect(player1, 1, spell1);
    assertThat(attackEnd).isEqualTo(minion1.getDamage());
  }
  
  @ParameterizedTest
  @CsvSource({"-1,0", "0,0","1,0","3,0"})
  public void testSpellAttackBuffingEffectNotMinion(int attackBuff, int attackEnd) {
    hero1.setDamage(0);
    spell1.setAttackBuff(attackBuff);
    spell1.setTarget("");
    gameMethods.setCurrentPlayer(player1);
    gameMethods.setOtherPlayer(player2);
    LinkedHashMap<String, AbstractCard> targets = new LinkedHashMap<String, AbstractCard>();
    targets.put("0", hero1);
    doReturn(targets).when(gameMethods).targetsFromTargetString(player1, player2, player1, -1, spell1.getTarget());
    gameMethods.spellAttackBuffingEffect(player1, -1, spell1);
    assertThat(attackEnd).isEqualTo(hero1.getDamage());
  }
  
  /*@ParameterizedTest
  @CsvSource({"true, 0, 0, 1"})
  public void testSpellPolymorphingEffectCurrentPlayer(boolean polymorph, String targetKey1, String targetKey2, int nbCallPolymorph) throws EngineException {
    spell1.setPolymorph(polymorph);
    targets.put(targetKey1+"_"+targetKey2, minion1);
    gameMethods.setCurrentPlayer(player1);
    board.add(minion1);
    player1.setBoard(board);
    doReturn(targets).when(gameMethods).targetsFromTargetString(player1, player2, player1, 0, spell1.getTarget());
    doNothing().when(gameMethods).removeAttackAuraFromMinions(board, minion1);
    doNothing().when(gameMethods).polymorph(gameMethods.getCurrentPlayer(), 13, "0");
    gameMethods.spellPolymorphingEffect(player1, 0, spell1);
    verify(gameMethods, times(nbCallPolymorph)).polymorph(gameMethods.getCurrentPlayer(), 13, "0");
  }*/
  
}
