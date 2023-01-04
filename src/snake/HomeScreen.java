package snake;

//@author Chiến


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class HomeScreen extends JFrame implements ActionListener {
	public JLabel lb_back_ground;
	private JButton bt_start;
	
	public HomeScreen() {

		this.setSize(650, 366);
		lb_back_ground = new JLabel(new ImageIcon("./resources/background.jpg"));
		this.add(lb_back_ground);
		
		bt_start = new JButton("START");
		bt_start.setBounds(285, 163, 80, 40);
		bt_start.addActionListener(this);
		lb_back_ground.add(bt_start);
		
		this.setVisible(true);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == bt_start){
			this.setVisible(false);
			new snake();
		}
	}
}
