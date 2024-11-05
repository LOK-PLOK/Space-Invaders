import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class GamePanel extends JPanel implements ActionListener, KeyListener {
    // Board
    private int tileSize = 32;
    private int rows = 16;
    private int columns = 16;
    private int boardWidth = tileSize * columns;
    private int boardHeight = tileSize * rows;

    // Images
    private Image shipImg;
    private ArrayList<Image> alienImgArray;

    // Ship
    private ShipBlock ship;
    private int shipWidth = tileSize * 2;
    private int shipHeight = tileSize;
    private int shipX = tileSize * columns / 2 - tileSize;
    private int shipY = boardHeight - tileSize * 2;
    private int shipVelocityX = tileSize / 2;

    // Aliens
    private ArrayList<AlienBlock> alienArray;
    private int alienWidth = tileSize * 2;
    private int alienHeight = tileSize;
    private int alienX = tileSize;
    private int alienY = tileSize;
    private int alienRows = 2;
    private int alienColumns = 3;
    private int alienCount = 0;
    private int alienVelocityX = 1;

    // Bullets
    private ArrayList<BulletBlock> bulletArray;
    private int bulletWidth = tileSize / 8;
    private int bulletHeight = tileSize / 2;
    private int bulletVelocityY = -10;

    // Ultimate Bar
    private boolean isUltimateActive = false;
    private long ultimateStartTime = 0;
    private final long duration = 2000;
    private final int requiredKills = 10;
    private int killCounter = 0;
    private Block ultimateLaser;

    private Timer gameLoop;
    private int score = 0;
    private boolean gameOver = false;

    // Level
    private int currentLevel = 1;

    // Alien Bullets
    private ArrayList<BulletBlock> alienBulletArray;
    private int alienBulletWidth = tileSize / 8;
    private int alienBulletHeight = tileSize / 2;
    private int alienBulletVelocityY = 5;

    // Variables to control firing rate
    private int alienFireRate = 180;
    private int fireCounter = alienFireRate;
    private Random random = new Random();

    public GamePanel() {
        setPreferredSize(new Dimension(boardWidth, boardHeight));
        setBackground(Color.black);
        setFocusable(true);
        addKeyListener(this);

        // Load Images
        shipImg = new ImageIcon(getClass().getResource("./Images/ship.png")).getImage();
        alienImgArray = new ArrayList<>();
        alienImgArray.add(new ImageIcon(getClass().getResource("./Images/alien.png")).getImage());
        alienImgArray.add(new ImageIcon(getClass().getResource("./Images/alien-cyan.png")).getImage());
        alienImgArray.add(new ImageIcon(getClass().getResource("./Images/alien-magenta.png")).getImage());
        alienImgArray.add(new ImageIcon(getClass().getResource("./Images/alien-yellow.png")).getImage());

        ship = new ShipBlock(shipX, shipY, shipWidth, shipHeight, shipImg);
        alienArray = new ArrayList<>();
        bulletArray = new ArrayList<>();
        alienBulletArray = new ArrayList<>();

        // Game timer
        gameLoop = new Timer(1000 / 60, this);
        createAliens();
        gameLoop.start();
    }

    private void createUltimateLaser() {
        int laserWidth = tileSize * 2;
        int laserHeight = ship.getY();
        int laserX = ship.getX() + (ship.getWidth() / 2) - (laserWidth / 2);
        ultimateLaser = new Block(laserX, 0, laserWidth, laserHeight, null);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    private void draw(Graphics g) {
        // Draw ship
        g.drawImage(ship.getImage(), ship.getX(), ship.getY(), ship.getWidth(), ship.getHeight(), null);

        // Draw aliens
        for (AlienBlock alien : alienArray) {
            if (alien.isAlive()) {
                g.drawImage(alien.getImage(), alien.getX(), alien.getY(), alien.getWidth(), alien.getHeight(), null);
            }
        }

        // Draw bullets
        g.setColor(Color.white);
        for (BulletBlock bullet : bulletArray) {
            if (!bullet.isUsed()) {
                g.fillRect(bullet.getX(), bullet.getY(), bullet.getWidth(), bullet.getHeight());
            }
        }

        // Draw alien bullets
        g.setColor(Color.red);
        for (BulletBlock alienBullet : alienBulletArray) {
            if (!alienBullet.isUsed()) {
                g.fillRect(alienBullet.getX(), alienBullet.getY(), alienBullet.getWidth(), alienBullet.getHeight());
            }
        }

        // Draw ultimate laser
        if (isUltimateActive && ultimateLaser != null) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setColor(Color.WHITE);
            g2.fillRect(ultimateLaser.getX(), ultimateLaser.getY(), ultimateLaser.getWidth(), ultimateLaser.getHeight());
            g2.setColor(Color.YELLOW);
            g2.setStroke(new BasicStroke(8));
            g2.drawRect(ultimateLaser.getX(), ultimateLaser.getY(), ultimateLaser.getWidth(), ultimateLaser.getHeight());
        }

        // Draw game over text
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

            g.setFont(new Font("Arial", Font.PLAIN, 20));
            String restartText = "Press SPACE to play again";
            metrics = g.getFontMetrics();
            int restartX = (boardWidth - metrics.stringWidth(restartText)) / 2;
            g.drawString(restartText, restartX, gameOverY + 100);
        } else {
            g.setFont(new Font("Arial", Font.PLAIN, 15));
            g.drawString("Level: " + currentLevel, 10, 20);
            g.drawString("Score: " + score, 10, 35);
            if (killCounter < 10) {
                g.drawString("Ultimate in " + Math.max(0, requiredKills - killCounter), 10, 50);
            } else {
                g.drawString("Ultimate Ready (Press F)", 10, 50);
            }
        }
    }

    private void move() {
        // Aliens movement and collision detection
        for (AlienBlock alien : alienArray) {
            if (alien.isAlive()) {
                alien.setX(alien.getX() + alienVelocityX);
                if (alien.getX() + alien.getWidth() >= boardWidth || alien.getX() <= 0) {
                    alienVelocityX *= -1;
                    alien.setX(alien.getX() + alienVelocityX * 2);
                    for (AlienBlock a : alienArray) {
                        a.setY(a.getY() + alienHeight);
                    }
                }
                if (detectCollision(alien, ship)) {
                    gameOver = true;
                }
            }
        }

        // Ultimate laser movement and collision detection
        if (isUltimateActive) {
            ultimateLaser.setX(ship.getX() + (ship.getWidth() / 2) - (ultimateLaser.getWidth() / 2));
            
            // Collision detection between ultimate laser and aliens
            for (AlienBlock alien : alienArray) {
                if (alien.isAlive() && detectCollision(ultimateLaser, alien)) {
                    alien.setAlive(false);
                    alienCount--;
                    score += 100;
                }
            }
            
            // Collision detection between ultimate laser and alien bullets
            for (BulletBlock alienBullet : alienBulletArray) {
                if (!alienBullet.isUsed() && detectCollision(ultimateLaser, alienBullet)) {
                    alienBullet.setUsed(true);
                }
            }
            
            if (System.currentTimeMillis() - ultimateStartTime >= duration) {
                isUltimateActive = false;
                ultimateLaser = null;
            }
        }

        // Bullets movement and collision detection
        for (BulletBlock bullet : bulletArray) {
            bullet.setY(bullet.getY() + bulletVelocityY);
            for (AlienBlock alien : alienArray) {
                if (!bullet.isUsed() && alien.isAlive() && detectCollision(bullet, alien)) {
                    bullet.setUsed(true);
                    alien.setAlive(false);
                    alienCount--;
                    score += 100;
                    killCounter++;
                }
            }
        }

        // Ship bullet and alien bullet collision detection
        for (BulletBlock shipBullet : bulletArray) {
            for (BulletBlock alienBullet : alienBulletArray) {
                if (!shipBullet.isUsed() && !alienBullet.isUsed() && detectCollision(shipBullet, alienBullet)) {
                    shipBullet.setUsed(true);
                    alienBullet.setUsed(true);
                }
            }
        }

        // Clear used bullets
        bulletArray.removeIf(bullet -> bullet.isUsed() || bullet.getY() < 0);

        // Next level
        if (alienCount == 0) {
            score += alienColumns * alienRows * 100;
            currentLevel++;
            
            // Reset alien array when reaching a multiple of 10 + 1 level
            if ((currentLevel - 1) % 10 == 0) {
                alienColumns = 3;
                alienRows = 2;
            } else {
                alienColumns = Math.min(alienColumns + 1, columns / 2 - 2);
                alienRows = Math.min(alienRows + 1, rows - 6);
            }
            
            alienArray.clear();
            bulletArray.clear();
            createAliens();

            // Adjust alien fire rate based on level
            if (currentLevel > 1 && (currentLevel - 1) % 10 == 0 && alienFireRate > 60) {
                alienFireRate -= 60;
            } else if(alienFireRate <= 60 && alienFireRate > 10) {
                // Max firerate of alien is 10
                alienFireRate -= 10;
            }
        }

        // Alien shooting
        handleAlienShooting();

        // Alien bullets movement and collision detection
        for (BulletBlock alienBullet : alienBulletArray) {
            alienBullet.setY(alienBullet.getY() + alienBulletVelocityY);
            if (!alienBullet.isUsed() && detectCollision(alienBullet, ship)) {
                gameOver = true;
            }
        }

        // Clear used alien bullets
        alienBulletArray.removeIf(alienBullet -> alienBullet.getY() > boardHeight);
    }

    private void handleAlienShooting() {
        if (fireCounter <= 0) {
            AlienBlock shooter = selectRandomAlien();
            if (shooter != null) {
                fireAlienBullet(shooter);
            }
            fireCounter = alienFireRate;
        } else {
            fireCounter--;
        }
    }

    private AlienBlock selectRandomAlien() {
        ArrayList<AlienBlock> aliveAliens = new ArrayList<>();
        for (AlienBlock alien : alienArray) {
            if (alien.isAlive()) {
                aliveAliens.add(alien);
            }
        }
        if (!aliveAliens.isEmpty()) {
            return aliveAliens.get(random.nextInt(aliveAliens.size()));
        }
        return null;
    }

    private void fireAlienBullet(AlienBlock alien) {
        BulletBlock alienBullet = new BulletBlock(
            alien.getX() + alien.getWidth() / 2 - alienBulletWidth / 2,
            alien.getY() + alien.getHeight(),
            alienBulletWidth,
            alienBulletHeight,
            null
        );
        alienBulletArray.add(alienBullet);
    }

    private void createAliens() {
        Random random = new Random();
        for (int r = 0; r < alienRows; r++) {
            for (int c = 0; c < alienColumns; c++) {
                int randomImgIndex = random.nextInt(alienImgArray.size());
                AlienBlock alien = new AlienBlock(
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

    private boolean detectCollision(Block a, Block b) {
        return a.getX() < b.getX() + b.getWidth() &&
               a.getX() + a.getWidth() > b.getX() &&
               a.getY() < b.getY() + b.getHeight() &&
               a.getY() + a.getHeight() > b.getY();
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
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        if (code == KeyEvent.VK_F && !isUltimateActive && killCounter >= requiredKills) {
            isUltimateActive = true;
            ultimateStartTime = System.currentTimeMillis();
            killCounter = 0;
            createUltimateLaser();
        } else if (code == KeyEvent.VK_LEFT && ship.getX() - shipVelocityX >= 0) {
            ship.setX(ship.getX() - shipVelocityX);
        } else if (code == KeyEvent.VK_RIGHT && ship.getX() + ship.getWidth() + shipVelocityX <= boardWidth) {
            ship.setX(ship.getX() + shipVelocityX);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();
        if (gameOver) {
            if (code == KeyEvent.VK_SPACE) {
                resetGame();
            }
        } else {
            if (code == KeyEvent.VK_SPACE && !isUltimateActive) {
                BulletBlock bullet = new BulletBlock(ship.getX() + shipWidth * 15 / 32, ship.getY(), bulletWidth, bulletHeight, null);
                bulletArray.add(bullet);
            }
        }
    }

    private void resetGame() {
        ship.setX(shipX);
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