package UIKit;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.File;

import objects.*;

/*
 * LocalStorage.java
 * Alex Rosenberg
 * 
 * Class used to store objects
 * locally and retrieve them at
 * a later time
 * 
 * NOTE: In order to use these
 * methods, the objects that are
 * to be saved must implement
 * Serializable and import
 * java.io.Serializable.
 * 
 * Also, objects must be read back
 * to their respective variables
 * in the same order they were
 * written in the file.
 * 
 * Serialized Java objects are
 * typically stored using the file
 * extension .ser
 */
public class LocalStorage {
	/*
	 * Save the current session
	 */
	public static void saveSession(User currentUser, Debate currentDebate) {
		// Create cache directory in current location
		File dir = new File("cache");
		dir.mkdir();
		
		// Clear the files
		try {
			PrintWriter upw = new PrintWriter("cache/user.ser");
			upw.close();
			PrintWriter dpw = new PrintWriter("cache/debate.ser");
			dpw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// Save the individual objects
		saveObjectToFile(currentUser, "cache/user.ser");
		saveObjectToFile(currentDebate, "cache/debate.ser");
	}
	
	/*
	 * Load the User
	 */
	public static User loadUser() {
		return (User) loadObjectFromFile("cache/user.ser");
	}
	
	/*
	 * Load the Debate
	 */
	public static Debate loadDebate() {
		return (Debate) loadObjectFromFile("cache/debate.ser");
	}
	
	/*
	 * Save an individual object to a file (*.ser)
	 */
	private static void saveObjectToFile(Object object, String filename) {
		try {
			FileOutputStream fileOut = new FileOutputStream(filename);
			ObjectOutputStream objOut = new ObjectOutputStream(fileOut);
			
			objOut.writeObject(object);
			objOut.close();
			fileOut.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * Load an individual object from a file (*.ser)
	 * 
	 * Remember to cast the result of this method to
	 * the type you are loading its value into
	 * 
	 * Returns null on failure
	 */
	private static Object loadObjectFromFile(String filename) {
		try {
			Object object;
			
			FileInputStream fileIn = new FileInputStream(filename);
			ObjectInputStream objIn = new ObjectInputStream(fileIn);
			
			object = objIn.readObject();
			objIn.close();
			fileIn.close();
			
			return object;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
}
