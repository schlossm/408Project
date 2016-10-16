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

/**
 * Created by michaelschloss on 9/22/16.
 */
public class DFDataUploaderTest implements DFDatabaseCallbackDelegate
{
    @Test
    public void uploadDataWith() throws Exception
    {
        DFSQL statement = new DFSQL().insert("user", new String[]{"mschloss", "blahblah20"}, new String[]{"userID", "password"});
        DFDatabase.defaultDatabase.delegate = this;
        DFDatabase.defaultDatabase.execute(statement);
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