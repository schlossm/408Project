package tests;

import UIKit.DFNotificationCenter;
import UIKit.DFNotificationCenterDelegate;


public class DFNotificationCenterExample
{
    private static final String TestNotificationString = "Test Notification String";

    public static void main(String[] args)
    {
        DFNotificationCenterListener listener = new DFNotificationCenterListener();
        DFNotificationCenter.defaultCenter.register(listener, TestNotificationString);

        DFNotificationCenter.defaultCenter.post(TestNotificationString, null);
        DFNotificationCenter.defaultCenter.post(TestNotificationString, "I'm test data!");

        DFNotificationCenter.defaultCenter.remove(listener);
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
