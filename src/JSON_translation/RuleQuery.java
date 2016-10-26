package JSON_translation;

import static database.DFDatabase.queue;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.omg.CORBA.PRIVATE_MEMBER;

import com.google.gson.JsonObject;

import UI.UIStrings;
import UIKit.DFNotificationCenter;
import UIKit.DFNotificationCenterDelegate;
import database.DFDatabase;
import database.DFDatabaseCallbackDelegate;
import database.DFError;
import database.DFSQL.DFSQL;
import database.DFSQL.DFSQLClauseStruct;
import database.DFSQL.DFSQLConjunctionClause;
import database.DFSQL.DFSQLError;
import database.DFSQL.WhereStruct;
import database.WebServer.DFDataUploaderReturnStatus;
import objects.Debate;
import objects.Rule;

public class RuleQuery implements DFDatabaseCallbackDelegate, DFNotificationCenterDelegate {
	private boolean getRuleReturn;
	private JsonObject jsonObject;
	private String title, text;
	private int ruleId;
	private ArrayList<Rule> rules;
	
	public void getAllRules(){
		DFSQL dfsql = new DFSQL();
		String[] selectedRows = {"ruleID", "title", "text"};
		Calendar calobj = Calendar.getInstance();
		Date currentDate = calobj.getTime();
		getRuleReturn = true;
		try {
			dfsql.select(selectedRows).from("Rule");
			System.out.println(dfsql.formattedSQLStatement());
			DFDatabase.defaultDatabase.execute(dfsql, this);
			} catch (DFSQLError e1) {
			e1.printStackTrace();
		}
	}
	@Override
	public void performActionFor(String notificationName, Object userData) {
		// TODO Auto-generated method stub
		
		if(notificationName.equals(UIStrings.ruleReturned)){
		if(userData == null){return;}
		ArrayList<Rule> ruleList = (ArrayList<Rule>) userData;
		for (Rule rule : ruleList) {
			System.out.println();
			System.out.println(rule.getTitle());
			System.out.println(rule.getText());
		}
		}
	}

	private void returnHandler() {
		// TODO Auto-generated method stub
		if(getRuleReturn){
			rules = new ArrayList<Rule>();
			try {
				for(int i = 0; i < jsonObject.get("Data").getAsJsonArray().size(); i++){
					ruleId = jsonObject.get("Data").getAsJsonArray().get(i).getAsJsonObject().get("ruleID").getAsInt();
					title = jsonObject.get("Data").getAsJsonArray().get(i).getAsJsonObject().get("title").getAsString();
					text = jsonObject.get("Data").getAsJsonArray().get(i).getAsJsonObject().get("text").getAsString();
					decryptRules();
					Rule rule = new Rule(ruleId, title, text);
					rules.add(rule);
				}
			} catch (NullPointerException e2) {
				getRuleReturn = false;
				DFNotificationCenter.defaultCenter.post(UIStrings.ruleReturned, rules);
			}
		}
		getRuleReturn = false;
		DFNotificationCenter.defaultCenter.post(UIStrings.ruleReturned, rules);
	}
	
	private void decryptRules() {
		// TODO Auto-generated method stub
		title = DFDatabase.defaultDatabase.decryptString(title);
		text = DFDatabase.defaultDatabase.decryptString(text);
	}
	@Override
	public void returnedData(JsonObject jsonObject, DFError error) {
		// TODO Auto-generated method stub
		this.jsonObject = null;
		if(error != null){
			System.out.println(error.code);
			System.out.println(error.description);
			System.out.println(error.userInfo);
			this.jsonObject = null;
		} else {
			this.jsonObject = jsonObject;
		}
		returnHandler();
	}

	@Override
	public void uploadStatus(DFDataUploaderReturnStatus success, DFError error) {
		// TODO Auto-generated method stub
		
	}
	
	public static void main(String[] args){
		RuleQuery ruleQuery = new RuleQuery();
		ruleQuery.getAllRules();
		while(true) 
		{
		try
		{
			queue.take().run();
		}
		catch (InterruptedException e)
		{
		e.printStackTrace();
		System.exit(-1);
		}
		}
	}
}
