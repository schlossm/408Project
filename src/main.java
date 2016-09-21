import database.*;
import objects.*;

/*
 * Main.java
 * Michael Schloss
 * Alex Rosenberg
 * 
 * Executable starter function:
 * loads UI among other things
 */
public class main
{
	public static void main(String[] args)
	{
		DFDatabase database = DFDatabase.defaultDatabase;
		System.out.println(database.decryptString(database.encryptString("How are you")));
		
		User testUser = new User("username"); // can be deleted eventually
		System.out.println(testUser.toString()); // ""
	}

}
