package com.ubhave.triggermanager.triggers.clockbased;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

import android.content.Context;

import com.ubhave.triggermanager.TriggerException;
import com.ubhave.triggermanager.config.GlobalConfig;

public class TimePreferences
{
	public final static int ERROR = -1;
	
	private final int earlyLimit;
	private final int lateLimit;
	private final int maxDaily;
	private final int minInterval;
	private final Random random;
	
	public TimePreferences(Context context) throws TriggerException
	{
		random = new Random();
		
		GlobalConfig config = GlobalConfig.getGlobalConfig(context);
		earlyLimit = (Integer) config.getParameter(GlobalConfig.DO_NOT_DISTURB_BEFORE_MINUTES);
		lateLimit = (Integer) config.getParameter(GlobalConfig.DO_NOT_DISTURB_AFTER_MINUTES);
		minInterval = (Integer) config.getParameter(GlobalConfig.MIN_TRIGGER_INTERVAL_MINUTES);
		maxDaily = (Integer) config.getParameter(GlobalConfig.MAX_DAILY_NOTIFICATION_CAP);
	}
	
	public int getDailyCap()
	{
		return maxDaily;
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
	
	private int currentMinute()
	{
		Calendar calendar = Calendar.getInstance();
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);
		
		return (60 * hour) + minute;
	}
	
	
}
