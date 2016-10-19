package database.dfDatabaseFramework.WebServerCommunicator;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import database.DFDatabase;
import database.DFDatabaseCallbackDelegate;
import database.DFError;
import database.dfDatabaseFramework.DFSQL.DFSQL;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static database.DFDatabase.getMethodName;
import static database.DFError.*;

/**
 * The downloader class.  Connects securely to the website, and uploads a POST statement containing the data required to download from the database
 */
public class DFDataDownloader
{
	private final String website;
	private final String readFile;
	private final String databaseUserName;
    private final String databaseUserPass;
	
    public DFDataDownloader(String website, String readFile, String databaseUserName, String databaseUserPass)
    {
    	this.website = website;
    	this.readFile = readFile;
    	this.databaseUserName = databaseUserName;
    	this.databaseUserPass = databaseUserPass;
    }

    /**
     * Downloads data from the database
     * @param SQLStatement the SQL statement to execute on the web server
     */
	public void downloadDataWith(DFSQL SQLStatement)
	{
		if (DFDatabase.defaultDatabase.debug == 1)
		{
			System.out.println(SQLStatement.formattedSQLStatement());
		}
		new Thread(() ->
        {
            DFDatabaseCallbackDelegate delegate = DFDatabase.defaultDatabase.delegate;
            try
            {
                if (DFDatabase.defaultDatabase.debug == 1)
                {
                    System.out.println("Downloading Data...");
                }
                String urlParameters = "Password=" + databaseUserPass + "&Username=" + databaseUserName + "&SQLQuery=" + SQLStatement.formattedSQLStatement();
                byte[] postData = urlParameters.getBytes(StandardCharsets.UTF_8);
                int postDataLength = postData.length;
                String request = website + "/" + readFile;
                URL url = new URL(request);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setDoOutput(true);
                conn.setInstanceFollowRedirects(false);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestProperty("charset", "utf-8");
                conn.setRequestProperty("Content-Length", Integer.toString(postDataLength));
                conn.setUseCaches(false);
                try (DataOutputStream wr = new DataOutputStream(conn.getOutputStream()))
                {
                    wr.write(postData);
                }

                Reader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                StringBuilder sb = new StringBuilder();
                for (int c; (c = in.read()) >= 0; )
                    sb.append((char) c);
                String response = sb.toString();
                DFDatabase.defaultDatabase.dataSizePrinter.printDataSize(response.length());
                if (DFDatabase.defaultDatabase.debug == 1)
                {
                    System.out.println("Data Downloaded!");
                    System.out.println(response);
                }

                conn.disconnect();

                if (Objects.equals(response, "") || response.contains("No Data"))
                {
                    Map<String, String> errorInfo = new HashMap<>();
                    errorInfo.put(kMethodName, getMethodName(1));
                    errorInfo.put(kExpandedDescription, "No data was returned from the database.  Response: " + response);
                    errorInfo.put(kURL, website + "/" + readFile);
                    errorInfo.put(kSQLStatement, SQLStatement.formattedSQLStatement());
                    DFError error = new DFError(1, "No data was returned", errorInfo);
                    delegate.returnedData(null, error);
                    DFDatabase.defaultDatabase.delegate = null;
                }
                else
                {
                    Gson gsonConverter = new Gson();
                    JsonObject object = gsonConverter.fromJson(response, JsonObject.class);
                    delegate.returnedData(object, null);
                    DFDatabase.defaultDatabase.delegate = null;
                }
            }
            catch (Exception e)
            {
                if (DFDatabase.defaultDatabase.debug == 1)
                {
                    e.printStackTrace();
                }

                if (delegate == null)
                {
                    System.out.println("Callback delegate got set to NULL before arriving at end of thread.  Please make sure you're not calling multiple");
                    return;
                }

                Map<String, String> errorInfo = new HashMap<>();
                errorInfo.put(kMethodName, getMethodName(1));
                errorInfo.put(kExpandedDescription, "A(n) "+ e.getCause() + " Exception was raised.  Setting DFDatabase -debug to 1 will print the stack trace for this error");
                errorInfo.put(kURL, website + "/" + readFile);
                errorInfo.put(kSQLStatement, SQLStatement.formattedSQLStatement());
                DFError error = new DFError(0, "There was a(n) " + e.getCause() + " error", errorInfo);
                delegate.returnedData(null, error);
                DFDatabase.defaultDatabase.delegate = null;
            }
        }).start();
    }
}
