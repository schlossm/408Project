package objects;

public class Rule {
	private int ruleId;
	private String title;
	private String text;
	
	public Rule(int ruleId, String title, String text){
		this.ruleId = ruleId;
		this.title = title;
		this.text = text;
	}
	
	public int getRuleId() {
		return ruleId;
	}
	public String getText() {
		return text;
	}
	public String getTitle() {
		return title;
	}
}
