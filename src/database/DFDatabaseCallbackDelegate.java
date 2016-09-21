package database;

import com.google.gson.JsonObject;
import com.sun.istack.internal.Nullable;

import database.dfDatabaseFramework.WebServerCommunicator.DFDataUploaderReturnStatus;

public interface DFDatabaseCallbackDelegate
{
	/*
	 * Called once data is done downloading from or uploading to the database.
	 * @param error error will be null if call was successful, else it'll contain relevant info.
	 */
	public void returnedData(@Nullable JsonObject jsonObject, @Nullable DFError error);
	
	public void uploadStatus(@Nullable DFDataUploaderReturnStatus success, @Nullable DFError error);
}
