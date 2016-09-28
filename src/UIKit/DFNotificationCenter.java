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

/*
*	An extremely simply Notification Observer and Poster
*/
public class DFNotificationCenter
{
	public static final DFNotificationCenter defaultCenter = new DFNotificationCenter();
	
	private ArrayList<DFNotificationCenterObject> observers = new ArrayList<>();
	private DFNotificationCenter() { }
	
	void addObserver(DFNotificationCenterDelegate object, String notificationName)
	{
		observers.add(new DFNotificationCenterObject(object, notificationName));
	}
	
	void postNotification(String notificationName, Object userData)
	{
		for (DFNotificationCenterObject observer : observers)
		{
			if (Objects.equals(observer.notificationName, notificationName))
			{
				observer.observer.performActionFor(notificationName, userData);
			}
		}
	}
}
