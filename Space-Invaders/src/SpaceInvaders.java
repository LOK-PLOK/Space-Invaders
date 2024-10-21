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

    //aliens
    ArrayList<Block>alienArray;
    int alienWidth = tileSize*2;
    int alienHieght = tileSize;
    int alienX = tileSize;
    int alienY = tileSize;

    int alienRows = 2;
    int alienColumns = 3;
    int alienCount = 0;
    int alienVelocityX = 1;

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
        alienArray = new ArrayList<Block>();

        // game timer
        gameLoop = new Timer(1000/60, this);
        createAliens();
        gameLoop.start();
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g){
        //ship
        g.drawImage(ship.image, ship.x, ship.y, ship.width, ship.height,null);

        //aliens
        for(int i = 0; i<alienArray.size();i++){
            Block alien = alienArray.get(i);
            if(alien.alive){
                g.drawImage(alien.image, alien.x, alien.y, alien.width,alien.height,null);
            }
        }
    }

    public void move(){
        //aliens
        for(int i=0; i<alienArray.size();i++){
            Block alien = alienArray.get(i);
            if(alien.alive){
                alien.x += alienVelocityX;

                //if alien touches the borders
                if(alien.x + alien.width >= boardWidth || alien.x <= 0){
                    alienVelocityX *= -1;
                    alien.x += alienVelocityX*2;

                    //move all aliens down by one row
                    for(int j = 0; j<alienArray.size();j++){
                        alienArray.get(j).y += alienHieght;
                    }
                }
            }
        }
    }

    public void createAliens(){
        Random random = new Random();
        for(int r = 0; r < alienRows; r++){
            for(int c = 0;  c <alienColumns; c++){
                int randomImgIndex = random.nextInt(alienImgArray.size());
                Block alien = new Block(
                        alienX+c*alienWidth,
                        alienY+r*alienHieght,
                        alienWidth,
                        alienHieght,
                        alienImgArray.get(randomImgIndex)
                );
                alienArray.add(alien);
            }
        }
        alienCount = alienArray.size();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {}
    @Override
    public void keyPressed(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_LEFT && ship.x - shipVelocityX >= 0){
            ship.x -= shipVelocityX;
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT && ship.x + ship.width + shipVelocityX <= boardWidth) {
            ship.x += shipVelocityX;
        }
    }


}

