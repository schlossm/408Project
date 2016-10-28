package objects;
import JSON_translation.PostQuery;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Post implements Serializable
{
	private String username;
	private String timeStamp;
	private String message;
	private Boolean  isHidden;
	public int numReports;
	private Boolean isFlagged; //
	public int postID; //
	private static PostQuery jsonQuery;
	// Utilized by PostQuery class
	public  Post(int postID, String message, String username, String timeStamp, int isFlagged, int isHidden){
		this.username = username;
		this.message = message;
		this.timeStamp = timeStamp;
		this.isHidden = false;
		this.postID = postID;
		this.isFlagged = isFlagged == 1;
		this.numReports = 0;
	}
	
	public  Post(String username, String text){
		try {
			Post.jsonQuery = new PostQuery();
		
			if(User.jsonQuery != null){
				this.username = username;
				this.message = text;
				this.timeStamp = makeTimestamp();
				this.isHidden =  false;
				this.numReports = 0;
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Post createPost(User user, String text){

		return new Post(user.getUsername(), text);
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
	private String makeTimestamp(){
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		 this.timeStamp = dateFormat.format(new Date());
		 return timeStamp;
	}
	
	public Boolean hidePost() {
		this.isHidden = true;
		return true;
		/*
		 * TODO: Update the UI on if post was hidden
		 */
	}
	
	public Boolean showPost() {
		this.isHidden  = false;
		return false;
		/*
		 * TODO: Update the UI on if post was shown
		 */
	}
	
	public boolean isHidden() {
		return this.isHidden;
	}
	
	public boolean isFlagged() {
		return this.isFlagged;
	}
	
	public int getNumFlags() {
		return this.numReports;
	}



	public int report() {
		this.numReports+=2;
		return this.numReports;
		/*
 		 * TODO: Message admin/moderators
 		 */
	}

}
