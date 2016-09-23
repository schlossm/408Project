package database.dfDatabaseFramework.WebServerCommunicator;
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
import java.util.Objects;

public class DFDataUploader implements Runnable
{
	private final String website;
	private final String writeFile;
	private final String databaseUserName;
    private final String databaseUserPass;
	private DFSQL statement;
    
    public DFDatabaseCallbackDelegate delegate;
	
    public DFDataUploader(String website, String writeFile, String databaseUserName, String databaseUserPass)
    {
    	this.website = website;
    	this.writeFile = writeFile;
    	this.databaseUserName = databaseUserName;
    	this.databaseUserPass = databaseUserPass;
    }
    
	public void uploadDataWith(DFSQL SQLStatement)
	{
		if (DFDatabase.defaultDatabase.debug == 1)
		{
			System.out.println(SQLStatement.formattedSQLStatement());
		}
		DFDataUploader threadedUploader = new DFDataUploader(website, writeFile, databaseUserName, databaseUserPass);
		threadedUploader.delegate = delegate;
		threadedUploader.statement = SQLStatement;
		new Thread(threadedUploader).start();
	}

	@Override
	public void run() {
		try
		{
			if (DFDatabase.defaultDatabase.debug == 1)
			{
				System.out.println("Uploading Data...");
			}
			String urlParameters  = "Password="+ databaseUserPass + "&Username="+ databaseUserName + "&SQLQuery=" + statement.formattedSQLStatement();
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
				DFError error = new DFError(1, "No data was returned.   Please try again if this is in error.");
				delegate.uploadStatus(DFDataUploaderReturnStatus.error, error);
				return;
			}

			if (response.contains("Success"))
			{
				delegate.uploadStatus(DFDataUploaderReturnStatus.success, null);
			}
			else
			{
				delegate.uploadStatus(DFDataUploaderReturnStatus.failure, null);
			}
		}
		catch(Exception e)
		{
			if (DFDatabase.defaultDatabase.debug == 1)
			{
				e.printStackTrace();
			}
			DFError error = new DFError(0, "There was an unknown error.  Please try again.");
			delegate.uploadStatus(DFDataUploaderReturnStatus.error, error);
		}
	}
}
