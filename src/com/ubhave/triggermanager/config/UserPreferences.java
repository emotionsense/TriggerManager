package com.ubhave.triggermanager.config;


public class UserPreferences
{

//	public void resetCap()
//	{
//		SharedPreferences.Editor editor = preferences.edit();
//		editor.putInt(NOTIFICATIONS, 0);
//		editor.commit();
//	}

//	private int surveysSent(int dayOfMonth)
//	{
//		int day = getInt(CURRENT_DAY, 0);
//		if (day != dayOfMonth)
//		{
//			SharedPreferences.Editor editor = preferences.edit();
//			editor.putInt(CURRENT_DAY, dayOfMonth);
//			editor.putInt(NOTIFICATIONS, 0);
//			editor.commit();
//			return 0;
//		}
//		else
//		{
//			return getInt(NOTIFICATIONS, 0);
//		}
//	}

//	public boolean aboveCap(int dayOfMonth)
//	{
//		int cap = getSurveyCap();
//		int sent = surveysSent(dayOfMonth);
//		return sent >= cap;
//	}

//	public void surveySent(Calendar surveyTime)
//	{
//		int dayOfMonth = surveyTime.get(Calendar.DAY_OF_MONTH);
//		int surveys = surveysSent(dayOfMonth);
//		SharedPreferences.Editor editor = preferences.edit();
//		editor.putInt(NOTIFICATIONS, surveys + 1);
//		editor.putLong(LAST_NOTIFICATION, surveyTime.getTimeInMillis());
//		editor.commit();
//	}
//
//	public long lastNotification()
//	{
//		return preferences.getLong(LAST_NOTIFICATION, 0);
//	}
//
//	private int getInt(String key, int def)
//	{
//		return preferences.getInt(key, def);
//	}

	
}
