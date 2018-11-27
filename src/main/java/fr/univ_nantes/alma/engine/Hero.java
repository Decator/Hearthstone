package fr.univ_nantes.alma.engine;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 *
 * The complete description of a Hero.
 *
 * @author Martin Ars Alexis Claveau Alexis Loret Maud Van Dorssen
 * @version 0.0.1
 */
@Entity
public class Hero {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
    private int id;
    private String type;
    private String name;
    private int healthPoints; //
    private int armorPoints; //
    private int damage;
	private int nbSummon;
	private int idInvocation;
	private int armorBuff;
	private String target;
	private String description;
    private boolean heroPowerUsed; //

    /**
     * Initialize the attributes of this class.
     * @param id the id of the hero
     * @param type There are three types : mage, paladin, warrior
     * @param name the name of the hero
     * @param damage the amount of damage that the hero can inflict
	 * @param nbSummon the amount of invocations that the hero can perfom
	 * @param idInvocation the id of the hero's invocation
	 * @param armorBuff the amount of armor buff that the hero can give
	 * @param target a simple text with the target of the hero
	 * @param description the description of the hero
     */
    public Hero(int id, String type, String name, int damage, int nbSummon, int idInvocation, int armorBuff, String target, String description) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.damage = damage;
		this.nbSummon = nbSummon;
		this.idInvocation = idInvocation;
		this.armorBuff = armorBuff;
		this.target = target;
		this.description = description;
        this.heroPowerUsed = false;
        this.armorPoints = 0;
        this.healthPoints = Rule.MAX_HERO_HEALTH_POINTS;
    }
    
    public Hero() {}

    /**
     * Get the id of the hero.
     * @return the id of the hero
     */
    public int getId() {
        return this.id;
    }

    /**
     * Get the type (class) of the hero.
     * @return the type of the hero
     */
    public String getType() {
        return this.type;
    }

    /**
     * Get the name of the hero.
     * @return the name of the hero
     */
    public String getName() {
        return this.name;
    }

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
     * Get the amount of damage that the hero can inflict. 
     * @return the amount of damage
     */
    public int getDamage() {
        return this.damage;
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
     * Get the target of the hero. 
     * @return the target of the hero
     */
    public String getTarget() {
        return this.target;
    }
    
    /**
     * Get the description of the hero. 
     * @return the description of the hero
     */
    public String getDescription() {
        return this.description;
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
    void setHeroPowerUsed() {
        this.heroPowerUsed = !this.heroPowerUsed;
    }
    
	/**
	 * Receive damage from an attack. 
	 * @param damage the received damage
	 */
	void recieveDamage(int damage){
		//check rules
		this.healthPoints -= damage;
	}
    
    public String toString() {
    	return "Hero " + this.id + "\n"
    			+ "Type : " + this.type + "\n"
    			+ "Name : " + this.name + "\n"
    			+ "Damage : " + this.damage + "\n"
    			+ "NbSummon : " + this.nbSummon + "\n"
    			+ "IdInvocation : " + this.idInvocation + "\n"
    			+ "ArmorBuff : " + this.armorBuff + "\n"
    			+ "Target : " + this.target + "\n"
    			+ "Description : " + this.description + "\n"
    			+ "HealthPoints : " + this.healthPoints + "\n"
    			+ "ArmorPoints : " + this.armorPoints + "\n"
    			+ "HeroPowerUsed : " + this.heroPowerUsed + "\n";
    }
}
