package UI;

import objects.*;


import javax.swing.JPanel;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JButton;

import java.awt.Dimension;
import java.awt.event.*;
import java.awt.*;

public class Account extends JPanel implements ActionListener{

	public JLabel label1, label2, label3;
	public JFormattedTextField username, email;
	public JPasswordField password;
	public JButton createAccount;
	private User u;
	
	public Account() {
		this.setLayout(new GridBagLayout());
		
		username = new JFormattedTextField();
		password = new JPasswordField();
		email = new JFormattedTextField();
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
		if (e.getActionCommand().equals("account")) {
			
		}
	}
	
}
