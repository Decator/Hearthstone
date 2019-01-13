package fr.univ.nantes.alma.engine;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * The complete description of a card wihch can be a Minion, a Spell or a Hero. 
 *
 * @author Alexis Claveau, Martin Ars, Maud Van Dorssen, Alexis Loret
 * @version 0.0.1
 */
@MappedSuperclass
public abstract class AbstractCard implements Cloneable {
  @Id
  @Column(name = "id") private int idCard;
  @Column(name = "type") private String type;
  @Column(name = "name") private String name;
  @Column(name = "mana_cost") private int manaCost;
  @Column(name = "damage") private int damage;
  @Column(name = "description") private String description;

  /**
   * Initialize the attributes of this class. 
   * @param idCard the id of the card.
   * @param type There are five types : common, mage, paladin, warrior and invocation. 
   * @param name the name of the card.
   * @param manaCost the amount of mana the card costs.
   * @param damage the amount of damage the card can inflict.
   * @param description the description of the card.
   */
  AbstractCard(int idCard, String type, String name, int manaCost, int damage, String description) {
    this.idCard = idCard;
    this.type = type;
    this.name = name;
    this.manaCost = manaCost;
    this.damage = damage;
    this.description = description;
  }
    
  AbstractCard() {}
    
  public Object clone() throws CloneNotSupportedException { 
    return super.clone();
  }

  public int getId() {
    return this.idCard;
  }

  public void setId(int idCard) {
    this.idCard = idCard;
  }

  public String getType() {
    return this.type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getManaCost() {
    return this.manaCost;
  }

  public void setManaCost(int manaCost) {
    this.manaCost = manaCost;
  }

  public int getDamage() {
    return this.damage;
  }

  public void setDamage(int damage) {
    this.damage = damage;
  }

  public String getDescription() {
    return this.description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + damage;
    result = prime * result + ((description == null) ? 0 : description.hashCode());
    result = prime * result + idCard;
    result = prime * result + manaCost;
    result = prime * result + ((name == null) ? 0 : name.hashCode());
    result = prime * result + ((type == null) ? 0 : type.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (!(obj instanceof AbstractCard))
      return false;
    AbstractCard other = (AbstractCard) obj;
    if (damage != other.damage)
      return false;
    if (description == null) {
      if (other.description != null)
        return false;
    } else if (!description.equals(other.description))
      return false;
    if (idCard != other.idCard)
      return false;
    if (manaCost != other.manaCost)
      return false;
    if (name == null) {
      if (other.name != null)
        return false;
    } else if (!name.equals(other.name))
      return false;
    if (type == null) {
      if (other.type != null)
        return false;
    } else if (!type.equals(other.type))
      return false;
    return true;
  }
  
  
}