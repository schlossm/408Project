package objects;
import JSON_translation.*;
import java.io.Serializable;

/*
 * User.java
 * Alex Rosenberg
 * 
 * Object that holds user data
 */
public class User implements Serializable {
	private String username;
	
	public enum UserType {
		USER, MOD, ADMIN
	}
	
	private UserType userType;
	private boolean isBanned;
	
	static UserQuery jsonQuery;
	
	/*
	 * Constructor for existing user
	 */
	public User(String username) {
		/*
		 * TODO: Get values from database
		 */
		this.jsonQuery = new UserQuery();
		
		this.username = username;
		this.userType = userType.USER; //this.jsonQuery.getUserPriv(username);
		this.isBanned = false; //this.jsonQuery.getUserBanStatus(username);
	}
	
	/*
	 * Constructor for JSON translation
	 * layer to use for a new user to
	 * pass back to the UI layer
	 */
	public User(String username, UserType userType, boolean isBanned) {
		this.jsonQuery = new UserQuery();
		
		this.username = username;
		this.userType = userType;
		this.isBanned = isBanned;
	}
	
	public String getUsername() {
		return this.username;
	}
	
	/*
	 * Demote to normal user status
	 */
	public void makeUser() {
		/*
		 * TODO: Set value in database
		 */
		//if (this.jsonQuery.modifyUserPriv(this.username, UserType.USER))
			this.userType = UserType.USER;
	}
	
	public void makeMod() {
		/*
		 * TODO: Set value in database
		 */
		//if (this.jsonQuery.modifyUserPriv(this.username, UserType.MOD))
			this.userType = UserType.MOD;
	}
	
	public void makeAdmin() {
		/*
		 * TODO: Set value in database
		 */
		//if (this.jsonQuery.modifyUserPriv(this.username, UserType.ADMIN))
			this.userType = UserType.ADMIN;
	}
	
	public UserType getUserType() {
		return this.userType;
	}
	
	public void ban() {
		/*
		 * TODO: set value in database
		 */
		//if (this.jsonQuery.setBanStatus(this.username, true))
			this.isBanned = true;
	}
	
	public void unban() {
		/*
		 * TODO: set value in database
		 */
		//if (this.jsonQuery.setBanStatus(this.username, false))
			this.isBanned = false;
	}
	
	public boolean isBanned() {
		return this.isBanned;
	}
	
	public String toString() {
		return "username:\t" + this.username + "\n"
				+ "userType:\t" + this.userType + "\n"
				+ "isBanned\t" + this.isBanned;
	}
}
