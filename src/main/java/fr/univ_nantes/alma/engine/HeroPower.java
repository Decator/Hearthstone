package fr.univ_nantes.alma.engine;

/**
 * The complete description of a Hero's Power. 
 *
 * @author Martin Ars, Alexis Claveau, Alexis Loret, Maud Van Dorssen
 * @version 0.0.1
 */
class HeroPower {
	
	private int damage;
	private int nbSummon;
	private int idInvocation;
	private int armorBuff;
	private String target;
	private String description;
	
	/**
	 * Initialize the attributes of this class. 
	 * @param damage the amount of damage that the hero can inflict
	 * @param nbSummon the amount of invocations that the hero can perfom
	 * @param idInvocation the id of the hero's invocation
	 * @param armorBuff the amount of armor buff that the hero can give
	 * @param target a simple text with the target of the hero
	 * @param description the description of the hero
	 */
	HeroPower(int damage, int nbSummon, int idInvocation, int armorBuff, String target, String description) {
		this.damage = damage;
		this.nbSummon = nbSummon;
		this.idInvocation = idInvocation;
		this.armorBuff = armorBuff;
		this.target = target;
		this.description = description;
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
}
