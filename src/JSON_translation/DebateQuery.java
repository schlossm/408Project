package JSON_translation;

import java.util.ArrayList;

import javax.print.attribute.standard.RequestingUserName;

import org.bouncycastle.crypto.MaxBytesExceededException;
import org.omg.CORBA.PUBLIC_MEMBER;

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
import objects.Debate;
import objects.Debate.*;
import objects.Post;
import objects.Post.*;

public class DebateQuery implements DFDatabaseCallbackDelegate, DFNotificationCenterDelegate{
	private  int debateId = 0;
	private  String debateTitle;
	private  String debateText;
	private  String debateStartDate;
	private  String debateEndDate;
	private  ArrayList<Post> debatePosts;
	private JsonObject jsonObject;
	private DFDataUploaderReturnStatus uploadSuccess;
	private boolean getDebateReturn;
	private boolean getMaxDebateId;
	
	public void getDebateByTitle(String debateTitle){	
		DFSQL dfsql = new DFSQL();
		String[] selectedRows = {"debateID", "text", "startDate", "endDate"};
		getDebateReturn = true;
		try {
			dfsql.select(selectedRows).from("Debate").whereEquals("title", debateTitle);
			System.out.println(dfsql.formattedSQLStatement());
			DFDatabase.defaultDatabase.delegate = this;
			DFDatabase.defaultDatabase.execute(dfsql);
			} catch (DFSQLError e1) {
			e1.printStackTrace();
		}
	}
	
	/* Add download all Debates and store in local storage
	 * Add a getCurrentDebateMethod
	 */
	
	public void createNewDebate(String debateTitle, String debateText, String startDate, String endDate){
		DFSQL dfsql = new DFSQL();
		getMaxDebateId = true;
		this.debateTitle = debateTitle;
		debateStartDate = startDate;
		debateEndDate = endDate;
		this.debateText = debateText;
		
		try {
			dfsql.select("MAX(debateID)").from("Debate");
			System.out.println(dfsql.formattedSQLStatement());
			DFDatabase.defaultDatabase.delegate = this;
			DFDatabase.defaultDatabase.execute(dfsql);
			} catch (DFSQLError e1) {
			e1.printStackTrace();
		}
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
		} else if (getMaxDebateId) {
			try {
				 debateId = jsonObject.get("Data").getAsJsonArray().get(0).getAsJsonObject().get("MAX(debateID)").getAsInt() + 1;
			}catch (NullPointerException e2){
				DFNotificationCenter.defaultCenter.post(UIStrings.debateCreated, Boolean.FALSE);			}
			uploadNewDebateToDatabase(debateId);
		}
		resetBooleans();
	}
	
	private void uploadNewDebateToDatabase(int debateId){
		boolean isaddSuccess;
		String[] rows = {"debateID", "title", "text", "startDate", "endDate"};
		String[] values = {String.valueOf(debateId), debateTitle, debateText, debateStartDate, debateEndDate};
		DFSQL dfsql = new DFSQL();
		try {
			dfsql.insert("Debate", values, rows);
			System.out.println(dfsql.formattedSQLStatement());
			DFDatabase.defaultDatabase.delegate = this;
			DFDatabase.defaultDatabase.execute(dfsql);
			} catch (DFSQLError e1) {
			e1.printStackTrace();
			isaddSuccess = false;
		}
		resetAttributes();
	}
	
	private void resetBooleans(){
		getDebateReturn = false;
		getMaxDebateId = false;
	}
	private void resetAttributes(){
		debateId = 0;
		debateEndDate = null;
		debatePosts = null;
		debateStartDate = null;
		debateText = null;
		debateTitle = null;
	}
	
	@Override
	public void uploadStatus(DFDataUploaderReturnStatus success, DFError error) {
		// TODO Auto-generated method stub
		if(success == DFDataUploaderReturnStatus.success){
			System.out.println("success uploading this");
			DFNotificationCenter.defaultCenter.post(UIStrings.debateCreated, Boolean.TRUE);
		} else if (success == DFDataUploaderReturnStatus.failure) {
			System.out.println("Failure uploading this");
			DFNotificationCenter.defaultCenter.post(UIStrings.debateCreated, Boolean.FALSE);
		}
		else if(success == DFDataUploaderReturnStatus.error){
			System.out.println("Error uploading this");
			System.out.println(error.code);
			System.out.println(error.description);
			System.out.println(error.userInfo);
			DFNotificationCenter.defaultCenter.post(UIStrings.debateCreated, Boolean.FALSE);
		} else {
			System.out.println("I have no clue!");
		}
	}

	@Override
	public void performActionFor(String notificationName, Object userData) {
		if(notificationName.equals(UIStrings.postsReturned)){
			/*if(userData == null){
				debatePosts = null;
			} else{
				debatePosts = (ArrayList<Post>)userData;
			}*/
			debatePosts = null;
			constructDebateAndPost(debatePosts);
		} else if (notificationName.equals(UIStrings.debateReturned)) {
			Debate debateObject = (Debate)userData;
			System.out.println(debateObject.getTitle());
			System.out.println(debateObject.isOpen());
		}
	}

	private void constructDebateAndPost(ArrayList<Post> debatePosts){
		 Debate debateToBeReturned = new Debate(debateTitle, debatePosts, true);
		 DFNotificationCenter.defaultCenter.post(UIStrings.debateReturned, debateToBeReturned);
		 resetAttributes();
	}
	
	public static void main(String[] args){
		DebateQuery debateQuery = new DebateQuery();
		debateQuery.getDebateByTitle("testDebate");
		//debateQuery.createNewDebate("createTestDebateWithMaxId", "mAX ID IS WORKING NOW", "10/21/2016 12:00 AM", "10/30/2016 12:00 AM");
	}
}
