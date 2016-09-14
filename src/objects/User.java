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
	
	public User(String username) {
		this.username = username;
	}
	
	public String toString() {
		return this.username;
	}
}
