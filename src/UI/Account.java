package UI;

import objects.*;
import objects.User.UserType;
import UIKit.*;

import javax.swing.JPanel;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JOptionPane;

import JSON_translation.UserQuery;
import database.DFDatabase;

import javax.swing.JButton;

import java.awt.Dimension;
import java.awt.event.*;
import java.awt.*;

@SuppressWarnings("deprecation")
public class Account extends JPanel implements ActionListener, DFNotificationCenterDelegate {

	public JLabel label1, label2;
	public JFormattedTextField username;
	public JPasswordField password;
	public JButton createAccount;
	private User user;
	public UserQuery uq;
	
	public Account() {
		user = null;
		uq = new UserQuery();
		DFNotificationCenter.defaultCenter.addObserver(this, "exists");
		
		this.setLayout(new GridBagLayout());
		Dimension size = new Dimension(40, 40);
		
		username = new JFormattedTextField();
		username.setSize(size);
		username.setMinimumSize(size);
		username.setMaximumSize(size);
		password = new JPasswordField(32);
		password.setMinimumSize(size);
		password.setSize(size);
		password.setMaximumSize(size);
		label1 = new JLabel("Username:");
		label2 = new JLabel("Password:");
		createAccount = new JButton("Submit");
		createAccount.setActionCommand("account");
		createAccount.addActionListener(this);
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		
		c.gridx = 0;
		c.gridy = 0;
		this.add(label1, c);
		c.gridx = 1;
		c.gridy = 0;
		this.add(username, c);
		c.gridx = 0;
		c.gridy = 1;
		this.add(label2, c);
		c.gridx = 1;
		c.gridy = 1;
		this.add(password, c);
		c.gridx = 1;
		c.gridy = 2;
		this.add(createAccount, c);
		
		this.setVisible(true);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getActionCommand().equals("account")) {
			if (username.getText().equals("") && password.getPassword() == null) {
				JOptionPane.showMessageDialog(this, "Please fill in all of the fields.", "Error", JOptionPane.ERROR_MESSAGE);	
			}
			else {				
				uq.getUser(username.getText());
				// replace line above
				// uq.usernameExists(username.getText());
			}
		}
	}

	@Override
	public void performActionFor(String notificationName, Object userData) {
		// TODO Auto-generated method stub
		System.out.println("Account performActionFor: " + notificationName);
		if (notificationName.equals(UIStrings.exists)) {
			boolean exists = (Boolean) userData;
			if (!exists) {
				//uq.addNewUser(username.getText(), DFDatabase.defaultDatabase.encryptString(password.getText()), UserType.USER);
				
				String actualPassword = "";
				for (int i = 0; i < password.getPassword().length; i++) {
					actualPassword += password.getPassword()[i];
				}
				
				uq.addNewUser(username.getText(), DFDatabase.defaultDatabase.hashString(actualPassword), UserType.USER);
				JOptionPane.showMessageDialog(this, "Your account was created.");
			}
			else {
				JOptionPane.showMessageDialog(this, "This username already exists, please use another.", "Error", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	
}
