package objects;
import JSON_translation.DebateQuery;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
 

public class Debate implements Serializable {
	private boolean isOpen;
	private ArrayList<Post> list;
	private int id;
	private String title;
	private String text;
	private Date startDate;
	private Date endDate;
	
	private static DebateQuery jsonQuery;
	
	public Debate(String title){
		try{
			Debate.jsonQuery = new DebateQuery();
			if(Debate.jsonQuery != null){
				this.title = title;
				this.list = new ArrayList<>();
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
			if(list == null){
				this.list = new ArrayList<>();
			} else {
				this.list = list;
			}
			this.isOpen = isOpen;
			this.text = text;
			this.startDate = startDate;
			this.endDate = endDate;
			this.id = id;
		}
	}

	public void closeDebate(Debate toBeClosed){
		toBeClosed.isOpen = false;
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
	public Date getStartDate(){
		return this.startDate;
	}
	public Date getEndDate(){
		return this.endDate;
	}
	public boolean isOpen() 
	{
		 return this.isOpen;
	}
	public String getText(){
		return this.text;
	}

	public int getId() {
		// TODO Auto-generated method stub
		return this.id;
	}
	
	public Post getPost(int id) {
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).postID == id) {
				return list.get(i);
			}
		}
		return null;
	}
}
