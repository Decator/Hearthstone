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
import org.junit.jupiter.params.provider.MethodSource;

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
  public void testEndTurn() throws EngineException {
    doNothing().when(gameMethods).initTurn();
    UUID uuid = UUID.randomUUID();
    player1.setUuid(uuid);
    gameMethods.setCurrentPlayer(player1);
    gameMethods.setOtherPlayer(player2);
    gameMethods.endTurn(uuid);
    assertThat(player2).isEqualTo(gameMethods.getCurrentPlayer());
    assertThat(player1).isEqualTo(gameMethods.getOtherPlayer());
  }
 
  @Test
  public void testEndTurnException() {
    UUID uuid = UUID.randomUUID();
    UUID uuid2 = UUID.randomUUID();
    player1.setUuid(uuid2);
    gameMethods.setCurrentPlayer(player1);
    gameMethods.setOtherPlayer(player2);
    Throwable exception = assertThrows(
        EngineException.class, () -> {
          gameMethods.endTurn(uuid);
        }
    );
    
    assertThat("Ce n'est pas votre tour !").isEqualTo(exception.getMessage());
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
  
  @ParameterizedTest
  @CsvSource({"false, 0, 0, '0_0'", "true, 1, 0, '0_0'" , "true, 0, 0, '2_0'"})
  public void testSpellPolymorphingEffectCurrentPlayer(boolean polymorph,
      int nbCallsPolymorphCurrentPlayer, int nbCallsPolymorphOtherPlayer, 
      String keyTarget) throws EngineException {
    spell1.setPolymorph(polymorph);
    spell1.setIdInvocation(1);
    spell1.setTarget("");
    board.add(minion1);
    player1.setBoard(board);
    Vector<MinionCard> board2 = new Vector<MinionCard>();
    board2.add(minion2);
    player2.setBoard(board2);
    ArrayList<MinionCard> invocations = new ArrayList<MinionCard>();
    gameMethods.setCurrentPlayer(player1);
    gameMethods.setOtherPlayer(player2);
    gameMethods.setInvocations(invocations);
    targets.put(keyTarget, minion1);
    doReturn(targets).when(gameMethods).targetsFromTargetString(player1, player2, player1, 0, "");
    doNothing().when(gameMethods).polymorph(player1, 1, "0");
    doNothing().when(gameMethods).removeAttackAuraFromMinions(board, minion1);
    doNothing().when(gameMethods).polymorph(player2, 1, "0");
    doNothing().when(gameMethods).removeAttackAuraFromMinions(board2, minion2);
    gameMethods.spellPolymorphingEffect(player1, 0, spell1);
    verify(gameMethods, times(nbCallsPolymorphCurrentPlayer)).polymorph(player1, 1, "0");
    verify(gameMethods, times(nbCallsPolymorphOtherPlayer)).polymorph(player2, 1, "0");
  }
  
  @Test
  public void testSpellPolymorphingEffectOtherPlayer() throws EngineException {
    spell1.setPolymorph(true);
    spell1.setIdInvocation(1);
    spell1.setTarget("");
    board.add(minion1);
    player1.setBoard(board);
    Vector<MinionCard> board2 = new Vector<MinionCard>();
    board2.add(minion2);
    player2.setBoard(board2);
    ArrayList<MinionCard> invocations = new ArrayList<MinionCard>();
    gameMethods.setCurrentPlayer(player1);
    gameMethods.setOtherPlayer(player2);
    gameMethods.setInvocations(invocations);
    targets.put("1_0", minion1);
    doReturn(targets).when(gameMethods).targetsFromTargetString(player1, player2, player1, 0, "");
    doNothing().when(gameMethods).polymorph(player1, 1, "0");
    doNothing().when(gameMethods).removeAttackAuraFromMinions(board, minion1);
    doNothing().when(gameMethods).polymorph(player2, 1, "0");
    doNothing().when(gameMethods).removeAttackAuraFromMinions(board2, minion2);
    gameMethods.spellPolymorphingEffect(player1, 0, spell1);
    verify(gameMethods, times(0)).polymorph(player1, 1, "0");
    verify(gameMethods, times(1)).polymorph(player2, 1, "0");
  }
  
  @ParameterizedTest
  @CsvSource({"0, -1, false, 1, 0","1, -1, false, 1, 1","1, -1, true, 0, 1" })
  public void testSpellDamageEffectHero(int spellDamage, int idTarget, boolean gameOver, 
      int heroHP, int nbCallsSubMethod) {
    hero1 = spy(new HeroCard());
    spell1.setDamage(spellDamage);
    spell1.setTarget("");
    hero1.setHealthPoints(heroHP);
    player1.setHero(hero1);
    LinkedHashMap<String, AbstractCard> targets = new LinkedHashMap<String, AbstractCard>();
    targets.put("0", hero1);
    gameMethods.setCurrentPlayer(player1);
    gameMethods.setOtherPlayer(player2);
    gameMethods.setGameOver(false);
    doReturn(targets).when(gameMethods).targetsFromTargetString(player1, player2, player1, idTarget, "");
    doNothing().when(hero1).receiveDamage(spellDamage);
    gameMethods.spellDamageEffect(player1, idTarget, spell1);
    verify(hero1, times(nbCallsSubMethod)).receiveDamage(spellDamage);
    assertThat(gameOver).isEqualTo(gameMethods.isGameOver());
  }
  
  @ParameterizedTest
  @CsvSource({"1, -1, 1, 1, 0", "1, -1, 0, 1, 1"})
  public void testSpellDamageEffectMinionP1(int spellDamage, int idTarget, int minionHP, 
      int nbCallsSubMethods1, int nbCallsSubMethods2) {
    minion1 = spy(new MinionCard());
    player1 = spy(new Player());
    spell1.setDamage(spellDamage);
    spell1.setTarget("");
    minion1.setHealthPoints(minionHP);
    board.add(minion1);
    player1.setBoard(board);
    LinkedHashMap<String, AbstractCard> targets = new LinkedHashMap<String, AbstractCard>();
    targets.put("0_0", minion1);
    gameMethods.setCurrentPlayer(player1);
    gameMethods.setOtherPlayer(player2);
    doReturn(targets).when(gameMethods).targetsFromTargetString(player1, player2, player1, idTarget, "");
    doNothing().when(minion1).receiveDamage(spellDamage);
    gameMethods.spellDamageEffect(player1, idTarget, spell1);
    verify(minion1, times(nbCallsSubMethods1)).receiveDamage(spellDamage);
    verify(gameMethods, times(nbCallsSubMethods2)).removeAttackAuraFromMinions(board, minion1);
    verify(player1, times(nbCallsSubMethods2)).removeCardFromBoard(0);
  }
  
  @ParameterizedTest
  @CsvSource({"1, -1, 1, 1, 0", "1, -1, 0, 1, 1"})
  public void testSpellDamageEffectMinionP2(int spellDamage, int idTarget, int minionHP, 
      int nbCallsSubMethods1, int nbCallsSubMethods2) {
    minion1 = spy(new MinionCard());
    player2 = spy(new Player());
    spell1.setDamage(spellDamage);
    spell1.setTarget("");
    minion1.setHealthPoints(minionHP);
    board.add(minion1);
    player2.setBoard(board);
    LinkedHashMap<String, AbstractCard> targets = new LinkedHashMap<String, AbstractCard>();
    targets.put("1_0", minion1);
    gameMethods.setCurrentPlayer(player1);
    gameMethods.setOtherPlayer(player2);
    doReturn(targets).when(gameMethods).targetsFromTargetString(player1, player2, player2, idTarget, "");
    doNothing().when(minion1).receiveDamage(spellDamage);
    gameMethods.spellDamageEffect(player2, idTarget, spell1);
    verify(minion1, times(nbCallsSubMethods1)).receiveDamage(spellDamage);
    verify(gameMethods, times(nbCallsSubMethods2)).removeAttackAuraFromMinions(board, minion1);
    verify(player2, times(nbCallsSubMethods2)).removeCardFromBoard(0);
  }
  
  @ParameterizedTest
  @CsvSource({"0, -1, false, 1, 0","1, -1, false, 1, 1","1, -1, true, 0, 1" })
  public void testMageHeroPowerHero(int spellDamage, int idTarget, boolean gameOver, 
      int heroHP, int nbCallsSubMethod) {
    hero1 = spy(new HeroCard());
    hero1.setDamage(spellDamage);
    hero1.setTarget("");
    hero1.setHealthPoints(heroHP);
    player1.setHero(hero1);
    LinkedHashMap<String, AbstractCard> targets = new LinkedHashMap<String, AbstractCard>();
    targets.put("0", hero1);
    gameMethods.setCurrentPlayer(player1);
    gameMethods.setOtherPlayer(player2);
    gameMethods.setGameOver(false);
    doReturn(targets).when(gameMethods).targetsFromTargetString(player1, player2, player1, idTarget, "");
    doNothing().when(hero1).receiveDamage(spellDamage);
    gameMethods.mageHeroPower(player1, idTarget, hero1);
    verify(hero1, times(nbCallsSubMethod)).receiveDamage(spellDamage);
    assertThat(gameOver).isEqualTo(gameMethods.isGameOver());
  }
  
  @ParameterizedTest
  @CsvSource({"1, -1, 1, 1, 0", "1, -1, 0, 1, 1"})
  public void testMageHeroPowerP1(int spellDamage, int idTarget, int minionHP, 
      int nbCallsSubMethods1, int nbCallsSubMethods2) {
    minion1 = spy(new MinionCard());
    player1 = spy(new Player());
    hero1.setDamage(spellDamage);
    hero1.setTarget("");
    minion1.setHealthPoints(minionHP);
    board.add(minion1);
    player1.setBoard(board);
    LinkedHashMap<String, AbstractCard> targets = new LinkedHashMap<String, AbstractCard>();
    targets.put("0_0", minion1);
    gameMethods.setCurrentPlayer(player1);
    gameMethods.setOtherPlayer(player2);
    doReturn(targets).when(gameMethods).targetsFromTargetString(player1, player2, player1, idTarget, "");
    doNothing().when(minion1).receiveDamage(spellDamage);
    gameMethods.mageHeroPower(player1, idTarget, hero1);
    verify(minion1, times(nbCallsSubMethods1)).receiveDamage(spellDamage);
    verify(gameMethods, times(nbCallsSubMethods2)).removeAttackAuraFromMinions(board, minion1);
    verify(player1, times(nbCallsSubMethods2)).removeCardFromBoard(0);
  }
  
  @ParameterizedTest
  @CsvSource({"1, -1, 1, 1, 0", "1, -1, 0, 1, 1"})
  public void testMageHeroPowerP2(int spellDamage, int idTarget, int minionHP, 
      int nbCallsSubMethods1, int nbCallsSubMethods2) {
    minion1 = spy(new MinionCard());
    player2 = spy(new Player());
    hero1.setDamage(spellDamage);
    hero1.setTarget("");
    minion1.setHealthPoints(minionHP);
    board.add(minion1);
    player2.setBoard(board);
    LinkedHashMap<String, AbstractCard> targets = new LinkedHashMap<String, AbstractCard>();
    targets.put("1_0", minion1);
    gameMethods.setCurrentPlayer(player1);
    gameMethods.setOtherPlayer(player2);
    doReturn(targets).when(gameMethods).targetsFromTargetString(player1, player2, player2, idTarget, "");
    doNothing().when(minion1).receiveDamage(spellDamage);
    gameMethods.mageHeroPower(player2, idTarget, hero1);
    verify(minion1, times(nbCallsSubMethods1)).receiveDamage(spellDamage);
    verify(gameMethods, times(nbCallsSubMethods2)).removeAttackAuraFromMinions(board, minion1);
    verify(player2, times(nbCallsSubMethods2)).removeCardFromBoard(0);
  }
  
  
  @ParameterizedTest
  @CsvSource({"1, 0, 1, 1, false, false, -1, 1, 0, true, false, 1, 1", "1, 0, 1, 1, false, false, -1, 3, 2, false, false, 1, 1",
    "1, 1, 1, 1, false, true, 0, 1, 1, false, true, 0, 0","1, 1, 2, 2, false, true, 0, 1, 1, false, true, 1, 1"})
  public void testAttack(int minionDamage1, int minionDamage2, int minionHP1, int minionHP2, boolean minionAttacked1, 
      boolean tauntStatus, int idTarget, int heroHP2, int heroHPEnd2, boolean gameOverStatus,
      boolean tauntStatusMinion2, int minionEndHP1, int minionEndHP2) throws EngineException {
    UUID uuid = UUID.randomUUID();
    player1.setUuid(uuid);
    minion1 = spy(new MinionCard());
    minion2 = spy(new MinionCard());
    minion1.setDamage(minionDamage1);
    minion1.setHealthPoints(minionHP1);
    minion1.setAttacked(minionAttacked1);
    minion2.setDamage(minionDamage2);
    minion2.setHealthPoints(minionHP2);
    minion2.setTaunt(tauntStatusMinion2);
    hero1 = spy(HeroCard.class);
    hero2 = spy(HeroCard.class);
    hero2.setHealthPoints(heroHP2);
    player1.setHero(hero1);
    player2.setHero(hero2);
    board.add(minion1);
    Vector<MinionCard> board2 = new Vector<MinionCard>();
    board2.add(minion2);
    player1.setBoard(board);
    player2.setBoard(board2);
    gameMethods.setCurrentPlayer(player1);
    gameMethods.setOtherPlayer(player2);
    gameMethods.setGameOver(false);
    doReturn(tauntStatus).when(gameMethods).taunt(board2);
    doNothing().when(gameMethods).lifesteal(hero2, minion1);
    gameMethods.attack(uuid, 0, idTarget);
    assertThat(true).isEqualTo(minion1.isAttacked());
    assertThat(heroHPEnd2).isEqualTo(hero2.getHealthPoints());
    assertThat(gameOverStatus).isEqualTo(gameMethods.isGameOver());
    assertThat(minionEndHP1).isEqualTo(minion1.getHealthPoints());
    assertThat(minionEndHP2).isEqualTo(minion2.getHealthPoints());
  }
  
  @ParameterizedTest
  @CsvSource({"0, false, false, -1, false, Ce serviteur ne peut pas attaquer !", 
    "1, true, false, -1, false, Ce serviteur a déjà attaqué durant ce tour !",
    "1, false, true, 0, false, 'Cible incorrecte, un serviteur adverse a provocation !'"})
  public void testAttackExceptions(int minionDamage1, boolean minionAttacked1, 
      boolean tauntStatus, int idTarget, boolean tauntStatusMinion2, String exceptionMessage) throws EngineException {
    UUID uuid = UUID.randomUUID();
    minion1 = spy(new MinionCard());
    minion2 = spy(new MinionCard());
    minion1.setDamage(minionDamage1);
    minion1.setAttacked(minionAttacked1);
    minion2.setTaunt(tauntStatusMinion2);
    hero1 = spy(HeroCard.class);
    hero2 = spy(HeroCard.class);
    board.add(minion1);
    Vector<MinionCard> board2 = new Vector<MinionCard>();
    board2.add(minion2);
    player1.setBoard(board);
    player1.setUuid(uuid);
    player2.setBoard(board2);
    gameMethods.setCurrentPlayer(player1);
    gameMethods.setOtherPlayer(player2);
    gameMethods.setGameOver(false);
    doReturn(tauntStatus).when(gameMethods).taunt(board2);
    doNothing().when(gameMethods).lifesteal(hero2, minion1);
    Throwable exception = assertThrows(
        EngineException.class, () -> {
          gameMethods.attack(uuid, 0, idTarget);
        }
    );
    
    assertThat(exceptionMessage).isEqualTo(exception.getMessage());
  }
  
  @Test
  public void testAttackExceptionTurn() {
    UUID uuid = UUID.randomUUID();
    UUID uuid2 = UUID.randomUUID();
    player1.setUuid(uuid2);
    gameMethods.setCurrentPlayer(player1);
    Throwable exception = assertThrows(
        EngineException.class, () -> {
          gameMethods.attack(uuid, 0, 0);
        }
    );
    
    assertThat("Ce n'est pas votre tour !").isEqualTo(exception.getMessage());
  }
  
  @ParameterizedTest
  @CsvSource({"mage, 2, 0, -1, false, true, 1, 0, 0", "warrior, 2, 0, -1, false, true, 0, 0, 2",
    "paladin, 2, 0, -1, false, true, 0, 1, 0"})
  public void testHeroPower(String heroType, int playerManaPool1, int playerManaPoolEnd1, int idTarget, 
      boolean heroPowerUsed1, boolean heroPowerUsedEnd1, int mageHeroPowerUsedCount, int summonEffectCount, 
      int heroArmorEnd) throws EngineException {
    UUID uuid = UUID.randomUUID();
    hero1 = spy(new HeroCard());
    hero1.setArmorPoints(0);
    hero1.setHeroPowerUsed(heroPowerUsed1);
    hero1.setArmorBuff(2);
    hero1.setType(heroType);
    hero1.setIdInvocation(12);
    player1.setHero(hero1);
    player1.setUuid(uuid);
    player1.setManaPool(playerManaPool1);
    gameMethods.setCurrentPlayer(player1);
    doNothing().when(gameMethods).mageHeroPower(player1, idTarget, hero1);
    doNothing().when(gameMethods).summonMinion(player1, 12);
    gameMethods.heroPower(uuid, player1, idTarget);
    assertThat(playerManaPoolEnd1).isEqualTo(player1.getManaPool());
    assertThat(heroArmorEnd).isEqualTo(hero1.getArmorPoints());
    assertThat(heroPowerUsedEnd1).isEqualTo(hero1.isHeroPowerUsed());
    verify(gameMethods, times(mageHeroPowerUsedCount)).mageHeroPower(player1, idTarget, hero1);
    verify(gameMethods, times(summonEffectCount)).summonMinion(player1, 12);
  }
  
  @ParameterizedTest
  @CsvSource({"bonjour, 2, 0, -1, false, true, Impossible de récupérer la classe du héros !", 
    "warrior, 2, 2, -1, true, true, Vous avez déjà utilisé votre pouvoir héroïque durant ce tour !",
    "warrior, 1, 1, -1, false, false, Vous n'avez pas assez de mana !"})
  public void testHeroPowerExceptions(String heroType, int playerManaPool1, int playerManaPoolEnd1, int idTarget, 
      boolean heroPowerUsed1, boolean heroPowerUsedEnd1, String exceptionMessage) throws EngineException {
    UUID uuid = UUID.randomUUID();
    hero1 = spy(new HeroCard());
    hero1.setHeroPowerUsed(heroPowerUsed1);
    hero1.setType(heroType);
    player1.setHero(hero1);
    player1.setUuid(uuid);
    player1.setManaPool(playerManaPool1);
    gameMethods.setCurrentPlayer(player1);
    Throwable exception = assertThrows(
        EngineException.class, () -> {
          gameMethods.heroPower(uuid, player1, idTarget);
        }
    );
    
    assertThat(exceptionMessage).isEqualTo(exception.getMessage());
    assertThat(playerManaPoolEnd1).isEqualTo(player1.getManaPool());
    assertThat(heroPowerUsedEnd1).isEqualTo(hero1.isHeroPowerUsed());
  }
  
  @Test
  public void testHeroPowerExceptionTurn() {
    UUID uuid = UUID.randomUUID();
    UUID uuid2 = UUID.randomUUID();
    hero1 = spy(new HeroCard());
    hero1.setHeroPowerUsed(false);
    hero1.setType("mage");
    player1.setHero(hero1);
    player1.setUuid(uuid2);
    player1.setManaPool(2);
    gameMethods.setCurrentPlayer(player1);
    Throwable exception = assertThrows(
        EngineException.class, () -> {
          gameMethods.heroPower(uuid, player1, -1);
        }
    );
    
    assertThat("Ce n'est pas votre tour !").isEqualTo(exception.getMessage());
  }
  
  @ParameterizedTest
  @CsvSource({"0, 10, 8, 0, 1, 0", "1, 10, 8, 0, 0, 1"})
  public void testPlayCard(int idCard, int manaPoolBegin1, int manaPoolEnd1, int idTarget, 
      int countSummonMinion, int countSpellSubMethods) throws EngineException {
    minion1.setManaCost(2);
    spell1.setManaCost(2);
    hand.add(minion1);
    hand.addElement(spell1);
    UUID uuid = UUID.randomUUID();
    player1.setUuid(uuid);
    player1.setManaPool(manaPoolBegin1);
    player1.setHero(hero1);
    player1.setHand(hand);
    gameMethods.setCurrentPlayer(player1);
    doNothing().when(gameMethods).summonMinionFromHand(idCard);
    doNothing().when(gameMethods).spellDrawingEffect(spell1);
    doNothing().when(gameMethods).spellArmorBuffing(hero1, spell1);
    doNothing().when(gameMethods).spellSummoningEffect(spell1);
    doNothing().when(gameMethods).spellAttackBuffingEffect(player1, idTarget, spell1);
    doNothing().when(gameMethods).spellPolymorphingEffect(player1, idTarget, spell1);
    doNothing().when(gameMethods).spellDamageEffect(player1, idTarget, spell1);
    gameMethods.playCard(uuid, idCard, player1, idTarget);
    assertThat(manaPoolEnd1).isEqualTo(player1.getManaPool());
    verify(gameMethods, times(countSummonMinion)).summonMinionFromHand(idCard);
    verify(gameMethods, times(countSpellSubMethods)).spellDrawingEffect(spell1);
    verify(gameMethods, times(countSpellSubMethods)).spellArmorBuffing(hero1, spell1);
    verify(gameMethods, times(countSpellSubMethods)).spellSummoningEffect(spell1);
    verify(gameMethods, times(countSpellSubMethods)).spellAttackBuffingEffect(player1, idTarget, spell1);
    verify(gameMethods, times(countSpellSubMethods)).spellPolymorphingEffect(player1, idTarget, spell1);
    verify(gameMethods, times(countSpellSubMethods)).spellDamageEffect(player1, idTarget, spell1);
  }
  
  @ParameterizedTest
  @CsvSource({"0, 1, 1, 0, Vous n'avez pas assez de mana !", "-1, 10, 10, 0, Votre carte est inexistante !",
    "5, 10, 10, 0, Votre carte est inexistante !", "2, 10, 10, 0, Ceci n'est pas une carte valide !"})
  public void testPlayCardExceptions(int idCard, int manaPoolBegin1, int manaPoolEnd1, int idTarget, 
      String exceptionMessage) throws EngineException {
    UUID uuid = UUID.randomUUID();
    minion1.setManaCost(2);
    spell1.setManaCost(2);
    hero2.setManaCost(0);
    hand.add(minion1);
    hand.addElement(spell1);
    hand.add(hero2);
    player1.setManaPool(manaPoolBegin1);
    player1.setHero(hero1);
    player1.setHand(hand);
    player1.setUuid(uuid);
    gameMethods.setCurrentPlayer(player1);
    Throwable exception = assertThrows(
        EngineException.class, () -> {
          gameMethods.playCard(uuid, idCard, player1, idTarget);
        }
    );
    
    assertThat(exceptionMessage).isEqualTo(exception.getMessage());
    assertThat(manaPoolEnd1).isEqualTo(player1.getManaPool());
  }
  
  @Test 
  public void testPlayCardTurnException() {
    UUID uuid = UUID.randomUUID();
    UUID uuid2 = UUID.randomUUID();
    player1.setUuid(uuid2);
    gameMethods.setCurrentPlayer(player1);
    Throwable exception = assertThrows(
        EngineException.class, () -> {
          gameMethods.playCard(uuid, 0, player1, 0);
        }
    );

    assertThat("Ce n'est pas votre tour !").isEqualTo(exception.getMessage());
    
  }
  
  @Test
  public void testSummonMinionFromHand() throws EngineException {
    player1 = spy(new Player());
    board = spy(new Vector<MinionCard>());
    player1.setBoard(board);
    hand.add(minion1);
    player1.setHand(hand);
    gameMethods.setCurrentPlayer(player1);
    doReturn(0).when(board).size();
    doReturn(minion1).when(board).lastElement();
    doNothing().when(player1).addCardToBoard(minion1);
    doNothing().when(player1).removeCardFromHand(0);
    doNothing().when(gameMethods).charge(minion1);
    doNothing().when(gameMethods).giveAttackAuraToOtherMinions(board, minion1);
    doNothing().when(gameMethods).getAttackAuraFromOtherMinions(board, minion1);
    gameMethods.summonMinionFromHand(0);
    verify(player1, times(1)).addCardToBoard(minion1);
    verify(player1, times(1)).removeCardFromHand(0);
    verify(gameMethods, times(1)).charge(minion1);
    verify(gameMethods, times(1)).giveAttackAuraToOtherMinions(board, minion1);
    verify(gameMethods, times(1)).getAttackAuraFromOtherMinions(board, minion1);
  }
  
  @Test
  public void testSummonMinionFromHandException() throws EngineException {
    player1 = spy(new Player());
    board = spy(new Vector<MinionCard>());
    player1.setBoard(board);
    hand.add(minion1);
    player1.setHand(hand);
    gameMethods.setCurrentPlayer(player1);
    doReturn(7).when(board).size();
    Throwable exception = assertThrows(
        EngineException.class, () -> {
          gameMethods.summonMinionFromHand(0);
        }
    );
    
    assertThat("Vous avez atteint le nombre maximum de serviteurs sur le plateau !")
      .isEqualTo(exception.getMessage());
  }
  
  @Test
  public void testSummonMinion() throws EngineException {
    board = spy(new Vector<MinionCard>());
    player1 = spy(new Player());
    player1.setBoard(board);
    ArrayList<MinionCard> invocations = spy(new ArrayList<MinionCard>());
    minion2.setId(3);
    invocations.add(minion2);
    gameMethods.setInvocations(invocations);
    doReturn(0).when(board).size();
    doReturn(minion2).when(board).lastElement();
    doNothing().when(player1).addCardToBoard(minion2);
    doNothing().when(gameMethods).giveAttackAuraToOtherMinions(board, minion2);
    doNothing().when(gameMethods).getAttackAuraFromOtherMinions(board, minion2);
    gameMethods.summonMinion(player1, 3);
    verify(player1, times(1)).addCardToBoard(minion2);
    verify(gameMethods, times(1)).giveAttackAuraToOtherMinions(board, minion2);
    verify(gameMethods, times(1)).getAttackAuraFromOtherMinions(board, minion2); 
  }
  
  @Test
  public void testSummonMinionException() throws EngineException {
    board = spy(new Vector<MinionCard>());
    player1 = new Player();
    player1.setBoard(board);
    ArrayList<MinionCard> invocations = new ArrayList<MinionCard>();
    gameMethods.setInvocations(invocations);
    doReturn(0).when(board).size();
    Throwable exception = assertThrows(
        EngineException.class, () -> {
          gameMethods.summonMinion(player1, 3);
        }
    );
    
    assertThat("Le minion n'a pas pu être invoqué !").isEqualTo(exception.getMessage());
    
  }
  
  @Test
  public void testPolymorph() throws EngineException {
    board = spy(new Vector<MinionCard>());
    player1 = spy(new Player());
    player1.setBoard(board);
    ArrayList<MinionCard> invocations = spy(new ArrayList<MinionCard>());
    minion2.setId(3);
    invocations.add(minion2);
    gameMethods.setInvocations(invocations);
    doReturn(1).when(board).size();
    doReturn(minion2).when(board).get(0);
    doNothing().when(player1).addCardToBoard(minion2, 0);
    doNothing().when(gameMethods).giveAttackAuraToOtherMinions(board, minion2);
    doNothing().when(gameMethods).getAttackAuraFromOtherMinions(board, minion2);
    gameMethods.polymorph(player1, 3, "0");
    verify(player1, times(1)).addCardToBoard(minion2, 0);
    verify(gameMethods, times(1)).giveAttackAuraToOtherMinions(board, minion2);
    verify(gameMethods, times(1)).getAttackAuraFromOtherMinions(board, minion2);
  }
  
  @Test
  public void testPolymorphException() throws EngineException {
    board = spy(new Vector<MinionCard>());
    player1 = new Player();
    player1.setBoard(board);
    ArrayList<MinionCard> invocations = new ArrayList<MinionCard>();
    gameMethods.setInvocations(invocations);
    doReturn(0).when(board).size();
    Throwable exception = assertThrows(
        EngineException.class, () -> {
          gameMethods.polymorph(player1, 3, "0");
        }
    );
    
    assertThat("Le minion n'a pas pu être invoqué !").isEqualTo(exception.getMessage());
  }
  
  @ParameterizedTest
  @CsvSource({"minion_all_hello", "minion_1_hello", "minion_hello_hello", 
    "all_all_hello", "all_1_hello", "all_hello_hello","hello_hello_hello"})
  public void testTargetsFromTargetStringDefault(String spellTarget) {
    spell1.setTarget(spellTarget);
    gameMethods.setCurrentPlayer(player1);
    gameMethods.setOtherPlayer(player2);
    LinkedHashMap<String, AbstractCard> result = new LinkedHashMap<String, AbstractCard>();
    result = gameMethods.targetsFromTargetString(player1, player2, player1, 0, spellTarget);
    assertThat(result).isEmpty();
  }
  
}
