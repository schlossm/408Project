package tests;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import objects.*;

/*
 * PostTest.java
 * Cody Tyson
 * 
 * Test the User class
 */
public class PostTest {
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
	@Test
	public void testPost() {
		Debate d = new Debate("This is a test");
		assertTrue(d != null);
	}
	@Test
	public void testCreatePost() {
		User u = new User("Cody");
		Post p = new Post("Null", "Null");
		p = p.createPost(u, "This is my message");
		assertTrue(p.getPoster() == "Cody");
	}
	@Test
	public void testGetText() {
		User u = new User("Cody");
		Post p = new Post("Null", "Null");
		p = p.createPost(u, "This is my message");
		assertTrue(p.getText() == "This is my message");
	}
	@Test
	public void testTimestamp() {
		User u = new User("Cody");
		Post p = new Post("Null", "Null");
		p = p.createPost(u, "This is my message");
		assertTrue(p.getTimestamp() != null);
	}
}
