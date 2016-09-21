import com.google.gson.JsonObject;

import database.*;
import database.dfDatabaseFramework.DFSQL.DFSQL;
import database.dfDatabaseFramework.DFSQL.DFSQLError;
import database.dfDatabaseFramework.WebServerCommunicator.DFDataUploaderReturnStatus;
import objects.*;

/*
 * Main.java
 * Michael Schloss
 * Alex Rosenberg
 * 
 * Executable starter function:
 * loads UI among other things
 */
public class Main implements DFDatabaseCallbackDelegate
{
	boolean done = false;
	
	public static void main(String[] args)
	{
		new Main().uploadTest();
	}
	
	public void uploadTest()
	{
		DFDatabase database = DFDatabase.defaultDatabase;
		try
		{
			database.executeSQLStatement(new DFSQL().select("userID").from("User"), this);
			
			while (done==false);
		} catch (DFSQLError e) 
		{
			e.printStackTrace();
		}
	}

	@Override
	public void returnedData(JsonObject jsonObject, DFError error)
	{
		if (error != null)
		{
			System.out.println(error.description);
		}
		else
		{
			System.out.println(jsonObject.toString());
		}
		done = true;
	}

	@Override
	public void uploadStatus(DFDataUploaderReturnStatus success, DFError error) 
	{
		
	}

}
