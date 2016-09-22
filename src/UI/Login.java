package UI;

import objects.*;
import database.*;
import javax.swing.JPanel;
import javax.swing.JFormattedTextField;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import javax.swing.JLabel;

import java.awt.Dimension;
import java.awt.event.*;
import java.awt.*;

public class Login extends JPanel implements ActionListener{

	public JFormattedTextField username;
	public JPasswordField password;
	public JButton logIn;
	public Frame frame;
	
	public Login(Frame f) {
		this.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		
		JLabel label1 = new JLabel("Username:");
		username = new JFormattedTextField();
		c.gridx = 0;
		c.gridy = 0;
		this.add(label1, c);
		c.gridx = 1;
		c.gridy = 0;
		this.add(username, c);
		
		JLabel label2 = new JLabel("Password:");
		password = new JPasswordField(30);
		c.gridx = 0;
		c.gridy = 1;
		this.add(label2, c);
		c.gridx = 1;
		c.gridy = 1;
		this.add(password, c);
				
		logIn = new JButton("Submit");
		c.gridx = 1;
		c.gridy = 2;
		this.add(logIn, c);
		logIn.setActionCommand("logIn");
		logIn.addActionListener(this);
		this.setVisible(true);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getActionCommand().equals("logIn")) {
			System.out.println("password is " + password.getText());
			DFDatabase.defaultDatabase.encryptString(password.getText());
			//if ()
		}
	}
	
}
