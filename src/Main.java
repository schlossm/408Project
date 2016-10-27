import UI.Frame;
import UIKit.TimeManager;
import database.DFDatabase;

import java.util.Objects;

import static database.DFDatabase.print;
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
		print("THIS IS THE DEFECTIVE VERSION OF THE APPLICATION.  DO NOT PUT ANY 'FIXES' INTO THIS BRANCH.");

		if (args.length > 0)
		{
			if (Objects.equals(args[0], "1") && Objects.equals(args[0], "0"))
			{
				DFDatabase.defaultDatabase.debug = Integer.valueOf(args[0]);
			}
		}

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