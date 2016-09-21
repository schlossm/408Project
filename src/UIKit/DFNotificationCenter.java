package UIKit;

import java.util.ArrayList;

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

/*
*	An extremely simply Notification Observer and Poster
*/
public class DFNotificationCenter
{
	public static final DFNotificationCenter defaultCenter = new DFNotificationCenter();
	
	private ArrayList<DFNotificationCenterObject> observers = new ArrayList<DFNotificationCenterObject>();
	private DFNotificationCenter() { }
	
	void addObserver(DFNotificationCenterDelegate object, String notificationName)
	{
		observers.add(new DFNotificationCenterObject(object, notificationName));
	}
	
	void postNotification(String notificationName)
	{
		for (DFNotificationCenterObject observer : observers)
		{
			if (observer.notificationName == notificationName)
			{
				observer.observer.performActionFor(notificationName);
			}
		}
	}
}
