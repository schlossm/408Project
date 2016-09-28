package tests;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import UIKit.*;
import objects.*;

/*
 * LocalStorageTest.java
 * Alex Rosenberg
 * 
 * Test the LocalStorage class
 */
public class LocalStorageTest {
	static User u;
	static String file;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		u = new User("savedUser", User.UserType.USER, false);
		file = "testSaveObjectToFile.ser";
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testSaveSession() {
		// TODO
	}
	
	@Test
	public void testLoadUser() {
		// TODO
	}
	
	@Test
	public void testLoadDebate() {
		// TODO
	}
	
	@Test
	public void testSaveObjectToFile() {
		// Method is now private, so this line is
		// commented to avoid compilation errors.
		//LocalStorage.saveObjectToFile(u, file);
	}

	@Test
	public void testLoadObjectFromFile() {
		// Method is now private, so these lines are
		// commented to avoid compilation errors.
		//Object obj = LocalStorage.loadObjectFromFile(file);
		//User v = (User) obj;
		//assertEquals(u.toString(), v.toString());
		
		
	}

}
