import javax.swing.*;

/**
 * The entry point of the Space Invaders game.
 * This class initializes the main frame and sets up the initial menu panel.
 */
public class Main {
    /**
     * The main method that serves as the entry point of the application.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame();
            frame.setTitle("Space Invaders");
            frame.setSize(512, 512);
            frame.setResizable(false);
            frame.setLocationRelativeTo(null);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            // Start with menu
            MenuPanel menuPanel = new MenuPanel(frame);
            frame.add(menuPanel);
            frame.pack();
            frame.setVisible(true);

            ImageIcon image = new ImageIcon("src/Images/space-invaders-icon.png");
            frame.setIconImage(image.getImage());
        });
    }
}