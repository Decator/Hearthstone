package engine;

/**
 *
 * The complete description of a Hero.
 *
 * @author Martin Ars Alexis Claveau Alexis Loret Maud Van Dorssen
 * @version 0.0.1
 */

class Hero {
    private int id;
    private String type;
    private String name;
    private int healthPoints;
    private int armorPoints;
    private boolean heroPowerUsed;
    private HeroPower heroPower;

    /**
     * Initialize the attributes of this class.
     * @param id the id of the card
     * @param type There are five types : common, mage, paladin, warrior and invocation
     * @param name the name of the card
     */

    Hero(int id, String type, String name) {
        this.id = id;
        this.type = type;
        this.name = name;
    }

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
     * Get the HeroPower.
     * @return the HeroPower
     */

    public HeroPower getHeroPower() {
        return this.heroPower;
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
}
