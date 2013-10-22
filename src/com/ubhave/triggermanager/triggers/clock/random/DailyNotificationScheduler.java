package com.ubhave.triggermanager.triggers.clock.random;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

import android.content.Context;
import android.util.Log;

import com.ubhave.triggermanager.ESTriggerManager;
import com.ubhave.triggermanager.TriggerException;
import com.ubhave.triggermanager.TriggerReceiver;
import com.ubhave.triggermanager.config.TriggerConfig;
import com.ubhave.triggermanager.config.TriggerManagerConstants;
import com.ubhave.triggermanager.triggers.TriggerUtils;

public class DailyNotificationScheduler implements TriggerReceiver
{
	private final static long DAILY_INTERVAL = 1000 * 60 * 60 * 24;
	private final static int MAX_SCHEDULING_ATTEMPTS = 1000;
	public final static int ERROR = -1;
	
	private final ESTriggerManager triggerManager;
	private final RandomFrequencyTrigger trigger;
	
	private int dailySchedulerId;
	private boolean isSubscribed;
	private final Random random;
	private TriggerConfig params;
	
	public DailyNotificationScheduler(Context context, TriggerConfig params, RandomFrequencyTrigger trigger) throws TriggerException
	{
		random = new Random();
		random.setSeed(System.currentTimeMillis());
		
		this.triggerManager = ESTriggerManager.getTriggerManager(context);
		this.params = params;
		this.trigger = trigger;
		this.isSubscribed = false;
	}
	
	public void start() throws TriggerException
	{
		if (isSubscribed)
		{
			stop();
		}
		
		scheduleNotifications();	
		TriggerConfig params = new TriggerConfig();
		params.addParameter(TriggerConfig.INTERVAL_TRIGGER_START_DELAY, startDelay());
		params.addParameter(TriggerConfig.INTERVAL_TIME_MILLIS, schedulerInterval());
		params.addParameter(TriggerConfig.IGNORE_USER_PREFERENCES, true);
		
		dailySchedulerId = triggerManager.addTrigger(TriggerUtils.TYPE_CLOCK_TRIGGER_ON_INTERVAL, this, params);
		isSubscribed = true;
	}
	
	private long schedulerInterval()
	{
		if (params.containsKey(TriggerConfig.INTERVAL_TIME_MILLIS))
		{
			return (Long) params.getParameter(TriggerConfig.INTERVAL_TIME_MILLIS);
		}
		else
		{
			return DAILY_INTERVAL;
		}
	}
	
	private long startDelay()
	{
		if (params.containsKey(TriggerConfig.INTERVAL_TRIGGER_START_DELAY))
		{
			return (Long) params.getParameter(TriggerConfig.INTERVAL_TRIGGER_START_DELAY);
		}
		else
		{
			// Milliseconds until midnight
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.HOUR_OF_DAY, 24 - calendar.get(Calendar.HOUR_OF_DAY));
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			
			return calendar.getTimeInMillis() - System.currentTimeMillis();
		}
	}
	
	public void stop() throws TriggerException
	{
		if (isSubscribed)
		{
			Log.d("Daily", "stop()");
			triggerManager.removeTrigger(dailySchedulerId);
			isSubscribed = false;
		}
	}
	
	@Override
	public void onNotificationTriggered(int triggerId)
	{
		if (triggerId == dailySchedulerId)
		{
			scheduleNotifications();
		}
	}
	
	private void scheduleNotifications()
	{
		ArrayList<Integer> times = new ArrayList<Integer>();
		int currentMinute = currentMinute();
		int earlyLimit = params.getValueInMinutes(TriggerConfig.DO_NOT_DISTURB_BEFORE_MINUTES);
		int lateLimit = params.getValueInMinutes(TriggerConfig.DO_NOT_DISTURB_AFTER_MINUTES);
		int minInterval = params.getValueInMinutes(TriggerConfig.MIN_TRIGGER_INTERVAL_MINUTES);
		
		int numberOfNotifications = params.numberOfNotifications();
		Log.d("Daily", "scheduleNotifications(), "+numberOfNotifications);
		if (TriggerManagerConstants.LOG_MESSAGES)
		{
			Log.d("Daily Scheduler", "Attempting to schedule: "+numberOfNotifications);
		}
		
		for (int t=0; t<numberOfNotifications; t++)
		{
			boolean entryAdded = false;
			for (int i=0; i<MAX_SCHEDULING_ATTEMPTS && !entryAdded; i++)
			{
				int time = pickRandomTimeWithinPreferences(currentMinute, earlyLimit, lateLimit);
				if (selectedTimeFitsGroup(time, times, minInterval))
				{
					for (int j=0; j<times.size(); j++)
					{
						if (times.get(j) > time)
						{
							times.add(j, time);
							entryAdded = true;
							break;
						}
					}
					if (!entryAdded)
					{
						times.add(time);
						entryAdded = true;
					}
				}
			}
		}
		
		if (TriggerManagerConstants.LOG_MESSAGES)
		{
			Log.d("Daily Scheduler", "Selected: "+times.size());
		}
		
		Calendar calendar = Calendar.getInstance();
		for (Integer minuteOfDay : times)
		{
			calendar.set(Calendar.HOUR_OF_DAY, (minuteOfDay / 60));
			calendar.set(Calendar.MINUTE, (minuteOfDay % 60));
			trigger.subscribeTriggerFor(calendar.getTimeInMillis());
		}
	}
	
	private int pickRandomTimeWithinPreferences(int currentMinute, int earlyLimit, int lateLimit)
	{
		int from = max(earlyLimit, currentMinute+1);
		if (lateLimit - from > 0)
		{
			return random.nextInt(lateLimit - from) + from;
		}
		else
		{
			return DailyNotificationScheduler.ERROR;
		}
	}
	
	private boolean selectedTimeFitsGroup(int selectedTime, ArrayList<Integer> times, int minInterval)
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
