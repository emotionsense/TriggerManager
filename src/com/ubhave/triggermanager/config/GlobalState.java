package com.ubhave.triggermanager.config;

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

	private void setParameter(String parameterName, Integer parameterValue)
	{
		SharedPreferences.Editor editor = preferences.edit();
		editor.putInt(parameterName, parameterValue);
		editor.commit();
	}
	
	public void incrementNotificationsSent()
	{
		synchronized (lock)
		{
			int notifications = preferences.getInt(NOTIFICATIONS, 0) + 1;
			setParameter(NOTIFICATIONS, notifications);
		}
	}

}
