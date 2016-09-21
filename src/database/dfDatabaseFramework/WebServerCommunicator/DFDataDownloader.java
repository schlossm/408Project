package database.dfDatabaseFramework.WebServerCommunicator;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import database.DFDatabaseCallbackDelegate;
import database.DFError;
import database.dfDatabaseFramework.DFSQL.*;

public class DFDataDownloader 
{
	private String website;
	private String readFile;
	private String databaseUserName;
    private String databaseUserPass;
    
    public DFDatabaseCallbackDelegate delegate;
	
    public DFDataDownloader(String website, String readFile, String databaseUserName, String databaseUserPass)
    {
    	this.website = website;
    	this.readFile = readFile;
    	this.databaseUserName = databaseUserName;
    	this.databaseUserPass = databaseUserPass;
    }
    
	public void downloadDataWith(DFSQL SQLStatement)
	{
		try
		{
			String urlParameters 	= "Password="+ databaseUserPass + "&Username="+ databaseUserName + "&SQLQuery=" + SQLStatement.formattedSQLStatement;
			byte[] postData       	= urlParameters.getBytes(StandardCharsets.UTF_8);
			int    postDataLength 	= postData.length;
			String request        	= website + "/" + readFile;
			URL    url            	= new URL(request);
			HttpURLConnection conn	= (HttpURLConnection)url.openConnection();           
			conn.setDoOutput(true);
			conn.setInstanceFollowRedirects(false);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded"); 
			conn.setRequestProperty("charset", "utf-8");
			conn.setRequestProperty("Content-Length", Integer.toString(postDataLength));
			conn.setUseCaches(false);
			try(DataOutputStream wr = new DataOutputStream(conn.getOutputStream())) 
			{
			   wr.write(postData);
			}
			
			Reader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
			StringBuilder sb = new StringBuilder();
	        for (int c; (c = in.read()) >= 0;)
	            sb.append((char)c);
	        String response = sb.toString();
	        
	        if (response == "" ||  response == null || response.contains("No Data"))
	        {
	        	DFError error = new DFError(1, "No data was returned.  Please check the SQL Statement delivered and try again.");
	        	delegate.returnedData(null, error);
	        	return;
	        }
	        
	        Gson gsonConverter = new Gson();
	        JsonObject object = gsonConverter.fromJson(response, JsonObject.class);
	        
	        delegate.returnedData(object, null);
		}
		catch(Exception e)
		{
			DFError error = new DFError(0, "There was an unkown error.  Please check the SQL Statement delivered and try again.");
        	delegate.returnedData(null, error);
		}
	}
}
