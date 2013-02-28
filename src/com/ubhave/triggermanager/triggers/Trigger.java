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
import com.ubhave.triggermanager.config.TriggerManagerConstants;
import com.ubhave.triggermanager.config.GlobalConfig;
import com.ubhave.triggermanager.config.GlobalState;
import com.ubhave.triggermanager.config.TriggerConfig;

public abstract class Trigger
{

	protected final TriggerReceiver listener;
	protected final GlobalState globalState;
	protected final GlobalConfig globalConfig;
	private final boolean systemTrigger;

	public Trigger(Context context, TriggerReceiver listener, TriggerConfig params) throws TriggerException
	{
		this.listener = listener;
		this.globalState = GlobalState.getGlobalState(context);
		this.globalConfig = GlobalConfig.getGlobalConfig(context);

		if (params.containsKey(TriggerConfig.IGNORE_USER_PREFERENCES))
		{
			systemTrigger = (Boolean) params.getParameter(TriggerConfig.IGNORE_USER_PREFERENCES);
		}
		else
			systemTrigger = false;
	}

	protected void sendNotification()
	{
		if (systemTrigger)
		{
			listener.onNotificationTriggered();
		}
		else
		{
			boolean triggersAllowed;
			int notificationsSent, notificationsAllowed;
			try
			{
				notificationsAllowed = (Integer) globalConfig.getParameter(GlobalConfig.MAX_DAILY_NOTIFICATION_CAP);
				triggersAllowed = (Boolean) globalConfig.getParameter(GlobalConfig.TRIGGERS_ENABLED);
				notificationsSent = globalState.getNotificationsSent();
			}
			catch (TriggerException e)
			{
				notificationsSent = 0;
				notificationsAllowed = TriggerManagerConstants.DEFAULT_DAILY_NOTIFICATION_CAP;
				triggersAllowed = TriggerManagerConstants.DEFAULT_TRIGGERS_ENABLED;
			}

			if (triggersAllowed && notificationsSent < notificationsAllowed)
			{
				listener.onNotificationTriggered();
				globalState.incrementNotificationsSent();
			}
			else if (TriggerManagerConstants.LOG_MESSAGES)
			{
				Log.d("Trigger", "Not sending notification: "+notificationsSent+" (sent) >= "+notificationsAllowed+" (allowed), triggers allowed: "+triggersAllowed);
			}
		}
	}

	public abstract void kill();

	public abstract void pause();

	public abstract void resume();

}
