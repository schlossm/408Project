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
import javax.swing.JRadioButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.*;

public class Admin extends JPanel implements ActionListener, DFNotificationCenterDelegate {
	public JLabel label1, label2;
	public JFormattedTextField searchUser, searchComment;
	public JButton searchUserButton;
	public JRadioButton user, mod, admin;
	public JPanel topPanel, middlePanel, bottomPanel;
	public User u;
	public Frame frame;
	public UserOptions userOptions;
	
	public Admin(Frame frame) {
		DFNotificationCenter.defaultCenter.register((DFNotificationCenterDelegate) this, "returned");
		
		this.frame = frame;
		u = frame.user;
		
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		this.setLayout(new GridBagLayout());

		// Top Panel
		this.topPanel = new JPanel();
		topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.PAGE_AXIS));
		JScrollPane scroll = new JScrollPane(topPanel);
		
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 3;
		this.add(scroll, c);
		
		// Left Middle Panel
		this.middlePanel = new JPanel();
		middlePanel.setLayout(new BorderLayout());
		
		label1 = new JLabel("Enter a Username");

		searchUser = new JFormattedTextField();
		searchUser.setColumns(36);
		
		searchUserButton = new JButton("Search");
		searchUserButton.setActionCommand("searchUser");
		searchUserButton.addActionListener(this);
		
		middlePanel.add(label1, BorderLayout.PAGE_START);
		middlePanel.add(searchUser, BorderLayout.PAGE_START);
		middlePanel.add(searchUserButton, BorderLayout.PAGE_START);
		
		c.gridx = 1;
		c.gridy = 1;
		c.gridwidth = 1;
		this.add(middlePanel, c);
		
		// Right Middle Panel
		
		//this.rightMiddlePanel = new JPanel();
		//rightMiddlePanel.setLayout(new BorderLayout());
		
		//label2 = new JLabel("Enter a Comment ID");
		/*
		searchComment = new JFormattedTextField();
		searchComment.setColumns(36);
		
		searchCommentButton = new JButton("Search");
		searchCommentButton.setActionCommand("searchComment");
		searchCommentButton.addActionListener(this);
		
		rightMiddlePanel.add(label2, BorderLayout.PAGE_START);
		rightMiddlePanel.add(searchComment, BorderLayout.PAGE_START);
		rightMiddlePanel.add(searchCommentButton, BorderLayout.PAGE_START);
		
		c.gridx = 2;
		c.gridy = 1;
		c.gridwidth = 1;
		this.add(rightMiddlePanel, c);
		*/
		// Bottom Panel
		bottomPanel = new JPanel();
		c.gridx = 1;
		c.gridy = 2;
		c.gridwidth = 1;
		this.add(bottomPanel, c);
		// Edit Rules?
		
		this.setVisible(true);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getActionCommand().equals("searchUser")) {
			frame.uq.getUser(searchUser.getText());
		}
		else if (e.getActionCommand().equals("banUser")) {
			frame.uq.updateBanStatus(u.getUsername(), true);
		}
		else if (e.getActionCommand().equals("unbanUser")) {
			frame.uq.updateBanStatus(u.getUsername(), false);
		}
		else if (e.getActionCommand().equals("setUser")) {
			frame.uq.modifyUserPriv(u.getUsername(), UserType.USER);
		}
		else if (e.getActionCommand().equals("setMod")) {
			frame.uq.modifyUserPriv(u.getUsername(), UserType.MOD);
		}
		else if (e.getActionCommand().equals("setAdmin")) {
			frame.uq.modifyUserPriv(u.getUsername(), UserType.ADMIN);
		}
		else if (e.getActionCommand().equals("searchComment")) {
			
		}
		else if (e.getActionCommand().equals("hideComment")) {
			
		}
		else if (e.getActionCommand().equals("showComment")) {
			
		}
	}
	
	public void populateReports() {
		
	}

	@Override
	public void performActionFor(String notificationName, Object userData) {
		// TODO Auto-generated method stub
		if (notificationName.equals(UIStrings.returned) && frame.listening == false) {
			u = (User) userData;
			//middlePanel.remove(3);
			userOptions = new UserOptions(u);
			if (frame.user.getUserType().equals(UserType.MOD)) {
				userOptions.setTypeDisabled();
			}
			middlePanel.add(userOptions);
		}
	}
	
}
