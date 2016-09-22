package database;

import java.util.Dictionary;

public class DFError 
{
	final public int code;
	final public String description;
	final public Dictionary<String, String> userInfo;
	
	public DFError(int code, String description)
	{
		this.code = code;
		this.description = description;
		this.userInfo = null;
	}
	
	public DFError(int code, String description, Dictionary<String, String> userInfo)
	{
		this.code = code;
		this.description = description;
		this.userInfo = userInfo;
	}
}
