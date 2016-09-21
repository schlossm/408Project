package dfDatabaseFramework.WebServerCommunicator;
import dfDatabaseFramework.DFSQL.*;

public class DFDataDownloader 
{
	private String website;
	private String readFile;
	private String websiteUserName;
    private String websiteUserPass;
    private String databaseUserPass;
	
    public DFDataDownloader(String website, String readFile, String websiteUserName, String websiteUserPass, String databaseUserPass)
    {
    	this.website = website;
    	this.readFile = readFile;
    	this.websiteUserName = websiteUserName;
    	this.websiteUserPass = websiteUserPass;
    	this.databaseUserPass = databaseUserPass;
    }
    
	public void downloadDataWith(DFSQL SQLStatement)
	{
		
	}
}
