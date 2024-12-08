import java.awt.Image;

/**
 * Represents the boss enemy in the game.
 * This class extends the Block class and adds specific properties for the boss.
 */
public class Boss extends Block {
    private double health;

    /**
     * Constructs a Boss with the specified position, size, and image.
     *
     * @param x the x-coordinate of the boss
     * @param y the y-coordinate of the boss
     * @param width the width of the boss
     * @param height the height of the boss
     * @param image the image representing the boss
     */
    public Boss(int x, int y, int width, int height, Image image) {
        super(x, y, width, height, image);
        this.health = 100;
    }

    /**
     * Gets the health of the boss.
     *
     * @return the health of the boss
     */
    public double getHealth() { return health; }

    /**
     * Sets the health of the boss.
     *
     * @param health the new health of the boss
     */
    public void setHealth(double health) { this.health = health; }
}