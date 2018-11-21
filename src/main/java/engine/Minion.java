package engine;
/**
 * 
 * The complete description of a Minion Card. 
 * 
 * @author Martin Ars Alexis Claveau Alexis Loret Maud Van Dorssen
 * @version 0.0.1
 */
class Minion extends Card {
	
	private int healthPoints;
	private boolean taunt;
	private boolean lifesteal;
	private boolean charge;
	private int attackBuff;
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
	
	Minion(int id, String type, String name, int manaCost, int damage, String target, String description, int healthPoints, boolean taunt, boolean lifeSteal, boolean charge, int attackBuff) {
		super(id, type, name, manaCost, damage, target, description);
		this.healthPoints = healthPoints;
		this.taunt = taunt;
		this.lifesteal = lifesteal;
		this.charge = charge;
		this.attackBuff = attackBuff;
		this.attacked = true;	
	}
	
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
		return this.lifesteal;
	}
	
	/**
     * Get the charge of the minion. 
     * @return the charge
     */
	public boolean getCharge() {
		return this.charge;
	}
	
	/**
     * Get the nbSummon of the card. 
     * @return the nbSummon
     */
	public int getAttackBuff() {
		return this.attackBuff;
	}
	
	/**
     * Get the nbSummon of the card. 
     * @return the nbSummon
     */
	public boolean getAttacked() {
		return this.attacked;
	}
	
	/**
     * Get the nbSummon of the card. 
     * @return the nbSummon
     */
	void setAttacked() {
		this.attacked = !this.attacked;
	}
	

}
