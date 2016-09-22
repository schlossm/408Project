package JSON_translation;

import org.json.*;

import com.google.gson.JsonObject;
import com.sun.org.apache.bcel.internal.generic.NEW;

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
	
	public User getUser(String username){		
		DFSQL dfsql = new DFSQL();
		String[] selectedRows = {"userID", "privilegeLevel", "banned"};
		try {
			dfsql.select(selectedRows).from("User").whereEquals("userID", username);
			DFDatabase.defaultDatabase.executeSQLStatement(dfsql, this);
		} catch (DFSQLError e1) {
			e1.printStackTrace();
		}
		
		String usernameRecieved = jsonObject.get("Data").getAsJsonArray().get(0).getAsJsonObject().get("userID").getAsString();
		boolean isBanned = jsonObject.get("Data").getAsJsonArray().get(0).getAsJsonObject().get("banned").getAsBoolean();
		int userPrivInt = jsonObject.get("Data").getAsJsonArray().get(0).getAsJsonObject().get("privilegeLevel").getAsInt();
		UserType userType = userPriviligeIntToEnumConverter(userPrivInt);
		User user = new User(usernameRecieved, userType, isBanned);
		return user;
	}
	
	public boolean getUserBanStatus(String username){
		boolean isBanned;
		try {
			DFSQL dfsql = new DFSQL().select("banned").from("User").whereEquals("userID", username);
			DFDatabase.defaultDatabase.executeSQLStatement(dfsql, this);
		} catch (DFSQLError e1) {
			e1.printStackTrace();
		}		
		isBanned = jsonObject.get("Data").getAsJsonArray().get(0).getAsJsonObject().get("banned").getAsBoolean();

		return isBanned;
	}
		
	public UserType getUserPriv(String username){
		try {
			DFSQL dfsql = new DFSQL().select("privilegeLevel").from("User").whereEquals("userID", username);
			DFDatabase.defaultDatabase.executeSQLStatement(dfsql, this);
		} catch (DFSQLError e1) {
			e1.printStackTrace();
		}
		int userTypeInt = jsonObject.get("Data").getAsJsonArray().get(0).getAsJsonObject().get("privilegeLevel").getAsInt();
		return userPriviligeIntToEnumConverter(userTypeInt);
	}
	
	public User addNewUser(String userName, String pw, UserType userType){
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
		isaddSuccess = true;
		if(isaddSuccess){
			return getUser(userName);
		} else {
			return null;
		}
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
			dfsql.update("User", "priviligeLevel", String.valueOf(convertedUserType)).whereEquals("userID", userName);
		} catch (DFSQLError e1) {
			e1.printStackTrace();
			return false;
		}
		return true;
	}
	
	public boolean updateBanStatus(String userName, boolean newBanStatus){
		DFSQL dfsql = new DFSQL();
		try {
			dfsql.update("User", "banned", String.valueOf(newBanStatus)).whereEquals("userID", userName);
		} catch (DFSQLError e1) {
			e1.printStackTrace();
			return false;
		}
		return true;
	}
	
	public boolean verifyUserLogin(String UserName, String password){
		return false;
	}

	@Override
	public void returnedData(JsonObject jsonObject, DFError error) {
		this.jsonObject = null;
		if(error != null){
			System.out.println(error.code);
			System.out.println(error.description);
			System.out.println(error.userInfo);
		} else {
			this.jsonObject = jsonObject;
		}
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
	
	public static void main(String[] args)
	{
		UserQuery userQuery = new UserQuery();
		System.out.println(userQuery.getUserPriv("naveenTest1"));
		//userQuery.addNewUser("naveenTest1", "dasdsada", UserType.USER);
		userQuery.getUser("testuser");
		System.out.println("end reached");
	}
}