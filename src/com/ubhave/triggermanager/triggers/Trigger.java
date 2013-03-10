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

import android.content.Context;
import android.util.Log;

import com.ubhave.triggermanager.TriggerException;
import com.ubhave.triggermanager.TriggerReceiver;
import com.ubhave.triggermanager.config.GlobalConfig;
import com.ubhave.triggermanager.config.GlobalState;
import com.ubhave.triggermanager.config.TriggerConfig;
import com.ubhave.triggermanager.config.TriggerManagerConstants;
import com.ubhave.triggermanager.triggers.clockbased.TimePreferences;

public abstract class Trigger
{
	protected final int RUNNING = 0;
	protected final int PAUSED = 1;
	protected final int DEAD = 2;
	
	protected final Context context;
	protected final TriggerReceiver listener;
	protected final GlobalState globalState;
	protected final GlobalConfig globalConfig;
	protected TriggerConfig params;
	protected int state;

	public Trigger(Context context, TriggerReceiver listener, TriggerConfig params) throws TriggerException
	{
		this.context = context;
		this.listener = listener;
		this.globalState = GlobalState.getGlobalState(context);
		this.globalConfig = GlobalConfig.getGlobalConfig(context);
		
		reset(params);	
	}

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
			if (notificationsAllowed())
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
	
	private boolean notificationsAllowed()
	{
		boolean triggersAllowed;
		try
		{
			triggersAllowed = (Boolean) globalConfig.getParameter(GlobalConfig.TRIGGERS_ENABLED);
		}
		catch (TriggerException e)
		{
			e.printStackTrace();
			triggersAllowed = TriggerManagerConstants.DEFAULT_TRIGGERS_ENABLED;
		}
		return triggersAllowed;
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
			Log.d("Trigger", "Notifications Sent = "+notificationsSent);
			Log.d("Trigger", "Notifications Allowed = "+notificationsAllowed);
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

	public void kill() throws TriggerException
	{
		state = DEAD;
	}

	public void pause() throws TriggerException
	{
		state = PAUSED;
	}

	public void resume() throws TriggerException
	{
		state = RUNNING;
	}
	
	protected void initialise() throws TriggerException
	{
		state = RUNNING;
	}
	
	protected abstract String getTriggerTag();
	
	public void reset(TriggerConfig params) throws TriggerException
	{
		this.params = params;
		kill();
		initialise();
	}
}
