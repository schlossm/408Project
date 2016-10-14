package objects;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import JSON_translation.*;

public class Post implements Serializable
{
	private String username;
	private String timeStamp;
	private String message;
	private Boolean  isHidden;
	private int numReports;
	private Boolean isFlagged; //
	private int postID; //
	static PostQuery jsonQuery;
	// Utilized by PostQuery class
	public  Post(int postID, String message, String username, String timeStamp, int isFlagged, int isHidden){
		this.username = username;
		this.message = message;
		this.timeStamp = timeStamp;
		this.isHidden =  isHidden == 1 ? true : false;
		this.postID = postID;
		this.isFlagged = isFlagged == 1 ? true : false;
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
	
	public Boolean hidePost() {
		this.isHidden = true;
		return this.isHidden;
		/*
		 * TODO: Update the UI on if post was hidden
		 */
	}
	
	public Boolean showPost() {
		this.isHidden  = false;
		return this.isHidden;
		/*
		 * TODO: Update the UI on if post was shown
		 */
	}



	public int report() {
		this.numReports++;
		return this.numReports;
		/*
 		 * TODO: Message admin/moderators
 		 */
	}

}
