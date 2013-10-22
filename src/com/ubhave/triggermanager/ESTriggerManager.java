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

package com.ubhave.triggermanager;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.ubhave.triggermanager.config.GlobalState;
import com.ubhave.triggermanager.config.TriggerConfig;
import com.ubhave.triggermanager.config.TriggerManagerConstants;
import com.ubhave.triggermanager.triggers.Trigger;
import com.ubhave.triggermanager.triggers.TriggerList;
import com.ubhave.triggermanager.triggers.TriggerUtils;

public class ESTriggerManager implements TriggerManagerInterface
{
	private static ESTriggerManager triggerManager;
	private static final Object lock = new Object();

	private final Context context;
	private final GlobalState state;
	private final TriggerList triggers;

	public static ESTriggerManager getTriggerManager(Context context) throws TriggerException
	{
		if (triggerManager == null)
		{
			synchronized (lock)
			{
				if (triggerManager == null)
				{
					triggerManager = new ESTriggerManager(context);
				}
			}
		}
		return triggerManager;
	}

	private ESTriggerManager(final Context appContext) throws TriggerException
	{
		context = appContext;
		triggers = new TriggerList();
		state = GlobalState.getGlobalState(context);
	}

	@Override
	public int addTrigger(int triggerType, TriggerReceiver listener, TriggerConfig parameters) throws TriggerException
	{
		int key = triggers.randomKey();
		Trigger trigger = TriggerList.createTrigger(context, triggerType, key, listener, parameters);
		if (TriggerManagerConstants.LOG_MESSAGES)
		{
			Log.d("TriggerManager", "Adding trigger type: "+TriggerUtils.getTriggerName(triggerType)+" to list, id = "+key);
		}
		trigger.start();
		triggers.add(key, trigger);
		return key;
	}

	@Override
	public void removeTrigger(int triggerId) throws TriggerException
	{
		triggers.remove(triggerId);
	}
	
	@Override
	public void removeAllTriggers() throws TriggerException
	{
		triggers.removeAll();
	}
	
	@Override
	public void resetCap()
	{
		state.reset();
	}
	
	@Override
	public void setNotificationCap(int value)
	{
		state.setNotificationCap(value);
	}
	
	@Override
	public boolean isTriggerAlive(int triggerType, int triggerId) throws TriggerException
	{
		String triggerName = TriggerUtils.getTriggerName(triggerType);
		String actionName = TriggerUtils.getTriggerActionName(triggerName);
		PendingIntent intent = PendingIntent.getBroadcast(context, triggerId, new Intent(actionName), PendingIntent.FLAG_NO_CREATE);
		return (intent != null);
	}
}
