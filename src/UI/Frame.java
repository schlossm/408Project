import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.JPanel;

import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.*;

public class Frame extends JFrame implements ActionListener {
	
	public JPanel login, debate, admin, rules;
	public JTabbedPane tabs;
	
	public Frame(String title) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		this.setSize(new Dimension(Window.WIDTH, Window.HEIGHT));
		
		login = new Login();
		debate = new DebateThread("If all living things have a conscious and unconscious thought, can a being with artificial intelligence ever be considered living?", "It seems impossible for a machine to ever have an unconsious thought because code executes exactly what it intends to.", "408Bosses");
		admin = new Admin(0);
		rules = new Rules("Hello, these are the rules.");
		
		tabs = new JTabbedPane();
		tabs.add("Log In", login);
		tabs.add("Debate", debate);
		tabs.add("Administration", admin);
		tabs.add("Rules", rules);
		tabs.setVisible(true);
		
		add(tabs);
		setVisible(true);
		
		this.setSize(new Dimension(300, 100));
		this.setTitle(title);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		System.out.println(e.getActionCommand());
		/*if () {

		}*/
	}
}
