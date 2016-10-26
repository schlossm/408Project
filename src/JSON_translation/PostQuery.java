package JSON_translation;

import UI.UIStrings;
import UIKit.DFNotificationCenter;
import UIKit.DFNotificationCenterDelegate;
import com.google.gson.JsonObject;
import database.DFDatabase;
import database.DFDatabaseCallbackDelegate;
import database.DFError;
import database.DFSQL.DFSQL;
import database.DFSQL.DFSQLError;
import database.WebServer.DFDataUploaderReturnStatus;
import objects.Post;

import java.util.ArrayList;

public class PostQuery implements DFDatabaseCallbackDelegate, DFNotificationCenterDelegate {
	private JsonObject jsonObject;

	private boolean getDebatePostsReturn;
	private boolean postToDebateReturn;
	
	private int postID = 0;
	private Post givenPost = null;
	private int givenDebateID = 0;

	public void getDebatePosts(int debateID) {
		DFSQL dfsql = new DFSQL();
		String[] selectedRows = {"postID", "message", "userID", "timeStamp", "flagged", "isHidden"};
		getDebatePostsReturn = true;
		try {
			dfsql.select(selectedRows).from("Comment").joinOn("DebateComment", "`DebateComment`.postID", "`Comment`.postID").whereEquals("`DebateComment`.debateID", Integer.toString(debateID));
			DFDatabase.defaultDatabase.execute(dfsql, this);
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
			DFDatabase.defaultDatabase.execute(dfsql, this);
		} catch (DFSQLError e1) {
			e1.printStackTrace();
		}
	}
	
	public boolean updateFlags(Post post) {
		DFSQL dfsql = new DFSQL();
		
		try {
			dfsql.update("Comment", "flagged", String.valueOf(post.getNumFlags())).whereEquals("postID", String.valueOf(postID));
			DFDatabase.defaultDatabase.execute(dfsql, this);
		} catch (DFSQLError e1) {
			e1.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	public boolean updateIsHidden(Post post) {
		DFSQL dfsql = new DFSQL();
		int isHidden = post.isHidden() ? 1 : 0;
		
		try {
			dfsql.update("Comment", "isHidden", String.valueOf(isHidden)).whereEquals("postID", String.valueOf(postID));
			DFDatabase.defaultDatabase.execute(dfsql, this);
		} catch (DFSQLError e1) {
			e1.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	private void returnHandler(){
		if (getDebatePostsReturn){
			int postIDReceived;
			String messageReceived;
			String usernameReceived;
			String timeStampReceived;
			int flaggedReceived;
			int isHiddenReceived;
			
			ArrayList<Post> posts = new ArrayList<>();
			
			try {
				for (int i = 0; i < jsonObject.get("Data").getAsJsonArray().size(); ++i) {
					postIDReceived = jsonObject.get("Data").getAsJsonArray().get(i).getAsJsonObject().get("postID").getAsInt();
					messageReceived = jsonObject.get("Data").getAsJsonArray().get(i).getAsJsonObject().get("message").getAsString();
					usernameReceived = jsonObject.get("Data").getAsJsonArray().get(i).getAsJsonObject().get("userID").getAsString();
					timeStampReceived = jsonObject.get("Data").getAsJsonArray().get(i).getAsJsonObject().get("timeStamp").getAsString();
					flaggedReceived = jsonObject.get("Data").getAsJsonArray().get(i).getAsJsonObject().get("flagged").getAsInt();
					isHiddenReceived = jsonObject.get("Data").getAsJsonArray().get(i).getAsJsonObject().get("isHidden").getAsInt();
					
					System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
					
					Post p = new Post(postIDReceived, DFDatabase.defaultDatabase.decryptString(messageReceived), usernameReceived, timeStampReceived, flaggedReceived, isHiddenReceived);
					posts.add(p);
				}
			} catch (NullPointerException e2){
				DFNotificationCenter.defaultCenter.post(UIStrings.postsReturned, null);				
			}
			
			 DFNotificationCenter.defaultCenter.post(UIStrings.postsReturned, posts);
		} else if (postToDebateReturn) {
			try {
				 postID = jsonObject.get("Data").getAsJsonArray().get(0).getAsJsonObject().get("MAX(postID)").getAsInt() + 1;
				 DFNotificationCenter.defaultCenter.post(UIStrings.postUploadSuccess, null);
			} catch (NullPointerException e2){
				DFNotificationCenter.defaultCenter.post(UIStrings.postUploadFailure, null);
			}
			uploadNewPostToDatabase(postID);
		}
		
		getDebatePostsReturn = false;
		postToDebateReturn = false;
	}
	
	private void uploadNewPostToDatabase(int postID){
		int isHidden = givenPost.isHidden() ? 1 : 0;
		String[] rows = {"postID", "message", "userID", "timeStamp", "flagged", "isHidden"};
		String[] values = {String.valueOf(postID), DFDatabase.defaultDatabase.encryptString(givenPost.getText()), givenPost.getPoster(), givenPost.getTimestamp(), String.valueOf(givenPost.getNumFlags()), String.valueOf(isHidden)};
		DFSQL dfsql = new DFSQL();
		try {
			dfsql.insert("Comment", values, rows);
			System.out.println(dfsql.formattedSQLStatement());
			DFDatabase.defaultDatabase.execute(dfsql, this);
		} catch (DFSQLError e1) {
			e1.printStackTrace();
		}
		
		String[] rows2 = {"debateID", "postID"};
		String[] values2 = {String.valueOf(givenDebateID), String.valueOf(postID)};
		DFSQL dfsql2 = new DFSQL();
		try {
			dfsql2.insert("DebateComment", values2, rows2);
			System.out.println(dfsql2.formattedSQLStatement());
			DFDatabase.defaultDatabase.execute(dfsql2, this);
		} catch (DFSQLError e1) {
			e1.printStackTrace();
		}

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
	}
	
	@Override
	@SuppressWarnings("unchecked") public void performActionFor(String notificationName, Object userData) {
		System.out.println("**** PERFORM ACTION FOR WAS CALLED ****");
		if(notificationName.equals(UIStrings.postsReturned)){
			ArrayList<Post> debatePosts;
			if(userData == null) {
				System.out.println("**** RETURNED USERDATA IS NULL ****");
			} else {
				debatePosts = (ArrayList<Post>)userData;
				System.out.println("**** DEBATE POSTS WILL APPEAR BELOW ****\n" + debatePosts);
			}
		}
	}

	@SuppressWarnings("unused") public void testPostQuery(int debateId){
		PostQuery postQuery = new PostQuery();
		DFNotificationCenter.defaultCenter.register(this, UIStrings.postsReturned);
		postQuery.getDebatePosts(debateId);
	}
	
	public static void main(String[] args){
		DebateQuery debateQuery = new DebateQuery();
		debateQuery.testPostQuery(1);
	}
}
