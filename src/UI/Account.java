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

public class Account extends JPanel implements ActionListener {

	public JLabel label1, label2;
	public JFormattedTextField username;
	public JPasswordField password;
	public JButton createAccount;
	public Frame frame;
	
	public Account(Frame frame) {
		this.frame = frame;
		
		this.setLayout(new GridBagLayout());
		Dimension size = new Dimension(40, 40);
		
		username = new JFormattedTextField();
		username.setSize(size);
		username.setMinimumSize(size);
		username.setMaximumSize(size);
		username.setColumns(36);
		password = new JPasswordField(36);
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
			else if (!username.getText().matches("[A-Za-z0-9._%+-]+")) {
				JOptionPane.showMessageDialog(this, "Please choose a username with valid characters (alphabetical letters, numbers, _, %, +, -, or .", "Error", JOptionPane.ERROR_MESSAGE);
			}
			else if (username.getText().length() > 32) {
				JOptionPane.showMessageDialog(this, "Please choose a username that is 32 characters or shorter.", "Error", JOptionPane.ERROR_MESSAGE);
			}
			else {				
				frame.uq.getUser(username.getText());
				// replace line above
				//frame.uq.usernameExists(username.getText());
			}
		}
	}
	
}
