import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class MenuPanel extends JPanel {
    private JFrame mainFrame;
    private JPanel howToPlayPanel;

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

        // How to Play Button
        JButton howToPlayButton = new JButton("HOW TO PLAY");
        howToPlayButton.setFont(new Font("Arial", Font.BOLD, 24));
        howToPlayButton.setBackground(Color.BLACK);
        howToPlayButton.setForeground(Color.WHITE);
        howToPlayButton.setFocusPainted(false);

        // Button Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.BLACK);
        buttonPanel.add(playButton);
        // buttonPanel.add(howToPlayButton); (optional)
        add(buttonPanel, BorderLayout.SOUTH);

        // Play Button Action
        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startGame();
            }
        });

        // How to Play Button Action (Optional)
        // howToPlayButton.addActionListener(new ActionListener() {
        //     @Override
        //     public void actionPerformed(ActionEvent e) {
        //         showHowToPlay();
        //     }
        // });

        // Show "How to Play" panel if high score is 0
        if (HighScoreManager.getHighScore() == 0) {
            SwingUtilities.invokeLater(this::showHowToPlay);
        }
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

    private void showHowToPlay() {
        if (howToPlayPanel == null) {
            howToPlayPanel = new JPanel(new BorderLayout());
            howToPlayPanel.setBackground(Color.BLACK);
            howToPlayPanel.setPreferredSize(new Dimension(300, 200));

            JTextArea instructions = new JTextArea();
            instructions.setText(
                "How to Play:\n\n" +
                "1. Use the LEFT and RIGHT arrow keys to move your ship.\n" +
                "2. Press SPACE to shoot bullets.\n" +
                "3. Destroy all aliens to advance to the next level.\n" +
                "4. Avoid getting hit by alien bullets.\n" +
                "5. Press F to activate the ultimate laser when ready.\n" +
                "6. Survive as long as you can and achieve the highest score!\n"+
                "Drops: 1 Red drop for +1 lives : 10 Yellow drops for +0.5 damage"
            );
            instructions.setFont(new Font("Arial", Font.BOLD, 12));
            instructions.setEditable(false);
            instructions.setWrapStyleWord(true);
            instructions.setLineWrap(true);
            instructions.setBackground(Color.BLACK);
            instructions.setForeground(Color.WHITE);
            instructions.setBorder(BorderFactory.createLineBorder(Color.BLACK));

            JScrollPane scrollPane = new JScrollPane(instructions);
            scrollPane.setBorder(BorderFactory.createLineBorder(Color.BLACK));

            howToPlayPanel.add(scrollPane, BorderLayout.CENTER);

            JButton closeButton = new JButton("X");
            closeButton.setFont(new Font("Arial", Font.BOLD, 14));
            closeButton.setBackground(Color.RED);
            closeButton.setForeground(Color.WHITE);
            closeButton.setFocusPainted(false);
            closeButton.addActionListener(e -> removeHowToPlay());

            JPanel closePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            closePanel.setBackground(Color.BLACK);
            closePanel.add(closeButton);

            howToPlayPanel.add(closePanel, BorderLayout.NORTH);
        }

        add(howToPlayPanel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    private void removeHowToPlay() {
        remove(howToPlayPanel);
        revalidate();
        repaint();
    }
}