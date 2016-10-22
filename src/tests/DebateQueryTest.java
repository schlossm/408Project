package tests;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.omg.CORBA.PRIVATE_MEMBER;

import JSON_translation.DebateQuery;
import JSON_translation.PostQuery;
import UI.UIStrings;
import UIKit.DFNotificationCenter;
import UIKit.DFNotificationCenterDelegate;
import objects.Debate;
import objects.Post;

/*
 * DebateQueryTest.java
 * Naveen Ganessin
 * 
 * Test the PostQuery class
 */
public class DebateQueryTest implements DFNotificationCenterDelegate{
	private static DebateQuery dq;
	private static Debate debate;
	private static String debateTitle, invalidDebateTitle;
	private static ArrayList<Post> posts;
	private static boolean isDebateCreationSuccess;
	/*
	 * This method is run only once before anything else
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		dq = new DebateQuery();
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
		debateTitle = "testDebate";
		invalidDebateTitle = "nothingButHogWash";
	}

	/*
	 * This method runs after each test
	 */
	@After
	public void tearDown() throws Exception {
		posts = null;
		debate = null;
		isDebateCreationSuccess = false;
	}

	/*
	 * This is a method to test a method of the
	 * target class
	 */
	
	@Override
	public void performActionFor(String notificationName, Object userData) {
		// TODO Auto-generated method stub
		if(notificationName.equals(UIStrings.debateReturned)){
			if(userData != null){
				debate = (Debate)userData;
			} else {
				debate = null;
			}
		} else if(notificationName.equals(UIStrings.debateCreated)){
			if(userData.equals(Boolean.TRUE)){
				isDebateCreationSuccess = true;
			} else if(userData.equals(Boolean.FALSE)){
				isDebateCreationSuccess = false;
			}
		}
	}
	
	@Test
	public void getDebateByTitleValidTitleTest(){
		DFNotificationCenter.defaultCenter.register(this, UIStrings.debateReturned);
		//dq.getDebateByTitle(debateTitle);
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		assertNotNull(debate);
		assertEquals(debate.getTitle(), debateTitle);
		assertTrue(debate.isOpen());
		assertNotNull(debate.getPosts());
	}
	
	@Test
	public void getDebateByTitleInvalidTitleTest(){
		DFNotificationCenter.defaultCenter.register(this, UIStrings.debateReturned);
		//dq.getDebateByTitle(invalidDebateTitle);
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		assertNull(debate);
	}
	
	@Test
	public void createNewDebateValidEverythingTest(){
		DFNotificationCenter.defaultCenter.register(this, UIStrings.debateReturned);
		//dq.getDebateByTitle(invalidDebateTitle);
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		assertNull(debate);
	}
}
