package tests;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import objects.*;

/*
 * UserTest.java
 * Alex Rosenberg
 * 
 * Test the User class
 */
public class DebateTest {
	/*
	 * This method is run only once before anything else
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
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
	public void testUser() {
		User u = new User("testusername");
		assertTrue(u != null);
	}
	
	@Test
	public void testUser2() {
		User u = new User("testusername1", User.UserType.USER, false);
		assertTrue(u != null);
	}

	@Test
	public void testGetUsername() {
		String name = "testUser";
		User u = new User(name);
		assertEquals(name, u.getUsername());
	}

	@Test
	public void testMakeUser() {
		User u = new User("mynewname2643");
		u.makeUser();
		assertEquals(u.getUserType(), User.UserType.USER);
	}

	@Test
	public void testMakeMod() {
		User u = new User("UserName2048");
		u.makeMod();
		assertEquals(u.getUserType(), User.UserType.MOD);
	}

	@Test
	public void testMakeAdmin() {
		User u = new User("AlexRosenberg");
		u.makeAdmin();
		assertEquals(u.getUserType(), User.UserType.ADMIN);
	}

	@Test
	public void testGetUserType() {
		User u = new User("HelloWorld00", User.UserType.USER, false);
		assertEquals(u.getUserType(), User.UserType.USER);
	}

	@Test
	public void testBan() {
		User u = new User("n00tn00t", User.UserType.USER, false);
		u.ban();
		assertTrue(u.isBanned());
	}

	@Test
	public void testUnban() {
		User u = new User("nootnoot", User.UserType.USER, false);
		u.unban();
		assertFalse(u.isBanned());
	}

	@Test
	public void testIsBanned() {
		User u = new User("outrageousUserName", User.UserType.USER, false);
		u.ban();
		assertTrue(u.isBanned());
	}

	@Test
	public void testToString() {
		User u = new User("BigBadAdmin", User.UserType.ADMIN, false);
		String uToString = "username:\t" + u.getUsername() + "\n"
				+ "userType:\t" + u.getUserType() + "\n"
				+ "isBanned\t" + u.isBanned();
		assertEquals(u.toString(), uToString);
	}

}
