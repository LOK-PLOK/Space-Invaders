/**
 * Represents a power-up in the game.
 * Power-ups can provide extra lives or increased damage.
 */
public class PowerUp extends Block {
    // 1 - extra life
    // 2 - extra damage
    private int type;
    private boolean taken = false;

    /**
     * Constructs a PowerUp with the specified type and position.
     *
     * @param type the type of the power-up (1 for extra life, 2 for extra damage)
     * @param x the x-coordinate of the power-up
     * @param y the y-coordinate of the power-up
     */
    public PowerUp(int type, int x, int y) {
        super(x, y, 4, 4, null);
        this.type = type;
    }

    /**
     * Gets the type of the power-up.
     *
     * @return the type of the power-up
     */
    public int getType() {
        return type;
    }

    /**
     * Checks if the power-up has been taken.
     *
     * @return true if the power-up has been taken, false otherwise
     */
    public boolean isTaken() {
        return taken;
    }

    /**
     * Sets the taken status of the power-up.
     *
     * @param taken the new taken status of the power-up
     */
    public void setTaken(boolean taken) {
        this.taken = taken;
    }

    /**
     * Updates the power-up's position and checks if it should be removed.
     *
     * @return true if the power-up should be removed, false otherwise
     */
    public boolean update() {
        setY(getY() + 2);
        return getY() > 512;
    }
}