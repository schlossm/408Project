package UI;
import JSON_translation.*;
import objects.*;
import objects.User.UserType;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;

import javax.swing.JPanel;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.Window;

import UIKit.*;
import database.DFDatabase;

public class Frame extends JFrame implements DFNotificationCenterDelegate {
	
	public Login login;
	public Account account;
	public DebateThread thread;
	public Admin admin;
	public Rules rules;
	public JTabbedPane tabs;
	public UserQuery uq, uq2;
	public DebateQuery dq;
	public PostQuery pq;
	public User user;
	public Debate debate;
	
	@SuppressWarnings("deprecation")
	public Frame(String title) {
		
		uq = new UserQuery();
		uq2 = new UserQuery();
		dq = new DebateQuery();
		pq = new PostQuery();
		
		DFNotificationCenter.defaultCenter.addObserver((DFNotificationCenterDelegate) this, "success");
		DFNotificationCenter.defaultCenter.addObserver((DFNotificationCenterDelegate) this, "failure");
		DFNotificationCenter.defaultCenter.addObserver((DFNotificationCenterDelegate) this, "returned");
		DFNotificationCenter.defaultCenter.addObserver((DFNotificationCenterDelegate) this, "exists");
		DFNotificationCenter.defaultCenter.addObserver((DFNotificationCenterDelegate) this, "debateReturned");
		//DFNotificationCenter.addObserver((DFNotificationCenterDelegate) debate, "debate");
		//DFNotificationCenter.addObserver((DFNotificationCenterDelegate) admin, "admin");
		//DFNotificationCenter.addObserver((DFNotificationCenterDelegate) rules, "rules");
		//DFNotificationCenter.addObserver((DFNotificationCenterDelegate) account, "account");
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		tabs = new JTabbedPane();
		tabs.setVisible(true);
	
		login = new Login(this);
		tabs.add(login, "Log in");
			
		account = new Account(this);
		tabs.add(account, "Create Account");
		
		thread = new DebateThread(this);
		
		admin = new Admin(this);

		rules = new Rules(this);
		rules.setText("Here are the rules");
		
		add(tabs);
		setVisible(true);
		
		this.setSize(Toolkit.getDefaultToolkit().getScreenSize());
		this.setMinimumSize(new Dimension(600, 400));
		this.setTitle(title);
	}

	@Override
	public void performActionFor(String notificationName, Object obj) {
		// TODO Auto-generated method stub
		System.out.println("PerformActionFor: " + notificationName);
		if (notificationName.equals(UIStrings.exists)) {
			//Account Action
			boolean exists = (Boolean) obj;
			if (!exists) {
				//uq.addNewUser(username.getText(), DFDatabase.defaultDatabase.encryptString(password.getText()), UserType.USER);
				
				String actualPassword = "";
				for (int i = 0; i < account.password.getPassword().length; i++) {
					actualPassword += account.password.getPassword()[i];
				}
				
				uq.addNewUser(account.username.getText(), DFDatabase.defaultDatabase.hashString(actualPassword), UserType.USER);
				JOptionPane.showMessageDialog(this, "Your account was created.");
			}
			else {
				JOptionPane.showMessageDialog(this, "This username already exists, please use another.", "Error", JOptionPane.ERROR_MESSAGE);
			}
		}
		if (notificationName.equals(UIStrings.success)) {
			//Login Action
			JOptionPane.showMessageDialog(this, "Login was successful.");
			uq2.getUser(login.username.getText());
			
			tabs.removeAll();

			dq.getDebateByTitle("Test Debate");
			
			tabs.addTab("Debate", thread);
			//dq.getCurrentDebate();
			tabs.addTab("Rules", rules);
			if (user != null && !user.getUserType().equals(UserType.USER)) {
				tabs.addTab("Administration", admin);
				admin.setUserType(user.getUserType());
			}
		}
		else if (notificationName.equals(UIStrings.failure)) {
			// Login Action
			JOptionPane.showMessageDialog(this, "The username or password is invalid.", "Error", JOptionPane.ERROR_MESSAGE);
		}
		else if (notificationName.equals(UIStrings.returned)) {
			// UserQuery Action
			user = (User) obj;
			// May need action to set the usertype of admin.
			if (user == null) {
				System.out.println("Returned user was null");
			}
			else if (user != null && !user.getUserType().equals(UserType.USER)) {
				tabs.addTab("Administration", admin);
				admin.setUserType(user.getUserType());
			}	
		}
		else if (notificationName.equals(UIStrings.debateReturned)) {
			// DebateQuery Action
			debate = (Debate) obj;
			
			if (debate == null) {
				System.out.println("Returned debate was null");
			}
			else if (debate != null) {
				thread.displayDebate(debate);
			}
		}
	}
}
