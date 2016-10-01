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

public class UserQuery implements DFDatabaseCallbackDelegate{
	private JsonObject jsonObject;
	private DFDataUploaderReturnStatus uploadSuccess;
	private boolean getUserReturn;
	private boolean verifyUserLoginReturn;
	private String bufferString;
	
	public void getUser(String username) {		
		DFSQL dfsql = new DFSQL();
		String[] selectedRows = {"userID", "privilegeLevel", "banned"};
		getUserReturn = true;
		try {
			dfsql.select(selectedRows).from("User").whereEquals("userID", username);
			DFDatabase.defaultDatabase.executeSQLStatement(dfsql, this);
		} catch (DFSQLError e1) {
			e1.printStackTrace();
		}
	}
	
	private void returnHandler(){
		if(getUserReturn){
			String usernameRecieved = null;
			boolean isBanned;
			int isBannedInt = 0, userPrivInt = 0;
			try {
				 usernameRecieved = jsonObject.get("Data").getAsJsonArray().get(0).getAsJsonObject().get("userID").getAsString();
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
		} else if (verifyUserLoginReturn) {
			String databasePassword = "";
			try {
				databasePassword = jsonObject.get("Data").getAsJsonArray().get(0).getAsJsonObject().get("password").getAsString();
			} catch (NullPointerException e2){
				DFNotificationCenter.defaultCenter.postNotification(UIStrings.failure, Boolean.FALSE);
				System.out.println("verifylogin returned nothing");
			}
			if(databasePassword.equals(bufferString)){DFNotificationCenter.defaultCenter.postNotification(UIStrings.success, Boolean.TRUE);System.out.println("verifylogin returned success");}
			else {DFNotificationCenter.defaultCenter.postNotification(UIStrings.failure, Boolean.FALSE);System.out.println("verifylogin returned fail cos paswords dont match");}
		}
		
		getUserReturn = false;
		verifyUserLoginReturn = false;
		bufferString = null;
	}
	
	public boolean getUserBanStatus(String username) throws InvalidUserException{
		boolean isBanned;
		int isBannedInt = 0;
		try {
			DFSQL dfsql = new DFSQL().select("banned").from("User").whereEquals("userID", username);
			DFDatabase.defaultDatabase.executeSQLStatement(dfsql, this);
			isBannedInt = jsonObject.get("Data").getAsJsonArray().get(0).getAsJsonObject().get("banned").getAsInt();
		} catch (DFSQLError e1) {
			e1.printStackTrace();
		} catch (NullPointerException e2){
			throw new InvalidUserException("Invalid User Supplied. User is not in database. Please check the username carefully");
		}
		
		if(isBannedInt == 0){isBanned = false;}
		else {isBanned = true;}
		return isBanned;
	}
		
	public UserType getUserPriv(String username) throws InvalidUserException{
		int userTypeInt = 0;
		try {
			DFSQL dfsql = new DFSQL().select("privilegeLevel").from("User").whereEquals("userID", username);
			DFDatabase.defaultDatabase.executeSQLStatement(dfsql, this);
			userTypeInt = jsonObject.get("Data").getAsJsonArray().get(0).getAsJsonObject().get("privilegeLevel").getAsInt();
		} catch (DFSQLError e1) {
			e1.printStackTrace();
		} catch (NullPointerException e2){
			throw new InvalidUserException("Invalid User Supplied. User is not in database. Please check the username carefully");
		}
		
		return userPriviligeIntToEnumConverter(userTypeInt);
	}
	
	public class InvalidUserException extends Exception{
		public InvalidUserException(String message) {super(message);}
	}
	
	
	public boolean addNewUser(String userName, String pw, UserType userType){
		int convertedUserType = userPriviligeEnumToIntConverter(userType);
		boolean isaddSuccess;
		String[] rows = {"userID", "password", "privilegeLevel", "banned"};
		String[] values = {userName, pw,  String.valueOf(convertedUserType), String.valueOf(0)};
		DFSQL dfsql = new DFSQL();
		try {
			dfsql.insert("User", values, rows);
			System.out.println(dfsql.formattedSQLStatement());
			DFDatabase.defaultDatabase.executeSQLStatement(dfsql, this);
		} catch (DFSQLError e1) {
			e1.printStackTrace();
			isaddSuccess = false;
		}
		if(uploadSuccess == DFDataUploaderReturnStatus.success){ isaddSuccess = true; }
		else{isaddSuccess = false;}
		/*
		if(isaddSuccess){
			return getUser(userName);
		} else {
			return null;
		}*/
		return isaddSuccess;
	}
	
	private int userPriviligeEnumToIntConverter(UserType userType){
		if(userType == UserType.USER)
			return 0;
		else if(userType == UserType.MOD)
			return 1;
		else
			return 2;
	}
	
	private UserType userPriviligeIntToEnumConverter(int userType){
		if(userType == 0)
			return UserType.USER;
		else if(userType == 1)
			return UserType.MOD;
		else
			return UserType.ADMIN;
	}
	
	public boolean modifyUserPriv(String userName, UserType newUserType){
		int convertedUserType = userPriviligeEnumToIntConverter(newUserType);
		DFSQL dfsql = new DFSQL();
		try {
			dfsql.update("User", "privilegeLevel", String.valueOf(convertedUserType)).whereEquals("userID", userName);
			DFDatabase.defaultDatabase.executeSQLStatement(dfsql, this);
		} catch (DFSQLError e1) {
			e1.printStackTrace();
			return false;
		}
		return true;
	}
	
	public boolean updateBanStatus(String userName, boolean newBanStatus){
		DFSQL dfsql = new DFSQL();
		int newbanStatusInt = newBanStatus ? 1 : 0;
		System.out.println(newbanStatusInt);
		try {
			dfsql.update("User", "banned", String.valueOf(newbanStatusInt)).whereEquals("userID", userName);
			DFDatabase.defaultDatabase.executeSQLStatement(dfsql, this); 
		} catch (DFSQLError e1) {
			e1.printStackTrace();
			return false;
		}
		return true;
	}
	
	public void verifyUserLogin(String userName, String password){
		DFSQL dfsql = new DFSQL();
		String[] selectedRows = {"userID", "password"};
		bufferString = password;
		verifyUserLoginReturn = true;
		try {
			dfsql.select(selectedRows).from("User").whereEquals("userID", userName);
			DFDatabase.defaultDatabase.executeSQLStatement(dfsql, this);
		} catch (DFSQLError e1) {
			e1.printStackTrace();
		}
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
	
	public static void main(String[] args)
	{
		UserQuery userQuery = new UserQuery();
		//System.out.println(userQuery.getUserBanStatus("naveenTest1"));
		//userQuery.addNewUser("naveenTest1", "dasdsada", UserType.USER);
		//userQuery.modifyUserPriv("testUser", UserType.USER);
		try{
			System.out.println(userQuery.getUserPriv("naveenTest1"));
		} catch (InvalidUserException e){
			System.out.println("Exception caught");
		}
		//System.out.println(userQuery.verifyUserLogin("naveenTest", "dasdsada"));
		//System.out.println(userQuery.getUserPriv("testUser1212"));
		//userQuery.getUser("testuser");
		System.out.println("end reached");
	}
}