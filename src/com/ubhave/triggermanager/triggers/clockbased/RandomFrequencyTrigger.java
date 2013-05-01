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

import java.util.HashSet;

import android.app.PendingIntent;
import android.content.Context;
import android.util.Log;

import com.ubhave.triggermanager.ESTriggerManager;
import com.ubhave.triggermanager.TriggerException;
import com.ubhave.triggermanager.TriggerReceiver;
import com.ubhave.triggermanager.config.TriggerConfig;
import com.ubhave.triggermanager.config.TriggerManagerConstants;
import com.ubhave.triggermanager.triggers.Trigger;
import com.ubhave.triggermanager.triggers.TriggerUtils;

public class RandomFrequencyTrigger extends Trigger implements TriggerReceiver
{
	private final static String LOG_TAG = "RandomFrequencyTrigger";
	
	private final ESTriggerManager triggerManager;
	private final DailyNotificationScheduler dailySchedulerAlarm;
	private HashSet<Integer> randomlySelectedTriggerIds;

	public RandomFrequencyTrigger(Context context, int id, TriggerReceiver listener, TriggerConfig params) throws TriggerException
	{
		super(context, id, listener, params);
		this.triggerManager = ESTriggerManager.getTriggerManager(context);
		this.dailySchedulerAlarm = new DailyNotificationScheduler(context, params, this);
		this.randomlySelectedTriggerIds = new HashSet<Integer>();
	}
	
	public void subscribeTriggerFor(long millis)
	{
		try
		{
			TriggerConfig params = new TriggerConfig();
			params.addParameter(TriggerConfig.CLOCK_TRIGGER_DATE_MILLIS, millis);
			
			int triggerId = triggerManager.addTrigger(TriggerUtils.CLOCK_TRIGGER_ONCE, this, params);
			randomlySelectedTriggerIds.add(triggerId);
			Log.d("RandomFrequency", "Trigger subscribed: "+triggerId);
		}
		catch (TriggerException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	protected String getActionName()
	{
		return TriggerManagerConstants.ACTION_NAME_RANDOM_FREQUENCY_TRIGGER;
	}
	
	@Override
	protected PendingIntent getPendingIntent()
	{
		return null; // Unused
	}
	
	@Override
	public void start() throws TriggerException
	{
		if (!isRunning)
		{
			Log.d("RandomFrequency", "Starting...");
			dailySchedulerAlarm.start();
			isRunning = true;
		}
	}
	
	@Override
	public void stop() throws TriggerException
	{
		if (isRunning)
		{
			Log.d("RandomFrequency", "Stopping...");
			dailySchedulerAlarm.stop();
			for (Integer triggerId : this.randomlySelectedTriggerIds)
			{
				triggerManager.removeTrigger(triggerId);
			}
			isRunning = false;
		}
	}
	
	@Override
	protected String getTriggerTag()
	{
		return LOG_TAG;
	}

	@Override
	protected void startAlarm() throws TriggerException
	{
		// Nothing to do
	}

	@Override
	public void onNotificationTriggered(int triggerId)
	{
		if (randomlySelectedTriggerIds.contains(triggerId))
		{
			Log.d("RandomFrequency", "onNotificationTriggered known = "+triggerId);
			listener.onNotificationTriggered(triggerId);
		}
		else
		{
			Log.d("RandomFrequency", "onNotificationTriggered unknown = "+triggerId);
		}
	}
}
