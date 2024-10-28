import java.awt.Image;

public class Block {
    int x, y, width, height;
    Image image;
    boolean alive = true; // for aliens
    boolean used = false; // for bullets

    public Block(int x, int y, int width, int height, Image image) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.image = image;
    }
}