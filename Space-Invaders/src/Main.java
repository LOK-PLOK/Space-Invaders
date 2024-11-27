import javax.swing.*;

public class Main {
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