package JSON_translation;

import UI.UIStrings;
import UIKit.DFNotificationCenter;
import UIKit.DFNotificationCenterDelegate;
import com.google.gson.JsonObject;
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
import objects.Post;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import org.w3c.dom.css.Counter;

public class DebateQuery implements DFDatabaseCallbackDelegate, DFNotificationCenterDelegate{
	private  int debateIdCounter = 0;
	private  int maxDebateId = 0;
	private  int debateId = 0;
	private  String debateTitle;
	private  String debateText;
	private  String debateStartDate;
	private  String debateEndDate;
	private  ArrayList<Post> debatePosts;
	private JsonObject jsonObject;
	private DFDataUploaderReturnStatus uploadSuccess;
	private boolean getDebateReturn, getMaxDebateId, getArchivedDebatesReturn;
	private HashMap<Integer, Debate> archivedDebates;
	
	public void getCurrentDebate(){	
		DFSQL dfsql = new DFSQL();
		String[] selectedRows = {"debateID", "text", "startDate", "endDate"};
		Calendar calobj = Calendar.getInstance();
		Date currentDate = calobj.getTime();
		getDebateReturn = true;
		try {
			new DFSQL().select(selectedRows).from("debate").whereCustom(new WhereStruct[]{new WhereStruct(DFSQLConjunctionClause.and, DFSQLConjunctionClause.lessThan, new DFSQLClauseStruct("startDate", dateToStringConverter(currentDate))), new WhereStruct(DFSQLConjunctionClause.none, DFSQLConjunctionClause.greaterThan, new DFSQLClauseStruct("endDate", dateToStringConverter(currentDate)))});
			System.out.println(dfsql.formattedSQLStatement());
			DFDatabase.defaultDatabase.execute(dfsql, this);
			} catch (DFSQLError e1) {
			e1.printStackTrace();
		}
	}
	
	public void getArchivedDebates(){
		DFSQL dfsql = new DFSQL();
		String[] selectedRows = {"debateID", "title", "text", "startDate", "endDate"};
		getArchivedDebatesReturn = true;
		archivedDebates = new HashMap<Integer, Debate>();
		try {
			dfsql.select(selectedRows).from("Debate");
			System.out.println(dfsql.formattedSQLStatement());
			DFDatabase.defaultDatabase.execute(dfsql, this);
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
			DFDatabase.defaultDatabase.execute(dfsql, this);
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
			System.out.println(jsonObject.get("Data").getAsJsonArray().size());
			try {
				for(int i = 0; i < jsonObject.get("Data").getAsJsonArray().size(); i++){
					debateId = jsonObject.get("Data").getAsJsonArray().get(i).getAsJsonObject().get("debateID").getAsInt();
					debateTitle = jsonObject.get("Data").getAsJsonArray().get(i).getAsJsonObject().get("title").getAsString();
					debateText = jsonObject.get("Data").getAsJsonArray().get(i).getAsJsonObject().get("text").getAsString();
					debateStartDate = jsonObject.get("Data").getAsJsonArray().get(i).getAsJsonObject().get("startDate").getAsString();
					debateEndDate = jsonObject.get("Data").getAsJsonArray().get(i).getAsJsonObject().get("endDate").getAsString();
					boolean isCurrentDebate = checkIfCurrentDebate(debateStartDate, debateEndDate);
					Debate debate = new Debate(debateTitle, null, isCurrentDebate, debateText, stringToDateConverter(debateStartDate), stringToDateConverter(debateEndDate), debateId);
					archivedDebates.put(Integer.valueOf(debateId), debate);
				}
				getPostsForArchivedDebates();
				resetBooleans();

				//Debate sample = archivedDebates.get(1);
				//System.out.println(sample.getTitle());
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
	
	private void getPostsForArchivedDebates(){
		maxDebateId = archivedDebates.size();
		debateIdCounter = 1;
		for(int i = 1; i <= archivedDebates.size(); i++){
			PostQuery postQuery = new PostQuery();
			DFNotificationCenter.defaultCenter.register(this, UIStrings.postsReturned);
			postQuery.getDebatePosts(i);
		}
	}
	
	private boolean checkIfCurrentDebate(String startDate, String endDate){
		Calendar calobj = Calendar.getInstance();
		java.util.Date startingDate = stringToDateConverter(startDate);
		java.util.Date endingDate = stringToDateConverter(endDate);
		java.util.Date currentDate = calobj.getTime();
		System.out.println(startingDate + " "+ endingDate+ " " + currentDate);
		if(currentDate.before(startingDate)){
			return false;
		} else if(currentDate.after(endingDate)) {
			return false;
		}
		return true;
	}
	
	private Date stringToDateConverter(String stringDate){
		DateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
		java.util.Date dateObject = null;
		try {
			 dateObject = (Date)sdf.parse(stringDate);
		} catch (java.text.ParseException e) {
			System.out.println("Error converting string to date");
		}
		return dateObject;
	}
	
	private String dateToStringConverter(Date dateObject){
		DateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
		String dateString = sdf.format(dateObject);
		return dateString;
	}
	
	private void uploadNewDebateToDatabase(int debateId){
		boolean isaddSuccess;
		String[] rows = {"debateID", "title", "text", "startDate", "endDate"};
		String[] values = {String.valueOf(debateId), debateTitle, debateText, debateStartDate, debateEndDate};
		DFSQL dfsql = new DFSQL();
		try {
			dfsql.insert("Debate", values, rows);
			System.out.println(dfsql.formattedSQLStatement());
			DFDatabase.defaultDatabase.execute(dfsql, this);
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
			if(userData == null){
				debatePosts = null;
			} else{
				debatePosts = (ArrayList<Post>)userData;
			}
			constructDebatesWithPosts(debatePosts);
		} else if (notificationName.equals(UIStrings.debateReturned)) {
			Debate debateObject = (Debate)userData;
			System.out.println(debateObject.getTitle());
			System.out.println(debateObject.isOpen());
		}
	}

	private void constructDebatesWithPosts(ArrayList<Post> debatePosts){
		if(debateIdCounter > maxDebateId){
			debateIdCounter = 1; return;
		}
		System.out.println(debateIdCounter + "What is going one~DSAFDSGVASDFGDSG");
		Debate debateAppend = archivedDebates.get(Integer.valueOf(debateIdCounter));
		 debateAppend.setPosts(debatePosts);
		 archivedDebates.replace(debateIdCounter, debateAppend);
		 debateIdCounter++;
		 resetAttributes();
	}
	
	public void testPostQuery(int debateId){
		PostQuery postQuery = new PostQuery();
		DFNotificationCenter.defaultCenter.register(this, UIStrings.postsReturned);
		postQuery.getDebatePosts(debateId);
	}
	
	public static void main(String[] args){
		DebateQuery debateQuery = new DebateQuery();
		//debateQuery.getDebateByTitle("testDebate");
		//debateQuery.createNewDebate("createTestDebateWithMaxId", "mAX ID IS WORKING NOW", "10/21/2016 12:00 AM", "10/30/2016 12:00 AM");
		//debateQuery.getArchivedDebates();
		Calendar calobj = Calendar.getInstance();
		Date currentDate = calobj.getTime();
		System.out.println(debateQuery.dateToStringConverter(currentDate));
		debateQuery.getCurrentDebate();
		//debateQuery.testPostQuery(1);
	}
	
}
