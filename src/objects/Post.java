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
	Post createPost(User user, String text){
		Post newPost = new Post(user.getUsername(), text);
		
		return newPost;
	}
	String getPoster(){
		return this.username;
	}
	String getText(){
		return this.message;
	}
	String getTimestamp(){
		return this.timeStamp;
	}
	String makeTimestamp(){
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		 this.timeStamp = dateFormat.format(new Date());
		 return timeStamp;
	}
}
