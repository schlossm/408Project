package UI;

import javax.swing.JPanel;
import javax.swing.JButton;

public class Report extends JPanel {
	public Comment comment;
	public JButton dismiss;
	
	public Report(Comment comment) {
		this.comment = comment;
		this.add(comment);
		this.dismiss = new JButton("Dismiss");
		this.add(comment);
		this.add(dismiss);
	}
}
