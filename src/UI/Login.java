package UI;

import objects.*;
import UIKit.*;
import java.awt.Point;
import objects.User.UserType;
import JSON_translation.*;
import database.*;
import javax.swing.JPanel;
import javax.swing.JFormattedTextField;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.BoxLayout;
import javax.swing.JTabbedPane;

import java.awt.event.*;
import java.awt.*;

@SuppressWarnings("deprecation")
public class Login extends JPanel implements ActionListener {

	public JFormattedTextField username;
	public JPasswordField password;
	public JButton logIn;
	public Frame frame;
	public User user;
	public Debate debate;
	public boolean verified;
	UserQuery uq = new UserQuery();
	
	public Login(Frame frame) {
		this.frame = frame;
		
		this.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		
		//this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		
		JLabel label1 = new JLabel("Username:");
		label1.setName("label1");
		username = new JFormattedTextField();
		username.setName("username");
		username.setColumns(36);
		
		c.gridx = 0;
		c.gridy = 0;
		this.add(label1, c);
		c.gridx = 1;
		c.gridy = 0;
		this.add(username, c);
		/*
		label1.setAlignmentX(Component.CENTER_ALIGNMENT);
		this.add(label1);
		username.setAlignmentX(Component.CENTER_ALIGNMENT);
		this.add(username);
		*/
		JLabel label2 = new JLabel("Password:");
		label2.setName("label2");
		password = new JPasswordField(36);
		password.setName("password");
		/*
		label2.setAlignmentX(Component.CENTER_ALIGNMENT);
		this.add(label2);
		password.setAlignmentX(Component.CENTER_ALIGNMENT);
		this.add(password);
		*/
		c.gridx = 0;
		c.gridy = 1;
		this.add(label2, c);
		c.gridx = 1;
		c.gridy = 1;
		this.add(password, c);
		
		logIn = new JButton("Submit");
		logIn.setName("login");
		
		c.gridx = 1;
		c.gridy = 2;
		this.add(logIn, c);
		
		logIn.setActionCommand("logIn");
		logIn.addActionListener(this);
		/*
		logIn.setAlignmentX(Component.CENTER_ALIGNMENT);
		this.add(logIn);
		*/
		this.setVisible(true);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getActionCommand().equals("logIn")) {
			System.out.println("Login attempt");
			//DebateQuery dq = new DebateQuery();
			//RulesQuery rq = new RulesQuery();
			//Debate d = dq.getDebateObject();
			
			//uq.getUser(username.getText());
			
			String actualPassword = "";
			for (int i = 0; i < password.getPassword().length; i++) {
				actualPassword += password.getPassword()[i];
			}
			frame.uq.verifyUserLogin(username.getText(), DFDatabase.defaultDatabase.hashString(actualPassword));

		}
	}
	
}
