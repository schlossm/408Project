package JSON_translation;

import UI.UIStrings;
import UIKit.LocalStorage;
import com.google.gson.JsonObject;
import database.DFDatabase;
import database.DFDatabaseCallbackDelegate;
import database.DFError;
import database.DFSQL.*;
import database.WebServer.DFDataUploaderReturnStatus;
import objects.Debate;
import objects.Post;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import static database.DFDatabase.queue;

@SuppressWarnings("unchecked")
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
	private boolean getCurrentDebateReturn, getMaxDebateId, getArchivedDebatesReturn;
	private HashMap<Integer, Debate> archivedDebates;
	
	public void getCurrentDebate(){	
		DFSQL dfsql = new DFSQL();
		String[] selectedRows = {"debateID", "title", "text", "startDate", "endDate"};
		Calendar calobj = Calendar.getInstance();
		Date currentDate = calobj.getTime();
		getCurrentDebateReturn = true;
		try {
			dfsql.select(selectedRows).from("Debate").whereCustom(new WhereStruct[]{new WhereStruct(DFSQLConjunctionClause.and, DFSQLConjunctionClause.lessThan, new DFSQLClauseStruct("startDate", dateToStringConverter(currentDate))), new WhereStruct(DFSQLConjunctionClause.none, DFSQLConjunctionClause.greaterThan, new DFSQLClauseStruct("endDate", dateToStringConverter(currentDate)))});
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
		archivedDebates = new HashMap<>();
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
		if(!checkForDuplicateDebateTitle(debateTitle)){return;}
		if(!checkForOverLappingDates(startDate, endDate)){return;}
		DFSQL dfsql = new DFSQL();
		getMaxDebateId = true;
		this.debateTitle = debateTitle;
		debateStartDate = startDate;
		debateEndDate = endDate;
		this.debateText = debateText;
		encryptDebateAttributes();
		try {
			dfsql.select("MAX(debateID)").from("Debate");
			System.out.println(dfsql.formattedSQLStatement());
			DFDatabase.defaultDatabase.execute(dfsql, this);
			} catch (DFSQLError e1) {
			e1.printStackTrace();
		}
	}
		
	public boolean checkForOverLappingDates(String startdate, String enddate){
		HashMap<Integer, Debate> cachedDebates = loadFromLocalStorage();
		Date javaStartDate = stringToDateConverter(startdate);
		Date javaEndDate = stringToDateConverter(enddate);
		for (Debate debate : cachedDebates.values()) {
			if(debate.getStartDate().before(javaStartDate) && debate.getEndDate().after(javaStartDate)){
				return false;
			}
			if(debate.getStartDate().before(javaEndDate) && debate.getEndDate().after(javaEndDate)){
				return false;
			}
			if(debate.getStartDate().equals(javaStartDate) || debate.getEndDate().equals(javaEndDate)){
				return false;
			}
			if(debate.getStartDate().after(javaStartDate) && debate.getEndDate().before(javaEndDate)){
				return false;
			}
		}
		return true;
	}
	public boolean checkForDuplicateDebateTitle(String debatetitle) {
		// TODO Auto-generated method stub
		HashMap<Integer, Debate> cachedDebates = loadFromLocalStorage();
		for (Debate debate : cachedDebates.values()) {
			if(debate.getTitle().equals(debatetitle)){
				return false;
			}
		}
		return true;
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
		if(getCurrentDebateReturn){
			try {
				 debateId = jsonObject.get("Data").getAsJsonArray().get(0).getAsJsonObject().get("debateID").getAsInt();
				 debateTitle = jsonObject.get("Data").getAsJsonArray().get(0).getAsJsonObject().get("title").getAsString();
				 debateText = jsonObject.get("Data").getAsJsonArray().get(0).getAsJsonObject().get("text").getAsString();
				 debateStartDate = jsonObject.get("Data").getAsJsonArray().get(0).getAsJsonObject().get("startDate").getAsString();
				 debateEndDate = jsonObject.get("Data").getAsJsonArray().get(0).getAsJsonObject().get("endDate").getAsString();
				 decryptDebateAttributes();
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
					decryptDebateAttributes();
					boolean isCurrentDebate = checkIfCurrentDebate(debateStartDate, debateEndDate);
					Debate debate = new Debate(debateTitle, null, isCurrentDebate, debateText, stringToDateConverter(debateStartDate), stringToDateConverter(debateEndDate), debateId);
					archivedDebates.put(Integer.valueOf(debateId), debate);
				}
				getPostsForArchivedDebates();
				//resetBooleans();

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
			resetBooleans();
		}
		//resetBooleans();
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
			 dateObject = sdf.parse(stringDate);
		} catch (java.text.ParseException e) {
			System.out.println("Error converting string to date");
		}
		return dateObject;
	}
	
	private String dateToStringConverter(Date dateObject){
		DateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
		return sdf.format(dateObject);
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
		}
		resetAttributes();
	}
	
	private void resetBooleans(){
		getCurrentDebateReturn = false;
		getMaxDebateId = false;
		getArchivedDebatesReturn = false;
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
		if(notificationName.equals(UIStrings.postsReturned) &&  getArchivedDebatesReturn){
			if(userData == null){
				debatePosts = null;
			} else{
				debatePosts = (ArrayList<Post>)userData;
			}
			constructDebatesWithPosts(debatePosts);
		}else if (notificationName.equals(UIStrings.postsReturned) && getCurrentDebateReturn) {
			if(userData == null){
				debatePosts = null;
			} else{
				debatePosts = (ArrayList<Post>)userData;
			}
			constructCurrentDebateWithPosts(debatePosts);
		}else if (notificationName.equals(UIStrings.debateReturned)) {
			Debate debateObject = (Debate)userData;
			System.out.println(debateObject.getTitle());
			System.out.println(debateObject.isOpen());
		} /*else if (notificationName.equals(UIStrings.postsReturned)) {
			System.out.println("testing posts and comes out safe.");
			if (userData != null) {
				System.out.println("posts returned");
				ArrayList<Post> posts = (ArrayList<Post>)userData;
				System.out.println(posts.get(0).getText());
			} else {
				System.out.println("null posts returnewd");
			}
		}*/
		resetBooleans();
	}
	
	private void constructCurrentDebateWithPosts(ArrayList<Post> debatePosts) {
		boolean isCurrentDebate = checkIfCurrentDebate(debateStartDate, debateEndDate);
		Debate debate = new Debate(debateTitle, debatePosts, isCurrentDebate, debateText, stringToDateConverter(debateStartDate), stringToDateConverter(debateEndDate), debateId);
		DFNotificationCenter.defaultCenter.post(UIStrings.debateReturned, debate);
	    System.out.println();
	    System.out.println(debate.getId());
	    System.out.println(debate.getTitle());
		System.out.println(debate.getText());
	    System.out.println(debate.getPosts());
		System.out.println(debate.getStartDate());
		System.out.println(debate.getEndDate());
		System.out.println();
		resetAttributes();
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
		 storeToLocalStorage();
	}
	
	private void storeToLocalStorage() {
		LocalStorage.saveObjectToFile(archivedDebates, "cache/archivedDebates.ser");
	}
	
	private HashMap<Integer, Debate> loadFromLocalStorage(){
		return (HashMap<Integer, Debate>) LocalStorage.loadObjectFromFile("cache/archivedDebates.ser");
	}
	
	private void encryptDebateAttributes(){
		debateTitle = DFDatabase.defaultDatabase.encryptString(debateTitle);
		debateText = DFDatabase.defaultDatabase.encryptString(debateText);
	}
	
	private void decryptDebateAttributes(){
		debateTitle = DFDatabase.defaultDatabase.decryptString(debateTitle);
		debateText = DFDatabase.defaultDatabase.decryptString(debateText);
	}

	public void testPostQuery(int debateId){
		PostQuery postQuery = new PostQuery();
		DFNotificationCenter.defaultCenter.register(this, UIStrings.postsReturned);
		postQuery.getDebatePosts(debateId);
	}
	
	private void printHashMap(HashMap<Integer, Debate> debates){
		for (HashMap.Entry<Integer, Debate> entry : debates.entrySet()) {
		    Integer key = entry.getKey();
		    Debate debate = entry.getValue();
		    System.out.println();
		    System.out.println("Debate Id: "+key);
		    System.out.println(debate.getTitle());
			System.out.println(debate.getText());
		    System.out.println(debate.getPosts());
			System.out.println(debate.getStartDate());
			System.out.println(debate.getEndDate());
			System.out.println();
		}
	}
	
	public static void main(String[] args){
		DebateQuery debateQuery = new DebateQuery();
		//debateQuery.getDebateByTitle("testDebate");
		//debateQuery.createNewDebate("createTestDebateWithMaxId", "mAX ID IS WORKING NOW", "10/21/2016 12:00 AM", "10/30/2016 12:00 AM");
		//debateQuery.getArchivedDebates();
		//debateQuery.printHashMap(debateQuery.archivedDebates);
		//Calendar calobj = Calendar.getInstance();
		//Date currentDate = calobj.getTime();
		//System.out.println(debateQuery.dateToStringConverter(currentDate));
		//debateQuery.getCurrentDebate();
		debateQuery.testPostQuery(1);
		//debateQuery.archivedDebates = debateQuery.loadFromLocalStorage();
		//System.out.println(debateQuery.checkForOverLappingDates("10/31/2016 12:00 AM", "11/09/2016 12:00 AM"));
		//debateQuery.printHashMap(debateQuery.archivedDebates);
		//debateQuery.createNewDebate("Loading Current Debate", "This is the most current Debate", "10/21/2016 12:00 AM", "10/29/2016 12:00 AM");
		//debateQuery.createNewDebate("Encryption Check", "Just checking if encrypting and decrypting works", "03/11/2016 12:00 AM", "03/13/2016 12:00 AM");
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
