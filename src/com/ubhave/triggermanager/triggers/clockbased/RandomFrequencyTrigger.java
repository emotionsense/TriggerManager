/* **************************************************
 Copyright (c) 2012, University of Cambridge
 Neal Lathia, neal.lathia@cl.cam.ac.uk
 Kiran Rachuri, kiran.rachuri@cl.cam.ac.uk

This library was developed as part of the EPSRC Ubhave (Ubiquitous and
Social Computing for Positive Behaviour Change) Project. For more
information, please visit http://www.emotionsense.org

Permission to use, copy, modify, and/or distribute this software for any
purpose with or without fee is hereby granted, provided that the above
copyright notice and this permission notice appear in all copies.

THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES
WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF
MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY
SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES
WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR
IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 ************************************************** */

package com.ubhave.triggermanager.triggers.clockbased;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.util.Log;

import com.ubhave.triggermanager.TriggerException;
import com.ubhave.triggermanager.TriggerReceiver;
import com.ubhave.triggermanager.config.TriggerConfig;
import com.ubhave.triggermanager.config.TriggerManagerConstants;

public class RandomFrequencyTrigger extends ClockTrigger
{
	private final static String LOG_TAG = "RandomFrequencyTrigger";
	private final static int MAX_SCHEDULING_ATTEMPTS = 500;
	
	private class Scheduler extends TimerTask
	{
		@Override
		public void run()
		{
			try
			{
				scheduleNotifications();
			}
			catch (TriggerException e)
			{
				e.printStackTrace();
			}
		}
	}

	private Timer schedulerTimer;

	public RandomFrequencyTrigger(Context context, TriggerReceiver listener, TriggerConfig params) throws TriggerException
	{
		super(context, listener, params);
	}

	@Override
	public void kill() throws TriggerException
	{
		super.kill();
		if (schedulerTimer != null)
		{
			schedulerTimer.cancel();
			schedulerTimer = null;
		}
	}
	
	@Override
	protected void initialise() throws TriggerException
	{
		super.initialise();
		scheduleDailyUpdate();
		scheduleNotifications();
	}
	
	private void scheduleDailyUpdate()
	{
		if (schedulerTimer != null)
		{
			schedulerTimer.cancel();
			schedulerTimer = null;
		}
		
		schedulerTimer = new Timer();
		Calendar calendar = Calendar.getInstance();
		long now = calendar.getTimeInMillis();

		calendar.add(Calendar.HOUR_OF_DAY, 24 - calendar.get(Calendar.HOUR_OF_DAY));
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);

		// Random surveys will be re-scheduled at midnight each night
		schedulerTimer.scheduleAtFixedRate(new Scheduler(), calendar.getTimeInMillis() - now, 1000 * 60 * 60 * 24);
	}

	private void scheduleNotifications() throws TriggerException
	{
		long midnight = getMidnight();
		ArrayList<Integer> randomTimes = pickTimes();
		if (TriggerManagerConstants.LOG_MESSAGES)
		{
			Log.d(LOG_TAG, "Scheduling: "+randomTimes.size()+" notifications.");
		}
		
		for (Integer time : randomTimes)
		{
			long triggerTime = midnight + (time * 60 * 1000);
			long diff = triggerTime - System.currentTimeMillis();
			if (diff > 0)
			{
				if (TriggerManagerConstants.LOG_MESSAGES)
				{
					Log.d(LOG_TAG, "Notifications scheduled for: " + (new Date(triggerTime)).toString());
				}
				surveyTimer.schedule(new SurveyNotification(), diff);
			}
		}
	}
	
	private long getMidnight()
	{
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		return calendar.getTimeInMillis();
	}

	private ArrayList<Integer> pickTimes() throws TriggerException
	{
		ArrayList<Integer> times = new ArrayList<Integer>();
		try
		{
			TimePreferences preferences = new TimePreferences(context);
			if (TriggerManagerConstants.LOG_MESSAGES)
			{
				Log.d(LOG_TAG, "Max daily allowed is: "+preferences.getDailyCap());
			}
			
			for (int i=0; i<preferences.getDailyCap(); i++)
			{
				boolean entryAdded = false;
				int attempts = 0;
				while (attempts < MAX_SCHEDULING_ATTEMPTS && !entryAdded)
				{
					attempts++;
					int time = preferences.pickRandomTimeWithinPreferences();
					if (preferences.selectedTimeFitsGroup(time, times))
					{
						times.add(time);
						entryAdded = true;
					}
				}
			}
		}
		catch (TriggerException e)
		{
			e.printStackTrace();
		}
		return times;
	}
	
	@Override
	protected String getTriggerTag()
	{
		return LOG_TAG;
	}
}
