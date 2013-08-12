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

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import com.ubhave.triggermanager.TriggerException;
import com.ubhave.triggermanager.TriggerReceiver;
import com.ubhave.triggermanager.config.GlobalState;
import com.ubhave.triggermanager.config.TriggerConfig;
import com.ubhave.triggermanager.config.TriggerManagerConstants;

public abstract class Trigger extends BroadcastReceiver
{
	protected final static String TRIGGER_ID = "com.ubhave.triggermanager.triggers.TRIGGER_ID";
	
	protected final AlarmManager alarmManager;
	protected final PendingIntent pendingIntent;

	protected final int triggerId;
	protected final Context context;
	protected final TriggerReceiver listener;
	protected final GlobalState globalState;

	protected TriggerConfig params;
	protected boolean isRunning;

	public Trigger(Context context, int id, TriggerReceiver listener, TriggerConfig params) throws TriggerException
	{
		this.context = context;
		this.triggerId = id;
		this.listener = listener;
		this.params = params;
		this.isRunning = false;
		
		this.globalState = GlobalState.getGlobalState(context);
		alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		pendingIntent = getPendingIntent();
	}

	protected abstract PendingIntent getPendingIntent();

	protected abstract String getTriggerTag();
	
	public abstract String getActionName();

	protected abstract void startAlarm() throws TriggerException;

	protected void sendNotification()
	{
		if (params.isSystemTrigger())
		{
			if (TriggerManagerConstants.LOG_MESSAGES)
			{
				Log.d(getTriggerTag(), "Sending system-level onNotificationTriggered()");
			}
			listener.onNotificationTriggered(triggerId);
		}
		else
		{
			if (globalState.areNotificationsAllowed())
			{
				int notificationsSent = globalState.getNotificationsSent();
				int notificationCap = globalState.getNotificationCap();
				if (notificationsSent < notificationCap)
				{
					if (isWithinAllowedTimes())
					{
						listener.onNotificationTriggered(triggerId);
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

	private boolean isWithinAllowedTimes()
	{
		Calendar calendar = Calendar.getInstance();	
		int currentMinute = (60 * calendar.get(Calendar.HOUR_OF_DAY)) + calendar.get(Calendar.MINUTE);
		int earlyLimit = params.getValueInMinutes(TriggerConfig.DO_NOT_DISTURB_AFTER_MINUTES);
		int lateLimit = params.getValueInMinutes(TriggerConfig.DO_NOT_DISTURB_BEFORE_MINUTES);
		
		return (currentMinute < earlyLimit
				|| currentMinute > lateLimit);
	}

	public void stop() throws TriggerException
	{
		if (isRunning)
		{
			alarmManager.cancel(pendingIntent);
			context.unregisterReceiver(this);
			isRunning = false;
		}
	}

	public void start() throws TriggerException
	{
		if (!isRunning)
		{
			IntentFilter intentFilter = new IntentFilter(getActionName());
			context.registerReceiver(this, intentFilter);	
			startAlarm();
			isRunning = true;
		}
	}

	@Override
	public void onReceive(final Context context, final Intent intent)
	{
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
