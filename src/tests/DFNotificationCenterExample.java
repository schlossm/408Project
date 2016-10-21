package tests;

import UIKit.DFNotificationCenter;
import UIKit.DFNotificationCenterDelegate;
import database.DFDatabase;

public class DFNotificationCenterExample
{
    private static final String TestNotificationString = "Test Notification String";

    public static void main(String[] args)
    {
        System.out.println(DFDatabase.defaultDatabase.encryptString("If you wish to add a comment to a debate, it must promote the discussion at hand and not detract from it.  You are welcome to start a new sub-conversation with a comment, but please do not attempt to change the debate's main focus.  Any posts that deviate from this rule will be immediately removed from the debate."));

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
