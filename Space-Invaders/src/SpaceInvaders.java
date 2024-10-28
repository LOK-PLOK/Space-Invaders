import javax.swing.*;

public class SpaceInvaders {
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setTitle("Space Invaders");
        frame.setSize(512, 512);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        GamePanel gamePanel = new GamePanel();
        frame.add(gamePanel);
        frame.pack();
        gamePanel.requestFocus();
        frame.setVisible(true);

        ImageIcon image = new ImageIcon("src/space-invaders-icon.png");
        frame.setIconImage(image.getImage());
    }
}