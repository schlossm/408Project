package tests;

import static org.junit.Assert.*;

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
import objects.*;
import objects.Post;
import java.util.ArrayList;

/*
 * PostQueryTest.java
 * Alex Rosenberg
 * 
 * Test the PostQuery class
 */
public class PostQueryTest implements DFNotificationCenterDelegate {
	private static PostQuery pq;
	private static int debateID, invalidDebateID;
	private static String username;
	private static ArrayList<Post> posts;
	
	/*
	 * This method is run only once before anything else
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		pq = new PostQuery();
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
		debateID = 1;
		invalidDebateID = 0;
		username = "testusername";
	}

	/*
	 * This method runs after each test
	 */
	@After
	public void tearDown() throws Exception {
		posts = null;
	}

	/*
	 * This is a method to test a method of the
	 * target class
	 */
	@Test
	public void testGetDebatePosts() {
		pq.getDebatePosts(debateID);
		
		DFNotificationCenter.defaultCenter.register(this, UIStrings.postsReturned);
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		// Still need to actually set posts
		assertTrue(posts != null);
	}
	
	@Test
	public void testGetInvalidDebatePosts() {
		pq.getDebatePosts(invalidDebateID);
		
		DFNotificationCenter.defaultCenter.register(this, UIStrings.postsReturned);
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		// Still need to actually set posts
		assertTrue(posts != null);
	}
	
	@Test
	public void testPostToDebate() {
		Post post = new Post(username, "This is a post!");
		pq.postToDebate(post, debateID);
		
		DFNotificationCenter.defaultCenter.register(this, UIStrings.postsReturned);
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		// Still need to get posts and compare size to before
		assertTrue(posts != null);
	}
	
	/*@Test
	public void testUpdateFlags() {
		
	}
	
	@Test
	public void testUpdateIsHidden() {
		
	}
	
	@Test
	public void testUploadNewPostToDatabase() {
		
	}*/

	@Override
	public void performActionFor(String notificationName, Object userData) {
		// Satisfy implementation of DFNotificationCenterDelegate
	}
}
