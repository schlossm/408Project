package database.dfDatabaseFramework.WebServerCommunicator;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import database.DFDatabaseCallbackDelegate;
import database.DFError;
import database.dfDatabaseFramework.DFSQL.DFSQL;

public class DFDataUploader 
{
	private String website;
	private String writeFile;
	private String databaseUserName;
    private String databaseUserPass;
    
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
		try
		{
			String urlParameters  = "Password="+ databaseUserPass + "&Username="+ databaseUserName + "&SQLQuery=" + SQLStatement.formattedSQLStatement;
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
	        
	        if (response == "" ||  response == null)
	        {
	        	DFError error = new DFError(1, "No data was returned.  Please check the SQL Statement delivered and try again.");
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
			DFError error = new DFError(0, "There was an unkown error.  Please check the SQL Statement delivered and try again.");
        	delegate.uploadStatus(DFDataUploaderReturnStatus.error, error);
		}
	}
}
