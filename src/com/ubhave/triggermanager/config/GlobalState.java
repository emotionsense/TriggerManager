package com.ubhave.triggermanager.config;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import android.content.Context;
import android.content.SharedPreferences;

import com.ubhave.triggermanager.TriggerException;

public class GlobalState
{
	private final static String CURRENT_DAY = "currentDay";
	private final static String NOTIFICATIONS = "notifications";
	private final static String LAST_NOTIFICATION = "lastNotification";
	
	private static GlobalState globalState;
	private static final Object lock = new Object();

	public static GlobalState getGlobalState(Context c) throws TriggerException
	{
		if (c == null)
		{
			throw new TriggerException(TriggerException.NO_CONTEXT, "Context is null");
		}
		if (globalState == null)
		{
			synchronized (lock)
			{
				if (globalState == null)
				{
					globalState = new GlobalState(c);
				}
			}
		}
		return globalState;
	}
	
	private final SharedPreferences preferences;
	
	public GlobalState(Context context)
	{
		preferences = context.getSharedPreferences(Constants.GLOBAL_STATE, Context.MODE_PRIVATE);
	}
	
	public void incrementNotificationsSent()
	{
		synchronized (lock)
		{
			int notifications = getNotificationsSent() + 1;
			SharedPreferences.Editor editor = preferences.edit();
			editor.putInt(NOTIFICATIONS, notifications);
			editor.putLong(LAST_NOTIFICATION, System.currentTimeMillis());
			editor.commit();
		}
	}
	
	public int getNotificationsSent()
	{
		DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		String dateKey = formatter.format(System.currentTimeMillis());
		String currentDate = preferences.getString(CURRENT_DAY, null);
		
		if (currentDate == null || !dateKey.equals(currentDate))
		{
			synchronized (lock)
			{
				SharedPreferences.Editor editor = preferences.edit();
				editor.putString(CURRENT_DAY, dateKey);
				editor.putInt(NOTIFICATIONS, 0);
				editor.commit();
			}
			return 0;
		}
		else return preferences.getInt(NOTIFICATIONS, 0);
	}
	
	public long getLastNotificationTime()
	{
		return preferences.getLong(LAST_NOTIFICATION, 0);
	}

}
