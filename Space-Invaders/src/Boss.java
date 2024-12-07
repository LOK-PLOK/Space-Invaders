import java.awt.Image;

public class Boss extends Block {
    private double health;

    public Boss(int x, int y, int width, int height, Image image) {
        super(x, y, width, height, image);
        this.health = 100;
    }

    public double getHealth() { return health; }
    public void setHealth(double health) { this.health = health; }
}
