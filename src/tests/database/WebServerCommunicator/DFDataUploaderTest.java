package tests.database.WebServerCommunicator;


import com.google.gson.JsonObject;
import com.sun.istack.internal.Nullable;
import database.DFDatabase;
import database.DFDatabaseCallbackDelegate;
import database.DFError;
import database.DFSQL.DFSQL;
import database.WebServer.DFDataUploaderReturnStatus;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by michaelschloss on 9/22/16.
 */
@SuppressWarnings("DefaultFileTemplate")
public class DFDataUploaderTest implements DFDatabaseCallbackDelegate
{
    @Test
    public void uploadDataWith() throws Exception
    {
        DFSQL statement = new DFSQL().insert("user", new String[]{"mschloss", "blahblah20"}, new String[]{"userID", "password"});
        DFDatabase.defaultDatabase.execute(statement, this);
    }

    @Override
    public void returnedData(@Nullable JsonObject jsonObject, @Nullable DFError error) {

    }

    @Override
    public void uploadStatus(@Nullable DFDataUploaderReturnStatus success, @Nullable DFError error)
    {
        assertEquals(error, null);
        assertEquals(success, DFDataUploaderReturnStatus.success);
    }
}