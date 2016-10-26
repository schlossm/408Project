import UI.Frame;
import UIKit.TimeManager;

import static database.DFDatabase.queue;

/**
 * Main.java
 * 
 * Executable starter function:
 * loads UI among other things
 */
public class Main
{
	public static void main(String[] args)
	{
		new Frame("School of Thought");

		new Thread(new TimeManager()).start();

		while(true)
		{
			try
			{
				queue.take().run();
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
				System.exit(-1);
			}
		}
	}
}