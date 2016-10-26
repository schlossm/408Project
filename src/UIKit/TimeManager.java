package UIKit;

import UI.UIStrings;

import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created by michaelschloss on 10/24/16.
 */
@SuppressWarnings("InfiniteLoopStatement")
public class TimeManager implements Runnable
{
	private final Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("America/Indianapolis"));
	private int startMillisecond = calendar.get(Calendar.MILLISECOND);
	private int oldDay = calendar.get(Calendar.DAY_OF_YEAR);

	private void listenForTimeChanges()
	{
		while (true)
		{
			if (calendar.get(Calendar.DAY_OF_YEAR) != oldDay)
			{
				oldDay = calendar.get(Calendar.DAY_OF_YEAR);
				startMillisecond = calendar.get(Calendar.MILLISECOND);
				DFNotificationCenter.defaultCenter.post(UIStrings.newDayNotification, null);
			}
			else if ((calendar.get(Calendar.MILLISECOND) - startMillisecond) % 60000 == 0)
			{
				DFNotificationCenter.defaultCenter.post(UIStrings.oneHourHasPassedNotification, null);
			}
			else if ((calendar.get(Calendar.MILLISECOND) - startMillisecond) % 25000 == 0)
			{
				DFNotificationCenter.defaultCenter.post(UIStrings.twentyFiveMinutesHavePassedNotification, null);
			}
			else if ((calendar.get(Calendar.MILLISECOND) - startMillisecond) % 10000 == 0)
			{
				DFNotificationCenter.defaultCenter.post(UIStrings.tenMinutesHavePassedNotification, null);
			}
			else if ((calendar.get(Calendar.MILLISECOND) - startMillisecond) % 5000 == 0)
			{
				DFNotificationCenter.defaultCenter.post(UIStrings.fiveMinutesHavePassedNotification, null);
			}
		}
	}

	@Override
	public void run()
	{
		listenForTimeChanges();
	}
}
