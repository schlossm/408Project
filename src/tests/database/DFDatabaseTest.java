package database;

import com.google.gson.JsonObject;
import com.sun.istack.internal.Nullable;
import database.dfDatabaseFramework.DFSQL.DFSQL;
import database.dfDatabaseFramework.WebServerCommunicator.DFDataUploaderReturnStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Created by Michael Schloss on 9/22/16.
 * Hello
 */
public class DFDatabaseTest implements DFDatabaseCallbackDelegate
{
    private DFDatabase database;

    @Before
    public void setUp() throws Exception
    {
        database = DFDatabase.defaultDatabase;
    }

    @After
    public void tearDown() throws Exception
    {
        database = null;
    }

    @Test
    public void executeSQLStatement() throws Exception
    {
        DFSQL statement = new DFSQL().select("userID").from("Users");
        DFDatabase.defaultDatabase.executeSQLStatement(statement, this);
    }

    @Test
    public void encryptString() throws Exception
    {
        String string = "Hello";
        assertTrue(database.encryptString(string).equals(string));
    }

    @Test
    public void decryptString() throws Exception
    {
        String string = "Hello";
        assertTrue(database.decryptString(string).equals(string));
    }

    @Override
    public void returnedData(@Nullable JsonObject jsonObject, @Nullable DFError error)
    {
        assertTrue(jsonObject != null);
        assertTrue(error == null);
    }

    @Override
    public void uploadStatus(@Nullable DFDataUploaderReturnStatus success, @Nullable DFError error)
    {

    }
}