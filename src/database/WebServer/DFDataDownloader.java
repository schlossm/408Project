package database.WebServer;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import database.DFDatabase;
import database.DFDatabaseCallbackDelegate;
import database.DFError;
import database.DFSQL.DFSQL;

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
import static database.DFDatabase.print;
import static database.DFError.*;
import static database.WebServer.DFWebServerDispatch.*;

/**
 * The downloader class.  Connects securely to the website, and uploads a POST statement containing the data required to download from the database
 */
class DFDataDownloader
{
    /**
     * Downloads data from the database
     * @param SQLStatement the SQL statement to execute on the web server
     */
    void downloadDataWith(DFSQL SQLStatement, DFDatabaseCallbackDelegate delegate)
	{
		if (DFDatabase.defaultDatabase.debug == 1)
		{
			print(SQLStatement.formattedSQLStatement());
		}
		new Thread(() ->
        {
            try
            {
                if (DFDatabase.defaultDatabase.debug == 1)
                {
                    print("Downloading Data...");
                }
                String urlParameters = "Password=" + databaseUserPass + "&Username=" + websiteUserName + "&SQLQuery=" + SQLStatement.formattedSQLStatement();
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
                DFWebServerDispatch.current.dataSizePrinter.printDataSize(response.length());
                if (DFDatabase.defaultDatabase.debug == 1)
                {
                    print("Data Downloaded!");
                    print(response);
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

                Map<String, String> errorInfo = new HashMap<>();
                errorInfo.put(kMethodName, getMethodName(1));
                errorInfo.put(kExpandedDescription, "A(n) "+ e.getCause() + " Exception was raised.  Setting DFDatabase -debug to 1 will print the stack trace for this error");
                errorInfo.put(kURL, website + "/" + readFile);
                errorInfo.put(kSQLStatement, SQLStatement.formattedSQLStatement());
                DFError error = new DFError(0, "There was a(n) " + e.getCause() + " error", errorInfo);
                delegate.returnedData(null, error);
            }
        }).start();
    }
}
