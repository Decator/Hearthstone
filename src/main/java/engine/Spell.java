package engine;

/**
* This class is the complete description of a Spell's card
*
* @author Alexis Claveau, Martin Ars, Maud Van Dorssen, Alexis Loret
* @version 0.0.1
*/
class Spell extends Card {
    private int nbSummon;
    private int idInvocation;
    private int attackBuff;
    private int armorBuff;
    private int nbDraw;
    private boolean polymorph;

    /**
    * This constructor allows to initialize the attributes of this class
    * @param id It's the id of the card
    * @param type There are five types : common, mage, paladin, warrior and invocation 
    * @param name It's the name of the card
    * @param manaCost It's the number of mana that the card costs
    * @param damage It's the number of damage that the card can inflict
    * @param target It's a simple text with the target of the card
    * @param description It's the description of the card
    * @param nbSummon It's the number of invocations that the card can perfom
    * @param idInvocation It's the card's id of the invocation
    * @param attackBuff It's the number of the attack buff that the card can give
    * @param armorBuff It's the number of the armor buff that the card can give
    * @param nbDraw It's the number of card that the player can draw
    * @param polymorph It's just a boolean to indicate if the card is polymorph or not
    */
    Spell(int id, String type, String name, int manaCost, int damage, String target, String description, int nbSummon, int idInvocation, int attackBuff, int armorBuff, int nbDraw, boolean polymorph) {
        super(id, type, name, manaCost, damage, target, description);
        
        this.nbSummon = nbSummon;
        this.idInvocation = idInvocation;
        this.attackBuff = attackBuff;
        this.armorBuff = armorBuff;
        this.nbDraw = nbDraw;
        this.polymorph = polymorph;
    }

    /**
    * This method return the nbSummon of the card
    * @return the nbSummon
    */
    public int getNbSummon() {
        return this.nbSummon;
    }

    /**
    * This method return the idInvocation of the card
    * @return the idInvocation
    */
    public int getIdInvocation() {
        return this.idInvocation;
    }

    /**
    * This method return the attackBuff of the card
    * @return the attackBuff
    */
    public int getAttackBuff() {
        return this.attackBuff;
    }

    /**
    * This method return the armorBuff of the card
    * @return the armorBuff
    */
    public int getArmorBuff() {
        return this.armorBuff;
    }

    /**
    * This method return the nbDraw of the card
    * @return the nbDraw
    */
    public int getNbDraw() {
        return this.nbDraw;
    }

    /**
    * This method return true or false according to the polymorph of the card
    * @return the true or false
    */
    public boolean getPolymorph() {
        return this.polymorph;
    }
}