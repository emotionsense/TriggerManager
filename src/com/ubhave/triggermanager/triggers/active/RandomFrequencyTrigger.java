package com.ubhave.triggermanager.triggers.active;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import com.ubhave.sensormanager.config.Constants;
import com.ubhave.sensormanager.logs.ESLogger;
import com.ubhave.triggermanager.TriggerReceiver;
import com.ubhave.triggermanager.preferences.UserPreferences;

public abstract class RandomFrequencyTrigger extends ActiveTrigger
{
	
	/*
	 * An active trigger is one that samples from a sensor in order
	 * to check whether to start a survey.
	 * 
	 * The RandomFrequencyTrigger starts this sampling at random times
	 * during the day.
	 */
	
	private final static String LOG_TAG = "RandomFrequencyTrigger";
	protected int senseCycle;

	private class SurveyNotification extends TimerTask
	{
		@Override
		public void run()
		{
			if (Constants.TEST_MODE)
			{
				ESLogger.log(LOG_TAG, "Random Timer firing");
			}
			sampleForSurvey();
		}
	}
	
	private class Scheduler extends TimerTask
	{
		@Override
		public void run()
		{
			scheduleNotifications();
		}
	}

	private Timer schedulerTimer;
	private Random random;

	public RandomFrequencyTrigger(TriggerReceiver listener)
	{
		super(listener);
		random = new Random();
		scheduleNotifications();
		
		// Random surveys will be re-scheduled at midnight each night
		schedulerTimer = new Timer();
		Calendar calendar = Calendar.getInstance();
		long now = calendar.getTimeInMillis();
		
		calendar.add(Calendar.HOUR_OF_DAY, 24 - calendar.get(Calendar.HOUR_OF_DAY));
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		
		if (Constants.TEST_MODE)
		{
			ESLogger.log(LOG_TAG, "Notifications will be scheduled at: "+calendar.getTime().toString());
		}
		schedulerTimer.scheduleAtFixedRate(new Scheduler(), calendar.getTimeInMillis() - now, 1000 * 60 * 60 * 24);
	}
	
	public void kill()
	{
		super.kill();
		if (schedulerTimer != null)
		{
			schedulerTimer.cancel();
			schedulerTimer = null;
		}
		if (Constants.TEST_MODE)
		{
			ESLogger.log(LOG_TAG, "Killing: "+LOG_TAG);
		}
	}
	
	private void scheduleNotifications()
	{
		int maxSurveys = manager.getPreferences().getSurveyCap();
		if (Constants.TEST_MODE)
		{
			ESLogger.log(LOG_TAG, "Scheduling "+maxSurveys+" notifications");
		}
		ArrayList<Integer> randomTimes = pickTimes(maxSurveys);
		
		Calendar calendar = Calendar.getInstance();
		long now = calendar.getTimeInMillis();
		
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		long midnight = calendar.getTimeInMillis();
		
		for (Integer time : randomTimes)
		{
			long triggerTime = midnight + (time * 60 * 1000);	
			long diff = triggerTime - now;
			if (diff > 0)
			{	
				if (Constants.TEST_MODE)
				{
					ESLogger.log(LOG_TAG, "Notifications scheduled for: "+(new Date(triggerTime)).toString());
				}
				surveyTimer.schedule(new SurveyNotification(), diff);
			}
		}
	}
	
	private ArrayList<Integer> pickTimes(int frequency)
	{
		UserPreferences preferences = manager.getPreferences();
		
		int before = minutes(preferences.getBeforeTime());
		int after = minutes(preferences.getAfterTime()) - 60;
		if (Constants.TEST_MODE)
		{
			ESLogger.log(LOG_TAG, "User preference: ["+before+", "+after+"]");
		}
		
		ArrayList<Integer> times = new ArrayList<Integer>();
		for (int i=0; i<frequency; i++)
		{
			try {
				int time = next(times, before, after);
				times.add(time);
			}
			catch(NullPointerException e)
			{
				ESLogger.error(LOG_TAG, "Insufficient user preference time allowance for random time trigger.");
				break;
			}
		}
		return times;
	}
	
	private int minutes(int bin)
	{
		int minutes = (bin / 2) * 60;
		if (bin % 2 != 0) minutes += 30;
		return minutes;
	}
	
	private int next(ArrayList<Integer> times, int before, int after) throws NullPointerException
	{	
		int selection = random.nextInt(after - before) + before;
		boolean conflict = false;
		int attempt = 0;
		
		do
		{
			conflict = false;
			for (Integer time : times)
			{
				if (Math.abs(time - selection) < 120) // 2 hours
				{
					conflict = true;
					selection = random.nextInt(after - before) + before;
					attempt++;
					if (attempt == 100)
					{
						throw new NullPointerException();
					}
					break;
				}
			}
		} while (conflict);
		
		return selection;
	}
}
