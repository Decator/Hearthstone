package fr.univ_nantes.alma.engine;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 *
 * The complete description of a Hero.
 *
 * @author Martin Ars Alexis Claveau Alexis Loret Maud Van Dorssen
 * @version 0.0.1
 */
@Entity
@Table(name="hero")
public class Hero extends Card {
	
	@Column(name = "nb_summon_hero") private int nbSummon;
	@Column(name = "id_invocation_hero")private int idInvocation; 
	@Column(name = "armor_buff_hero") private int armorBuff;
    @Column(name = "target_hero") private String target;
    @Column(name = "health_points_hero") private int healthPoints;
    @Column(name = "armor_points_hero") private int armorPoints;
    @Column(name = "hero_power_used") private boolean heroPowerUsed;
    /**
     * Initialize the attributes of this class.
     * @param id the id of the hero
     * @param type There are three types : mage, paladin, warrior
     * @param name the name of the hero
     * @param damage the amount of damage that the hero can inflict
	 * @param nbSummon the amount of invocations that the hero can perform
	 * @param idInvocation the id of the hero's invocation
	 * @param armorBuff the amount of armor buff that the hero can give
	 * @param description the description of the hero
	 * @param target the target of the hero power
     */
    Hero(int id, String type, String name, int damage, int nbSummon, int idInvocation, int armorBuff, String description, String target) {
    	super(id, type, name, 0, damage, description);
		this.nbSummon = nbSummon;
		this.idInvocation = idInvocation;
		this.armorBuff = armorBuff;
        this.heroPowerUsed = false;
        this.armorPoints = 0;
        this.healthPoints = Rule.MAX_HERO_HEALTH_POINTS;
        this.target = target;
    }
    
    public Hero() {}
    

    /**
     * Get the healthPoints of the hero.
     * @return the healthPoints of the hero
     */
    public int getHealthPoints() {
        return this.healthPoints;
    }

    /**
     * Get the armorPoints of the hero.
     * @return the armorPoints of the hero
     */
    public int getArmorPoints() {
        return this.armorPoints;
    }

    /**
     * Get the boolean which checks if the hero has already used his power.
     * @return the boolean which checks if the hero has already used his power
     */
    public boolean getHeroPowerUsed() {
        return this.heroPowerUsed;
    }
    
    /**
     * Get the amount of invocations that the hero can perform. 
     * @return the amount of invocations
     */
    public int getNbSummon() {
        return this.nbSummon;
    }
    
    /**
     * Get the id of the hero's invocation. 
     * @return the id
     */
    public int getIdInvocation() {
        return this.idInvocation;
    }
    
    /**
     * Gets the target of the hero power
     * @return target of the hero power
     */
    public String getTarget() {
    	return this.target;
    }
    
    /**
     * Get the amount of armor buff that the hero can give. 
     * @return the amount of armor buff
     */
    public int getArmorBuff() {
        return this.armorBuff;
    }

    /**
     * Set the amount of healthPoints
     * @param healthPoints the amount of healthPoints
     */
    void setHealthPoints(int healthPoints) {
        this.healthPoints = healthPoints;
    }

    /**
     * Set the amount of armorPoints
     * @param armorPoints the amount of armorPoints
     */
    void setArmorPoints(int armorPoints) {
        this.armorPoints = armorPoints;
    }

    /**
     * Replaces the heroPowerUsed variable by its reverse (true, false).
     */
    void setHeroPowerUsed(boolean bool) {
        this.heroPowerUsed = bool;
    }
   
	/**
	 * Receive damage from an attack. If the hero has armor, then armor is reduced before health points.
	 * @param damage the received damage
	 */
	void receiveDamage(int damage){
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
	
	/**
	 *  Receive heal from an effect
	 * @param healing the receive heal
	 */
	void receiveHealing(int healing) {
		this.healthPoints += healing;
	}
}
