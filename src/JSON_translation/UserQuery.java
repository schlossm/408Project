package JSON_translation;

import org.json.*;
import database.DFDatabase;
import database.dfDatabaseFramework.DFSQL.DFSQL;
import database.dfDatabaseFramework.DFSQL.DFSQLError;
import objects.User;
import objects.User.UserType;

public class UserQuery {
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
	
	public boolean addNewUser(String userName, String pw, UserType userType){
		//INSERT INTO USER VALUES (userName, pw, userTypeInt, isbanned)
		int convertedUserType = userPriviligeEnumToIntConverter(userType);
		String[] rows = {"UserID", "password", "priviligeLevel", "banned"};
		String[] values = {userName, pw,  String.valueOf(convertedUserType), String.valueOf(false)};
		DFSQL dfsql = new DFSQL();
		try {
			dfsql.insert("User", values, rows);
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
	
	public static void main(String[] args)
	{
		System.out.println("This should be starting up here");
		DFSQL dfsql = new DFSQL();
		String[] selectedRows = {"UserID", "priviligeLevel", "banned"};
		try {
			//dfsql.select(selectedRows).from("User").whereEquals("userId", "whatever");
			dfsql.update("User", "userId", "naveen").whereEquals("userid", "naveendup");
			
			System.out.println("Succeeds try");
		} catch (DFSQLError e1) {
			// TODO Auto-generated catch block
			System.out.println("Fails try");
			e1.printStackTrace();
		}
	}
}