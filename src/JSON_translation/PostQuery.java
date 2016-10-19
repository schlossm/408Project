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
import objects.Post;
import objects.Post.*;

import java.util.ArrayList;

public class PostQuery implements DFDatabaseCallbackDelegate {
	private JsonObject jsonObject;
	private DFDataUploaderReturnStatus uploadSuccess;
	private boolean getDebatePostsReturn;
	private boolean postToDebateReturn;
	private int postID = 0;
	private Post givenPost = null;
	private int givenDebateID = 0;
	private String bufferString;
	
	public void getDebatePosts(int debateID) {
		DFSQL dfsql = new DFSQL();
		String[] selectedRows = {"postID", "message", "username", "timeStamp", "flagged", "isHidden"};
		getDebatePostsReturn = true;
		try {
			dfsql.select(selectedRows).from("Comment").joinOn("DebateComment", "`DebateComment`.postID", "`Comment`.postID").whereEquals("`DebateComment`.debateID", Integer.toString(debateID));
			DFDatabase.defaultDatabase.execute(dfsql);
		} catch (DFSQLError e1) {
			e1.printStackTrace();
		}
	}
	
	/*
	 * Insert Post into Comment
	 * Insert postID and debateID into DebateComment
	 */
	public void postToDebate(Post post, int debateID) {
		DFSQL dfsql = new DFSQL();
		postToDebateReturn = true;
		givenPost = post;
		givenDebateID = debateID;
		
		try {
			dfsql.select("MAX(postID)").from("Comment");
			System.out.println(dfsql.formattedSQLStatement());
			DFDatabase.defaultDatabase.execute(dfsql);
		} catch (DFSQLError e1) {
			e1.printStackTrace();
		}
	}
	
	private void returnHandler(){
		if (getDebatePostsReturn){
			int postIDReceived = 0;
			String messageReceived = null;
			String usernameReceived = null;
			String timeStampReceived = null;
			int flaggedReceived = 0;
			int isHiddenReceived = 0;
			
			ArrayList<Post> posts = new ArrayList<Post>();
			
			try {
				for (int i = 0; i < jsonObject.get("Data").getAsJsonArray().size(); ++i) {
					postIDReceived = jsonObject.get("Data").getAsJsonArray().get(i).getAsJsonObject().get("postID").getAsInt();
					messageReceived = jsonObject.get("Data").getAsJsonArray().get(i).getAsJsonObject().get("message").getAsString();
					usernameReceived = jsonObject.get("Data").getAsJsonArray().get(i).getAsJsonObject().get("username").getAsString();
					timeStampReceived = jsonObject.get("Data").getAsJsonArray().get(i).getAsJsonObject().get("timeStamp").getAsString();
					flaggedReceived = jsonObject.get("Data").getAsJsonArray().get(i).getAsJsonObject().get("flagged").getAsInt();
					isHiddenReceived = jsonObject.get("Data").getAsJsonArray().get(i).getAsJsonObject().get("isHidden").getAsInt();
					
					Post p = new Post(postIDReceived, messageReceived, usernameReceived, timeStampReceived, flaggedReceived, isHiddenReceived);
					posts.add(p);
				}
			} catch (NullPointerException e2){
				DFNotificationCenter.defaultCenter.postNotification(UIStrings.postsReturned, null);				
			}
			
			 DFNotificationCenter.defaultCenter.postNotification(UIStrings.postsReturned, posts);
		} else if (postToDebateReturn) {
			try {
				 postID = jsonObject.get("Data").getAsJsonArray().get(0).getAsJsonObject().get("MAX(postID)").getAsInt() + 1;
			} catch (NullPointerException e2){
				//DFNotificationCenter.defaultCenter.post(UIStrings.debateReturned, null);
			}
			uploadNewPostToDatabase(postID);
		}
		
		getDebatePostsReturn = false;
		postToDebateReturn = false;
		bufferString = null;
	}
	
	private void uploadNewPostToDatabase(int postID){
		int isHidden = givenPost.isHidden() ? 1 : 0;
		String[] rows = {"postID", "message", "userID", "timeStamp", "flagged", "isHidden"};
		String[] values = {String.valueOf(postID), givenPost.getText(), givenPost.getPoster(), givenPost.getTimestamp(), String.valueOf(givenPost.getNumFlags()), String.valueOf(isHidden)};
		DFSQL dfsql = new DFSQL();
		try {
			dfsql.insert("Post", values, rows);
			System.out.println(dfsql.formattedSQLStatement());
			DFDatabase.defaultDatabase.execute(dfsql);
		} catch (DFSQLError e1) {
			e1.printStackTrace();
		}
		
		String[] rows2 = {"debateID", "postID"};
		String[] values2 = {String.valueOf(givenDebateID), String.valueOf(postID)};
		DFSQL dfsql2 = new DFSQL();
		try {
			dfsql2.insert("DebateComment", values2, rows2);
			System.out.println(dfsql2.formattedSQLStatement());
			DFDatabase.defaultDatabase.execute(dfsql2);
		} catch (DFSQLError e1) {
			e1.printStackTrace();
		}
		
		postID = 0;
		givenPost = null;
		givenDebateID = 0;
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
