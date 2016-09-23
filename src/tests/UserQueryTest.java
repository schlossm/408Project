package tests;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import JSON_translation.*;
import JSON_translation.UserQuery.InvalidUserException;
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
		User user = null;
		try {
			user = userQuery.getUser(name);
		} catch (InvalidUserException e) {
			fail("User was not found");
		}
		assertTrue(user != null);
		assertTrue(user.isBanned() == false);
		assertTrue(user.getUserType() == UserType.USER);
		assertTrue(user.getUsername().equals("testuser"));
	}
	
	@Test
	public void testGetUserBanStatus() {
		boolean banStatus = false;
		try {
			banStatus = userQuery.getUserBanStatus(name);
		} catch (InvalidUserException e) {
			fail("Invalid username Entered!");
		}
		assertFalse(banStatus);
	}

	@Test
	public void testGetUserPriv() {
		UserType userType = null;
		try {
			userType = userQuery.getUserPriv(name);
		} catch (InvalidUserException e) {
			fail("Invalid username Entered!");
		}
		assertEquals(userType, UserType.USER);
	}

	@Test
	public void testModifyUserPriv() {
		boolean uploadStatus = userQuery.modifyUserPriv(name, User.UserType.MOD);
		assertTrue(uploadStatus);
		UserType userType = null;
		try {
			userType = userQuery.getUserPriv(name);
		} catch (InvalidUserException e) {
			fail("Invalid username Entered!");
		}
		assertEquals(userType, UserType.MOD);
		uploadStatus = userQuery.modifyUserPriv(name, User.UserType.USER);
		assertTrue(uploadStatus);
	}

	@Test
	public void testUpdateUserBanStatus() {
		boolean uploadStatus = userQuery.updateBanStatus(name, true);
		assertTrue(uploadStatus);
		boolean banStatus = false;
		try {
			banStatus = userQuery.getUserBanStatus(name);
		} catch (InvalidUserException e) {
			fail("Invalid username Entered!");
		}
		assertEquals(banStatus, true);
		uploadStatus = userQuery.updateBanStatus(name, false);
		assertTrue(uploadStatus);
	}
	
	@Test
	public void testInvalidUsernamegetUserBanStatus(){
		try {
			userQuery.getUserBanStatus(invalidName);
		} catch (InvalidUserException e) {
			return;
		}
		fail("Invalid Username Entered and not handled");
	}
	
	@Test
	public void testInvalidAddNewUser(){
		User user = userQuery.addNewUser("naveenTest1", "dasdsada", UserType.USER);
		assertNull(user);
	}
	
	@Test
	public void testVerifyLoginValidUserValidPassword(){
		boolean isPresent = userQuery.verifyUserLogin(name, "blahblah");
		assertTrue(isPresent);
	}
	
	@Test
	public void testVerifyLoginInvalidUser(){
		boolean isPresent = userQuery.verifyUserLogin(invalidName, "blahblah");
		assertFalse(isPresent);
	}
	
	@Test
	public void testVerifyLoginValidUserInvalidPassword(){
		boolean isPresent = userQuery.verifyUserLogin(name, "invalidPassword");
		assertFalse(isPresent);
	}
	
	@Test
	public void testAddNewValidUser(){
		User addedUser = userQuery.addNewUser("naveenTest3", "password", UserType.MOD);
		User receivedUser = null;
		try {
			receivedUser = userQuery.getUser("naveenTest3");
		} catch (InvalidUserException e) {
			fail();
		}
		assertEquals(receivedUser.getUserType(), addedUser.getUserType());
		assertEquals(receivedUser.isBanned(), addedUser.isBanned());
		assertEquals(receivedUser.getUsername(), addedUser.getUsername());
	}
	
	@Test
	public void testAddNewInvalidUser(){
		User addedUser = userQuery.addNewUser("naveenTest1", "password", UserType.MOD);
		assertNull(addedUser);
	}
}
