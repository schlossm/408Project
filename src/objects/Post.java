package objects;
import JSON_translation.*;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Post implements Serializable {
	private String username;
	private String timeStamp;
	private String message;
	public  Post(String username, String text){
		this.username = username;
		this.message = text;
		this.timeStamp = makeTimestamp();
	}
	public Post createPost(User user, String text){
		Post newPost = new Post(user.getUsername(), text);
		
		return newPost;
	}
	public String getPoster(){
		return this.username;
	}
	public String getText(){
		return this.message;
	}
	public String getTimestamp(){
		return this.timeStamp;
	}
	public String makeTimestamp(){
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		 this.timeStamp = dateFormat.format(new Date());
		 return timeStamp;
	}
	
	public void hidePost() {
		/* Mod/Admin would hide this post
		 * TO DO
		 */
	}
	
	public void showPost() {
		/* Mod/Admin would show this post
		 * TO DO
		 */
	}
	
	public void report() {
		/* Mod/Admin would be notified that this post was reported
		 * TO DO
		 */
	}
	
}
