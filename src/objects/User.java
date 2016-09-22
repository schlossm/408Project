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
		User.jsonQuery = new UserQuery();
		
		this.username = username;
		this.userType = User.jsonQuery.getUserPriv(username);
		this.isBanned = User.jsonQuery.getUserBanStatus(username);
	}
	
	/*
	 * Constructor for JSON translation
	 * layer to use for a new user to
	 * pass back to the UI layer
	 */
	public User(String username, UserType userType, boolean isBanned) {
		User.jsonQuery = new UserQuery();
		
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
		if (User.jsonQuery.modifyUserPriv(this.username, UserType.USER))
			this.userType = UserType.USER;
	}
	
	public void makeMod() {
		if (User.jsonQuery.modifyUserPriv(this.username, UserType.MOD))
			this.userType = UserType.MOD;
	}
	
	public void makeAdmin() {
		if (User.jsonQuery.modifyUserPriv(this.username, UserType.ADMIN))
			this.userType = UserType.ADMIN;
	}
	
	public UserType getUserType() {
		return this.userType;
	}
	
	public void ban() {
		if (User.jsonQuery.updateBanStatus(this.username, true))
			this.isBanned = true;
	}
	
	public void unban() {
		if (User.jsonQuery.updateBanStatus(this.username, false))
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
