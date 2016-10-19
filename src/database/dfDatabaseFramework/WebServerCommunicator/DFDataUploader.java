package database.dfDatabaseFramework.WebServerCommunicator;

import database.DFDatabase;
import database.DFDatabaseCallbackDelegate;
import database.DFError;
import database.dfDatabaseFramework.DFSQL.DFSQL;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static database.DFDatabase.getMethodName;
import static database.DFError.*;

/**
 * Uploads an SQL statement to a remote SQL database
 */
public class DFDataUploader
{
	private final String website;
	private final String writeFile;
	private final String databaseUserName;
    private final String databaseUserPass;

    public DFDataUploader(String website, String writeFile, String databaseUserName, String databaseUserPass)
    {
    	this.website = website;
    	this.writeFile = writeFile;
    	this.databaseUserName = databaseUserName;
    	this.databaseUserPass = databaseUserPass;
    }

    /**
     * Uploads data to the database
     * @param SQLStatement the SQL statement to execute on the web server
     */
	public void uploadDataWith(DFSQL SQLStatement)
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
                    System.out.println("Uploading Data...");
                }
                String urlParameters  = "Password="+ databaseUserPass + "&Username="+ databaseUserName + "&SQLQuery=" + SQLStatement.formattedSQLStatement();
                byte[] postData       = urlParameters.getBytes(StandardCharsets.UTF_8);
                int    postDataLength = postData.length;
                String request        = website + "/" + writeFile;
                URL    url            = new URL(request);
                HttpURLConnection conn= (HttpURLConnection)url.openConnection();
                conn.setDoOutput(true);
                conn.setInstanceFollowRedirects(false);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestProperty("charset", "utf-8");
                conn.setRequestProperty("Content-Length", Integer.toString(postDataLength));
                conn.setUseCaches(false);
                try( DataOutputStream wr = new DataOutputStream(conn.getOutputStream()))
                {
                    wr.write(postData);
                }

                Reader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                StringBuilder sb = new StringBuilder();
                for (int c; (c = in.read()) >= 0;)
                    sb.append((char)c);
                String response = sb.toString();

                if (DFDatabase.defaultDatabase.debug == 1)
                {
                    System.out.println("Data Uploaded! Response: " + response);
                }

                if (Objects.equals(response, ""))
                {
                    Map<String, String> errorInfo = new HashMap<>();
                    errorInfo.put(kMethodName, getMethodName(1));
                    errorInfo.put(kExpandedDescription, "No data was returned from the database.  Response: " + response);
                    errorInfo.put(kURL, website + "/" + writeFile);
                    errorInfo.put(kSQLStatement, SQLStatement.formattedSQLStatement());
                    DFError error = new DFError(1, "No data was returned", errorInfo);
                    delegate.uploadStatus(DFDataUploaderReturnStatus.error, error);
                    DFDatabase.defaultDatabase.delegate = null;
                    return;
                }

                if (response.contains("Success"))
                {
                    delegate.uploadStatus(DFDataUploaderReturnStatus.success, null);
                    DFDatabase.defaultDatabase.delegate = null;
                }
                else
                {
                    delegate.uploadStatus(DFDataUploaderReturnStatus.failure, null);
                    DFDatabase.defaultDatabase.delegate = null;
                }
            }
            catch(NullPointerException | IOException e)
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
                errorInfo.put(kURL, website + "/" + writeFile);
                errorInfo.put(kSQLStatement, SQLStatement.formattedSQLStatement());
                DFError error = new DFError(0, "There was a(n) " + e.getCause() + " error", errorInfo);
                delegate.uploadStatus(DFDataUploaderReturnStatus.error, error);
                DFDatabase.defaultDatabase.delegate = null;
            }
        }).start();
	}
}
