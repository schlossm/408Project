package database;

import com.google.gson.JsonObject;
import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import database.WebServer.DFDataUploaderReturnStatus;

public interface DFDatabaseCallbackDelegate
{
	/**
	 * Called once data is done downloading from the database.
	 * @param jsonObject jsonObject will contain the json information returned from the database or null if there was an error
	 * @param error error will be null if call was successful, else it'll contain relevant info.
	 */
	void returnedData(@Nullable JsonObject jsonObject, @Nullable DFError error);

	/**
	 * Called once data is done uploading to the database.
	 * @param success will contain either .success, .failure, or .error depending on data status
	 * @param error error will be null if call was successful, else it'll contain relevant info.
	 */
	void uploadStatus(@NotNull DFDataUploaderReturnStatus success, @Nullable DFError error);
}
