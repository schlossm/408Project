package UIKit;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.FileInputStream;
import java.io.ObjectInputStream;

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
	 * Save an individual object to a file (*.ser)
	 */
	public static void saveObjectToFile(Object object, String filename) {
		try {
			FileOutputStream fileOut = new FileOutputStream(filename);
			ObjectOutputStream objOut = new ObjectOutputStream(fileOut);
			
			objOut.writeObject(object);
			objOut.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * Load an individual object from a file (*.ser)
	 * 
	 * Remember to case the result of this method to
	 * the type you are loading its value into
	 * 
	 * Returns null on failure
	 */
	public static Object loadObjectFromFile(String filename) {
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
