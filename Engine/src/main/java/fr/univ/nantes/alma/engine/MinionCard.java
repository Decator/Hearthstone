package fr.univ.nantes.alma.engine;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * The complete description of a Minion Card. 
 * 
 * @author Martin Ars, Alexis Claveau, Alexis Loret, Maud Van Dorssen
 * @version 0.0.1
 */
@Entity
@Table(name = "minion")
public class MinionCard extends AbstractCard {
  @Column(name = "health_points_minion") private int healthPoints;
  @Column(name = "taunt_minion") private boolean taunt;
  @Column(name = "lifesteal_minion") private boolean lifesteal;
  @Column(name = "charge_minion") private boolean charge;
  @Column(name = "attack_buff_aura_minion") private int attackBuffAura;
  @Column(name = "attacked_minion")private boolean attacked;
  
  /**
   * Initialize the attributes of this class. 
   * @param idMinion the id of the card
   * @param type There are five types : common, mage, paladin, warrior and invocation 
   * @param name the name of the card
   * @param manaCost the amount of mana that the card costs
   * @param damage the amount of damage that the card can inflict
   * @param description the description of the card
   * @param attackBuff the amount of the attack buff that the card can give
   * @param taunt if the card is taunting or not
   * @param lifesteal if the can can steal life or not
   * @param charge if the card can charge or not
   * @param healthPoints the amount of health points the minion has
   */

  MinionCard(int idMinion, String type, String name, int manaCost, 
      int damage, String description, int healthPoints, boolean taunt, 
      boolean lifesteal, boolean charge, int attackBuffAura) {
    super(idMinion, type, name, manaCost, damage, description);
    this.healthPoints = healthPoints;
    this.taunt = taunt;
    this.lifesteal = lifesteal;
    this.charge = charge;
    this.attackBuffAura = attackBuffAura;
    this.attacked = true;
  }
  
  public MinionCard() {}
  
  /**
   * Returns a clone of the minion.
   */
  public Object clone() throws CloneNotSupportedException { 
    return super.clone(); 
  }

  /** Gets the health points of the minion.
   * @return the healthPoints of the minion.
   */
  public int getHealthPoints() {
    return this.healthPoints;
  }

  /** Sets the health points of the minion.
   * @param healthPoints the healthPoints to set.
   */
  public void setHealthPoints(int healthPoints) {
    this.healthPoints = healthPoints;
  }

  /** Gets if the minion has taunt.
   * @return the taunt status to get.
   */
  public boolean isTaunt() {
    return this.taunt;
  }

  /** Sets if the minion has taunt.
   * @param taunt the taunt status to set.
   */
  public void setTaunt(boolean taunt) {
    this.taunt = taunt;
  }

  /** Gets if the minion has Lifesteal.
   * @return the lifeSteal status to get.
   */
  public boolean isLifesteal() {
    return this.lifesteal;
  }

  /** Sets if the minion has Lifesteal.
   * @param lifesteal the lifesteal status to set.
   */
  public void setLifesteal(boolean lifesteal) {
    this.lifesteal = lifesteal;
  }

  /** Gets if the minion has Charge.
   * @return the charge status to get.
   */
  public boolean isCharge() {
    return this.charge;
  }

  /** Sets if the minion has Charge.
   * @param charge the charge status to set.
   */
  public void setCharge(boolean charge) {
    this.charge = charge;
  }

  /** Gets the minion's attack buff aura.
   * @return the attackBuffAura of the minion.
   */
  public int getAttackBuffAura() {
    return this.attackBuffAura;
  }
  
  /** Sets the attack buff aura of the minion.
   * @param attackBuffAura the attackBuffAura to set.
   */
  public void setAttackBuffAura(int attackBuffAura) {
    this.attackBuffAura = attackBuffAura;
  }

  /** Gets if the minion has attacked already.
   * @return the attacked status of the minion.
   */
  public boolean isAttacked() {
    return this.attacked;
  }

  /** Sets if the minion has attacked already.
   * @param attacked the attacked status to set.
   */
  public void setAttacked(boolean attacked) {
    this.attacked = attacked;
  }

  /**
   * Receives damage from an attack. 
   * @param damage the received damage.
   */
  void receiveDamage(int damage) {
    if (damage >= 0) {
      this.healthPoints -= damage;
    }
  }

  @Override
  public String toString() {
    return "MinionCard [healthPoints=" + healthPoints + ", taunt=" + taunt + ", lifesteal=" 
        + lifesteal + ", charge=" + charge + ", attackBuffAura=" + attackBuffAura 
        + ", attacked=" + attacked + "]";
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + attackBuffAura;
    result = prime * result + (attacked ? 1231 : 1237);
    result = prime * result + (charge ? 1231 : 1237);
    result = prime * result + healthPoints;
    result = prime * result + (lifesteal ? 1231 : 1237);
    result = prime * result + (taunt ? 1231 : 1237);
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!super.equals(obj)) {
      return false;
    }
    if (!(obj instanceof MinionCard)) {
      return false;
    }
    MinionCard other = (MinionCard) obj;
    if (attackBuffAura != other.attackBuffAura) {
      return false;
    }
    if (attacked != other.attacked) {
      return false;
    }
    if (charge != other.charge) {
      return false;
    }
    if (healthPoints != other.healthPoints) {
      return false;
    }
    if (lifesteal != other.lifesteal) {
      return false;
    }
    if (taunt != other.taunt) {
      return false;
    }
    return true;
  }
  
}