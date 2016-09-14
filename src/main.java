import database.*;

public class main
{
	public static void main(String[] args) 
	{
		DFDatabase database = DFDatabase.defaultDatabase;
		System.out.println(database.decryptString(database.encryptString("How are you")));
	}

}
