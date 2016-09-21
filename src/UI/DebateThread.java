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
	private String title, description, author;
	
	public DebateThread(String title, String description, String author) {
		this.setLayout(new GridBagLayout());
		
		this.title = title;
		this.description = description;
		this.author = author;
		
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		
		threadAuthor = new JLabel(this.author);
		c.gridx = 0;
		c.gridy = 0;
		this.add(threadAuthor, c);
		
		threadTitle = new JLabel(this.title);
		c.gridx = 1;
		c.gridy = 0;
		this.add(threadTitle, c);
		
		threadDescription = new JLabel(this.description);
		c.gridx = 2;
		c.gridy = 0;
		this.add(threadDescription, c);
		
		commentList = new JPanel();
		commentList.setLayout(new BoxLayout(commentList, BoxLayout.PAGE_AXIS));
		JScrollPane scrollPane = new JScrollPane(commentList);
		commentList.add(new Comment(1, "User A", "It may not be the distant future where we have droids like C3PO which definitely seem to be living. Robots even seem to be living more than tiny organisms like jellyfish which only respond to certain things in the environment."));
		commentList.add(new Comment(2, "User B", "I'd like to mention that a living thing should be composed of organic, living organisms. Every animal, plant, bacteria has DNA in their cells. A robot is not made of cells, can not reproduce cells the way living organisms can, and the physical appearance of a robot does not depend on DNA. Therefore, robots should never be considered to be living beings."));
		commentList.add(new Comment(3, "User A", "Fair point, sir."));
		commentList.add(new Comment(4, "User B", "lol"));
		c.gridwidth = 3;
		c.gridx = 0;
		c.gridy = 1;
		this.add(scrollPane, c);

		addPoll = new JButton("Add Poll");
		addPoll.setActionCommand("poll");
		addPoll.setEnabled(false);
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 2;
		this.add(addPoll, c);
		
		comment = new JTextArea("Write a comment in here");
		c.gridx = 1;
		c.gridy = 2;
		this.add(comment, c);
		
		postComment = new JButton("Post Comment");
		postComment.setActionCommand("comment");
		c.gridx = 2;
		c.gridy = 2;
		this.add(postComment, c);
		
		this.setVisible(true);
	}
	
	public void setTitle(String t) {
		title = t;
		threadTitle.setText(title);
	}
	
	public String getTitle() {
		return title;
	}
	
	public void addCommentToPanel(Comment obj) {
		
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
}