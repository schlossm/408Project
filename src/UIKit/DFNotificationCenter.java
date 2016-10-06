package UIKit;

import database.DFDatabase;

import java.util.ArrayList;
import java.util.Objects;

class DFNotificationCenterObject
{
	final DFNotificationCenterDelegate observer;
	final String notificationName;
	
	DFNotificationCenterObject(DFNotificationCenterDelegate object, String notificationName)
	{
		observer = object;
		this.notificationName = notificationName;
	}
}

/**
*	DFNotificationCenter - A Notification parser
*	Objects can place calls to register(_:, _:) to register themselves to listen for notifications
*	Objects can place calls to post(_:, _:) to post notifications with optional userData
*
*	This class is meant to be used for passing information among different packages(modules)
*/
@SuppressWarnings({"unused", "unchecked"})
public class DFNotificationCenter
{
	public static final DFNotificationCenter defaultCenter = new DFNotificationCenter();
	
	private ArrayList<DFNotificationCenterObject> observers = new ArrayList<>();
	private DFNotificationCenter() { }

	/**
	 * 	@deprecated use `register(_:, _:)` instead
	 *	@param object The object that will be receiving the notification.  This object must conform to the DFNotificationCenterDelegate interface
	 *  @param notificationName The notification name the object wishes to listen for.  All other notifications will be ignored
	 */
	@Deprecated public void addObserver(DFNotificationCenterDelegate object, String notificationName)
	{
		System.out.println(DFDatabase.getMethodName(2) + " is now deprecated.  Use `register(_:, _:)` instead.  Will call new method for you this time");
		register(object, notificationName);
	}

	/**
	 *	@param observer The object that will be receiving the notification.  This object must conform to the DFNotificationCenterDelegate interface
	 *  @param notificationName The notification name the object wishes to listen for.  All other notifications will be ignored
	 */
	public void register(DFNotificationCenterDelegate observer, String notificationName)
	{
		for (DFNotificationCenterObject object : observers)
		{
			if (object.observer == observer && Objects.equals(object.notificationName, notificationName))
			{
				return;
			}
		}
		observers.add(new DFNotificationCenterObject(observer, notificationName));
	}

	/**
	 *	@param observer The object that was receiving the notification.  This object must conform to the DFNotificationCenterDelegate interface
	 *  @param notificationName The notification name the object was listening for.  All other notifications will be ignored
	 */
	public void remove(DFNotificationCenterDelegate observer, String notificationName)
	{
		((ArrayList<DFNotificationCenterObject>) observers.clone()).stream().filter(object -> object.observer == observer).forEach(object -> observers.remove(object));
	}

	/**
	 * 	A convenience method for removing all instances of an Object
	 * 	@param observer The object that wishes to be removed
	 */
	public void remove(DFNotificationCenterDelegate observer)
	{
		((ArrayList<DFNotificationCenterObject>) observers.clone()).stream().filter(object -> object.observer == observer).forEach(object -> observers.remove(object));
	}

	/**
	 * 	@deprecated use `post(_:, _:)` instead
	 * 	@param notificationName The notification name the object wishes to post for
	 * 	@param userData Optional data the object calling this function wishes to pass on to any observers
	 */
	@Deprecated public void postNotification(String notificationName, Object userData)
	{
		System.out.println(DFDatabase.getMethodName(2) + " is now deprecated.  Use `post(_:, _:)` instead.  Will call new method for you this time");
		post(notificationName, userData);
	}

	/**
	 * 	@param notificationName The notification name the object wishes to post for
	 * 	@param userData Optional data the object calling this function wishes to pass on to any observers
	 */
	public void post(String notificationName, Object userData)
	{
		observers.stream().filter(observer -> Objects.equals(observer.notificationName, notificationName)).forEach(observer -> observer.observer.performActionFor(notificationName, userData));
	}
}
