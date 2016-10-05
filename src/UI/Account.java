package UI;

import UIKit.DFNotificationCenter;
import UIKit.DFNotificationCenterDelegate;
import com.google.gson.JsonObject;
import com.sun.istack.internal.Nullable;
import database.DFDatabaseCallbackDelegate;
import database.DFError;
import database.dfDatabaseFramework.WebServerCommunicator.DFDataUploaderReturnStatus;
import objects.*;
import objects.User.UserType;

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

public class Account extends JPanel implements ActionListener, DFNotificationCenterDelegate{

	public JLabel label1, label2, label3;
	public JFormattedTextField username, email;
	public JPasswordField password;
	public JButton createAccount;
	private User u;
	UserQuery uq = new UserQuery();
	
	public Account() {
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
		email = new JFormattedTextField();
		email.setMinimumSize(size);
		email.setMaximumSize(size);
		email.setSize(size);
		label1 = new JLabel("Username:");
		label2 = new JLabel("Password:");
		label3 = new JLabel("Email:");
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
		c.gridx = 0;
		c.gridy = 2;
		this.add(label3, c);
		c.gridx = 1;
		c.gridy = 2;
		this.add(email, c);
		c.gridx = 2;
		c.gridy = 2;
		this.add(createAccount, c);
		
		
		this.setVisible(true);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		System.out.println(username.getText());
		System.out.println(password.getText());
		System.out.println(email.getText());
		if (e.getActionCommand().equals("account")) {
			if (username.getText().equals("") || password.getText().equals("") || email.getText().equals("")) {
				JOptionPane.showMessageDialog(this, "Please fill in all of the fields.", "Error", JOptionPane.ERROR_MESSAGE);	
			}
			else {
				User u = null;
				DFNotificationCenter.defaultCenter.register(this, UIStrings.returned);
				uq.getUser(username.getText());
			}
		}
	}


	@Override
	public void performActionFor(String notificationName, Object userData)
	{
		User u = (User)userData;
		if (u!= null)
		{
			uq.addNewUser(username.getText(), DFDatabase.defaultDatabase.encryptString(password.getText()), UserType.USER);
			JOptionPane.showMessageDialog(this, "Your account was created.");
		}
		else
		{
			JOptionPane.showMessageDialog(this, "This username already exists, please use another.", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
}
