package UI;

public class UIStrings
{
	public static final String success = "success";
	// use if username and password from login is correct
	public static final String failure = "failure";
	// use if username and password from login is incorrect
	public static final String returned = "returned";
	// use to send User object from getUser()
	public static final String exists = "exists";
	// use to check if a username already existing in database (true if exists).
	public static final String postsReturned = "postsReturned";
	public static final String postUploadSuccess = "postsReturned";
	public static final String postUploadFailure = "postsReturned";
	
	// used to listen for debate object to be returned.
	public static final String debateReturned = "debateReturned";
	// used to listen for debate object to be returned.
	public static final String debateCreated = "debateCreated";
	public static final String ruleReturned = "ruleReturned";


	//TIME MANAGER CONSTANTS
	public static final String fiveMinutesHavePassedNotification        = "Five Minutes Have Passed";
	public static final String tenMinutesHavePassedNotification         = "Ten Minutes Have Passed";
	public static final String twentyFiveMinutesHavePassedNotification  = "Twenty Five Minutes Have Passed";
	public static final String oneHourHasPassedNotification             = "One Hour Has Passed";
	public static final String newDayNotification                       = "Midnight Has Passed";
}
