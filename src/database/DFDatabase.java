package database;
import dfDatabaseFramework.*;
import dfDatabaseFramework.DFSQL.*;
import dfDatabaseFramework.WebServerCommunicator.*;
import dfDatabaseFramework.Utilities.*;

public class DFDatabase
{
	public static final DFDatabase defaultDatabase = new DFDatabase();
	
	public final DFDataDownloader 	dataDownloader 	= new DFDataDownloader();
	public final DFDataUploader 		dataUploader 	= new DFDataUploader();
	
	private final String website 	= "";
	private final String readFile 	= "ReadFile.php";
	private final String writeFile	= "WriteFile.php";
	
	///If your URL is protected by `https` or is in a password protected directory, `websiteUserName` and `websiteUserPass` are used to login to the directory
    private final String websiteUserName 	= "SMLTiOS";
    ///If your URL is protected by `https` or is in a password protected directory, `websiteUserName` and `websiteUserPass` are used to login to the directory
    private final String websiteUserPass 	= "FtV-tYf-bfA-8a6";
    ///MYSQL databases require user login and password to access the database schema.  MSFramework assumes the login is the same as `websiteUserName` combined with the password
    private final String databaseUserPass	= "FtV-tYf-bfA-8a6";
	
	private final String encryptionKey = "";
	
	public final DFDataSizePrinter dataSizePrinter = new DFDataSizePrinter();
	
	
	
	
	private DFDatabase()
	{
		
	}
}
