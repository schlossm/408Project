
import com.google.gson.JsonObject;
import UI.*;

import database.*;
import database.dfDatabaseFramework.DFSQL.DFSQL;
import database.dfDatabaseFramework.DFSQL.DFSQLError;
import database.dfDatabaseFramework.WebServerCommunicator.DFDataUploaderReturnStatus;
import objects.*;
import UI.Frame;


/**
 * Main.java
 * 
 * Executable starter function:
 * loads UI among other things
 */
public class Main
{
	public static void main(String[] args)
	{
		Frame f = new Frame("School of Thought");
		DFDatabase.mainThread = Thread.currentThread();
	}
}
