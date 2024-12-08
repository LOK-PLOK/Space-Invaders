public class PowerUp extends Block {
    // 1 - extra life
    // 2 - extra damage
    private int type;
    private boolean taken = false;

    public PowerUp(int type, int x, int y) {
        super(x, y, 4, 4, null);
        this.type = type;
    }

    public int getType() { return type; }

    public boolean isTaken() { return taken; }
    public void setTaken(boolean taken) { this.taken = taken; }

    public boolean update() {
        setY(getY() + 2);

        return getY() > 512;
    }
}