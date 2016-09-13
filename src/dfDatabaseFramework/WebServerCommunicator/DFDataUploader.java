package dfDatabaseFramework.WebServerCommunicator;
import dfDatabaseFramework.DFSQL.*;

public class DFDataUploader 
{
	private String website;
	private String writeFile;
	private String websiteUserName;
    private String websiteUserPass;
    private String databaseUserPass;
	
    public DFDataUploader(String website, String writeFile, String websiteUserName, String websiteUserPass, String databaseUserPass)
    {
    	this.website = website;
    	this.writeFile = writeFile;
    	this.websiteUserName = websiteUserName;
    	this.websiteUserPass = websiteUserPass;
    	this.databaseUserPass = databaseUserPass;
    }
    
	public void uploadDataWith(DFSQL SQLStatement)
	{
		
	}
}
