import java.awt.Image;

/**
 * Represents a bullet block in the game.
 * This class extends the Block class and adds specific properties for bullets.
 */
public class BulletBlock extends Block {
    /**
     * Constructs a BulletBlock with the specified position, size, and image.
     *
     * @param x the x-coordinate of the bullet block
     * @param y the y-coordinate of the bullet block
     * @param width the width of the bullet block
     * @param height the height of the bullet block
     * @param image the image representing the bullet block
     */
    public BulletBlock(int x, int y, int width, int height, Image image) {
        super(x, y, width, height, image);
    }
}