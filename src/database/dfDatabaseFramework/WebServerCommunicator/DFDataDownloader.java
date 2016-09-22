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
import java.util.Objects;

public class DFDataDownloader 
{
	private final String website;
	private final String readFile;
	private final String databaseUserName;
    private final String databaseUserPass;
    
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
			System.out.println(SQLStatement.formattedSQLStatement());
			String urlParameters 	= "Password="+ databaseUserPass + "&Username="+ databaseUserName + "&SQLQuery=" + SQLStatement.formattedSQLStatement();
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

			DFDatabase.defaultDatabase.dataSizePrinter.printDataSize(conn.getContentLength());
			Reader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
			StringBuilder sb = new StringBuilder();
	        for (int c; (c = in.read()) >= 0;)
	            sb.append((char)c);
	        String response = sb.toString();
	        
	        if (Objects.equals(response, "") || response.contains("No Data"))
	        {
	        	DFError error = new DFError(1, "No data was returned.  Please try again if this is in error");
	        	delegate.returnedData(null, error);
	        }
	        else
	        {
	        	Gson gsonConverter = new Gson();
	        	JsonObject object = gsonConverter.fromJson(response, JsonObject.class);
	        	delegate.returnedData(object, null);
	        }
		}
		catch(Exception e)
		{
			DFError error = new DFError(0, "There was an unknown error.  Please try again.");
        	delegate.returnedData(null, error);
		}
	}
}
