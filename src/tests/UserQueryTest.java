package tests;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import JSON_translation.*;
import objects.User;
import objects.User.UserType;

/*
 * UserQueryTest.java
 * Naveen Ganessin
 * 
 * Test the UserQuery class
 */

public class UserQueryTest {
	private static UserQuery userQuery;
	private static String name, invalidName;
	/*
	 * This method is run only once before anything else
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		userQuery = new UserQuery();
	}

	/*
	 * This method runs only once after everything is done
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	/*
	 * This method runs before each test
	 */
	@Before
	public void setUp() throws Exception {
		 name = "testuser";
		 invalidName = "testUsermessedUp";
	}

	/*
	 * This method runs after each test
	 */
	@After
	public void tearDown() throws Exception {
	}

	/*
	 * This is a method to test a method of the
	 * target class
	 */
	@Test
	public void testGetUser() {
		User user = userQuery.getUser(name);
		assertTrue(user != null);
		assertTrue(user.isBanned() == false);
		assertTrue(user.getUserType() == UserType.USER);
		assertTrue(user.getUsername().equals("testuser"));
	}
	
	@Test
	public void testGetUserBanStatus() {
		boolean banStatus = userQuery.getUserBanStatus(name);
		assertFalse(banStatus);
	}

	@Test
	public void testGetUserPriv() {
		UserType userType = userQuery.getUserPriv(name);
		assertEquals(userType, UserType.USER);
	}

	@Test
	public void testModifyUserPriv() {
		boolean uploadStatus = userQuery.modifyUserPriv(name, User.UserType.MOD);
		assertTrue(uploadStatus);
		UserType userType = userQuery.getUserPriv(name);
		assertEquals(userType, UserType.MOD);
		uploadStatus = userQuery.modifyUserPriv(name, User.UserType.USER);
		assertTrue(uploadStatus);
	}

	@Test
	public void testUpdateUserBanStatus() {
		boolean uploadStatus = userQuery.updateBanStatus(name, true);
		assertTrue(uploadStatus);
		boolean userBanStatus = userQuery.getUserBanStatus(name);
		assertEquals(userBanStatus, true);
		uploadStatus = userQuery.updateBanStatus(name, false);
		assertTrue(uploadStatus);
	}
	
	@Test
	public void testInvalidUsernamegetUserBanStatus(){
		UserType userType = userQuery.getUserPriv(invalidName);
		assertNull(userType);
	}
	
	@Test
	public void testInvalidAddNewUser(){
		User user =userQuery.addNewUser("naveenTest1", "dasdsada", UserType.USER);
		assertNull(user);
	}
}
