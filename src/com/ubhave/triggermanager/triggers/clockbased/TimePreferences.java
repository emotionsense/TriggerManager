package com.ubhave.triggermanager.triggers.clockbased;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

import android.content.Context;

import com.ubhave.triggermanager.TriggerException;
import com.ubhave.triggermanager.config.GlobalConfig;
import com.ubhave.triggermanager.config.TriggerManagerConstants;

public class TimePreferences
{
	public final static int ERROR = -1;
	
	private final int earlyLimit;
	private final int lateLimit;
	private final int minInterval;
	private final Random random;
	
	public TimePreferences(Context context)
	{
		random = new Random();
		GlobalConfig config = null;
		try
		{
			config = GlobalConfig.getGlobalConfig(context);
		}
		catch (TriggerException e)
		{}
		
		earlyLimit = getParameter(config, GlobalConfig.DO_NOT_DISTURB_BEFORE_MINUTES, TriggerManagerConstants.DEFAULT_DO_NOT_DISTURB_BEFORE_MINUTES);
		lateLimit = getParameter(config, GlobalConfig.DO_NOT_DISTURB_AFTER_MINUTES, TriggerManagerConstants.DEFAULT_DO_NOT_DISTURB_AFTER_MINUTES);
		minInterval = getParameter(config, GlobalConfig.MIN_TRIGGER_INTERVAL_MINUTES, TriggerManagerConstants.DEFAULT_MIN_TRIGGER_INTERVAL_MINUTES);
	}
	
	private int getParameter(GlobalConfig config, String key, int defaultValue)
	{
		int value = defaultValue;
		try
		{
			if (config != null)
			{
				value = (Integer) config.getParameter(key);
			}
		}
		catch (Exception e)
		{
			value = defaultValue;
		}
		return value;
	}
	
	public int pickRandomTimeWithinPreferences()
	{
		int currentMinute = currentMinute();
		int from = max(earlyLimit, currentMinute);
		if (lateLimit - from > 0)
		{
			return random.nextInt(lateLimit - from) + from;
		}
		else
		{
			return TimePreferences.ERROR;
		}
	}
	
	public boolean timeAllowed(int minuteOfDay)
	{
		return (minuteOfDay >= earlyLimit && minuteOfDay <= lateLimit);
	}
	
	public boolean selectedTimeFitsGroup(int selectedTime, ArrayList<Integer> times)
	{
		if (selectedTime == ERROR)
		{
			return false;
		}
		else
		{
			for (Integer time : times)
			{
				if (Math.abs(time.intValue() - selectedTime) <= minInterval)
				{
					return false;
				}
			}
			return true;
		}
	}
	
	private int max(int a, int b)
	{
		if (a > b)
		{
			return a;
		}
		else return b;
	}
	
	public int currentMinute()
	{
		Calendar calendar = Calendar.getInstance();
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);	
		return (60 * hour) + minute;
	}
}
