import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class MenuPanel extends JPanel {
    private JFrame mainFrame;

    public MenuPanel(JFrame frame) {
        this.mainFrame = frame;

        setLayout(new BorderLayout());
        setBackground(Color.BLACK);
        setPreferredSize(new Dimension(512, 512));

        // Title Label
        JLabel titleLabel = new JLabel("SPACE INVADERS", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 48));
        titleLabel.setForeground(Color.WHITE);
        add(titleLabel, BorderLayout.NORTH);

        // High Score Label
        JLabel highScoreLabel = new JLabel("HIGH SCORE: " + HighScoreManager.getHighScore(), SwingConstants.CENTER);
        highScoreLabel.setFont(new Font("Arial", Font.BOLD, 24));
        highScoreLabel.setForeground(Color.WHITE);
        add(highScoreLabel, BorderLayout.CENTER);

        // Play Button
        JButton playButton = new JButton("PLAY");
        playButton.setFont(new Font("Arial", Font.BOLD, 24));
        playButton.setBackground(Color.BLACK);
        playButton.setForeground(Color.WHITE);
        playButton.setFocusPainted(false);

        // Button Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.BLACK);
        buttonPanel.add(playButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Play Button Action
        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startGame();
            }
        });
    }

    private void startGame() {
        // Remove menu panel
        mainFrame.getContentPane().removeAll();

        // Create game panel
        GamePanel gamePanel = new GamePanel();
        mainFrame.add(gamePanel);
        mainFrame.revalidate();
        mainFrame.repaint();
        gamePanel.requestFocusInWindow();
    }
}