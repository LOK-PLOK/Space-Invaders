import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class SpaceInvaders extends JPanel implements ActionListener, KeyListener {
    class Block {
        int x, y, width, height;
        Image image;
        boolean alive = true; // for aliens
        boolean used = false; // for bullets

        Block(int x, int y, int width, int height, Image image) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.image = image;
        }
    }

    // Board
    int tileSize = 32;
    int rows = 16;
    int columns = 16;
    int boardWidth = tileSize * columns; // 32* 16 = 512px
    int boardHeight = tileSize * rows; // 32* 16 = 512px

    // Images
    Image shipImg;
    Image alienImg;
    Image alienCyan;
    Image alienMagenta;
    Image alienYellow;
    ArrayList<Image> alienImgArray;

    // Ship
    int shipWidth = tileSize * 2; // 64px
    int shipHeight = tileSize; // 32px
    int shipX = tileSize * columns / 2 - tileSize;
    int shipY = boardHeight - tileSize * 2;
    int shipVelocityX = tileSize;
    Block ship;

    // Aliens
    ArrayList<Block> alienArray;
    int alienWidth = tileSize * 2;
    int alienHeight = tileSize;
    int alienX = tileSize;
    int alienY = tileSize;
    int alienRows = 2;
    int alienColumns = 3;
    int alienCount = 0; // number of aliens to defeat
    int alienVelocityX = 1; // alien moving speed

    // Bullets
    ArrayList<Block> bulletArray;
    int bulletWidth = tileSize / 8;
    int bulletHeight = tileSize / 2;
    int bulletVelocityY = -10; // bullet moving speed

    // Ultimate Bar
    boolean isUltimateActive = false;
    long ultimateStartTime = 0;
    final long duration = 2000; // 2 seconds in milliseconds
    final int requiredKills = 10;
    int killCounter = 0;
    Block ultimateLaser;

    Timer gameLoop;
    int score = 0;
    boolean gameOver = false;

    //level
    int currentLevel = 1;

    //Alien Bullets
    ArrayList<Block> alienBulletArray;
    int alienBulletWidth = tileSize/8;
    int alienBulletHeight = tileSize/2;
    int alienBulletVelocityY = 5; // Alien bullets move downward

      // Variables to control firing rate
    int alienFireRate = 180; // Adjust this for slower firing, 180 frames = 3 seconds
    int fireCounter = alienFireRate;
    Random random = new Random(); //Random fire logic

    SpaceInvaders() {
        setPreferredSize(new Dimension(boardWidth, boardHeight));
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

        ship = new Block(shipX, shipY, shipWidth, shipHeight, shipImg);
        alienArray = new ArrayList<Block>();
        bulletArray = new ArrayList<Block>();
        alienBulletArray = new ArrayList<>();

        // game timer
        gameLoop = new Timer(1000 / 60, this);
        createAliens();
        gameLoop.start();

    }

    private void createUltimateLaser() {
        int laserWidth = tileSize * 2;
        int laserHeight = ship.y;
        int laserX = ship.x + (ship.width / 2) - (laserWidth / 2); // Center of ship logic
        ultimateLaser = new Block(laserX, 0, laserWidth, laserHeight, null);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        //ship
        g.drawImage(ship.image, ship.x, ship.y, ship.width, ship.height, null);

        //aliens
        for (int i = 0; i < alienArray.size(); i++) {
            Block alien = alienArray.get(i);
            if (alien.alive) {
                g.drawImage(alien.image, alien.x, alien.y, alien.width, alien.height, null);
            }
        }

        //bullets
        g.setColor(Color.white);
        for (int i = 0; i < bulletArray.size(); i++) {
            Block bullet = bulletArray.get(i);
            if (!bullet.used) {
                //g.drawRect(bullet.x,bullet.y,bullet.width,bullet.height); // hollow bullets
                g.fillRect(bullet.x, bullet.y, bullet.width, bullet.height); //solid white
            }
        }


        // Draw alien bullets
        g.setColor(Color.red);
        for (int i = 0; i < alienBulletArray.size(); i++) {
            Block alienBullet = alienBulletArray.get(i);
            if (!alienBullet.used) {
                g.fillRect(alienBullet.x, alienBullet.y, alienBullet.width, alienBullet.height);
            }
        }

        //ultimate laser
        if (isUltimateActive && ultimateLaser != null) {
            Graphics2D g2 = (Graphics2D) g;

            g2.setColor(Color.WHITE);
            g2.fillRect(ultimateLaser.x, ultimateLaser.y, ultimateLaser.width, ultimateLaser.height);
            
            g2.setColor(Color.YELLOW);
            g2.setStroke(new BasicStroke(8));
            g2.drawRect(ultimateLaser.x, ultimateLaser.y, ultimateLaser.width, ultimateLaser.height);
        }
        
        g.setColor(Color.white);
        if (gameOver) {
            g.setFont(new Font("Arial", Font.BOLD, 50));
            String gameOverText = "GAME OVER";
            FontMetrics metrics = g.getFontMetrics();
            int gameOverX = (boardWidth - metrics.stringWidth(gameOverText)) / 2;
            int gameOverY = boardHeight / 2;
            g.drawString(gameOverText, gameOverX, gameOverY);

            g.setFont(new Font("Arial", Font.BOLD, 30));
            String scoreText = "Final Score: " + score;
            metrics = g.getFontMetrics();
            int scoreX = (boardWidth - metrics.stringWidth(scoreText)) / 2;
            g.drawString(scoreText, scoreX, gameOverY + 50);

            // play again instructions
            g.setFont(new Font("Arial", Font.PLAIN, 20));
            String restartText = "Press SPACE to play again";
            metrics = g.getFontMetrics();
            int restartX = (boardWidth - metrics.stringWidth(restartText)) / 2;
            g.drawString(restartText, restartX, gameOverY + 100);
        } else {
            g.setFont(new Font("Arial", Font.PLAIN, 15));
            g.drawString("Level: " + String.valueOf(currentLevel), 10, 20);
            g.drawString("Score: " + String.valueOf(score), 10, 35);
            if(killCounter < 10){
                g.drawString("Ultimate in " + Math.max(0, requiredKills - killCounter), 10, 50);
            } else {
                g.drawString("Ultimate Ready (Press F)", 10, 50);
            }
        }
    }

    public void move() {
        //aliens
        for (int i = 0; i < alienArray.size(); i++) {
            Block alien = alienArray.get(i);
            if (alien.alive) {
                alien.x += alienVelocityX;

                //if alien touches the borders
                if (alien.x + alien.width >= boardWidth || alien.x <= 0) {
                    alienVelocityX *= -1;
                    alien.x += alienVelocityX * 2;

                    //move all aliens down by one row
                    for (int j = 0; j < alienArray.size(); j++) {
                        alienArray.get(j).y += alienHeight;
                    }
                }

                //check if any alien hits the ship
                if (detectCollision(alien, ship)) {
                    gameOver = true;
                }
            }
        }

        //update ultimate laser position and check collisions if active
        if (isUltimateActive) {
            //update laser x position to follow ship
            ultimateLaser.x = ship.x + (ship.width / 2) - (ultimateLaser.width / 2);
            
            //check for collisions with aliens
            for (Block alien : alienArray) {
                if (alien.alive && detectCollision(ultimateLaser, alien)) {
                    alien.alive = false;
                    alienCount--;
                    score += 100;
                }
            }
            
            //check if ultimate duration is over
            if (System.currentTimeMillis() - ultimateStartTime >= duration) {
                isUltimateActive = false;
                ultimateLaser = null;
            }
        }

        //bullets
        for (int i = 0; i < bulletArray.size(); i++) {
            Block bullet = bulletArray.get(i);
            bullet.y += bulletVelocityY;

            //bullet collision with aliens
            for (int j = 0; j < alienArray.size(); j++) {
                Block alien = alienArray.get(j);
                if (!bullet.used && alien.alive && detectCollision(bullet, alien)) {
                    bullet.used = true;
                    alien.alive = false;
                    alienCount--;
                    score += 100;
                    killCounter++;
                }
            }
        }

        // clear bullets
        while (bulletArray.size() > 0 && (bulletArray.get(0).used || bulletArray.get(0).y < 0)) {
            bulletArray.remove(0);
        }

        // next level
        if (alienCount == 0) {
            score += alienColumns * alienRows * 100;
            currentLevel++;
            alienColumns = Math.min(alienColumns + 1, columns / 2 - 2);
            alienRows = Math.min(alienRows + 1, rows - 6);
            alienArray.clear();
            bulletArray.clear();
//          alientVelocityX = 1; optional
            createAliens();
        }

        // Check if aliens should fire a bullet
        handleAlienShooting();

        // Move alien bullets
        for (int i = 0; i < alienBulletArray.size(); i++) {
            Block alienBullet = alienBulletArray.get(i);
            alienBullet.y += alienBulletVelocityY;

            // Check collision with the player's ship
            if (!alienBullet.used && detectCollision(alienBullet, ship)) {
                gameOver = true;
            }
        }

        // Remove alien bullets that go off-screen
        while (alienBulletArray.size() > 0 && (alienBulletArray.get(0).y > boardHeight)) {
            alienBulletArray.remove(0);
        }
        

    }

    private void handleAlienShooting() {
        // Shoot only if fireCounter reaches 0
        if (fireCounter <= 0) {
            // Select a random alive alien to shoot
            Block shooter = selectRandomAlien();
            if (shooter != null) {
                fireAlienBullet(shooter);
            }
            fireCounter = alienFireRate; // Reset the fire counter after shooting
        } else {
            fireCounter--; // Decrement the counter each frame
        }
    }

    private Block selectRandomAlien() {
        ArrayList<Block> aliveAliens = new ArrayList<>();
        for (Block alien : alienArray) {
            if (alien.alive) {
                aliveAliens.add(alien);
            }
        }
        if (aliveAliens.size() > 0) {
            return aliveAliens.get(random.nextInt(aliveAliens.size()));
        }
        return null;
    }

    private void fireAlienBullet(Block alien) {
        // Fire a bullet from the alien's position
        Block alienBullet = new Block(
            alien.x + alien.width / 2 - alienBulletWidth / 2, 
            alien.y + alien.height, 
            alienBulletWidth, 
            alienBulletHeight, 
            null
        );
        alienBulletArray.add(alienBullet);
    }

    public void createAliens() {
        Random random = new Random();
        for (int r = 0; r < alienRows; r++) {
            for (int c = 0; c < alienColumns; c++) {
                int randomImgIndex = random.nextInt(alienImgArray.size());
                Block alien = new Block(
                    alienX + c * alienWidth,
                    alienY + r * alienHeight,
                    alienWidth,
                    alienHeight,
                    alienImgArray.get(randomImgIndex)
                );
                alienArray.add(alien);
            }
        }
        alienCount = alienArray.size();
    }

    public boolean detectCollision(Block a, Block b) {
        return a.x < b.x + b.width &&   // a's top left corner doesn't reach b's top right corner
                a.x + a.width > b.x &&    // a's top right corner passes b't top left corner
                a.y < b.y + b.height &&   // a's top left corner doesn't reach b's bottom left corner
                a.y + a.height > b.y;       // a's bottom left corner passes b's top left corner
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
        if (gameOver) {
            gameLoop.stop();
            alienBulletArray.clear();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();

        if (code == KeyEvent.VK_F && !isUltimateActive && killCounter >= requiredKills) {
            isUltimateActive = true;
            ultimateStartTime = System.currentTimeMillis();
            killCounter = 0; // Reset kill counter
            createUltimateLaser();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (gameOver) {
            if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                ship.x = shipX;
                alienArray.clear();
                bulletArray.clear();
                alienBulletArray.clear();
                score = 0;
                killCounter = 0;
                currentLevel = 1;
                isUltimateActive = false;
                alienVelocityX = 1;
                alienColumns = 3;
                alienRows = 2;
                gameOver = false;
                createAliens();
                gameLoop.start();
            }
        }
        else if (e.getKeyCode() == KeyEvent.VK_LEFT && ship.x - shipVelocityX >= 0) {
            ship.x -= shipVelocityX;
        }
        else if (e.getKeyCode() == KeyEvent.VK_RIGHT && ship.x + ship.width + shipVelocityX <= boardWidth) {
            ship.x += shipVelocityX;
        }
        else if (e.getKeyCode() == KeyEvent.VK_SPACE && !isUltimateActive) {
            Block bullet = new Block(ship.x + shipWidth * 15/32, ship.y, bulletWidth, bulletHeight, null);
            bulletArray.add(bullet);
        }
    }
}