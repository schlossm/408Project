package UI;

import objects.*;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import javax.swing.JTextArea;

import JSON_translation.DebateQuery;

import javax.swing.JOptionPane;
import java.awt.Dimension;
import java.awt.event.*;
import java.awt.*;

public class DebateThread extends JPanel implements ActionListener{

	public JTextArea comment;
	public JButton postComment, addPoll;
	public JLabel threadTitle, threadDescription, threadEnd, noThread;
	public JPanel commentList;
	public ArrayList<Post> commentArray;
	private Debate d;
	private User u;
	public DebateQuery dq = new DebateQuery();
	
	public Frame frame;
	
	public DebateThread(Frame frame) {
		this.frame = frame;
		this.u = frame.user;
		this.setLayout(new GridBagLayout());
		
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		
		/*threadAuthor = new JLabel();
		c.gridx = 0;
		c.gridy = 0;
		this.add(threadAuthor, c);
		*/
		threadTitle = new JLabel();
		threadTitle.setForeground(Color.BLUE);
		c.gridx = 1;
		c.gridy = 0;
		this.add(threadTitle, c);
		this.add(Box.createVerticalGlue(), c);
		threadDescription = new JLabel();
		c.gridx = 1;
		c.gridy = 1;
		//this.add(Box.createRigidArea(new Dimension(0, 5)), c);
		this.add(threadDescription, c);
		
		commentList = new JPanel();

		this.setVisible(true);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getActionCommand().equals("comment")) {
			
			Post userPost = new Post(frame.user.getUsername(), comment.getText());
			try {
				d.post(userPost);
				JOptionPane.showMessageDialog(this, "Your comment has been submitted.");
				comment.setText("");
			} catch (Exception exception) {
				JOptionPane.showMessageDialog(this, "An error occurred. Your comment was not submitted", "Error", JOptionPane.ERROR_MESSAGE);
			}
			populateComments(d.getPosts());
		}
	}
	
	private void populateComments(ArrayList<Post> commentArray) {

		commentList.setLayout(new BoxLayout(commentList, BoxLayout.PAGE_AXIS));
		commentList.removeAll();
		JScrollPane scrollPane = new JScrollPane(commentList);
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridwidth = 3;
		c.gridx = 0;
		c.gridy++;
		//c.gridy = 2;
		for (int i = 0; i < commentArray.size(); i++) {
			if (!commentArray.get(i).isHidden()) {
				commentList.add(new Comment(commentArray.get(i)));
			}
		}
		//this.remove(1);
		this.add(scrollPane, c);
	}
	
	public void displayDebate(Debate d) {
		this.d = d;
		
		System.out.println("Title: " + d.getTitle());
		System.out.println("Description: " + d.getText());
		System.out.println("Start Date: " + d.getStartDate());
		System.out.println("End Date: " + d.getEndDate());
		
		
		threadTitle.setText(d.getTitle());
		threadDescription.setText(d.getText());
		
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		
		if (d.getPosts() != null) {
			populateComments(d.getPosts());
		}
		System.out.println(frame.user.isBanned());
		if (d.isOpen() && frame.user.isBanned() == false) {
			addPoll = new JButton("Add Poll");
			addPoll.setActionCommand("poll");
			addPoll.setEnabled(false);
			c.gridx = 0;
			c.gridy = 3;
			this.add(addPoll, c);
			
			comment = new JTextArea("Write a comment in here.");
			c.gridx = 1;
			c.gridy = 3;
			this.add(comment, c);
			
			postComment = new JButton("Submit");
			postComment.setActionCommand("comment");
			postComment.addActionListener(this);
			c.gridx = 2;
			c.gridy = 3;
			this.add(postComment, c);
		}
		else if (frame.user.isBanned()) {
			c.gridx = 1;
			c.gridy = 3;
			this.add(new JLabel("Sorry, your account does not have access to post on this thread."), c);
		}
		else {
			c.gridx = 1;
			c.gridy = 2;
			this.add(new JLabel("Sorry, this debate is closed."), c);
		}
	}
	
	public void displayNoDebate() {
		this.removeAll();
		noThread = new JLabel("A debate is not currently open. Please check again later.");
		this.add(noThread);
	}
	
}
