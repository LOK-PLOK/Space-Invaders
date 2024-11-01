import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class GamePanel extends JPanel implements ActionListener, KeyListener {
    // Board
    int tileSize = 32;
    int rows = 16;
    int columns = 16;
    int boardWidth = tileSize * columns;
    int boardHeight = tileSize * rows;

    // Images
    Image shipImg;
    ArrayList<Image> alienImgArray;

    // Ship
    Block ship;
    int shipWidth = tileSize * 2;
    int shipHeight = tileSize;
    int shipX = tileSize * columns / 2 - tileSize;
    int shipY = boardHeight - tileSize * 2;
    int shipVelocityX = tileSize;

    // Aliens
    ArrayList<Block> alienArray;
    int alienWidth = tileSize * 2;
    int alienHeight = tileSize;
    int alienX = tileSize;
    int alienY = tileSize;
    int alienRows = 2;
    int alienColumns = 3;
    int alienCount = 0;
    int alienVelocityX = 1;

    // Bullets
    ArrayList<Block> bulletArray;
    int bulletWidth = tileSize / 8;
    int bulletHeight = tileSize / 2;
    int bulletVelocityY = -10;

    // Ultimate Bar
    boolean isUltimateActive = false;
    long ultimateStartTime = 0;
    final long duration = 2000;
    final int requiredKills = 10;
    int killCounter = 0;
    Block ultimateLaser;

    Timer gameLoop;
    int score = 0;
    boolean gameOver = false;

    // Level
    int currentLevel = 1;

    // Alien Bullets
    ArrayList<Block> alienBulletArray;
    int alienBulletWidth = tileSize / 8;
    int alienBulletHeight = tileSize / 2;
    int alienBulletVelocityY = 5;

    // Variables to control firing rate
    int alienFireRate = 1;
    int fireCounter = alienFireRate;
    Random random = new Random();

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

        ship = new Block(shipX, shipY, shipWidth, shipHeight, shipImg);
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
        int laserHeight = ship.y;
        int laserX = ship.x + (ship.width / 2) - (laserWidth / 2);
        ultimateLaser = new Block(laserX, 0, laserWidth, laserHeight, null);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    private void draw(Graphics g) {
        // Draw ship
        g.drawImage(ship.image, ship.x, ship.y, ship.width, ship.height, null);

        // Draw aliens
        for (Block alien : alienArray) {
            if (alien.alive) {
                g.drawImage(alien.image, alien.x, alien.y, alien.width, alien.height, null);
            }
        }

        // Draw bullets
        g.setColor(Color.white);
        for (Block bullet : bulletArray) {
            if (!bullet.used) {
                g.fillRect(bullet.x, bullet.y, bullet.width, bullet.height);
            }
        }

        // Draw alien bullets
        g.setColor(Color.red);
        for (Block alienBullet : alienBulletArray) {
            if (!alienBullet.used) {
                g.fillRect(alienBullet.x, alienBullet.y, alienBullet.width, alienBullet.height);
            }
        }

        // Draw ultimate laser
        if (isUltimateActive && ultimateLaser != null) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setColor(Color.WHITE);
            g2.fillRect(ultimateLaser.x, ultimateLaser.y, ultimateLaser.width, ultimateLaser.height);
            g2.setColor(Color.YELLOW);
            g2.setStroke(new BasicStroke(8));
            g2.drawRect(ultimateLaser.x, ultimateLaser.y, ultimateLaser.width, ultimateLaser.height);
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
        for (Block alien : alienArray) {
            if (alien.alive) {
                alien.x += alienVelocityX;
                if (alien.x + alien.width >= boardWidth || alien.x <= 0) {
                    alienVelocityX *= -1;
                    alien.x += alienVelocityX * 2;
                    for (Block a : alienArray) {
                        a.y += alienHeight;
                    }
                }
                if (detectCollision(alien, ship)) {
                    gameOver = true;
                }
            }
        }

        // Ultimate laser movement and collision detection
        if (isUltimateActive) {
            ultimateLaser.x = ship.x + (ship.width / 2) - (ultimateLaser.width / 2);
            for (Block alien : alienArray) {
                if (alien.alive && detectCollision(ultimateLaser, alien)) {
                    alien.alive = false;
                    alienCount--;
                    score += 100;
                }
            }
            if (System.currentTimeMillis() - ultimateStartTime >= duration) {
                isUltimateActive = false;
                ultimateLaser = null;
            }
        }

        // Bullets movement and collision detection
        for (Block bullet : bulletArray) {
            bullet.y += bulletVelocityY;
            for (Block alien : alienArray) {
                if (!bullet.used && alien.alive && detectCollision(bullet, alien)) {
                    bullet.used = true;
                    alien.alive = false;
                    alienCount--;
                    score += 100;
                    killCounter++;
                }
            }
        }

        // Clear used bullets
        bulletArray.removeIf(bullet -> bullet.used || bullet.y < 0);

        // Next level
        if (alienCount == 0) {
            score += alienColumns * alienRows * 100;
            currentLevel++;
            alienColumns = Math.min(alienColumns + 1, columns / 2 - 2);
            alienRows = Math.min(alienRows + 1, rows - 6);
            alienArray.clear();
            bulletArray.clear();
            createAliens();
        }

        // Alien shooting
        handleAlienShooting();

        // Alien bullets movement and collision detection
        for (Block alienBullet : alienBulletArray) {
            alienBullet.y += alienBulletVelocityY;
            if (!alienBullet.used && detectCollision(alienBullet, ship)) {
                gameOver = true;
            }
        }

        // Clear used alien bullets
        alienBulletArray.removeIf(alienBullet -> alienBullet.y > boardHeight);
    }

    private void handleAlienShooting() {
        if (fireCounter <= 0) {
            Block shooter = selectRandomAlien();
            if (shooter != null) {
                fireAlienBullet(shooter);
            }
            fireCounter = alienFireRate;
        } else {
            fireCounter--;
        }
    }

    private Block selectRandomAlien() {
        ArrayList<Block> aliveAliens = new ArrayList<>();
        for (Block alien : alienArray) {
            if (alien.alive) {
                aliveAliens.add(alien);
            }
        }
        if (!aliveAliens.isEmpty()) {
            return aliveAliens.get(random.nextInt(aliveAliens.size()));
        }
        return null;
    }

    private void fireAlienBullet(Block alien) {
        Block alienBullet = new Block(
            alien.x + alien.width / 2 - alienBulletWidth / 2,
            alien.y + alien.height,
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

    private boolean detectCollision(Block a, Block b) {
        return a.x < b.x + b.width &&
               a.x + a.width > b.x &&
               a.y < b.y + b.height &&
               a.y + a.height > b.y;
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
            if (code == KeyEvent.VK_LEFT && ship.x - shipVelocityX >= 0) {
                ship.x -= shipVelocityX;
            } else if (code == KeyEvent.VK_RIGHT && ship.x + ship.width + shipVelocityX <= boardWidth) {
                ship.x += shipVelocityX;
            } else if (code == KeyEvent.VK_SPACE && !isUltimateActive) {
                Block bullet = new Block(ship.x + shipWidth * 15 / 32, ship.y, bulletWidth, bulletHeight, null);
                bulletArray.add(bullet);
            }
        }
    }

    private void resetGame() {
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