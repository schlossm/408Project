package JSON_translation;

import org.json.*;

import com.google.gson.JsonObject;

import database.DFDatabase;
import database.DFDatabaseCallbackDelegate;
import database.DFError;
import database.dfDatabaseFramework.DFSQL.DFSQL;
import database.dfDatabaseFramework.DFSQL.DFSQLError;
import database.dfDatabaseFramework.WebServerCommunicator.DFDataUploaderReturnStatus;
import objects.User;
import objects.User.UserType;

public class UserQuery implements DFDatabaseCallbackDelegate{
	public User getUser(String username){
		String userJSONStr, usernameRecieved;
		UserType userType;
		boolean isBanned;
		User requestedUser = null;
		//select  userID, priviligeLevel, banned from User WHERE UserId = userName 
		//DFDatabase dfDatabase = new DFDatabase();
		DFSQL dfsql = new DFSQL();
		String[] selectedRows = {"UserID", "priviligeLevel", "banned"};
		try {
			dfsql.select(selectedRows).from("User").whereEquals("userId", username);
		} catch (DFSQLError e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		//get UsERpRIVILIGE
		//GET 
		//DFDATABASE.defaultdatabase.executedsplstaement()
		
		userJSONStr = "";
		JSONObject userJSON;
		try{
			userJSON = new JSONObject(userJSONStr);
			usernameRecieved = userJSON.getString("userID");
			isBanned = userJSON.getBoolean("banned");
			switch(userJSON.getInt("priviligeLevel")){
				case 1 : userType = UserType.MOD;
					break;
				case 2 : userType = UserType.ADMIN;
					break;
				default : userType = UserType.USER;
			}
			//requestedUser = new User(usernameRecieved, userType, isBanned);
		} catch (Exception e){
			 
		}
		return requestedUser;
	}
	
	public boolean getUserBanStatus(String username){
		String userJSONStr, usernameRecieved;
		boolean isBanned;
		DFSQL dfsql = new DFSQL();
		try {
			dfsql.select("banned").from("User").whereEquals("userID", username);
			DFDatabase.defaultDatabase.executeSQLStatement(dfsql, this);
		} catch (DFSQLError e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		return true;
	}
		
	//Getter 
	
	public boolean addNewUser(String userName, String pw, UserType userType){
		//INSERT INTO USER VALUES (userName, pw, userTypeInt, isbanned)
		int convertedUserType = userPriviligeEnumToIntConverter(userType);

		String[] rows = {"userID", "password", "privilegeLevel", "banned"};
		String[] values = {userName, pw,  String.valueOf(convertedUserType), String.valueOf(0)};
		DFSQL dfsql = new DFSQL();
		try {
			dfsql.insert("User", values, rows);
			System.out.println(dfsql.formattedSQLStatement);
			DFDatabase.defaultDatabase.executeSQLStatement(dfsql, this);
		} catch (DFSQLError e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return false;
		}
		return true;
	}
	
	public int userPriviligeEnumToIntConverter(UserType userType){
		if(userType == UserType.USER)
			return 0;
		else if(userType == UserType.MOD)
			return 1;
		else
			return 2;
	}
	
	public boolean modifyUserPriv(String userName, UserType newUserType){
		//UPDATE INTO 
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
		System.out.println(error.code);
		System.out.println(error.description);
		System.out.println(error.userInfo);
	}

	@Override
	public void uploadStatus(DFDataUploaderReturnStatus success, DFError error) {
		if(success == DFDataUploaderReturnStatus.success){
			System.out.println("success uploading this");
		} else if (success == DFDataUploaderReturnStatus.failure) {
			System.out.println("Faiylure uploading this");
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
		//userQuery.getUserBanStatus("testUser");
		userQuery.addNewUser("naveenTest1", "dasdsada", UserType.USER);
		System.out.println("end reached");
	}
}