package tests;

import UIKit.DFNotificationCenter;
import UIKit.DFNotificationCenterDelegate;
import database.DFDatabase;

public class DFNotificationCenterExample
{
    private static final String TestNotificationString = "Test Notification String";

    public static void main(String[] args)
    {
        System.out.print(DFDatabase.defaultDatabase._decryptString("84C0F380C2162077C8B3CFD162335ED5CEA3ABB096FA1298BA916B03E7C985DA"));


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
