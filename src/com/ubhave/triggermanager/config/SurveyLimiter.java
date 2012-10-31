package com.ubhave.triggermanager.config;

import java.util.Calendar;

import android.content.Context;

public class SurveyLimiter
{
	
	
	public boolean surveyAllowedNow(Context c)
	{
		GlobalConfig config = GlobalConfig.getGlobalConfig(c);
		Calendar calendar = Calendar.getInstance();

		if (!preferences.aboveCap(calendar.get(Calendar.DAY_OF_MONTH)))
		{
			if (userAllowsNotification(calendar))
			{
				long lastNotification = preferences.lastNotification();
				if (Math.abs(System.currentTimeMillis() - lastNotification) > preferences.getInterval())
				{
					return true;
				}
			}
		}
		return false;
	}

	public boolean userAllowsNotification(Calendar time)
	{
		int minTime = getBeforeTime();
		int minHour = (minTime / 2);
		int minMinute;
		if (minHour % 2 == 0)
		{
			minMinute = 0;
		}
		else
		{
			minMinute = 30;
		}

		int maxTime = getAfterTime();
		int maxHour = (maxTime / 2);
		int maxMinute;
		if (maxHour % 2 == 0)
		{
			maxMinute = 0;
		}
		else
		{
			maxMinute = 30;
		}
		int hour = time.get(Calendar.HOUR_OF_DAY);
		int minute = time.get(Calendar.MINUTE);

		if (hour < minHour || (hour == minHour && minute <= minMinute))
		{
			return false;
		}
		else if (hour > maxHour || (hour == maxHour && minute > maxMinute))
		{
			return false;
		}
		else
		{
			return true;
		}
	}

}
