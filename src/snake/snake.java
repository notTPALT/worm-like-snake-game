package snake;

import javax.swing.*;

public class snake extends JFrame {
    
    public snake() {

        this.setSize(660, 490);
        
        this.setTitle("Worm-like snake game");
        this.setVisible(true);
        this.setLayout(null);
        this.setResizable(false);
        this.add(new GamePlay());
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);

    }

}


