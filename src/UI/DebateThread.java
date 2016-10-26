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
import JSON_translation.PostQuery;

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
	public PostQuery pq = new PostQuery();
	public JScrollPane scrollPane;
	
	public Frame frame;
	
	public DebateThread(Frame frame) {
		this.frame = frame;
		this.u = frame.user;
		this.setLayout(new BorderLayout());
		
		/*threadAuthor = new JLabel();
		c.gridx = 0;
		c.gridy = 0;
		this.add(threadAuthor, c);
		*/
		threadTitle = new JLabel();
		threadTitle.setForeground(Color.BLUE);

		JPanel topPanel = new JPanel();
		topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.PAGE_AXIS));
		topPanel.add(threadTitle);
		
		//this.add(Box.createVerticalGlue(), c);
		threadDescription = new JLabel();
		
		//this.add(Box.createRigidArea(new Dimension(0, 5)), c);
		topPanel.add(threadDescription);
		
		this.add(topPanel, BorderLayout.PAGE_START);
		
		commentList = new JPanel();
		commentArray = new ArrayList<Post>();
		scrollPane = new JScrollPane(commentList);
		commentList.setLayout(new BoxLayout(commentList, BoxLayout.PAGE_AXIS));
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		this.add(scrollPane, BorderLayout.CENTER);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getActionCommand().equals("comment")) {
			
			Post userPost = new Post(frame.user.getUsername(), comment.getText());
			try {
				d.post(userPost);
				pq.postToDebate(userPost, d.getId());
				JOptionPane.showMessageDialog(this, "Your comment has been submitted.");
				comment.setText("");
				dq.getCurrentDebate();
				populateComments(d.getPosts());
			} catch (Exception exception) {
				JOptionPane.showMessageDialog(this, "An error occurred. Your comment was not submitted", "Error", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	
	private void populateComments(ArrayList<Post> commentArray) {
		for (int i = 0; i < commentArray.size(); i++) {
			if (!commentArray.get(i).isHidden()) {
				if (i >= this.commentArray.size() - 1) {
					commentList.add(new Comment(commentArray.get(i)));
				}
				//System.out.println("i: " + i + " size: " + this.commentArray.size());
			}
		}
		this.commentArray = commentArray;
		//this.remove(1);
	}
	
	public void displayDebate(Debate d) {
		this.d = d;
		JPanel bottomPanel = new JPanel();
		bottomPanel.setLayout(new BorderLayout());
		threadTitle.setText(d.getTitle());
		threadDescription.setText(d.getText());
		
		if (d.getPosts() != null) {
			populateComments(d.getPosts());
		}

		if (d.isOpen() && frame.user.isBanned() == false) {
			addPoll = new JButton("Add Poll");
			addPoll.setActionCommand("poll");
			addPoll.setEnabled(false);
			bottomPanel.add(addPoll, BorderLayout.LINE_START);
			
			comment = new JTextArea("Write a comment in here.");
			bottomPanel.add(comment, BorderLayout.CENTER);
			
			postComment = new JButton("Submit");
			postComment.setActionCommand("comment");
			postComment.addActionListener(this);
			bottomPanel.add(postComment, BorderLayout.LINE_END);
		}
		else if (frame.user.isBanned()) {
			bottomPanel.add(new JLabel("Sorry, your account does not have access to post on this thread."), BorderLayout.CENTER);
		}
		else {
			bottomPanel.add(new JLabel("Sorry, this debate is closed."), BorderLayout.CENTER);
		}
		this.add(bottomPanel, BorderLayout.PAGE_END);
	}
	
	public void displayNoDebate() {
		this.removeAll();
		noThread = new JLabel("A debate is not currently open. Please check again later.");
		this.add(noThread, BorderLayout.CENTER);
	}
	
}
