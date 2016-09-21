import javax.swing.JPanel;
import javax.swing.JLabel;

public class Rules extends JPanel {
	public JLabel rules;
	private String htmltext;

	public Rules(String htmltext) {
		rules = new JLabel(htmltext);
		rules.setVisible(true);
		this.add(rules);
		this.setVisible(true);
	}
	
	public void setText(String text) {
		htmltext = text;
		rules.setText(htmltext);
	}
	
	public String getText() {
		return htmltext;
	}
	
}
