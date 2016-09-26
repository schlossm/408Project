package UI;

import objects.*;
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

import java.awt.event.*;
import java.awt.*;

public class Login extends JPanel implements ActionListener{

	public JFormattedTextField username;
	public JPasswordField password;
	public JButton logIn;
	public Frame frame;
	public UserQuery uq = new UserQuery();
	
	public Login(Frame frame) {
		this.frame = frame;
		this.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		
		JLabel label1 = new JLabel("Username:");
		label1.setName("label1");
		username = new JFormattedTextField();
		username.setName("username");
		c.gridx = 0;
		c.gridy = 0;
		this.add(label1, c);
		c.gridx = 1;
		c.gridy = 0;
		this.add(username, c);
		
		JLabel label2 = new JLabel("Password:");
		label2.setName("label2");
		password = new JPasswordField(30);
		password.setName("password");
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

		this.setVisible(true);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getActionCommand().equals("logIn")) {
			//DebateQuery dq = new DebateQuery();
			//RulesQuery rq = new RulesQuery();
			//Debate d = dq.getDebateObject();
			Debate d = null;
			
			System.out.println("password is " + password.getText());
			System.out.println("encrypted is " + DFDatabase.defaultDatabase.encryptString(password.getText()));
			User u = null;
			if (uq.verifyUserLogin(username.getText(), DFDatabase.defaultDatabase.encryptString(password.getText()))){
				u = uq.getUser(username.getText());
				if (u != null) {
					JOptionPane.showMessageDialog(this, "Login was successful.");
					
					frame.tabs.removeAll();
					frame.remove(frame.tabs);
					
					frame.debate = new DebateThread(u, d);
					frame.tabs.addTab("Debate", frame.debate);
					
					if (u.getUserType().equals(UserType.MOD) || u.getUserType().equals(UserType.ADMIN)) {
						frame.admin = new Admin(u);
						frame.tabs.addTab("Administration", frame.admin);
					}
					//frame.rules = rq.getRules();
					//frame.tabs.addTab("Rules", frame.rules);
				}
			}
			else {
				JOptionPane.showMessageDialog(this, "The username or password is invalid.", "Error", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	
}
