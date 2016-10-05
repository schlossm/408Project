package UI;

import objects.*;

import javax.swing.JPanel;
import javax.swing.JButton;
import java.awt.BorderLayout;
import javax.swing.JOptionPane;

import javax.swing.JLabel;

import java.awt.event.*;

public class Comment extends JPanel implements ActionListener{
	private JButton report;
	
	public JLabel name, com, comID, timestamp;
	
	public Comment(Post p) {
		//this.id = 0; used for id
		this.report = new JButton("Report Comment");
		this.report.setActionCommand("report");
		this.report.addActionListener(this);
		this.setLayout(new BorderLayout());
		
		this.name = new JLabel(p.getPoster() + " says ");
		this.comID = new JLabel(String.valueOf(0));
		this.com = new JLabel(p.getText());
		this.timestamp = new JLabel(p.getTimestamp());
		
		this.add(comID, BorderLayout.PAGE_START);
		this.add(name, BorderLayout.LINE_START);
		this.add(com, BorderLayout.CENTER);
		this.add(timestamp, BorderLayout.LINE_END);
		this.add(report, BorderLayout.PAGE_END);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getActionCommand().equals("report")) {
			JOptionPane.showMessageDialog(this, "Thank you. This comment has been flagged for revision.");
		}
	}
}
