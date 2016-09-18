package objects;
import JSON_translation.*;

/*
 * User.java
 * Alex Rosenberg
 * 
 * Object that holds user data
 */
public class User {
	private String username;
	public enum UserType {
		USER, MOD, ADMIN
	}
	private UserType userType;
	private boolean isBanned;
	
	public User(String username) {
		/*
		 * TODO: Get values from database
		 */
		this.username = username;
		this.userType = UserType.USER;
		this.isBanned = false;
		
		/*
		 * TODO: handle new user
		 */
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
		this.userType = UserType.USER;
	}
	
	public void makeMod() {
		/*
		 * TODO: Set value in database
		 */
		this.userType = UserType.MOD;
	}
	
	public void makeAdmin() {
		/*
		 * TODO: Set value in database
		 */
		this.userType = UserType.ADMIN;
	}
	
	public UserType getUserType() {
		return this.userType;
	}
	
	public void ban() {
		/*
		 * TODO: set value in database
		 */
		this.isBanned = true;
	}
	
	public void unban() {
		/*
		 * TODO: set value in database
		 */
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
