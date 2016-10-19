package database;


import java.util.Map;

/**
 * The error class for DFDatabase.  More powerful than implementing the Exception class
 */
public class DFError
{
	public static String kSQLStatement = "SQL Statement";
	public static String kURL = "URL";
	public static String kMethodName = "Method Name";
	public static String kExpandedDescription = "Expanded Description";

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
	final public Map<String, String> userInfo;
	
	public DFError(int code, String description, Map<String, String> userInfo)
	{
		this.code = code;
		this.description = description;
		this.userInfo = userInfo;
	}
}
