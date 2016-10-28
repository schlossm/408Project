package UI;

import objects.*;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JLabel;

public class Rules extends JPanel {
	public Frame frame;
	public JScrollPane scroll;
	public JPanel mainPanel;

	public Rules(Frame frame) {
		this.frame = frame;
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		this.setVisible(true);
		mainPanel = new JPanel();
		scroll = new JScrollPane(mainPanel);
		this.add(scroll);
	}
	
	public void populateRules(ArrayList<Rule> list) {
		for (int i = 0; i < list.size(); i++) {
			JPanel panel = new JPanel();
			JLabel id = new JLabel(Integer.toString(i+1));
			id.setFont(new Font(null, Font.BOLD, 16));
			panel.add(id);
			
			JLabel ruleTitle = new JLabel(list.get(i).getTitle());
			panel.add(ruleTitle);
			
			mainPanel.add(panel);
			/*
			String r = "";
			int j;
			for (j = 0; j+29 < list.get(i).getText().length(); j+=29) {
				r += list.get(i).getText().substring(j, j+30) + "\n";
			}
			r += list.get(i).getText().substring(j);
			JLabel ruleText = new JLabel(r);
			*/
			JLabel ruleText = new JLabel(list.get(i).getText());
			ruleText.setFont(new Font(null, Font.ITALIC, 16));
			
			
			mainPanel.add(ruleText);
		}
	}
	
}
