package com.ubhave.triggermanager.preferences;

import java.util.Calendar;

import android.content.Context;
import android.content.SharedPreferences;

public class UserPreferences
{

	private final static String PREFERENCES = "trigger_preferences";
	private final static String BEFORE = "before";
	private final static String AFTER = "after";
	private final static String INTERVAL = "interval";

	private final static String CAP = "cap";

	private final static String CURRENT_DAY = "currentDay";
	private final static String NOTIFICATIONS = "notifications";
	private final static String LAST_NOTIFICATION = "lastNotification";

	private final SharedPreferences preferences;

	public UserPreferences(Context context)
	{
		preferences = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
	}

	public int getBeforeTime()
	{
		return getInt(BEFORE, 8 * 2);
	}

	public int getAfterTime()
	{
		return getInt(AFTER, 22 * 2);
	}

	public int getSurveyCap()
	{
		return getInt(CAP, 2);
	}
	
	public int getInterval()
	{
		return getInt(INTERVAL, 0);
	}

	public void setBeforeTime(int hour)
	{
		SharedPreferences.Editor editor = preferences.edit();
		editor.putInt(BEFORE, hour);
		editor.commit();
	}

	public void setAfterTime(int hour)
	{
		SharedPreferences.Editor editor = preferences.edit();
		editor.putInt(AFTER, hour);
		editor.commit();
	}

	public void setSurveyCap(int cap)
	{
		SharedPreferences.Editor editor = preferences.edit();
		editor.putInt(CAP, cap);
		editor.commit();
	}
	
	public void setSurveyInterval(int minutes)
	{
		SharedPreferences.Editor editor = preferences.edit();
		editor.putInt(INTERVAL, minutes);
		editor.commit();
	}

	public void resetCap()
	{
		SharedPreferences.Editor editor = preferences.edit();
		editor.putInt(NOTIFICATIONS, 0);
		editor.commit();
	}

	private int surveysSent(int dayOfMonth)
	{
		int day = getInt(CURRENT_DAY, 0);
		if (day != dayOfMonth)
		{
			SharedPreferences.Editor editor = preferences.edit();
			editor.putInt(CURRENT_DAY, dayOfMonth);
			editor.putInt(NOTIFICATIONS, 0);
			editor.commit();
			return 0;
		}
		else
		{
			return getInt(NOTIFICATIONS, 0);
		}
	}

	public boolean aboveCap(int dayOfMonth)
	{
		int cap = getSurveyCap();
		int sent = surveysSent(dayOfMonth);
		return sent >= cap;
	}

	public void surveySent(Calendar surveyTime)
	{
		int dayOfMonth = surveyTime.get(Calendar.DAY_OF_MONTH);
		int surveys = surveysSent(dayOfMonth);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putInt(NOTIFICATIONS, surveys + 1);
		editor.putLong(LAST_NOTIFICATION, surveyTime.getTimeInMillis());
		editor.commit();
	}

	public long lastNotification()
	{
		return preferences.getLong(LAST_NOTIFICATION, 0);
	}

	private int getInt(String key, int def)
	{
		return preferences.getInt(key, def);
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
