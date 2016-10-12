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

/**
 * The downloader class.  Connects securely to the website, and uploads a POST statement containing the data required to download from the database
 */
public class DFDataDownloader
{
	private final String website;
	private final String readFile;
	private final String databaseUserName;
    private final String databaseUserPass;

    /**
     * The callback delegate that conforms to DFDatabaseCallbackDelegate
     */
    public DFDatabaseCallbackDelegate delegate;
	
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
            catch (Exception e)
            {
                if (DFDatabase.defaultDatabase.debug == 1)
                {
                    e.printStackTrace();
                }
                DFError error = new DFError(0, "There was an unknown error.  Please try again.  SQL statement delivered: " + SQLStatement.formattedSQLStatement());
                delegate.returnedData(null, error);
            }
        }).start();
    }
}
