import java.awt.Image;

/**
 * Represents the player's ship in the game.
 * This class extends the Block class and adds specific properties for the ship.
 */
public class ShipBlock extends Block {
    /**
     * Constructs a ShipBlock with the specified position, size, and image.
     *
     * @param x the x-coordinate of the ship block
     * @param y the y-coordinate of the ship block
     * @param width the width of the ship block
     * @param height the height of the ship block
     * @param image the image representing the ship block
     */
    public ShipBlock(int x, int y, int width, int height, Image image) {
        super(x, y, width, height, image);
    }
}