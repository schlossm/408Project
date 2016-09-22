package database;

import com.google.gson.JsonObject;
import com.sun.istack.internal.Nullable;
import database.dfDatabaseFramework.WebServerCommunicator.DFDataUploaderReturnStatus;

public interface DFDatabaseCallbackDelegate
{
	/*
	 * Called once data is done downloading from the database.
	 * @param error error will be null if call was successful, else it'll contain relevant info.
	 */
	void returnedData(@Nullable JsonObject jsonObject, @Nullable DFError error);
	
	void uploadStatus(@Nullable DFDataUploaderReturnStatus success, @Nullable DFError error);
}
