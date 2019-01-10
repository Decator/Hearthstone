package fr.univ.nantes.alma.engine;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * The complete description of a Hero.
 *
 * @author Martin Ars Alexis Claveau Alexis Loret Maud Van Dorssen
 * @version 0.0.1
 */
@Entity
@Table(name = "hero")
public class HeroCard extends AbstractCard {
  @Column(name = "nb_summon_hero") private int nbSummon;
  @Column(name = "id_invocation_hero")private int idInvocation; 
  @Column(name = "armor_buff_hero") private int armorBuff;
  @Column(name = "target_hero") private String target;
  @Column(name = "health_points_hero") private int healthPoints;
  @Column(name = "armor_points_hero") private int armorPoints;
  @Column(name = "hero_power_used") private boolean heroPowerUsed;
    
  /**
   * Initialize the attributes of this class.
   * @param idHero the id of the hero
   * @param type There are three types : mage, paladin, warrior
   * @param name the name of the hero
   * @param damage the amount of damage that the hero can inflict
   * @param nbSummon the amount of invocations that the hero can perform
   * @param idInvocation the id of the hero's invocation
   * @param armorBuff the amount of armor buff that the hero can give
   * @param description the description of the hero
   * @param target the target of the hero power
   */
  HeroCard(int idHero, String type, String name, int damage, int nbSummon,
      int idInvocation, int armorBuff, String description, String target) {
    super(idHero, type, name, 0, damage, description);
    this.nbSummon = nbSummon;
    this.idInvocation = idInvocation;
    this.armorBuff = armorBuff;
    this.heroPowerUsed = false;
    this.armorPoints = 0;
    this.healthPoints = GameRuleUtil.MAX_HERO_HP;
    this.target = target;
  }

  /**
   * Empty Constructor.
   */
  public HeroCard() { }

  /** Gets the number of minions summoned by the hero power.
   * @return the number of summoned minions.
   */
  public int getNbSummon() {
    return nbSummon;
  }

  /** Sets the number of minions summoned by the hero power.
   * @param nbSummon the number of minions to summon.
   */
  public void setNbSummon(int nbSummon) {
    this.nbSummon = nbSummon;
  }
  
  /** Get the ID of the minion summoned by the hero power.
   * @return the ID of the minion summoned minion.
   */
  public int getIdInvocation() {
    return idInvocation;
  }

  /** Sets the ID of the minion summoned by the hero power.
   * @param idInvocation the ID if the minion to summon.
   */
  public void setIdInvocation(int idInvocation) {
    this.idInvocation = idInvocation;
  }

  /** Gets the armor buff of the hero power.
   * @return the armorBuff. 
   */
  public int getArmorBuff() {
    return armorBuff;
  }

  /** Sets the armor buff of the hero power.
   * @param armorBuff the armor buff to set.
   */
  public void setArmorBuff(int armorBuff) {
    this.armorBuff = armorBuff;
  }

  /** Gets the potential target(s) of the hero power.
   * @return the target.
   */
  public String getTarget() {
    return target;
  }

  /** Sets the potential target(s) of the hero power.
   * @param target the target to set.
   */
  public void setTarget(String target) {
    this.target = target;
  }

  /** Gets the health points of the hero.
   * @return the healthPoints.
   */
  public int getHealthPoints() {
    return healthPoints;
  }

  /** Sets the health points of the hero.
   * @param healthPoints the health points to set.
   */
  public void setHealthPoints(int healthPoints) {
    this.healthPoints = healthPoints;
  }

  /** Gets the armor points of the hero.
   * @return the armorPoints.
   */
  public int getArmorPoints() {
    return armorPoints;
  }

  /** Sets the armor points of the hero.
   * @param armorPoints the armor points to set.
   */
  public void setArmorPoints(int armorPoints) {
    this.armorPoints = armorPoints;
  }

  /** Gets if the hero power has already been used.
   * @return the heroPowerUsed status.
   */
  public boolean isHeroPowerUsed() {
    return heroPowerUsed;
  }

  /** Sets if the hero power has already been used.
   * @param heroPowerUsed the heroPowerUsed to set.
   */
  public void setHeroPowerUsed(boolean heroPowerUsed) {
    this.heroPowerUsed = heroPowerUsed;
  }

  /**
   * Receives damage from an attack. If the hero has armor, then armor is reduced before health points.
   * @param damage the received damage.
   */
  void receiveDamage(int damage) {
    if (damage >= 0) {
      if (this.armorPoints >= damage && this.armorPoints > 0) {
        this.armorPoints -= damage;
      } else if (this.armorPoints < damage && this.armorPoints > 0) {
        int remainingDamage = damage - this.armorPoints;
        this.armorPoints = 0;
        this.healthPoints -= remainingDamage;
      } else {
        this.healthPoints -= damage;
      }
    }
  }

  /**
   * Receives healing from an effect.
   * @param healing the receive heal.
   */
  void receiveHealing(int healing) {
    if (healing >= 0) {
      if (!GameRuleUtil.checkHealthPoints(this.getHealthPoints() + healing)) {
        this.setHealthPoints(GameRuleUtil.MAX_HERO_HP);
      } else {
        this.healthPoints += healing;
      }
    }
  }

  @Override
  public String toString() {
    return "Hero [nbSummon=" + nbSummon + ", idInvocation=" + idInvocation + ", armorBuff=" + armorBuff
        + ", target=" + target + ", healthPoints=" + healthPoints + ", armorPoints=" + armorPoints
        + ", heroPowerUsed=" + heroPowerUsed + "]";
  }
}
