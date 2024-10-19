import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {

        int tileSize  = 32;
        int rows = 16;
        int columns = 16;
        int boardWidth = tileSize * columns; // 32* 16 = 512px
        int boardHeight = tileSize * rows; // 32* 16 = 512px
        JFrame frame = new JFrame();
        frame.setTitle("Space Invaders"); // set title
        frame.setSize(boardWidth,boardHeight);// Sets the X and Y dimension
        frame.setResizable(false); // will not make the screen resize
        frame.setLocationRelativeTo(null);// makes the window show at the center of the screen
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);// when pressing the X it willl close the program

        //Background related styling
        SpaceInvaders spaceInvaders = new SpaceInvaders();
        frame.add(spaceInvaders);
        frame.pack();
        spaceInvaders.requestFocus();
        frame.setVisible(true);// maike frame visible
        ImageIcon image = new ImageIcon("src/space-invaders-icon.png"); //create image icon
        frame.setIconImage(image.getImage()); // changes icon of frame
    }
}