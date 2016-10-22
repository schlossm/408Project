package objects;
import JSON_translation.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import com.sun.istack.internal.Nullable;
 

public class Debate implements Serializable {
	boolean isOpen;
	ArrayList<Post> list;
	int id;
	String title;
	String text;
	Date startDate;
	Date endDate;
	
	static DebateQuery jsonQuery;
	static PostQuery jsonQuery2;
	
	public Debate(String title){
		try{
			Debate.jsonQuery = new DebateQuery();
			if(Debate.jsonQuery != null){
				this.title = title;
				this.list = new ArrayList<Post>();
				this.isOpen = true;
			}
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	
	//Needed for DebateQuery
	public Debate(String title, ArrayList<Post> list, Boolean isOpen, String text, Date startDate, Date endDate, int id){
		Debate.jsonQuery = new DebateQuery();
		
		if(Debate.jsonQuery != null){
			this.title = title;
			this.list = list;
			this.isOpen = isOpen;
			this.text = text;
			this.startDate = startDate;
			this.endDate = endDate;
			this.id = id;
		}
	}
	
	public boolean createDebate(String title, String debateText, String start, String end){
		boolean debateMade = false;
		try {
			Debate.jsonQuery.createNewDebate(title, debateText, start, end);
			debateMade = true;
			}
		 catch (Exception e){
			e.printStackTrace();
		}
		return debateMade;
	}
	public void closeDebate(Debate toBeClosed){
		toBeClosed.isOpen = false;
	}
	public void getDebateWithTitle(String title){
		try{
			Debate.jsonQuery.getDebateByTitle(title);
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	public ArrayList<Post> getPosts(){
		return this.list;
	}
	public void setPosts(ArrayList<Post> list){
		this.list = list;
	}
	public void post(Post post){
		list.add(post);
	}
	public String getTitle() 
	{
		 return this.title;
	}
		 	
	public boolean isOpen() 
	{
		 return this.isOpen;

	}
}
