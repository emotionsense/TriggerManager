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
//				if (interval)
				{
					return true;
				}
			}
		}
		return false;
	}
	
}
