package tests;

import UIKit.DFNotificationCenter;
import UIKit.DFNotificationCenterDelegate;

public class DFNotificationCenterExample
{
    static public final String TestNotificationString = "Test Notification String";

    public static void main(String[] args)
    {
        DFNotificationCenter.defaultCenter.addObserver(new DFNotificationCenterListener(), TestNotificationString);

        DFNotificationCenter.defaultCenter.postNotification(TestNotificationString, null);
        DFNotificationCenter.defaultCenter.postNotification(TestNotificationString, "I'm test data!");
    }
}

class DFNotificationCenterListener implements DFNotificationCenterDelegate
{
    @Override
    public void performActionFor(String notificationName, Object userData)
    {
        if (userData == null)
        {
            System.out.println("Hello! I'm from a notification!");
        }
        else
        {
            System.out.print("Hello! I'm from a notification!  Here's my data: ");
            System.out.println(userData.toString());
        }
    }
}
