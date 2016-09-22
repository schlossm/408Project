package UI;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.JPanel;

import java.awt.Dimension;
import java.awt.Window;

public class Frame extends JFrame {
	
	public JPanel login, debate, admin, rules, account;
	public JTabbedPane tabs;
	
	public Frame(String title) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		this.setSize(new Dimension(Window.WIDTH, Window.HEIGHT));
		
		login = new Login(this);
		//debate = new DebateThread("If all living things have a conscious and unconscious thought, can a being with artificial intelligence ever be considered living?", "It seems impossible for a machine to ever have an unconsious thought because code executes exactly what it intends to.", "408Bosses");
		//debate = new DebateThread();
		//admin = new Admin(0);
		rules = new Rules("Hello, these are the rules.");
		account = new Account();
		
		tabs = new JTabbedPane();
		tabs.add("Log In", login);
		tabs.add("Create Account", account);
		tabs.add("Debate", debate);
		tabs.add("Administration", admin);
		tabs.add("Rules", rules);
		tabs.setVisible(true);
		
		add(tabs);
		setVisible(true);
		
		this.setSize(new Dimension(1200, 900));
		this.setTitle(title);
	}
}
