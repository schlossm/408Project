package UI;

import objects.*;

import javax.swing.JPanel;

import JSON_translation.PostQuery;

import javax.swing.JButton;
import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JOptionPane;

import javax.swing.JLabel;

import java.awt.event.*;

public class Comment extends JPanel implements ActionListener{
	private JButton report;
	
	public JLabel name, com, comID, timestamp;
	
	public Post post;
	public PostQuery pq;
	
	public Comment(Post post) {
		//this.id = 0; used for id
		this.pq = new PostQuery();
		if (post != null) {
			this.report = new JButton("Report Comment");
			this.report.setActionCommand("report");
			this.report.addActionListener(this);
			this.setLayout(new BorderLayout());
			
			this.name = new JLabel(post.getPoster() + " says ");
			name.setForeground(Color.GREEN);
			this.comID = new JLabel(Integer.toString(post.postID));
			this.com = new JLabel(post.getText());
			this.timestamp = new JLabel(post.getTimestamp());
			timestamp.setForeground(Color.PINK);
			this.post = post;
			/*
			if (post.isFlagged()) {
				com.setForeground(Color.RED);
			}
			*/
			this.add(comID, BorderLayout.LINE_END);
			this.add(name, BorderLayout.LINE_START);
			this.add(com, BorderLayout.CENTER);
			this.add(timestamp, BorderLayout.PAGE_START);
			this.add(report, BorderLayout.PAGE_END);
			this.setBackground(Color.WHITE);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getActionCommand().equals("report")) {
			JOptionPane.showMessageDialog(this, "Thank you. This comment has been flagged for revision.");
			this.setForeground(Color.RED);
			post.numReports++;
			pq.updateFlags(post);
		}
	}
}
