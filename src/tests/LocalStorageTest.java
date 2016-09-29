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
		User currentUser = new User("testuser");
		Debate currentDebate = new Debate("Debate Title");
		
		LocalStorage.saveSession(currentUser, currentDebate);
	}
	
	@Test
	public void testLoadUser() {
		// NOTE: The matching session must already be saved
		//		 before this test can be run properly.
		//
		//		 (The suite may have to be run twice to
		//		 obtain the correct result otherwise because
		//		 the tests may automatically be run in an
		//		 illogical order.)
		User loadedUser = LocalStorage.loadUser();
		assertEquals(loadedUser.getUsername(), "testuser");
	}
	
	@Test
	public void testLoadDebate() {
		// NOTE: The matching session must already be saved
		//		 before this test can be run properly.
		//
		//		 (The suite may have to be run twice to
		//		 obtain the correct result otherwise because
		//		 the tests may automatically be run in an
		//		 illogical order.)
		Debate loadedDebate = LocalStorage.loadDebate();
		assertEquals(loadedDebate.getTitle(), "Debate Title");
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
