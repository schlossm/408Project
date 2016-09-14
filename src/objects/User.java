package objects;
import JSON_translation.*;

public class User {
	private String username;
	
	public User(String username) {
		this.username = username;
	}
	
	public String toString() {
		return this.username;
	}
}
