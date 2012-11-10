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
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;

import com.ubhave.sensormanager.logs.ESLogger;
import com.ubhave.triggermanager.TriggerException;
import com.ubhave.triggermanager.TriggerReceiver;
import com.ubhave.triggermanager.config.Constants;
import com.ubhave.triggermanager.config.GlobalConfig;

public class RandomFrequencyTrigger extends ClockTrigger
{
	private final static String LOG_TAG = "RandomFrequencyTrigger";

	private class Scheduler extends TimerTask
	{
		@Override
		public void run()
		{
			scheduleNotifications();
		}
	}

	private Timer schedulerTimer;
	private final Random random;

	public RandomFrequencyTrigger(Context context, TriggerReceiver listener) throws TriggerException
	{
		super(context, listener);
		random = new Random();
		initialise();
	}
	
	protected void initialise()
	{
		scheduleNotifications();

		// Random surveys will be re-scheduled at midnight each night
		schedulerTimer = new Timer();
		Calendar calendar = Calendar.getInstance();
		long now = calendar.getTimeInMillis();

		calendar.add(Calendar.HOUR_OF_DAY, 24 - calendar.get(Calendar.HOUR_OF_DAY));
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);

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
	}

	private void scheduleNotifications()
	{
		int maxSurveys;
		try
		{
			maxSurveys = (Integer) globalConfig.getParameter(GlobalConfig.MAXIMUM_DAILY_SURVEYS);
		}
		catch (TriggerException e)
		{
			maxSurveys = Constants.DEFAULT_MAXIMUM_DAILY_SURVEYS;
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
					ESLogger.log(LOG_TAG, "Notifications scheduled for: " + (new Date(triggerTime)).toString());
				}
				surveyTimer.schedule(new SurveyNotification(), diff);
			}
		}
	}

	private ArrayList<Integer> pickTimes(int frequency)
	{
		ArrayList<Integer> times = new ArrayList<Integer>();
		try
		{
			int before = (Integer) globalConfig.getParameter(GlobalConfig.DO_NOT_DISTURB_BEFORE);
			int after = (Integer) globalConfig.getParameter(GlobalConfig.DO_NOT_DISTURB_AFTER) - 60;
			int interval = (Integer) globalConfig.getParameter(GlobalConfig.MIN_TRIGGER_INTERVAL_MILLIES) / (60 * 1000);

			for (int i = 0; i < frequency; i++)
			{
				try
				{
					int time = next(times, before, after, interval);
					times.add(time);
				}
				catch (NullPointerException e)
				{
					ESLogger.error(LOG_TAG, "Insufficient user preference time allowance for random time trigger.");
					break;
				}
			}
		}
		catch (TriggerException e)
		{
			e.printStackTrace();
		}
		return times;
	}

	private int next(ArrayList<Integer> times, int before, int after, int interval) throws NullPointerException
	{
		int selection = random.nextInt(after - before) + before;
		boolean conflict = false;
		int attempt = 0;

		do
		{
			conflict = false;
			for (Integer time : times)
			{
				if (Math.abs(time - selection) < interval)
				{
					conflict = true;
					selection = random.nextInt(after - before) + before;
					attempt++;
					if (attempt == 500)
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
