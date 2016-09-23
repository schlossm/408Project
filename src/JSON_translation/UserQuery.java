package JSON_translation;


import com.google.gson.JsonElement;
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
	private DFDataUploaderReturnStatus uploadSuccess;
	
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
		int isBannedInt = jsonObject.get("Data").getAsJsonArray().get(0).getAsJsonObject().get("banned").getAsInt();
		if(isBannedInt == 0){isBanned = false;}
		else {isBanned = true;}
		return isBanned;
	}
		
	public UserType getUserPriv(String username){
		try {
			DFSQL dfsql = new DFSQL().select("privilegeLevel").from("User").whereEquals("userID", username);
			DFDatabase.defaultDatabase.executeSQLStatement(dfsql, this);
		} catch (DFSQLError e1) {
			e1.printStackTrace();
		}
		JsonElement jsonElement = jsonObject.get("Data").getAsJsonArray().get(0).getAsJsonObject().get("privilegeLevel");
		if(jsonElement == null){
			return null;
		}
		int userTypeInt = jsonElement.getAsInt();
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
		if(uploadSuccess == DFDataUploaderReturnStatus.success){ isaddSuccess = true; }
		else{isaddSuccess = false;}

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
		userQuery.addNewUser("naveenTest1", "dasdsada", UserType.USER);
		//userQuery.modifyUserPriv("testUser", UserType.USER);
		//System.out.println(userQuery.getUserPriv("naveenTest1"));
		//System.out.println(userQuery.getUserPriv("testUser1212"));
		//userQuery.getUser("testuser");
		System.out.println("end reached");
	}
}