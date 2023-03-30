package snake;

// @author Phú, Chiến, lmao

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.Font;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import java.util.LinkedList;
import java.util.Queue;

import javax.swing.Timer;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.JLabel;
import javax.swing.JOptionPane;


public class GamePlay extends JPanel {
    public static final int UP = 1, DOWN = 2, LEFT = 3, RIGHT = 4;
    public static final int PLAYING = 1, PAUSING = 2, LOST = 3, WAITING = 4;

    //GamePlay components
    JButton bt_restart;
    JButton bt_resetHighScore;

    JLabel lb_score;
    JLabel lb_highScore;
    JLabel lb_status;
    JLabel lb_level;
    JLabel lb_penetrable;
    JLabel lb_guide;
    JLabel lb_reminder;

    JLabel hd_guide;
    JLabel hd_1;
    JLabel hd_2;
    JLabel hd_space;
    JLabel hd_exit;
    JLabel hd_st;
    
    JPanel playfield;
 
    JComboBox<String> cbb_level;
    JComboBox<String> cbb_penetrable;

    int highScore;
    int score;
    int t, k;
    int speed[] = {125, 100, 75, 50, 25, 1};
    
    //playfield components
    JButton bt[][];
    
    Queue<Integer> xSnake = new LinkedList<>();
    Queue<Integer> ySnake = new LinkedList<>();

    int row, column;
    int headX, headY, appleX, appleY;
    int inputDirection;
    int direction;
    int convertX[] = {0, 0, -1, 1};
    int convertY[] = {-1, 1, 0, 0};
    int status; 
    int old_headX, old_headY;

    //Timer variable that incicates the game's difficulty
    Timer timer;

