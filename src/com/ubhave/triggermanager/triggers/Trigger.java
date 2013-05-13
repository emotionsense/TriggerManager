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

package com.ubhave.triggermanager.triggers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import com.ubhave.triggermanager.TriggerException;
import com.ubhave.triggermanager.TriggerReceiver;
import com.ubhave.triggermanager.config.GlobalConfig;
import com.ubhave.triggermanager.config.GlobalState;
import com.ubhave.triggermanager.config.TriggerConfig;
import com.ubhave.triggermanager.config.TriggerManagerConstants;
import com.ubhave.triggermanager.triggers.clockbased.TimePreferences;

public abstract class Trigger extends BroadcastReceiver
{
	private final static String TRIGGER_ID = "com.ubhave.triggermanager.triggers.TRIGGER_ID";
	protected final AlarmManager alarmManager;
	protected final PendingIntent pendingIntent;

	protected final Context context;
	private final int triggerId;
	protected final TriggerReceiver listener;
	protected final GlobalState globalState;
	protected final GlobalConfig globalConfig;

	protected TriggerConfig params;
	private boolean isRunning;

	public Trigger(Context context, int id, TriggerReceiver listener, TriggerConfig params) throws TriggerException
	{
		this.context = context;
		this.triggerId = id;
		this.listener = listener;
		this.params = params;
		this.isRunning = false;
		
		this.globalState = GlobalState.getGlobalState(context);
		this.globalConfig = GlobalConfig.getGlobalConfig(context);
		
		alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		pendingIntent = getPendingIntent();
	}

	protected abstract PendingIntent getPendingIntent();

	protected abstract String getTriggerTag();

	protected abstract void startAlarm() throws TriggerException;

	protected void sendNotification()
	{
		if (isSystemTrigger())
		{
			if (TriggerManagerConstants.LOG_MESSAGES)
			{
				Log.d(getTriggerTag(), "Sending system-level onNotificationTriggered()");
			}
			listener.onNotificationTriggered();
		}
		else
		{
			if (globalConfig.notificationsAllowed())
			{
				if (belowDailyCap())
				{
					if (isWithinAllowedTimes())
					{
						listener.onNotificationTriggered();
						globalState.incrementNotificationsSent();
					}
					else if (TriggerManagerConstants.LOG_MESSAGES)
					{
						Log.d(getTriggerTag(), "Notification scheduled outside of allowed times");
					}
				}
				else if (TriggerManagerConstants.LOG_MESSAGES)
				{
					Log.d(getTriggerTag(), "Notifications not allowed: daily cap has been reached.");
				}
			}
			else if (TriggerManagerConstants.LOG_MESSAGES)
			{
				Log.d(getTriggerTag(), "Notifications not allowed: not calling onNotificationTriggered()");
			}
		}
	}

	private boolean belowDailyCap()
	{
		int notificationsSent, notificationsAllowed;
		try
		{
			notificationsSent = globalState.getNotificationsSent();
			notificationsAllowed = (Integer) globalConfig.getParameter(GlobalConfig.MAX_DAILY_NOTIFICATION_CAP);
		}
		catch (TriggerException e)
		{
			e.printStackTrace();
			notificationsSent = 0;
			notificationsAllowed = TriggerManagerConstants.DEFAULT_DAILY_NOTIFICATION_CAP;
		}
		if (TriggerManagerConstants.LOG_MESSAGES)
		{
			Log.d("Trigger", "Notifications Sent = " + notificationsSent);
			Log.d("Trigger", "Notifications Allowed = " + notificationsAllowed);
		}

		return (notificationsSent < notificationsAllowed);
	}

	private boolean isWithinAllowedTimes()
	{
		TimePreferences timePreferences = new TimePreferences(context);
		return timePreferences.timeAllowed(timePreferences.currentMinute());
	}

	private boolean isSystemTrigger()
	{
		if (params.containsKey(TriggerConfig.IGNORE_USER_PREFERENCES))
		{
			return (Boolean) params.getParameter(TriggerConfig.IGNORE_USER_PREFERENCES);
		}
		else
		{
			return TriggerManagerConstants.DEFAULT_IS_TRIGGER_UNCAPPED;
		}
	}

	public void stop() throws TriggerException
	{
		if (isRunning)
		{
			Log.d(getTriggerTag(), "Stopping...");
			alarmManager.cancel(pendingIntent);
			context.unregisterReceiver(this);
			isRunning = false;
		}
		else
		{
			Log.d(getTriggerTag(), "Already stopped!");
		}
	}
	
	protected abstract String getActionName();

	public void start() throws TriggerException
	{
		if (!isRunning)
		{
			Log.d(getTriggerTag(), "Starting...");
			IntentFilter intentFilter = new IntentFilter(getActionName());
			context.registerReceiver(this, intentFilter);
			
			startAlarm();
			isRunning = true;
		}
		else
		{
			Log.d(getTriggerTag(), "Already started!");
		}
	}

	public void reset(TriggerConfig params) throws TriggerException
	{
		this.params = params;
		if (isRunning)
		{
			stop();
			start();
		}
	}

	@Override
	public void onReceive(final Context context, final Intent intent)
	{
		Log.d("Trigger", "onReceive = " + (listener == null));
		if (listener != null)
		{
			int id = intent.getIntExtra(TRIGGER_ID, -1);
			if (triggerId == id)
			{
				sendNotification();
			}
		}
	}
}
