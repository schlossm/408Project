package tests.database;

import com.google.gson.JsonObject;
import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import database.DFDatabase;
import database.DFDatabaseCallbackDelegate;
import database.DFError;
import database.DFSQL.DFSQL;
import database.DFSQL.DFSQLClauseStruct;
import database.DFSQL.DFSQLError;
import database.WebServer.DFDataUploaderReturnStatus;

import static database.DFDatabase.print;


/**
 *  I Will crash the backend web server for a few minutes if you run this.
 */
public class DatabaseStressClass implements DFDatabaseCallbackDelegate
{

	public static void main(String[] args)
	{
		for (int i = 0; i < 500; i++)
		{
			try
			{
				DFSQL statement = new DFSQL().update("Comment", new DFSQLClauseStruct[]{ new DFSQLClauseStruct("flagged", String.valueOf(i))}).whereEquals("postID", "1");
				statement.append(new DFSQL().update("Comment", new DFSQLClauseStruct[]{ new DFSQLClauseStruct("flagged", "3")}).whereGreaterThan("flagged", "3"));
				print(statement.formattedSQLStatement());
				DFDatabase.defaultDatabase.execute(statement, new DatabaseStressClass());

			}
			catch (DFSQLError dfsqlError)
			{
				dfsqlError.printStackTrace();
			}
		}
	}

	@Override
	public void returnedData(@Nullable JsonObject jsonObject, @Nullable DFError error)
	{

	}

	@Override
	public void uploadStatus(@NotNull DFDataUploaderReturnStatus success, @Nullable DFError error)
	{
		print(success == DFDataUploaderReturnStatus.success ? "Success":"Failure");
	}
}
