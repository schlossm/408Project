package JSON_translation;

import javax.print.attribute.standard.RequestingUserName;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.sun.org.apache.bcel.internal.generic.NEW;

import UI.UIStrings;
import UIKit.DFNotificationCenter;
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

public class PostQuery implements DFDatabaseCallbackDelegate {
	private JsonObject jsonObject;
	private DFDataUploaderReturnStatus uploadSuccess;
	private boolean getDebatePostsReturn;
	private String bufferString;
	
	public void getDebatePosts(int debateID) {
		DFSQL dfsql = new DFSQL();
		String[] selectedRows = {"postID", "message", "username", "timeStamp", "flagged"};
		getDebatePostsReturn = true;
		try {
			dfsql.select(selectedRows).from("Comment").joinOn("DebateComment", "`DebateComment`.postID", "`Comment`.postID").whereEquals("`DebateComment`.debateID", Integer.toString(debateID));
			DFDatabase.defaultDatabase.executeSQLStatement(dfsql, this);
		} catch (DFSQLError e1) {
			e1.printStackTrace();
		}
	}
	
	private void returnHandler(){
		/*if(getDebatePostsReturn){
			String postIDRecieved = null;
			String messageReceived = null;
			String usernameReceived = null;
			String timeStampReceived = null
			int flaggedReceived = 0;
			
			try {
				 postIDReceived = jsonObject.get("Data").getAsJsonArray().get(0).getAsJsonObject().get("userID").getAsString();
				 isBannedInt = jsonObject.get("Data").getAsJsonArray().get(0).getAsJsonObject().get("banned").getAsInt();
				 userPrivInt = jsonObject.get("Data").getAsJsonArray().get(0).getAsJsonObject().get("privilegeLevel").getAsInt();
			}catch (NullPointerException e2){
				DFNotificationCenter.defaultCenter.postNotification(UIStrings.returned, null);				
			}
			 if(isBannedInt == 0){isBanned = false;}
			 else {isBanned = true;}
			 UserType userType = userPriviligeIntToEnumConverter(userPrivInt);
			 User user = new User(usernameRecieved, userType, isBanned);
			 DFNotificationCenter.defaultCenter.postNotification(UIStrings.returned, user);
		}
		
		getDebatePostsReturn = false;
		bufferString = null;*/
	}
	
	@Override
	public void returnedData(JsonObject jsonObject, DFError error) {
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
		this.uploadSuccess = null;
		if(success == DFDataUploaderReturnStatus.success){
			System.out.println("success uploading this");
		} else if (success == DFDataUploaderReturnStatus.failure) {
			System.out.println("Failure uploading this");
		}
		else if(success == DFDataUploaderReturnStatus.error){
			System.out.println("Error uploading this");
			System.out.println(error.code);
			System.out.println(error.description);
			System.out.println(error.userInfo);
		} else {
			System.out.println("I have no clue!");
		}
		this.uploadSuccess = success;
	}
}
