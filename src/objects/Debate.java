package objects;
import JSON_translation.*;
import java.io.Serializable;
import java.util.ArrayList;


public class Debate implements Serializable {
	boolean isOpen;
	ArrayList<Post> list;
	String title;
	public Debate(String title){
		this.title = title;
		this.list = new ArrayList<Post>();
		this.isOpen = true;
	}
	public Debate createDebate(String title){
		Debate newDebate = new Debate(title);
		return newDebate;
	}
	public void closeDebate(Debate toBeClosed){
		toBeClosed.isOpen = false;
	}
	public Debate getDebateWithTitle(String title){
		Debate oldDebate;
		/*
		 * TODO: Get old debate from database
		 */
		return oldDebate;
	}
	public ArrayList<Post> getPosts(){
		return this.list;
	}
	public void post(Post post){
		list.add(post);
	}
	public String getTitle()  {
		return this.title;
	}
	public boolean isOpen() {
		return this.isOpen();
	}
}
