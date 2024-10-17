import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;


public class SpaceInvaders extends JPanel {
    // Board
    int tileSize  = 32;
    int rows = 16;
    int columns = 16;
    int boardWidth = tileSize * columns; // 32* 16 = 512px
    int boardHeight = tileSize * rows; // 32* 16 = 512px

    Image shipImg;
    Image alienImg;
    Image alienCyan;
    Image alienMagenta;
    Image alienYellow;
    ArrayList<Image> alienImgArray;

    SpaceInvaders(){
        setPreferredSize(new Dimension(boardWidth,boardHeight));
        setBackground(Color.black);

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
    }
}

