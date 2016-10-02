package tests;

import static org.junit.Assert.*;

import javax.net.ssl.SSLException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import JSON_translation.*;
import JSON_translation.UserQuery.InvalidUserException;
import UI.UIStrings;
import UIKit.DFNotificationCenter;
import UIKit.DFNotificationCenterDelegate;
import objects.User;
import objects.User.UserType;

/*
 * UserQueryTest.java
 * Naveen Ganessin
 * 
 * Test the UserQuery class
 */

public class UserQueryTest implements DFNotificationCenterDelegate{
	private static UserQuery userQuery;
	private static String name, invalidName;
	private static User user;
	public static boolean loginSuccess;
	
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
		user = null;
		loginSuccess = false;
	}

	/*
	 * This is a method to test a method of the
	 * target class
	 */
	@Test
	public void testGetUser() {
		user = null;
		
		userQuery.getUser(name);
		DFNotificationCenter.defaultCenter.addObserver(this, UIStrings.returned);
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		assertTrue(user != null);
		assertTrue(!user.isBanned());
		assertTrue(user.getUserType() == UserType.USER);
		assertTrue(user.getUsername().equals("testuser"));
	}
	
	@Test
	public void testGetInvalidUser() {
		User user = null;		
		userQuery.getUser(invalidName);
		DFNotificationCenter.defaultCenter.addObserver(this, UIStrings.returned);
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		assertNull(user);
	}
	
	/*
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
	*/ 
	@Test
	public void testVerifyLoginValidUserValidPassword(){
		DFNotificationCenter.defaultCenter.addObserver(this, UIStrings.success);
		DFNotificationCenter.defaultCenter.addObserver(this, UIStrings.failure);
		userQuery.verifyUserLogin(name, "blahblah");
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("loginSuccess : "+ loginSuccess);
		assertTrue(loginSuccess);
	}
	
	@Test
	public void testVerifyLoginInvalidUser(){
		DFNotificationCenter.defaultCenter.addObserver(this, UIStrings.success);
		DFNotificationCenter.defaultCenter.addObserver(this, UIStrings.failure);
		userQuery.verifyUserLogin(invalidName, "blahblah");
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		assertFalse(loginSuccess);
	}
	
	@Test
	public void testVerifyLoginValidUserInvalidPassword(){
		DFNotificationCenter.defaultCenter.addObserver(this, UIStrings.success);
		DFNotificationCenter.defaultCenter.addObserver(this, UIStrings.failure);
		userQuery.verifyUserLogin(name, "invalidPassword");
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		assertFalse(loginSuccess);
	}
	
	@Override
	public void performActionFor(String notificationName, Object userData) {
		// TODO Auto-generated method stub
		if(notificationName.equals(UIStrings.returned)){
			if(userData == null){
				user = null;
			} else{
				user = (User)userData;
			}
			System.out.println("getUser activated in performactionfor");
		} else if (notificationName.equals(UIStrings.success)) {
			System.out.println("verifylogin returned success in performactionfor");
			loginSuccess = true;
		} else if (notificationName.equals(UIStrings.failure)) {
			loginSuccess = false;
			System.out.println("verifylogin returned success in performactionfor");
		}
	}
	
	/*@Test
	public void testAddNewValidUser(){
		User addedUser = userQuery.addNewUser("naveenTest3", "password", UserType.MOD);
		User receivedUser = null;

		receivedUser = userQuery.getUser("naveenTest3");

		assertEquals(receivedUser.getUserType(), addedUser.getUserType());
		assertEquals(receivedUser.isBanned(), addedUser.isBanned());
		assertEquals(receivedUser.getUsername(), addedUser.getUsername());
	}
	
	@Test
	public void testAddNewInvalidUser(){
		User addedUser = userQuery.addNewUser("naveenTest1", "password", UserType.MOD);
		assertNull(addedUser);
	}*/
}
