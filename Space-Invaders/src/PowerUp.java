public class PowerUp {
    private double x;
    private double y;
    private int r;
    private boolean taken = false;
    
    // 1 - extra life
    // 2 - extra damage
    private int type;

    public PowerUp(int type, double x, double y) {
        this.type = type;
        this.x = x;
        this.y = y;
        r = 4;
    }

    public double getx(){ return x; }
    public double gety(){ return y; }
    public double getr(){ return r; }

    public int getType(){ return type; }

    public boolean isTaken(){ return taken; }
    public void setTaken(boolean taken) { this.taken = taken; }

    public boolean update(){
        y += 2;

        return y > 512;
    }
}