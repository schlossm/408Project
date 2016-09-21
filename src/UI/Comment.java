package UI;

import javax.swing.JPanel;

import java.awt.BorderLayout;

import javax.swing.JLabel;

public class Comment extends JPanel {
	private int id;
	private String username, comment;
	
	public JLabel name, com, comID;
	
	public Comment(int commentID, String username, String comment) {
		this.id = commentID;
		this.username = username;
		this.comment = comment;
		this.setLayout(new BorderLayout());
		
		this.name = new JLabel(username + " says ");
		this.comID = new JLabel(String.valueOf(id));
		this.com = new JLabel(comment);
		
		this.add(comID, BorderLayout.PAGE_START);
		this.add(name, BorderLayout.LINE_START);
		this.add(com, BorderLayout.CENTER);
	}
	
	public void setID(int id) {
		this.id = id;
	}
	
	public int getID() {
		return id;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setComment(String comment) {
		this.comment = comment;
	}
	
	public String getComment() {
		return comment;
	}
}
