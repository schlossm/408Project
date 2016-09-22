package UI;

import objects.*;

import javax.swing.JPanel;
import javax.swing.JFormattedTextField;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JRadioButton;

import java.awt.Dimension;
import java.awt.event.*;

public class Admin extends JPanel implements ActionListener{
	public JLabel label1, label2;
	public JFormattedTextField searchUser, searchComment;
	public JButton searchUserButton, banUserButton, unbanUserButton, searchCommentButton, hideCommentButton, showCommentButton;
	public JRadioButton user, mod, admin;
	private User u;
	
	public Admin(User u) {
		this.u = u;
		
		label1 = new JLabel("Enter a Username");
		searchUser = new JFormattedTextField();
		searchUserButton = new JButton("Search");
		searchUserButton.setActionCommand("searchUser");
		searchUser.addActionListener(this);
		banUserButton = new JButton("Ban");
		banUserButton.setActionCommand("banUser");
		banUserButton.addActionListener(this);
		unbanUserButton = new JButton("Unban");
		unbanUserButton.setActionCommand("unbanUser");
		unbanUserButton.addActionListener(this);
		
		user = new JRadioButton("User");
		user.setActionCommand("setUser");
		user.addActionListener(this);
		mod = new JRadioButton("Moderator");
		mod.setActionCommand("setMod");
		mod.addActionListener(this);
		admin = new JRadioButton("Administrator");
		admin.setActionCommand("setAdmin");
		admin.addActionListener(this);
		
		ButtonGroup group = new ButtonGroup();
		group.add(user);
		group.add(mod);
		group.add(admin);
		
		label2 = new JLabel("Enter a Comment ID");
		searchComment = new JFormattedTextField();
		searchCommentButton = new JButton("Search");
		searchCommentButton.setActionCommand("searchComment");
		searchCommentButton.addActionListener(this);
		hideCommentButton = new JButton("Hide");
		hideCommentButton.setActionCommand("hideComment");
		hideCommentButton.addActionListener(this);
		showCommentButton = new JButton("Show");
		showCommentButton.setActionCommand("showComment");
		showCommentButton.addActionListener(this);
		
		this.add(label1);
		this.add(searchUser);
		this.add(searchUserButton);
		this.add(banUserButton);
		this.add(unbanUserButton);
		this.add(user);
		this.add(mod);
		this.add(admin);
		
		this.add(label2);
		this.add(searchComment);
		this.add(searchCommentButton);
		this.add(hideCommentButton);
		this.add(showCommentButton);
		this.setVisible(true);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getActionCommand().equals("searchUser")) {
			
		}
		else if (e.getActionCommand().equals("banUser")) {
			
		}
		else if (e.getActionCommand().equals("unbanUser")) {
			
		}
		else if (e.getActionCommand().equals("setUser")) {
			
		}
		else if (e.getActionCommand().equals("setMod")) {
			
		}
		else if (e.getActionCommand().equals("setAdmin")) {
			
		}
		else if (e.getActionCommand().equals("searchComment")) {
			
		}
		else if (e.getActionCommand().equals("hideComment")) {
			
		}
		else if (e.getActionCommand().equals("showComment")) {
			
		}
	}
	
}