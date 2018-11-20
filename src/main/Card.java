package engine;

/**
* This class is the complete description of a card wich can be either a Minion or a Spell
*
* @author Alexis Claveau, Martin Ars, Maud Van Dorssen, Alexis Loret
* @version 0.0.1
*/
class abstract Card {
    protected int id;
    protected String type;
    protected String name;
    protected int manaCost;
    protected int damage;
    protected String target;
    protected String description;

    /**
    * This constructor allows to initialize the attributes of this class
    * @param id It's the id of the card
    * @param type There are five types : common, mage, paladin, warrior and invocation 
    * @param name It's the name of the card
    * @param manaCost It's the number of mana that the card costs
    * @param damage It's the number of damage that the card can inflict
    * @param target It's a simple text with the target of the card
    * @param description It's the description of the card
    */
    Card(int id, String type, String name, int manaCost, int damage, String target, String description ) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.manaCost = damage;
        this.damage = damage;
        this.target = target;
        this.description = description;
    }

    /**
    * This method return the id of the card
    * @return the id
    */
    public int getId() {
        return this.id;
    }

    /**
    * This method return the type of the card
    * @return the type
    */
    public String getType() {
        return this.type;
    }

    /**
    * This method return the name of the card
    * @return the name
    */
    public String getName() {
        return this.name
    }

    /**
    * This method return the manaCost of the card
    * @return the manaCost
    */
    public int getManaCost() {
        return this.manaCost;
    }

    /**
    * This method return the damage of the card
    * @return the damage
    */
    public int getDamage() {
        return this.damage;
    }

    /**
    * This method return the target of the card
    * @return the target
    */
    public String getTarget() {
        return this.target;
    }

    /**
    * This method return the description of the card
    * @return the description
    */
    public String getDescription() {
        return this.description;
    }
}