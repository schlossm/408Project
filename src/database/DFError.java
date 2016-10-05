package database;

import java.util.Dictionary;

/**
 * The error class for DFDatabase.  More powerful than implementing the Exception class
 */
@SuppressWarnings("unused")
public class DFError
{
	/**
	 * The error code
	 */
	final public int code;
	/**
	 * A human readable description of the error
	 */
	final public String description;
	/**
	 * Optional user information.  Strings only
	 */
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
