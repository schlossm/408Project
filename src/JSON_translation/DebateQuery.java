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
import objects.Debate;
import objects.Post;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class DebateQuery implements DFDatabaseCallbackDelegate, DFNotificationCenterDelegate{
	private  int debateId = 0;
	private  String debateTitle;
	private  String debateText;
	private  String debateStartDate;
	private  String debateEndDate;
	private  ArrayList<Post> debatePosts;
	private JsonObject jsonObject;
	private DFDataUploaderReturnStatus uploadSuccess;
	private boolean getDebateReturn, getMaxDebateId, getArchivedDebatesReturn;
	private HashMap<String, Debate> archivedDebates;
	
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
	
	public void getArchivedDebates(){
		DFSQL dfsql = new DFSQL();
		String[] selectedRows = {"debateID", "text", "startDate", "endDate"};
		getArchivedDebatesReturn = true;
		try {
			dfsql.select(selectedRows).from("Debate");
			System.out.println(dfsql.formattedSQLStatement());
			DFDatabase.defaultDatabase.delegate = this;
			DFDatabase.defaultDatabase.execute(dfsql);
			} catch (DFSQLError e1) {
			e1.printStackTrace();
		}	
	}
	
	/* Add download all Debates and store in local storage
	 * Add a getCurrentDebateMethod
	 * Check for duplicate debate Title.
	 * Use Local Storage
	 * Check for timestamp issue when adding debate
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
		} else if (getArchivedDebatesReturn) {
			
			try {
				for(int i = 0; i < jsonObject.get("Data").getAsJsonArray().size(); i++){
					debateId = jsonObject.get("Data").getAsJsonArray().get(i).getAsJsonObject().get("debateID").getAsInt();
					debateTitle = jsonObject.get("Data").getAsJsonArray().get(i).getAsJsonObject().get("title").getAsString();
					debateText = jsonObject.get("Data").getAsJsonArray().get(i).getAsJsonObject().get("text").getAsString();
					debateStartDate = jsonObject.get("Data").getAsJsonArray().get(i).getAsJsonObject().get("startDate").getAsString();
					debateEndDate = jsonObject.get("Data").getAsJsonArray().get(i).getAsJsonObject().get("endDate").getAsString();
					boolean isCurrentDebate = checkIfCurrentDebate(debateStartDate, debateEndDate);
					Debate debate = new Debate(debateTitle, null, isCurrentDebate);
					archivedDebates.put(debateTitle, debate);
				}
			}catch (NullPointerException e2){
				DFNotificationCenter.defaultCenter.post(UIStrings.debateReturned, null);
			}
		} else if (getMaxDebateId) {
			try {
				 debateId = jsonObject.get("Data").getAsJsonArray().get(0).getAsJsonObject().get("MAX(debateID)").getAsInt() + 1;
			}catch (NullPointerException e2){
				DFNotificationCenter.defaultCenter.post(UIStrings.debateCreated, Boolean.FALSE);			
			}
			uploadNewDebateToDatabase(debateId);
		}
		resetBooleans();
	}
	
	private boolean checkIfCurrentDebate(String startDate, String endDate){
		
		DateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
		Calendar calobj = Calendar.getInstance();
		System.out.println(sdf.format(calobj.getTime()));
			try {
				java.util.Date startingDate = (Date)sdf.parse(startDate);
				java.util.Date endingDate = (Date)sdf.parse(endDate);
				java.util.Date currentDate = calobj.getTime();
				System.out.println(startingDate + " "+ endingDate+ " " + currentDate);
				if(currentDate.before(startingDate)){
					return false;
				} else if(currentDate.after(endingDate)) {
					return false;
				}
			} catch (java.text.ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return true;
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
		//debateQuery.getDebateByTitle("testDebate");
		//debateQuery.createNewDebate("createTestDebateWithMaxId", "mAX ID IS WORKING NOW", "10/21/2016 12:00 AM", "10/30/2016 12:00 AM");
		debateQuery.checkIfCurrentDebate("10/21/2016 12:00 AM", "10/30/2016 12:00 AM");
	}
	
	private class debateHashObject{
		
	}
	
}
