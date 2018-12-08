package fr.univ_nantes.alma.engine;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * The complete description of a card wich can be either a Minion or a Spell. 
 *
 * @author Alexis Claveau, Martin Ars, Maud Van Dorssen, Alexis Loret
 * @version 0.0.1
 */
@MappedSuperclass
public abstract class Card implements Cloneable {
	@Id
	@Column(name = "id") private int id;
	@Column(name = "type") private String type;
	@Column(name = "name") private String name;
	@Column(name = "mana_cost") private int manaCost;
	@Column(name = "damage") private int damage;
	@Column(name = "description") private String description;

    /**
     * Initialize the attributes of this class. 
     * @param id the id of the card
     * @param type There are five types : common, mage, paladin, warrior and invocation 
     * @param name the name of the card
     * @param manaCost the amount of mana the card costs
     * @param damage the amount of damage that the card can inflict
     * @param description the description of the card
     */
    Card(int id, String type, String name, int manaCost, int damage, String description) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.manaCost = manaCost;
        this.damage = damage;
        this.description = description;
    }
    
    Card() {}
    
    public Object clone() throws CloneNotSupportedException { 
    	return super.clone(); 
	} 

    /**
     * Get the id of the card. 
     * @return the id of the card
     */
    public int getId() {
        return this.id;
    }

    /**
     * Get the type of the card. 
     * @return the type of the card
     */
    public String getType() {
        return this.type;
    }

    /**
     * Get the name of the card. 
     * @return the name of the card
     */
    public String getName() {
        return this.name;
    }

    /**
     * Get the manaCost of the card. 
     * @return the manaCost of the card
     */
    public int getManaCost() {
        return this.manaCost;
    }

    /**
     * Get the damage of the card. 
     * @return the damage of the card
     */
    public int getDamage() {
        return this.damage;
    }
    
	/**
	 * Sets the damage attribute of a card
	 * @param damage
	 */
	
	
	void setDamage(int damage) {
		this.damage = damage;
	}

    /**
     * Get the description of the card. 
     * @return the description of the card
     */
    public String getDescription() {
        return this.description;
    }
    
    public String toString() {
    	return "Card " + this.id + "\n"
    			+ "Type : " + this.type + "\n"
    			+ "Name : " + this.name + "\n"
    			+ "ManaCost : " + this.manaCost + "\n"
    			+ "Damage : " + this.damage + "\n"
    			+ "Description : " + this.description + "\n";
    }
}