package UI;

import objects.*;
import objects.User.UserType;
import JSON_translation.*;
import UIKit.DFNotificationCenter;
import UIKit.DFNotificationCenterDelegate;

import javax.swing.JPanel;
import javax.swing.JFormattedTextField;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.*;

public class Admin extends JPanel implements ActionListener, DFNotificationCenterDelegate {
	public JLabel label1, label2, label3;
	public JFormattedTextField searchUser, searchComment, searchDebate;
	public JButton searchUserButton, searchCommentButton;
	public JRadioButton user, mod, admin;
	public JPanel topPanel, middlePanel, middlePanel2, middlePanel3, bottomPanel;
	public User u;
	public Frame frame;
	public UserOptions userOptions;
	public Comment comment;
	public PostQuery pq;
	public boolean listening;
	
	public Admin(Frame frame) {
		DFNotificationCenter.defaultCenter.register((DFNotificationCenterDelegate) this, "returned");
		listening = false;
		
		pq = new PostQuery();
		
		this.frame = frame;
		
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		
		// Top Panel
		/*this.topPanel = new JPanel();
		topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.PAGE_AXIS));
		JScrollPane scroll = new JScrollPane(topPanel);
		this.add(scroll, c);
		*/
		// Top Panel
		this.topPanel = new JPanel();
		topPanel.setLayout(new FlowLayout());
		
		label1 = new JLabel("Enter a Username");

		searchUser = new JFormattedTextField();
		searchUser.setColumns(36);
		searchUser.setText("username");
		
		searchUserButton = new JButton("Search");
		searchUserButton.setActionCommand("searchUser");
		searchUserButton.addActionListener(this);
		
		topPanel.add(label1);
		topPanel.add(searchUser);
		topPanel.add(searchUserButton);
		
		this.add(topPanel);
	
		middlePanel3 = new JPanel();
		
		this.add(middlePanel3);
		// Middle Panel

		middlePanel2 = new JPanel();
		label3 = new JLabel("Enter a Debate ID");
		
		searchDebate = new JFormattedTextField();
		searchDebate.setColumns(36);

		middlePanel2.add(label3);
		middlePanel2.add(searchDebate);
		
		this.add(middlePanel2);
		
		this.middlePanel = new JPanel();
		middlePanel.setLayout(new FlowLayout());
		
		label2 = new JLabel("Enter a Comment ID");
		
		searchComment = new JFormattedTextField();
		searchComment.setColumns(36);
		
		searchCommentButton = new JButton("Search");
		searchCommentButton.setActionCommand("searchComment");
		searchCommentButton.addActionListener(this);
		
		middlePanel.add(label2);
		middlePanel.add(searchComment);
		middlePanel.add(searchCommentButton);
		
		this.add(middlePanel);
		
		// Bottom Panel
		bottomPanel = new JPanel();
		comment = new Comment(null);
		bottomPanel.add(comment);
		this.add(bottomPanel);
		// Edit Rules?
		
		this.setVisible(true);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getActionCommand().equals("searchUser")) {
			frame.uq.getUser(searchUser.getText());
		}
		
		else if (e.getActionCommand().equals("searchComment")) {
			if (!searchDebate.getText().matches("[0-9]+")) {
				JOptionPane.showMessageDialog(this, "Please enter a valid debate ID.", "Error", JOptionPane.ERROR_MESSAGE);
			}
			else if (!searchComment.getText().matches("[0-9]+")) {
				JOptionPane.showMessageDialog(this, "Please enter a valid comment ID.", "Error", JOptionPane.ERROR_MESSAGE);
			}
		}
		else if (e.getActionCommand().equals("hideComment")) {
			//pq.updateIsHidden();
		}
		else if (e.getActionCommand().equals("showComment")) {
			//pq.updateIsHidden()
		}
		frame.repaint();
	}
	
	public void populateReports() {
		
	}

	@Override
	public void performActionFor(String notificationName, Object userData) {
		// TODO Auto-generated method stub
		System.out.println("Listening: " + listening);
		if (notificationName.equals(UIStrings.returned) && listening) {
			u = (User) userData;
			System.out.println("returned");
			if (u == null) {
				System.out.println("null");
				middlePanel3.removeAll();
				JOptionPane.showMessageDialog(this, "This username does not exist", "Error", JOptionPane.ERROR_MESSAGE);
				frame.repaint();
			}
			else {
				userOptions = new UserOptions((User) userData, this);
				
				if (frame.user.getUserType().equals(UserType.MOD)) {
					userOptions.setTypeDisabled();
				}
				middlePanel3.removeAll();
				middlePanel3.add(userOptions);
				frame.repaint();
			}
		}
		else {
			listening = true;
		}
	}
	
}
