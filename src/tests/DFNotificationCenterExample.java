package tests;

import UIKit.DFNotificationCenter;
import UIKit.DFNotificationCenterDelegate;
import database.DFDatabase;

public class DFNotificationCenterExample
{
    private static final String TestNotificationString = "Test Notification String";

    public static void main(String[] args)
    {
        System.out.println(DFDatabase.defaultDatabase._decryptString("03535FC5648C24BA3960BB53EE33808016CF6C75DD38F1F8A1684D64E72FCEE1"));


        DFNotificationCenterListener listener = new DFNotificationCenterListener();
        DFNotificationCenter.defaultCenter.addObserver(listener, TestNotificationString);

        DFNotificationCenter.defaultCenter.postNotification(TestNotificationString, null);
        DFNotificationCenter.defaultCenter.postNotification(TestNotificationString, "I'm test data!");

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
