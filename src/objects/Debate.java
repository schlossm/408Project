package objects;
import JSON_translation.*;
import java.io.Serializable;
import java.util.ArrayList;

import com.sun.istack.internal.Nullable;
 

public class Debate implements Serializable {
	boolean isOpen;
	ArrayList<Post> list;
	String title;
	
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
	public Debate(String title, ArrayList<Post> list, Boolean isOpen){
		Debate.jsonQuery = new DebateQuery();
		
		if(Debate.jsonQuery != null){
			this.title = title;
			this.list = list;
			this.isOpen = isOpen;
		}
	}
	
	public boolean createDebate(String title){
		boolean debateMade = false;
		try {
			if(Debate.jsonQuery.createNewDebate(title)){
				debateMade = true;
			}
		} catch (Exception e){
			e.printStackTrace();
		}
		return debateMade;
	}
	public void closeDebate(Debate toBeClosed){
		toBeClosed.isOpen = false;
	}
	@Nullable public Debate getDebateWithTitle(String title){
		Debate oldDebate = null;
		try{
			oldDebate = Debate.jsonQuery.getDebatebyTitle(title));
		}catch (Exception e){
			e.printStackTrace();
		}
		return oldDebate;
	}
	public ArrayList<Post> getPosts(){
		return this.list;
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
