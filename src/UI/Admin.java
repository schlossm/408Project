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
	private int privilege; //0 = banned user, 1 = user, 2 = moderator, 3 = administrator
	
	public Admin(int privilege) {
		label1 = new JLabel("Enter a Username");
		searchUser = new JFormattedTextField();
		searchUserButton = new JButton("Search");
		searchUserButton.setActionCommand("searchUser");
		banUserButton = new JButton("Ban");
		banUserButton.setActionCommand("banUser");
		unbanUserButton = new JButton("Unban");
		unbanUserButton.setActionCommand("unbanUser");
		
		user = new JRadioButton("User");
		user.setActionCommand("setUser");
		mod = new JRadioButton("Moderator");
		mod.setActionCommand("setMod");
		admin = new JRadioButton("Administrator");
		admin.setActionCommand("setAdmin");
		
		ButtonGroup group = new ButtonGroup();
		group.add(user);
		group.add(mod);
		group.add(admin);
		
		label2 = new JLabel("Enter a Comment ID");
		searchComment = new JFormattedTextField();
		searchCommentButton = new JButton("Search");
		searchCommentButton.setActionCommand("searchComment");
		hideCommentButton = new JButton("Hide");
		hideCommentButton.setActionCommand("hideComment");
		showCommentButton = new JButton("Show");
		showCommentButton.setActionCommand("showComment");
		

		
		this.privilege = privilege;
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
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
}