package com.ubhave.triggermanager.triggers.clockbased;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

import android.content.Context;

import com.ubhave.triggermanager.ESTriggerManager;
import com.ubhave.triggermanager.TriggerException;
import com.ubhave.triggermanager.TriggerReceiver;
import com.ubhave.triggermanager.config.TriggerConfig;
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
		TriggerConfig params = new TriggerConfig();
		params.addParameter(TriggerConfig.INTERVAL_TRIGGER_START_DELAY, millisUntilMidnight());
		params.addParameter(TriggerConfig.INTERVAL_TRIGGER_TIME_MILLIS, DAILY_INTERVAL);
		params.addParameter(TriggerConfig.IGNORE_USER_PREFERENCES, true);
		
		dailySchedulerId = triggerManager.addTrigger(TriggerUtils.CLOCK_TRIGGER_ON_INTERVAL, this, params);
		isSubscribed = true;
	}
	
	public void stop() throws TriggerException
	{
		if (isSubscribed)
		{
			triggerManager.removeTrigger(dailySchedulerId);
			isSubscribed = false;
		}
	}
	
	@Override
	public void onNotificationTriggered(int triggerId)
	{
		if (triggerId == dailySchedulerId)
		{
			ArrayList<Integer> selectedTimes = pickTimes();
			for (Integer time : selectedTimes)
			{
				trigger.subscribeTriggerFor(time);
			}
		}
	}
	
	public ArrayList<Integer> pickTimes()
	{
		ArrayList<Integer> times = new ArrayList<Integer>();
		
		int currentMinute = currentMinute();
		int earlyLimit = params.getValueInMinutes(TriggerConfig.DO_NOT_DISTURB_BEFORE_MINUTES);
		int lateLimit = params.getValueInMinutes(TriggerConfig.DO_NOT_DISTURB_AFTER_MINUTES);
		int minInterval = params.getValueInMinutes(TriggerConfig.MIN_TRIGGER_INTERVAL_MINUTES);
		
		int numberOfNotifications = params.numberOfNotifications();
		for (int i=0; i<numberOfNotifications; i++)
		{
			boolean entryAdded = false;
			int attempts = 0;
			while (attempts < MAX_SCHEDULING_ATTEMPTS && !entryAdded)
			{
				attempts++;
				int time = pickRandomTimeWithinPreferences(currentMinute, earlyLimit, lateLimit);
				if (selectedTimeFitsGroup(time, times, minInterval))
				{
					times.add(time);
					entryAdded = true;
				}
			}
		}
		return times;
	}
	
	private int pickRandomTimeWithinPreferences(int currentMinute, int earlyLimit, int lateLimit)
	{
		int from = max(earlyLimit, currentMinute);
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
	
	private long millisUntilMidnight()
	{
		Calendar calendar = Calendar.getInstance();
		long now = calendar.getTimeInMillis();
		
		calendar.add(Calendar.HOUR_OF_DAY, 24 - calendar.get(Calendar.HOUR_OF_DAY));
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		
		return calendar.getTimeInMillis() - now;
	}
}
