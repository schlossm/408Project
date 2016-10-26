package UIKit;

import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created by michaelschloss on 10/24/16.
 */
public class TimeManager implements Runnable
{
	private Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("America/Indianapolis"));

	void listenForTimeChanges()
	{

	}

	@Override
	public void run()
	{
		listenForTimeChanges();
	}
}
