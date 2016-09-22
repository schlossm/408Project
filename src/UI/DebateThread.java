package UI;

import objects.*;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import java.awt.Dimension;
import java.awt.event.*;
import java.awt.*;

public class DebateThread extends JPanel implements ActionListener{

	public JTextArea comment;
	public JButton postComment, addPoll;
	public JLabel threadTitle, threadDescription, threadAuthor;
	public JPanel commentList;
	public ArrayList<Post> commentArray;
	private Debate d;
	private User u;
	
	public DebateThread(User u, Debate d) {
		this.d = d;
		this.setLayout(new GridBagLayout());
		
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		
		/*threadAuthor = new JLabel();
		c.gridx = 0;
		c.gridy = 0;
		this.add(threadAuthor, c);
		*/
		threadTitle = new JLabel(d.getTitle());
		c.gridx = 1;
		c.gridy = 0;
		this.add(threadTitle, c);
		/*
		threadDescription = new JLabel(d.description());
		c.gridx = 2;
		c.gridy = 0;
		this.add(threadDescription, c);
		*/
		commentList = new JPanel();
		populateComments(d.getPosts());

		if (d.isOpen()) {
			addPoll = new JButton("Add Poll");
			addPoll.setActionCommand("poll");
			addPoll.setEnabled(false);
			c.gridx = 0;
			c.gridy = 2;
			this.add(addPoll, c);
			
			comment = new JTextArea("Write a comment in here");
			c.gridx = 1;
			c.gridy = 2;
			this.add(comment, c);
			
			postComment = new JButton("Submit");
			postComment.setActionCommand("comment");
			postComment.addActionListener(this);
			c.gridx = 2;
			c.gridy = 2;
			this.add(postComment, c);
		}
		else {
			c.gridx = 1;
			c.gridy = 2;
			this.add(new JLabel("Sorry, this debate is closed."), c);
		}
		this.setVisible(true);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getActionCommand().equals("comment")) {
			Post userPost = new Post(u.getUsername(), comment.getText());
			d.post(userPost);
			populateComments(d.getPosts());
		}
	}
	
	private void populateComments(ArrayList<Post> commentArray) {

		commentList.setLayout(new BoxLayout(commentList, BoxLayout.PAGE_AXIS));
		JScrollPane scrollPane = new JScrollPane(commentList);
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridwidth = 3;
		c.gridx = 0;
		c.gridy = 1;
		this.remove(commentList);
		commentList = new JPanel();
		for (int i = 0; i < commentArray.size(); i++) {
			commentList.add(new Comment(commentArray.get(i)));
		}
		this.add(scrollPane, c);
	}
	
}
