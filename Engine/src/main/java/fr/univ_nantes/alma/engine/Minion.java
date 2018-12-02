package fr.univ_nantes.alma.engine;

import javax.persistence.Entity;

/**
 * 
 * The complete description of a Minion Card. 
 * 
 * @author Martin Ars, Alexis Claveau, Alexis Loret, Maud Van Dorssen
 * @version 0.0.1
 */
@Entity
public class Minion extends Card {
	
	private int healthPoints;
	private boolean taunt;
	private boolean lifeSteal;
	private boolean charge;
	private int attackBuffAura;
	private boolean attacked;
	
	/**
     * Initialize the attributes of this class. 
     * @param id the id of the card
     * @param type There are five types : common, mage, paladin, warrior and invocation 
     * @param name the name of the card
     * @param manaCost the amount of mana that the card costs
     * @param damage the amount of damage that the card can inflict
     * @param target a simple text with the target of the card
     * @param description the description of the card
     * @param attackBuff the amount of the attack buff that the card can give
     * @param taunt if the card is taunting or not
     * @param lifeSteal if the can can steal life or not
     * @param charge if the card can charge or not
     * @param healthPoints the amount of health points the minion has
     */
	
	public Minion(int id, String type, String name, int manaCost, int damage, String description, int healthPoints, boolean taunt, boolean lifeSteal, boolean charge, int attackBuffAura) {
		super(id, type, name, manaCost, damage, description);
		this.healthPoints = healthPoints;
		this.taunt = taunt;
		this.lifeSteal = lifeSteal;
		this.charge = charge;
		this.attackBuffAura = attackBuffAura;
		this.attacked = true;	
	}
	
	public Minion() {}
	
	/**
     * Get the healthPoints of the minion
     * @return the healthPoints
     */
	public int getHealthPoints() {
		return this.healthPoints;
	}
	
	/**
     * Get the taunt of the minion 
     * @return the taunt
     */
	public boolean getTaunt() {
		return this.taunt;
	}
	
	/**
     * Get the lifeSteal of the minion. 
     * @return the lifeSteal
     */
	public boolean getLifesteal() {
		return this.lifeSteal;
	}
	
	/**
     * Get the charge of the minion. 
     * @return the charge
     */
	public boolean getCharge() {
		return this.charge;
	}
	
	/**
     * Get the attackBuffAura of the card. 
     * @return the attackBuffAura
     */
	public int getAttackBuffAura() {
		return this.attackBuffAura;
	}
	
	/**
     * Get the attacked of the card. 
     * @return the attacked
     */
	public boolean getAttacked() {
		return this.attacked;
	}
	
	/**
     * Replaces the attacked variable by (true, false).
     */
	void setAttacked(boolean bool) {
		this.attacked = bool;
	}
	
	/**
	 * Sets the damage attribute of a minion
	 * @param damage
	 */
	void setDamage(int damage) {
		this.damage = damage;
	}
	
	/**
	 * Receive damage from an attack. 
	 * @param damage the received damage
	 */
	void receiveDamage(int damage){
		this.healthPoints -= damage;
	}
}
