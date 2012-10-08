package com.ubhave.triggermanager.preferences;

import java.util.Calendar;

public class SurveyLimiter
{

	public static boolean surveyAllowed(UserPreferences preferences)
	{
		Calendar calendar = Calendar.getInstance();
		if (!preferences.aboveCap(calendar.get(Calendar.DAY_OF_MONTH)))
		{
			if (preferences.userAllowsNotification(calendar))
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
	
}
