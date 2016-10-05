package tests;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import objects.*;

/*
 * DebateTest.java
 * Cody Tyson
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
	
	@Test
	public void testDebate() {
		Debate d = new Debate("This is a test");
		assertTrue(d != null);
	}
	@Test
	public void testPostToDebate() {
		Debate d = new Debate("This is a test");
		User u  = new User("Cody");
		Post p = new Post(u.getUsername(), "This  is my post");
		d.post(p);
		assertTrue(d.getPosts().size() != 0);
	}
	@Test
	public void testDebateTitle() {
		Debate d = new Debate("This is a test");
		assertTrue("This is a test".equals(d.getTitle()));
	}
	@Test
	public void testDebateOpen() {
		Debate d = new Debate("This is a test");
		assertTrue(d.isOpen());
	}
	@Test
	public void testDebateClosed() {
		Debate d = new Debate("This is a test");
		d.closeDebate(d);
		assertTrue(!d.isOpen());
	}
}

