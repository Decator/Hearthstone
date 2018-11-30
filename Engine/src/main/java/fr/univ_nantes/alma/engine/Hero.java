package fr.univ_nantes.alma.engine;

import javax.persistence.Entity;

/**
 *
 * The complete description of a Hero.
 *
 * @author Martin Ars Alexis Claveau Alexis Loret Maud Van Dorssen
 * @version 0.0.1
 */
@Entity
public class Hero extends Card {
	
    private int healthPoints; //
    private int armorPoints; //
	private int nbSummon;
	private int idInvocation;
	private int armorBuff;
    private boolean heroPowerUsed;

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
	 * @param manaCost the manaCost of the hero, irrelevant
     */
    public Hero(int id, String type, String name, int damage, int nbSummon, int idInvocation, int armorBuff, String description) {
    	super(id, type, name, 0, damage, description);
		this.nbSummon = nbSummon;
		this.idInvocation = idInvocation;
		this.armorBuff = armorBuff;
        this.heroPowerUsed = false;
        this.armorPoints = 0;
        this.healthPoints = Rule.MAX_HERO_HEALTH_POINTS;
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
     * Get the amount of invocations that the hero can perfom. 
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
     * Get the amount of armor buff that the hero can give. 
     * @return the amount of armor buff
     */
    public int getArmorBuff() {
        return this.armorBuff;
    }

    /**
     * Set the amout of healthPoints
     * @param healthPoints the amout of healthPoints
     */
    void setHealthPoints(int healthPoints) {
        this.healthPoints = healthPoints;
    }

    /**
     * Set the amout of armorPoints
     * @param armorPoints the amout of armorPoints
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
	 * Receive damage from an attack. 
	 * @param damage the received damage
	 */
	void receiveDamage(int damage){
		//check rules
		this.healthPoints -= damage;
	}
	
	void receiveHealing(int healing) {
		this.healthPoints += healing;
	}
    
    public String toString() {
    	return "Hero " + super.toString()
    			+ "NbSummon : " + this.nbSummon + "\n"
    			+ "IdInvocation : " + this.idInvocation + "\n"
    			+ "ArmorBuff : " + this.armorBuff + "\n"
    			+ "HealthPoints : " + this.healthPoints + "\n"
    			+ "ArmorPoints : " + this.armorPoints + "\n"
    			+ "HeroPowerUsed : " + this.heroPowerUsed + "\n";
    }
}
