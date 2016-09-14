package dfDatabaseFramework.Utilities;
import java.util.*;

public class DFDataSizePrinter
{
	public static final DFDataSizePrinter current = new DFDataSizePrinter();
	
	private DFDataSizePrinter() { }
	
	public void printDataSize(Integer dataSize)
    {
        Integer startSize = 0;
        
        Double length = new Double(dataSize);
        
        while (length >= 1024.0)
        {
            startSize += 1;
            length /= 1024.0;
        }
        
        StringBuilder sb = new StringBuilder();
        Formatter formatter = new Formatter(sb, Locale.US);
        formatter.format("%.2f", length);
        formatter.close();
        
        String fileSize = "";
        
        switch (startSize)
        {
        case 0:
        	fileSize = "B";
        	break;
        case 1:
        	fileSize = "KB";
        	break;
        case 2:
        	fileSize = "MB";
        	break;
        case 3:
        	fileSize = "GB";
        	break;
        case 4:
        	fileSize = "TB";
        	break;
        case 5:
        	fileSize = "PB";
        	break;
        }
        
        System.out.println("Downloaded Data Size: " + new String(sb) + " " + fileSize);
    }
}
