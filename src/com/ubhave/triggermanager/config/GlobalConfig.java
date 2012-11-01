package com.ubhave.triggermanager.config;

import android.content.Context;
import android.content.SharedPreferences;

import com.ubhave.triggermanager.TriggerException;

public class GlobalConfig
{
	public static final String DO_NOT_DISTURB_BEFORE = "beforeHour";
	public static final String DO_NOT_DISTURB_AFTER = "afterHour";
	public static final String MAXIMUM_DAILY_SURVEYS = "maxSurveys";
	public static final String MIN_TRIGGER_INTERVAL_MINUTES = "minInterval";
	public static final String NOTIFICATION_PROBABILITY = "notificationProb";
	public static final String SENSE_TIME = "senseTime";

	private static GlobalConfig globalConfig;
	private static final Object lock = new Object();

	public static GlobalConfig getGlobalConfig(Context c) throws TriggerException
	{
		if (c == null)
		{
			throw new TriggerException(TriggerException.NO_CONTEXT, "Context is null");
		}
		if (globalConfig == null)
		{
			synchronized (lock)
			{
				if (globalConfig == null)
				{
					globalConfig = new GlobalConfig(c);
				}
			}
		}
		return globalConfig;
	}

	private final SharedPreferences preferences;

	public GlobalConfig(Context context)
	{
		preferences = context.getSharedPreferences(Constants.GLOBAL_PREFERENCES, Context.MODE_PRIVATE);
	}

	public void setParameter(String parameterName, Object parameterValue)
	{
		synchronized (lock)
		{
			SharedPreferences.Editor editor = preferences.edit();
			if (parameterName.equals(NOTIFICATION_PROBABILITY))
			{
				editor.putFloat(parameterName, (Float) parameterValue);
			}
			else
			{
				editor.putInt(parameterName, (Integer) parameterValue);
			}
			editor.commit();
		}
	}

	public Object getParameter(String parameterName) throws TriggerException
	{
		synchronized (lock)
		{
			if (parameterName.equals(NOTIFICATION_PROBABILITY))
			{
				return preferences.getFloat(parameterName, getDefault(parameterName));
			}
			else
			{
				return preferences.getInt(parameterName, getDefault(parameterName));
			}
		}
	}

	private int getDefault(String key) throws TriggerException
	{
		if (key.equals(DO_NOT_DISTURB_BEFORE))
			return Constants.DEFAULT_DO_NOT_DISTURB_BEFORE;
		else if (key.equals(DO_NOT_DISTURB_AFTER))
			return Constants.DEFAULT_DO_NOT_DISTURB_AFTER;
		else if (key.equals(MAXIMUM_DAILY_SURVEYS))
			return Constants.DEFAULT_MAXIMUM_DAILY_SURVEYS;
		else if (key.equals(MIN_TRIGGER_INTERVAL_MINUTES))
			return Constants.DEFAULT_MIN_TRIGGER_INTERVAL_MINUTES;
		else if (key.equals(NOTIFICATION_PROBABILITY))
			return Constants.DEFAULT_NOTIFICATION_PROBABILITY;
		else if (key.equals(SENSE_TIME))
			return Constants.DEFAULT_SENSE_TIME_MINUTES;
		else
			throw new TriggerException(TriggerException.INVALID_CONFIG_KEY, "Key: " + key + " does not exist");
	}

}
