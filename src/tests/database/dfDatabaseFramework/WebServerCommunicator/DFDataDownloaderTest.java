package tests.database.dfDatabaseFramework.WebServerCommunicator;

import com.google.gson.JsonObject;
import com.sun.istack.internal.Nullable;
import database.DFDatabase;
import database.DFDatabaseCallbackDelegate;
import database.DFError;
import database.dfDatabaseFramework.DFSQL.DFSQL;
import database.dfDatabaseFramework.WebServerCommunicator.DFDataUploaderReturnStatus;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by michaelschloss on 9/22/16.
 */
public class DFDataDownloaderTest implements DFDatabaseCallbackDelegate
{
    @Test
    public void downloadDataWith() throws Exception
    {
        DFSQL statement = new DFSQL().select("userID").from("Users");
        DFDatabase.defaultDatabase.delegate = this;
        DFDatabase.defaultDatabase.execute(statement);
    }

    @Override
    public void returnedData(@Nullable JsonObject jsonObject, @Nullable DFError error)
    {
        assertTrue(jsonObject != null);
        assertEquals(error, null);
    }

    @Override
    public void uploadStatus(@Nullable DFDataUploaderReturnStatus success, @Nullable DFError error)
    {

    }
}