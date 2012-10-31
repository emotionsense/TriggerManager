package com.ubhave.triggermanager.config;

import java.util.Calendar;

import com.ubhave.triggermanager.TriggerException;

import android.content.Context;

public class SurveyLimiter
{

	public boolean surveyAllowedNow(Context c)
	{
		try
		{
			GlobalConfig config = GlobalConfig.getGlobalConfig(c);
			GlobalState state = GlobalState.getGlobalState(c);

			Calendar calendar = Calendar.getInstance();
			int maxSurveys = (Integer) config.getParameter(GlobalConfig.MAXIMUM_DAILY_SURVEYS);
			if (state.getNotificationsSent() < maxSurveys)
			{
				if (userAllowsNotification(calendar, config))
				{
					long lastNotification = state.getLastNotificationTime();
					long interval = (Integer) config.getParameter(GlobalConfig.MIN_TRIGGER_INTERVAL_MINUTES) * 60 * 1000;
					if (Math.abs(System.currentTimeMillis() - lastNotification) > interval)
					{
						return true;
					}
				}
			}
		}
		catch (TriggerException e)
		{
			e.printStackTrace();
		}
		return false;
	}

	// TODO
	public boolean userAllowsNotification(Calendar time, GlobalConfig config) throws TriggerException
	{
		int minTime = (Integer) config.getParameter(GlobalConfig.DO_NOT_DISTURB_BEFORE);
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

		int maxTime = (Integer) config.getParameter(GlobalConfig.DO_NOT_DISTURB_AFTER);
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
