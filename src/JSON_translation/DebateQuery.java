package JSON_translation;

import java.util.ArrayList;

import javax.print.attribute.standard.RequestingUserName;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.sun.org.apache.bcel.internal.generic.NEW;

import UI.UIStrings;
import UIKit.DFNotificationCenter;
import UIKit.DFNotificationCenterDelegate;
import database.DFDatabase;
import database.DFDatabaseCallbackDelegate;
import database.DFError;
import database.dfDatabaseFramework.DFSQL.DFSQL;
import database.dfDatabaseFramework.DFSQL.DFSQLError;
import database.dfDatabaseFramework.WebServerCommunicator.DFDataUploaderReturnStatus;
import objects.User;
import objects.User.UserType;

import objects.Debate.*;
import objects.Post.*;

public class DebateQuery implements DFDatabaseCallbackDelegate, DFNotificationCenterDelegate{
	private static int debateId = 0;
	private static String debateTitle;
	private static String debateText;
	private static String debateStartDate;
	private static String debateEndDate;
	private static ArrayList<Post> debatePosts;
	private JsonObject jsonObject;
	private DFDataUploaderReturnStatus uploadSuccess;
	private boolean getDebateReturn;
	
	public void getDebatebyTitle(String debateTitle){	
		DFSQL dfsql = new DFSQL();
		String[] selectedRows = {"debateID", "text", "startDate", "endDate"};
		getDebateReturn = true;
		try {
			dfsql.select(selectedRows).from("Debate").whereEquals("title", debateTitle);
			DFDatabase.defaultDatabase.execute(dfsql, this);
		} catch (DFSQLError e1) {
			e1.printStackTrace();
		}
	}
	
	public boolean createNewDebate(){
		return false;
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
	
	private void returnHandler(){
		if(getDebateReturn){
			try {
				 debateId = jsonObject.get("Data").getAsJsonArray().get(0).getAsJsonObject().get("debateID").getAsInt();
				 debateText = jsonObject.get("Data").getAsJsonArray().get(0).getAsJsonObject().get("text").getAsString();
				 debateStartDate = jsonObject.get("Data").getAsJsonArray().get(0).getAsJsonObject().get("startDate").getAsString();
				 debateEndDate = jsonObject.get("Data").getAsJsonArray().get(0).getAsJsonObject().get("endDate").getAsString();
			}catch (NullPointerException e2){
				DFNotificationCenter.defaultCenter.post(UIStrings.debateReturned, null);
			}
			PostQuery postQuery = new PostQuery();
			DFNotificationCenter.defaultCenter.register(this, UIStrings.postsReturned);
			postQuery.getDebatePosts(debateId);
		}
		resetBooleans();
	}
	
	private void resetBooleans(){
		getDebateReturn = false;
		
	}
	
	@Override
	public void uploadStatus(DFDataUploaderReturnStatus success, DFError error) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void performActionFor(String notificationName, Object userData) {
		// TODO Auto-generated method stub
		if(notificationName.equals(UIStrings.postsReturned)){
			if(userData == null){
				debatePosts = null;
			} else{
				debatePosts = userData;
			}
		}
	}

}
