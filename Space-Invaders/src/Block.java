import java.awt.Image;

/**
 * Represents a generic block in the game.
 * This class serves as a base class for various game elements like ships, aliens, bullets, etc.
 */
public class Block {
    private int x, y, width, height;
    private Image image;
    private boolean alive = true; // for aliens
    private boolean used = false; // for bullets

    /**
     * Constructs a Block with the specified position, size, and image.
     *
     * @param x the x-coordinate of the block
     * @param y the y-coordinate of the block
     * @param width the width of the block
     * @param height the height of the block
     * @param image the image representing the block
     */
    public Block(int x, int y, int width, int height, Image image) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.image = image;
    }

    // Getters and setters for encapsulation

    /**
     * Gets the x-coordinate of the block.
     *
     * @return the x-coordinate of the block
     */
    public int getX() { return x; }

    /**
     * Sets the x-coordinate of the block.
     *
     * @param x the new x-coordinate of the block
     */
    public void setX(int x) { this.x = x; }

    /**
     * Gets the y-coordinate of the block.
     *
     * @return the y-coordinate of the block
     */
    public int getY() { return y; }

    /**
     * Sets the y-coordinate of the block.
     *
     * @param y the new y-coordinate of the block
     */
    public void setY(int y) { this.y = y; }

    /**
     * Gets the width of the block.
     *
     * @return the width of the block
     */
    public int getWidth() { return width; }

    /**
     * Sets the width of the block.
     *
     * @param width the new width of the block
     */
    public void setWidth(int width) { this.width = width; }

    /**
     * Gets the height of the block.
     *
     * @return the height of the block
     */
    public int getHeight() { return height; }

    /**
     * Sets the height of the block.
     *
     * @param height the new height of the block
     */
    public void setHeight(int height) { this.height = height; }

    /**
     * Gets the image representing the block.
     *
     * @return the image representing the block
     */
    public Image getImage() { return image; }

    /**
     * Sets the image representing the block.
     *
     * @param image the new image representing the block
     */
    public void setImage(Image image) { this.image = image; }

    /**
     * Checks if the block is alive.
     *
     * @return true if the block is alive, false otherwise
     */
    public boolean isAlive() { return alive; }

    /**
     * Sets the alive status of the block.
     *
     * @param alive the new alive status of the block
     */
    public void setAlive(boolean alive) { this.alive = alive; }

    /**
     * Checks if the block is used.
     *
     * @return true if the block is used, false otherwise
     */
    public boolean isUsed() { return used; }

    /**
     * Sets the used status of the block.
     *
     * @param used the new used status of the block
     */
    public void setUsed(boolean used) { this.used = used; }
}