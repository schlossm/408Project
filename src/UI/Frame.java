package UI;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;

import javax.swing.JPanel;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.Window;

import UIKit.*;

public class Frame extends JFrame {
	
	public JPanel login, debate, admin, rules, account;
	public JTabbedPane tabs;
	
	public Frame(String title) {
		
		DFNotificationCenter.defaultCenter.addObserver((DFNotificationCenterDelegate) login, "login");
		//DFNotificationCenter.addObserver((DFNotificationCenterDelegate) debate, "debate");
		//DFNotificationCenter.addObserver((DFNotificationCenterDelegate) admin, "admin");
		//DFNotificationCenter.addObserver((DFNotificationCenterDelegate) rules, "rules");
		//DFNotificationCenter.addObserver((DFNotificationCenterDelegate) account, "account");
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		login = new Login(this);
		//debate = new DebateThread("If all living things have a conscious and unconscious thought, can a being with artificial intelligence ever be considered living?", "It seems impossible for a machine to ever have an unconsious thought because code executes exactly what it intends to.", "408Bosses");
		//debate = new DebateThread();
		//admin = new Admin(0);
		rules = new Rules("Hello, these are the rules.");
		account = new Account();
		
		tabs = new JTabbedPane();
		tabs.add("Log In", login);
		tabs.add("Create Account", account);
		//tabs.add("Debate", debate);
		//tabs.add("Administration", admin);
		//tabs.add("Rules", rules);
		tabs.setVisible(true);
		add(tabs);
		setVisible(true);
		
		this.setSize(Toolkit.getDefaultToolkit().getScreenSize());
		this.setMinimumSize(new Dimension(600, 400));
		this.setTitle(title);
	}
}
