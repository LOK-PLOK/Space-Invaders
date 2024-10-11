import javax.swing.*;
import java.awt.*;

public class MyFrame extends JFrame {
    MyFrame(){

        //Frame Setup
        this.setTitle("Space Invaders"); // set title
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);// when pressing the X it willl close the program
        this.setResizable(false); // will not make the screen resize
        this.setSize(768,900);// Sets the X and Y dimension
        this.setVisible(true);// maike frame visible

        //Background related styling
        ImageIcon image = new ImageIcon("src/space-invaders-icon.png"); //create image icon
        this.setIconImage(image.getImage()); // changes icon of frame
        this.getContentPane().setBackground(new Color(35,43,43)); // changes the background to black
    }
}
