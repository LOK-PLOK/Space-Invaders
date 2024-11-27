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
    private int lives = 3;

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

    // PowerUps
    private ArrayList<PowerUp> powerUps;

    // Alien Bullets
    private ArrayList<BulletBlock> alienBulletArray;
    private int alienBulletWidth = tileSize / 8;
    private int alienBulletHeight = tileSize / 2;
    private int alienBulletVelocityY = 5;

    // Variables to control firing rate
    private int alienFireRate = 180;
    private int fireCounter = alienFireRate;
    private Random random = new Random();

    // Pause
    private boolean isPaused = false;
    
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
        powerUps = new ArrayList<>();

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

        // Draw powerups
        for(PowerUp powerUp : powerUps){
            if(!powerUp.isTaken()){
                Graphics2D g2 = (Graphics2D) g;
                Color powerUpColor = (powerUp.getType() == 1) ? Color.PINK : Color.YELLOW;
                g2.setColor(powerUpColor);
                g2.fillRect((int) (powerUp.getx() - powerUp.getr()), 
                            (int) (powerUp.gety() - powerUp.getr()), 
                            (int) (2 * powerUp.getr()), 
                            (int) (2 * powerUp.getr()));

                g2.setColor(powerUpColor.darker());
                g2.setStroke(new BasicStroke(2));

                g2.drawRect((int) (powerUp.getx() - powerUp.getr()), 
                            (int) (powerUp.gety() - powerUp.getr()), 
                            (int) (2 * powerUp.getr()), 
                            (int) (2 * powerUp.getr()));
                
                g2.setStroke(new BasicStroke(2));
            }
        }

        // Draw game over text
        g.setColor(Color.white);
        if(isPaused){
            Graphics2D g2 = (Graphics2D) g;
    
            Composite originalComposite = g2.getComposite();
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
            
            // Calculate overlay dimensions and position
            int overlayWidth = boardWidth - 100;
            int overlayHeight = 250;
            int overlayX = 50;
            int overlayY = (boardHeight - overlayHeight) / 2;
            
            g2.setColor(Color.DARK_GRAY);
            g2.fillRect(overlayX, overlayY, overlayWidth, overlayHeight);
            
            g2.setComposite(originalComposite);
        
            g2.setColor(Color.WHITE);
            
            g2.setFont(new Font("Arial", Font.BOLD, 50));
            String pauseText = "PAUSED";
            FontMetrics metrics = g2.getFontMetrics();
            int pauseX = (boardWidth - metrics.stringWidth(pauseText)) / 2;
            g2.drawString(pauseText, pauseX, overlayY + 100);
        
            g2.setFont(new Font("Arial", Font.PLAIN, 20));
            String resumeText = "Press P to Resume";
            metrics = g.getFontMetrics();
            int resumeX = (boardWidth - metrics.stringWidth(resumeText)) / 2;
            g2.drawString(resumeText, resumeX, overlayY + 150);
            
            g2.setFont(new Font("Arial", Font.PLAIN, 20));
            String homeText = "Press H for Home Screen";
            metrics = g.getFontMetrics();
            int homeX = (boardWidth - metrics.stringWidth(homeText)) / 2;
            g2.drawString(homeText, homeX, overlayY + 200);

        } else if (gameOver) {
            Graphics2D g2 = (Graphics2D) g;
    
            Composite originalComposite = g2.getComposite();
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
            
            // Calculate overlay dimensions and position
            int overlayWidth = boardWidth - 100;
            int overlayHeight = 300;
            int overlayX = 50;
            int overlayY = (boardHeight - overlayHeight) / 2;
            
            g2.setColor(Color.DARK_GRAY);
            g2.fillRect(overlayX, overlayY, overlayWidth, overlayHeight);
            
            g2.setComposite(originalComposite);

            g2.setColor(Color.WHITE);
            
            g2.setFont(new Font("Arial", Font.BOLD, 50));
            String gameOverText = "GAME OVER";
            FontMetrics metrics = g.getFontMetrics();
            int gameOverX = (boardWidth - metrics.stringWidth(gameOverText)) / 2;
            g2.drawString(gameOverText, gameOverX, overlayY + 100);

            g2.setFont(new Font("Arial", Font.BOLD, 30));
            String scoreText = "Final Score: " + score;
            metrics = g.getFontMetrics();
            int scoreX = (boardWidth - metrics.stringWidth(scoreText)) / 2;
            g2.drawString(scoreText, scoreX, overlayY + 150);

            g2.setFont(new Font("Arial", Font.PLAIN, 20));
            String restartText = "Press SPACE to play again";
            metrics = g.getFontMetrics();
            int restartX = (boardWidth - metrics.stringWidth(restartText)) / 2;
            g2.drawString(restartText, restartX, overlayY + 200);

            g2.setFont(new Font("Arial", Font.PLAIN, 20));
            String homeText = "Press H for Home Screen";
            metrics = g.getFontMetrics();
            int homeX = (boardWidth - metrics.stringWidth(homeText)) / 2;
            g2.drawString(homeText, homeX, overlayY + 250);
        } else {
            g.setFont(new Font("Arial", Font.PLAIN, 15));
            g.drawString("Level: " + currentLevel, 10, 20);
            g.drawString("Score: " + score, 10, 35);
            g.drawString("Lives: " + lives, 10, 50);
            if (killCounter < 10) {
                g.drawString("Ultimate in " + Math.max(0, requiredKills - killCounter), 10, 65);
            } else {
                g.drawString("Ultimate Ready (Press F)", 10, 65);
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
                    
                    // Drop powerup
                    double rand = Math.random();
                    if(rand < 0.001){
                        powerUps.add(new PowerUp(1, alien.getX(), alien.getY()));
                    } else if(rand < 1) {
                        powerUps.add(new PowerUp(2, alien.getX(), alien.getY()));
                    }
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

                    // Drop powerup
                    double rand = Math.random();
                    if(rand < 1){
                        powerUps.add(new PowerUp(1, alien.getX(), alien.getY()));
                    } else if(rand < 0.001) {
                        powerUps.add(new PowerUp(2, alien.getX(), alien.getY()));
                    }
                }
            }
        }

        // PowerUp Update
        for (int i = powerUps.size() - 1; i >= 0; i--) {
            PowerUp powerUp = powerUps.get(i);

            if (!powerUp.isTaken()) {
                double dx = ship.getX() - powerUp.getx();
                double dy = ship.getY() - powerUp.gety();
                double distance = Math.sqrt(dx * dx + dy * dy);

                if (distance < ship.getWidth() / 2 + powerUp.getr()) {
                    powerUp.setTaken(true);

                    if (powerUp.getType() == 1) {
                        // Extra life
                        addLife();
                    } else if (powerUp.getType() == 2) {
                        // Extra damage
                        addDamage();
                    }
                }
            }

            // Remove power-ups if taken or off-screen
            if (powerUp.isTaken() || powerUp.update()) {
                powerUps.remove(i);
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
                alienBullet.setUsed(true);
                lives = lives - 1;
                if(lives == 0){
                    gameOver = true;
                }
            }
        }

        // Clear used alien bullets
        alienBulletArray.removeIf(alienBullet -> alienBullet.getY() > boardHeight);
    }

    // Add life
    private void addLife(){
        if(lives < 5){
            lives++;
        }
    }

    // Boost damage
    private void addDamage(){
        // Does nothing for now
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

    private void goToHomeScreen(){
        gameLoop.stop();

        JFrame mainFrame = (JFrame) SwingUtilities.getWindowAncestor(this);

        mainFrame.getContentPane().removeAll();

        MenuPanel menuPanel = new MenuPanel(mainFrame);
        mainFrame.add(menuPanel);

        mainFrame.revalidate();
        mainFrame.repaint();
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if(!isPaused){
            move();
            repaint();
            if (gameOver) {
                HighScoreManager.setHighScore(score);
                gameLoop.stop();
                alienBulletArray.clear();
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();

        // Add explicit pause toggling
        if (code == KeyEvent.VK_P) {
            isPaused = !isPaused;
            
            if (isPaused) {
                gameLoop.stop();
            } else {
                gameLoop.start();
            }
            repaint();
            return;
        }
    
        if(isPaused){
            if(code == KeyEvent.VK_H){
                goToHomeScreen();
                return;
            }
            return;
        }

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
            } else if (code == KeyEvent.VK_H){
                goToHomeScreen();
                return;
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
        lives = 3;
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