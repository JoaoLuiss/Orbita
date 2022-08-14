package orbita;

import javax.swing.*;
import java.awt.*;

public class MyFrame extends JFrame {
    
    public MyFrame(){
        super("Entre em órbita!"); // it's the same thing as setTitle()
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        add(new MyPanel()); // it shows the painting at this panel
        pack(); // I don't know what it does
        setLocationRelativeTo(null); // it aligns the frame at the screen center
        setVisible(true);
        setBackground(Color.BLACK);
    }
    
}
