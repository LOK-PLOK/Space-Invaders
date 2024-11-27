import java.awt.Image;

public class Boss extends Block {
    private int health;

    public Boss(int x, int y, int width, int height, Image image) {
        super(x, y, width, height, image);
        this.health = 100;
    }

    public int getHealth() { return health; }
    public void setHealth(int health) { this.health = health; }
}
