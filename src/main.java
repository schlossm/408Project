import database.*;

public class main
{
	public static void main(String[] args) 
	{
		DFDatabase database = DFDatabase.defaultDatabase;
		database.dataSizePrinter.printDataSize(1024);
	}

}
