import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;


public class SpaceInvaders extends JPanel implements  ActionListener, KeyListener {


    class Block{
       int x,y,width,height;
       Image image;
       boolean alive = true; // for aliens
       boolean used = false; // for bullets

        Block(int x,int y,int width, int height, Image image){
            this.x = x;
            this .y = y;
            this.width = width;
            this.height = height;
            this.image = image;
        }

    }

    // Board
    int tileSize  = 32;
    int rows = 16;
    int columns = 16;
    int boardWidth = tileSize * columns; // 32* 16 = 512px
    int boardHeight = tileSize * rows; // 32* 16 = 512px

    Image shipImg;
    Image alienImg;
    Image alienCyan;
    Image alienMagenta;
    Image alienYellow;
    ArrayList<Image> alienImgArray;

    //ship;
    int shipWidth = tileSize*2; // 64px
    int shipHeight = tileSize; //32px
    int shipX = tileSize*columns/2-tileSize;
    int shipY = boardHeight - tileSize*2;
    int shipVelocityX = tileSize;

    Block ship;
    Timer gameLoop;

    SpaceInvaders(){
        setPreferredSize(new Dimension(boardWidth,boardHeight));
        setBackground(Color.black);
        setFocusable(true);
        addKeyListener(this);

        // load Images
        shipImg = new ImageIcon(getClass().getResource("./ship.png")).getImage();
        alienImg = new ImageIcon(getClass().getResource("./alien.png")).getImage();
        alienCyan = new ImageIcon(getClass().getResource("./alien-cyan.png")).getImage();
        alienMagenta = new ImageIcon(getClass().getResource("./alien-magenta.png")).getImage();
        alienYellow = new ImageIcon(getClass().getResource("./alien-yellow.png")).getImage();

        alienImgArray = new ArrayList<Image>();
        alienImgArray.add(alienImg);
        alienImgArray.add(alienCyan);
        alienImgArray.add(alienMagenta);
        alienImgArray.add(alienYellow);

        ship = new Block(shipX,shipY,shipWidth,shipHeight,shipImg);

        // game timer
        gameLoop = new Timer(1000/60, this);
        gameLoop.start();
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g){
        g.drawImage(ship.image, ship.x, ship.y, ship.width, ship.height,null);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {}
    @Override
    public void keyPressed(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_LEFT){
            ship.x -= shipVelocityX;
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            ship.x += shipVelocityX;
        }
    }


}

