package objects;
import JSON_translation.*;
import java.io.Serializable;
import java.util.Objects;

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
		try {
			User.jsonQuery = new UserQuery();
		
			if (User.jsonQuery != null) {
				this.username = username;
				this.userType = User.jsonQuery.getUserPriv(username);
				this.isBanned = User.jsonQuery.getUserBanStatus(username);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * Constructor for JSON translation
	 * layer to use for a new user to
	 * pass back to the UI layer
	 */
	public User(String username, UserType userType, boolean isBanned) {
		User.jsonQuery = new UserQuery();
		
		if (User.jsonQuery != null) {
			this.username = username;
			this.userType = userType;
			this.isBanned = isBanned;
		}
	}	
	
	public String getUsername() {
		return this.username;
	}
	
	/*
	 * Demote to normal user status
	 */
	public void makeUser() {
		try {
			if (User.jsonQuery.modifyUserPriv(this.username, UserType.USER))
				this.userType = UserType.USER;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void makeMod() {
		try {
			if (User.jsonQuery.modifyUserPriv(this.username, UserType.MOD))
				this.userType = UserType.MOD;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void makeAdmin() {
		try {
			if (User.jsonQuery.modifyUserPriv(this.username, UserType.ADMIN))
				this.userType = UserType.ADMIN;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public UserType getUserType() {
		return this.userType;
	}
	
	public void ban() {
		try {
			if (User.jsonQuery.updateBanStatus(this.username, true))
				this.isBanned = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void unban() {
		try {
			if (User.jsonQuery.updateBanStatus(this.username, false))
				this.isBanned = false;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public boolean isBanned() {
		return this.isBanned;
	}
	
	public boolean equals(Object o) {
		if (o != null) {
			if (o instanceof User && Objects.equals(this.toString(), o.toString()))
				return true;
		}
		
		return false;
	}
	
	public int hashCode() {
		return (this.username + this.userType + this.isBanned).hashCode();
	}
	
	public String toString() {
		return "username:\t" + this.username + "\n"
				+ "userType:\t" + this.userType + "\n"
				+ "isBanned\t" + this.isBanned;
	}
}
