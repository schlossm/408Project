package UI;

import javax.swing.JPanel;
import javax.swing.JFormattedTextField;
import javax.swing.JPasswordField;
import javax.swing.JButton;

import java.awt.Dimension;
import java.awt.event.*;

public class Account extends JPanel implements ActionListener{

	public JFormattedTextField username, email;
	public JPasswordField password;
	public JButton createAccount;
	
	public Account() {
		username = new JFormattedTextField();
		password = new JPasswordField();
		email = new JFormattedTextField();
		createAccount = new JButton("Create Account");
		this.add(username);
		this.add(password);
		this.setVisible(true);
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
}