    //Intialitize JPanel Gameplay 
    public GamePlay() {

        //Initialize timer
        timer = new Timer(speed[2], new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                runSnake();
            }
        });

        this.setSize(660, 450);
        this.setLayout(null);
        
        //Initialize the playfield
        playfield = new JPanel();
        playfield.setLocation(0, 0);
        playfield.setSize(450, 450);
        row = 30; column = 30;
        playfield.setLayout(new GridLayout(row, column));
        this.add(playfield);

        //JButton for letting people restart the game
        bt_restart = new JButton("Start");
        bt_restart.setBounds(470, 20, 120, 40);
        bt_restart.setBackground(Color.white);
        bt_restart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                bt_restart.setText("Try again");
                restart();
            }
        }); //The text "Start" appears first time we run the game, then changed to "Try again" until we close the game

        this.add(bt_restart);

        //Get previously saved informations
        getInfos();

        //JLabel to show score
        lb_score = new JLabel("Score: 0");
        lb_score.setBounds(470, 60, 120, 20);
        this.add(lb_score);

        //JLabel to show high score
        lb_highScore = new JLabel("High score: " + highScore);
        lb_highScore.setBounds(470, 90, 100, 20);
        this.add(lb_highScore);

        //JButton to reset high score
        bt_resetHighScore = new JButton("Reset");
        bt_resetHighScore.setBounds(570, 90, 70, 20);
        bt_resetHighScore.setBackground(Color.white);
        bt_resetHighScore.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (JOptionPane.showConfirmDialog(null, 
                                                    "Reset high score?", "Confirm", JOptionPane.YES_NO_OPTION) == 0) {
                    highScore = 0;
                    updateInfos();
                    lb_highScore.setText("High score: 0");
                }
            }
        });

        this.add(bt_resetHighScore);
        
        // JLabel to show level
        lb_level = new JLabel("Level: ");
        lb_level.setBounds(470, 120, 50, 30);
        this.add(lb_level);
        
        //JComboBox to adjust difficulty
        cbb_level = new JComboBox<String>();

        //Add difficulties
        cbb_level.addItem("Very Easy");
        cbb_level.addItem("Easy");
        cbb_level.addItem("Normal");
        cbb_level.addItem("Hard");
        cbb_level.addItem("Very hard");
        cbb_level.addItem("I'm the Fast");

        cbb_level.setBounds(520, 125, 100, 20);
        cbb_level.setSelectedIndex(2); // Normal as default
        this.add(cbb_level);

        //JLabel to show penetrable text
        lb_penetrable = new JLabel("Penetrable?");
        lb_penetrable.setBounds(470, 160, 100, 30);
        this.add(lb_penetrable);

        //JComboBox to set penetrable status
        cbb_penetrable = new JComboBox<String>();
        cbb_penetrable.setBounds(570, 165, 50, 20);
        cbb_penetrable.addItem("Yes");
        cbb_penetrable.addItem("No");
        cbb_penetrable.setSelectedIndex(1);
        this.add(cbb_penetrable);
        
        //JLabel to show play status
        lb_status = new JLabel("Status: Waiting");
        lb_status.setBounds(470, 185, 200, 30);
        this.add(lb_status);

        //Guide text from line 186 - 210
        
        hd_guide = new JLabel();
        hd_guide.setBounds(510, 225, 70, 20);
        hd_guide.setText("Guide");
        hd_guide.setFont(new Font("Arial", Font.ITALIC, 20));
        this.add(hd_guide);

        hd_1 = new JLabel();
        hd_1.setBounds(470, 250, 170, 20);
        hd_1.setText("How to play: Use arrow keys");
        this.add(hd_1);

        hd_2 = new JLabel();
        hd_2.setBounds(470, 270, 170, 20);
        hd_2.setText("to direct your snake.");
        this.add(hd_2);

        hd_st = new JLabel();
        hd_st.setBounds(470, 300, 150, 20);
        hd_st.setText("Press any arrow keys!");
        hd_st.setVisible(false);
        this.add(hd_st);

        hd_space = new JLabel();
        hd_space.setBounds(470, 380, 160, 20);
        hd_space.setText("'Space' - Pause/Continue");
        this.add(hd_space);

        hd_exit = new JLabel();
        hd_exit.setBounds(470, 410, 150, 20);
        hd_exit.setText("'Esc' - Exit game");
        this.add(hd_exit);
        
        //Set key bindings for playfield
        setKeyBindings(); 
        
        //Create the field
        bt = new JButton[row][column];
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
                bt[i][j] = new JButton();
                bt[i][j].setEnabled(false); //No need to have those components enabled, and to improve GUI
                bt[i][j].setBackground(Color.gray);
                playfield.add(bt[i][j]);
            }
        }  
    }
    
    //Method to intialize things for a new game
    public void restart() {
        
        if (timer.isRunning()) timer.stop(); //In case someone restarted mid_game
        timer.setDelay(speed[cbb_level.getSelectedIndex()]); //Set difficulty

        //Reset variables with WAITING status
        inputDirection = RIGHT;
        direction = RIGHT;
        status = WAITING;
        score = 0;
        lb_score.setText("Score: 0");
        lb_status.setText("Status: Waiting");
        bt_resetHighScore.setEnabled(false);
        cbb_level.setEnabled(false);
        cbb_penetrable.setEnabled(false);
        hd_st.setVisible(true);
        
        //Delete xSnake, ySnake components and playfield color
        while (!xSnake.isEmpty()) {
            bt[ySnake.poll()][xSnake.poll()].setBackground(Color.gray);
        }
        bt[appleY][appleX].setBackground(Color.gray);
        
        //Initialize snake and apple locations
        initSnakeBody();
        locateApple();
        
        playfield.requestFocusInWindow(); //Get focus
    }

	//Create the snake at default location
    public void initSnakeBody() {
        
        ySnake.add(14);
        xSnake.add(5);
        bt[14][5].setBackground(Color.yellow);

        ySnake.add(14);
        xSnake.add(6);
        bt[14][6].setBackground(Color.yellow);
        
        ySnake.add(14);
        xSnake.add(7);
        bt[14][7].setBackground(Color.red);
        
        headY = 14; headX = 7;
    }
    
    //Create the apple at random location
    public void locateApple() {
        do {
            appleX = (int)(Math.random() * (column));
            appleY = (int)(Math.random() * (row));            
        } while (bt[appleY][appleX].getBackground() != Color.gray); //Generate again if previous generated location coindides with snake body locations.
        bt[appleY][appleX].setBackground(Color.green);
    }

    //Move the snake body by 1 to the expected direction
    public void runSnake() {

        //Set inputDirection value to direction, without rotating 180 degrees
        switch (inputDirection) {
            case 1: 
                if (direction != DOWN) direction = UP; break;
            case 2: 
                if (direction != UP) direction = DOWN; break;
            case 3: 
                if (direction != RIGHT) direction = LEFT; break;
            case 4:
                if (direction != LEFT) direction = RIGHT; break;
        }
        
        //Get the previous snake head location
        old_headY = headY;
        old_headX = headX;

        //Get the new snake head location
        headY += convertY[direction - 1];
        headX += convertX[direction - 1];
        
        /** Line 308-344
         * IF head location is out of playfield border: go check penetrable status:
         *      Yes: 
         *          Move head location to the opposite side with current direction
         *          IF touched the body => lost (run updateVariablesWhenLost() method)
         *          ELSE update the field
         *      No => lost     
         * ELSE (is in playfield) IF touched the body => lost
         *      ELSE update the field            
         */
        if (headX < 0 || headX >= row || headY < 0 || headY >= column) { //If head location is out of playfield border
            if (cbb_penetrable.getSelectedItem() == "Yes") { //If the field is penetrable

                //Move head location to the other side
                if (headX < 0) headX = column - 1;
                else if (headY < 0) headY = row - 1;
                else if (headX >= row) headX = 0;
                else if (headY >= column) headY = 0;

                //Check if the head has touched snake body
                if (bt[headY][headX].getBackground() == Color.yellow) {
                    updateVariablesWhenLost();
                } else {
                    updateField();
                }
            } else { //If not penetrable => lose
                updateVariablesWhenLost(); 
            }
        } else {
            
            //Check if the head has touched snake body
            if (bt[headY][headX].getBackground() == Color.yellow) {
                updateVariablesWhenLost();
            } else {
                updateField();
            }
        }
    }

    //Get saved infos
    public void getInfos() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("./resources/infos.txt"));
            highScore = Integer.parseInt(reader.readLine());
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Update infos to file(s)
    public void updateInfos() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("./resources/infos.txt"));
            writer.write(String.valueOf(highScore));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Update current score
    public void updateScore() {
        score += 1;
        lb_score.setText("Score: " + score);
    }

    //Update high score
    public void updateHighScore() {
        if (score > highScore) {
            highScore = score;
            lb_highScore.setText("High score: " + highScore);
        }
    }

    //Reset variables with LOST status
    public void updateVariablesWhenLost() {
        timer.stop(); //Stop the game
        status = LOST;
        updateInfos();
        updateHighScore();
        bt_resetHighScore.setEnabled(true);
        cbb_level.setEnabled(true);
        cbb_penetrable.setEnabled(true);
        lb_status.setText("Status: Skill Issue");        
    }

    //Update the GUI
    public void updateField() {

        //Add new head location to the body
        ySnake.add(headY);
        xSnake.add(headX);
        if (headX != appleX || headY != appleY) { //Check if the snake hadn't eaten the apple

            //Take out the previous tail from the body (If eaten there's no need to take out the tail)
            bt[ySnake.poll()][xSnake.poll()].setBackground(Color.gray);
        } else {
            locateApple(); //Create new apple
            updateScore(); //Update the current score
        }

        //Set color for old and new head
        bt[headY][headX].setBackground(Color.red);
        bt[old_headY][old_headX].setBackground(Color.yellow);
    }
    	
    //Key mapping
    public void setKeyBindings() {
    
        playfield.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0, false), "up");
        playfield.getActionMap().put("up", go_up);

        playfield.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0, false), "down");
        playfield.getActionMap().put("down", go_down);

        playfield.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0, false), "left");
        playfield.getActionMap().put("left", go_left);

        playfield.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0, false), "right");
        playfield.getActionMap().put("right", go_right);

        playfield.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0, false), "space");
        playfield.getActionMap().put("space", go_pause);

        playfield.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0, false), "escape");
        playfield.getActionMap().put("escape", go_exit);

    }
    

    //Action variables
    Action go_up = new AbstractAction() {
        public void actionPerformed(ActionEvent e) {
            inputDirection = UP;
            waitingStatusBreak();
        }
    };
    
    Action go_down = new AbstractAction() {
        public void actionPerformed(ActionEvent e) {
            inputDirection = DOWN;
            waitingStatusBreak();
        }
    };
    
    Action go_left = new AbstractAction() {
        public void actionPerformed(ActionEvent e) {
            inputDirection = LEFT;
            waitingStatusBreak();
        }
    };
    
    Action go_right = new AbstractAction() {
        public void actionPerformed(ActionEvent e) {
            inputDirection = RIGHT;
            waitingStatusBreak();
        }
    };

    Action go_pause = new AbstractAction() {
        public void actionPerformed(ActionEvent e) {
            if (status == PAUSING) { 
                timer.start();
                status = PLAYING;
                lb_status.setText("Status: Playing");
            } else if (status == PLAYING) {
                timer.stop();
                status = PAUSING;
                lb_status.setText("Status: Pausing");
            } //Change between PLAYING and PAUSING status, as SPACE key striked
        }
    };
    
    Action go_exit = new AbstractAction() {
        public void actionPerformed(ActionEvent e) {
            if (status == PLAYING) go_pause.actionPerformed(null);
            if (JOptionPane.showConfirmDialog(null, "Are you sure you want to exit?", "Confirm", JOptionPane.YES_NO_OPTION) == 0) {
                System.exit(0);
            } 
        }
    };

    //Method to change start the game (if WAITING)
    public void waitingStatusBreak() {
        if (status == WAITING) {
            timer.start();
            hd_st.setVisible(false);
            playfield.requestFocus();
            status = PLAYING;
        }
    }
}