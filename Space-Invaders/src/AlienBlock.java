import java.awt.Image;

/**
 * Represents an alien block in the game.
 * This class extends the Block class and adds specific properties for aliens.
 */
public class AlienBlock extends Block {
    /**
     * Constructs an AlienBlock with the specified position, size, and image.
     *
     * @param x the x-coordinate of the alien block
     * @param y the y-coordinate of the alien block
     * @param width the width of the alien block
     * @param height the height of the alien block
     * @param image the image representing the alien block
     */
    public AlienBlock(int x, int y, int width, int height, Image image) {
        super(x, y, width, height, image);
    }
}