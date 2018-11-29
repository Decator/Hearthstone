package fr.univ_nantes.alma.engine;

import javax.persistence.Entity;

/**
 * The complete description of a Spell Card. 
 *
 * @author Alexis Claveau, Martin Ars, Maud Van Dorssen, Alexis Loret
 * @version 0.0.1
 */
@Entity
public class Spell extends Card {
	
    private int nbSummon;
    private int idInvocation;
    private int attackBuff;
    private int armorBuff;
    private int nbDraw;
    private boolean polymorph;
    private String target;

    /**
     * Initialize the attributes of this class. 
     * @param id the id of the card
     * @param type There are five types : common, mage, paladin, warrior and invocation 
     * @param name the name of the card
     * @param manaCost the amount of mana that the card costs
     * @param damage the amount of damage that the card can inflict
     * @param description the description of the card
     * @param nbSummon the amount of invocations that the card can perfom
     * @param idInvocation the card's id of the invocation
     * @param attackBuff the amount of the attack buff that the card can give
     * @param armorBuff the amount of the armor buff that the card can give
     * @param nbDraw the amount of cards that the player can draw
     * @param polymorph a boolean to indicate if the card is polymorphic or not
     * @param target a simple text with the target of the card
     */
    public Spell(int id, String type, String name, int manaCost, int damage, String description, int nbSummon, int idInvocation, int attackBuff, int armorBuff, int nbDraw, boolean polymorph, String target) {
        super(id, type, name, manaCost, damage, description);
        
        this.nbSummon = nbSummon;
        this.idInvocation = idInvocation;
        this.attackBuff = attackBuff;
        this.armorBuff = armorBuff;
        this.nbDraw = nbDraw;
        this.polymorph = polymorph;
        this.target = target;
    }
    
    public Spell() {}

    /**
     * Get the nbSummon of the card. 
     * @return the nbSummon
     */
    public int getNbSummon() {
        return this.nbSummon;
    }

    /**
     * Get the idInvocation of the card.
     * @return the idInvocation
     */
    public int getIdInvocation() {
        return this.idInvocation;
    }

    /**
     * Get the attackBuff of the card. 
     * @return the attackBuff
     */
    public int getAttackBuff() {
        return this.attackBuff;
    }

    /**
     * Get the armorBuff of the card. 
     * @return the armorBuff
     */
    public int getArmorBuff() {
        return this.armorBuff;
    }

    /**
     * Get the nbDraw of the card. 
     * @return the nbDraw
     */
    public int getNbDraw() {
        return this.nbDraw;
    }

    /**
     * Get the polymorph attribute of the card. 
     * @return the polymorph attribute
     */
    public boolean getPolymorph() {
        return this.polymorph;
    }
    
    /**
     * Get the target of the card. 
     * @return the target of the card
     */
    public String getTarget() {
        return this.target;
    }
    
    public String toString() {
    	return "Spell " + super.toString()
    			+ "NbSummon : " + this.nbSummon + "\n"
    			+ "IdInvocation : " + this.idInvocation + "\n"
    			+ "AttackBuff : " + this.attackBuff + "\n"
    			+ "ArmorBuff : " + this.armorBuff + "\n"
    			+ "NbDraw : " + this.nbDraw + "\n"
    			+ "Polymorph : " + this.polymorph + "\n"
    			+ "Target : " + this.target + "\n";
    }
}