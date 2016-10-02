package UIKit;

import java.util.ArrayList;
import java.util.Objects;

class DFNotificationCenterObject
{
	DFNotificationCenterDelegate observer;
	String notificationName;
	
	DFNotificationCenterObject(DFNotificationCenterDelegate object, String notificationName)
	{
		observer = object;
		this.notificationName = notificationName;
	}
}

/**
*	DFNotificationCenter - A Notification parser
*	Objects can place calls to addObserver(_:, _:) to register themselves to listen for notifications
*	Objects can place calls to postNotification(_:, _:) to post notifications with optional userData
*
*	This class is meant to be used for passing information among different pacakges(modules)
*/
public class DFNotificationCenter
{
	public static final DFNotificationCenter defaultCenter = new DFNotificationCenter();
	
	private ArrayList<DFNotificationCenterObject> observers = new ArrayList<>();
	private DFNotificationCenter() { }

	/**
	 *	addObserver(_:, _:)
	 *	@param object The object that will be receiving the notification.  This object must conform to the DFNotificationCenterDelegate interface
	 *  @param notificationName The notification name the object wishes to listen for.  All other notifications will be ignored
	 */
	public void addObserver(DFNotificationCenterDelegate object, String notificationName)
	{
		observers.add(new DFNotificationCenterObject(object, notificationName));
	}

	/**
	 *	postNotification(_:, _:)
	 * @param notificationName The notification name the object wishes to post for
	 * @param userData Optional data the object calling this function wishes to pass on to any observers
	 */
	public void postNotification(String notificationName, Object userData)
	{
		observers.stream().filter(observer -> Objects.equals(observer.notificationName, notificationName)).forEach(observer -> observer.observer.performActionFor(notificationName, userData));
	}
}
