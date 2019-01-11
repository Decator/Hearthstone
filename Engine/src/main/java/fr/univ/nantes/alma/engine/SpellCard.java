package fr.univ.nantes.alma.engine;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * The complete description of a Spell Card. 
 *
 * @author Alexis Claveau, Martin Ars, Maud Van Dorssen, Alexis Loret
 * @version 0.0.1
 */
@Entity
@Table(name = "spell")
public class SpellCard extends AbstractCard {
  @Column(name = "nb_summon_spell") private int nbSummon;
  @Column(name = "id_invocation_spell") private int idInvocation;
  @Column(name = "attack_buff_spell") private int attackBuff;
  @Column(name = "armor_buff_spell") private int armorBuff;
  @Column(name = "nb_draw_spell") private int nbDraw;
  @Column(name = "polymorph_spell") private boolean polymorph;
  @Column(name = "target_spell") private String target;
    
  /**
   * Initialize the attributes of this class. 
   * @param idSpell the id of the card
   * @param type There are five types : common, mage, paladin, warrior and invocation 
   * @param name the name of the card
   * @param manaCost the amount of mana that the card costs
   * @param damage the amount of damage that the card can inflict
   * @param description the description of the card
   * @param nbSummon the amount of invocations that the card can perfom
   * @param idInvocation the card's id of the invocation
   * @param attackBuff the amount of the attack buff that the card can give
   * @param armorBuff the amount of the armor buff that the card can give
   * @param nbDraw the amount of cards that the player can draw
   * @param polymorph a boolean to indicate if the card is polymorphic or not
   * @param target a simple text with the target of the card
   */
  SpellCard(int idSpell, String type, String name, int manaCost, int damage, 
      String description, int nbSummon, int idInvocation, int attackBuff, 
      int armorBuff, int nbDraw, boolean polymorph, String target) {
    super(idSpell, type, name, manaCost, damage, description);
    this.nbSummon = nbSummon;
    this.idInvocation = idInvocation;
    this.attackBuff = attackBuff;
    this.armorBuff = armorBuff;
    this.nbDraw = nbDraw;
    this.polymorph = polymorph;
    this.target = target;
  }
    
  public SpellCard() {}
    
  /**
   * Returns a clone of the spell (based on Card clone()).
   */
  public Object clone() throws CloneNotSupportedException { 
    return super.clone(); 
  }

  /**
   * Gets the number of minions summoned by the spell.
   * @return the number of summoned minions.
   */
  public int getNbSummon() {
    return this.nbSummon;
  }

  /**
   * Sets the number of minions summoned by the spell.
   * @param nbSummon the number of summoned minions.
   */
  public void setNbSummon(int nbSummon) {
    this.nbSummon = nbSummon;
  }

  /**
   * Gets the ID of the minion to be summoned by the spell.
   * @return the ID of the minion to be summoned.
   */
  public int getIdInvocation() {
    return this.idInvocation;
  }

  /**
   * Sets the ID of the minion to be summoned by the spell.
   * @param idInvocation the ID of the minion to be summoned.
   */
  public void setIdInvocation(int idInvocation) {
    this.idInvocation = idInvocation;
  }

  /**
   * Gets the attack buff given by the spell.
   * @return the attack buff.
   */
  public int getAttackBuff() {
    return this.attackBuff;
  }

  /**
   * Sets the attack buff given by the spell.
   * @param attackBuff the attack buff.
   */
  public void setAttackBuff(int attackBuff) {
    this.attackBuff = attackBuff;
  }

  /**
   * Gets the armor buff given by the spell.
   * @return the armor buff.
   */
  public int getArmorBuff() {
    return this.armorBuff;
  }

  /**
   * Sets the armor buff given by the spell.
   * @param armorBuff the armor buff.
   */
  public void setArmorBuff(int armorBuff) {
    this.armorBuff = armorBuff;
  }
  
  /**
   * Gets the number of cards drawn thanks to the spell.
   * @return the number of cards drawn.
   */
  public int getNbDraw() {
    return this.nbDraw;
  }

  /**
   * Sets the number of cards drawn thanks to the spell.
   * @param nbDraw the number of cards drawn.
   */
  public void setNbDraw(int nbDraw) {
    this.nbDraw = nbDraw;
  }

  /**
   * Gets if the spell has a polymorphing effect.
   * @return a statement about the polymorphing effect.
   */
  public boolean isPolymorph() {
    return this.polymorph;
  }

  /**
   * Sets if the spell has a polymorphing effect.
   * @param polymorph the statement about the polymorphing effect.
   */
  public void setPolymorph(boolean polymorph) {
    this.polymorph = polymorph;
  }

  /**
   * Gets the potential target(s) of the spell.
   * @return the potential target(s).
   */
  public String getTarget() {
    return this.target;
  }

  /**
   * Sets the potential target(s) of the spell.
   * @param target the potential target(s) of the spell.
   */
  public void setTarget(String target) {
    this.target = target;
  }

  @Override
  public String toString() {
    return "SpellCard [nbSummon=" + nbSummon + ", idInvocation=" + idInvocation + ", attackBuff=" + attackBuff
        + ", armorBuff=" + armorBuff + ", nbDraw=" + nbDraw + ", polymorph=" + polymorph + ", target=" + target + "]";
  }
  
  
}