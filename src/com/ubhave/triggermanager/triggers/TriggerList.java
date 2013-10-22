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

import com.ubhave.sensormanager.ESException;
import com.ubhave.triggermanager.AbstractSubscriptionList;
import com.ubhave.triggermanager.TriggerException;
import com.ubhave.triggermanager.TriggerReceiver;
import com.ubhave.triggermanager.config.TriggerConfig;
import com.ubhave.triggermanager.triggers.clock.IntervalTrigger;
import com.ubhave.triggermanager.triggers.clock.OneTimeTrigger;
import com.ubhave.triggermanager.triggers.clock.TimeOfDayTrigger;
import com.ubhave.triggermanager.triggers.clock.random.RandomFrequencyTrigger;
import com.ubhave.triggermanager.triggers.sensor.DelayedSensorTrigger;
import com.ubhave.triggermanager.triggers.sensor.ImmediateSensorTrigger;

public class TriggerList extends AbstractSubscriptionList<Trigger>
{
	public static Trigger createTrigger(Context context, int type, int id, TriggerReceiver listener, TriggerConfig params) throws TriggerException
	{
		if (type == TriggerUtils.TYPE_CLOCK_TRIGGER_ONCE)
		{
			return new OneTimeTrigger(context, id, listener, params);
		}
		else if (type == TriggerUtils.TYPE_CLOCK_TRIGGER_ON_INTERVAL)
		{
			return new IntervalTrigger(context, id, listener, params);
		}
		else if (type == TriggerUtils.TYPE_CLOCK_TRIGGER_DAILY_RANDOM)
		{
			return new RandomFrequencyTrigger(context, id, listener, params);
		}
		else if (type == TriggerUtils.TYPE_CLOCK_TRIGGER_DAY_INTERVAL)
		{
			return new TimeOfDayTrigger(context, id, listener, params);
		}
		else
		{
			return getSensorTrigger(context, type, id, listener, params);
		}
	}
	
	private static Trigger getSensorTrigger(Context context, int type, int id, TriggerReceiver listener, TriggerConfig params) throws TriggerException
	{
		try
		{
			if (type == TriggerUtils.TYPE_SENSOR_TRIGGER_IMMEDIATE)
			{
				return new ImmediateSensorTrigger(context, id, listener, params);
			}
			else if (type == TriggerUtils.TYPE_SENSOR_TRIGGER_DELAYED)
			{
				return new DelayedSensorTrigger(context, id, listener, params);
			}
			else
			{
				throw new TriggerException(TriggerException.INVALID_STATE, "Unknown trigger.");
			}
		}
		catch (ESException e)
		{
			throw new TriggerException(TriggerException.UNABLE_TO_ALLOCATE, "Cannot subscribe to sensor. Do you have battery permissions?");
		}
	}

	@Override
	public void remove(int triggerId) throws TriggerException
	{
		Trigger s = map.get(triggerId);
		if (s != null)
		{
			s.stop();
		}
		super.remove(triggerId);
	}
	
	public void removeAll() throws TriggerException
	{
		for (int i=0; i<map.size(); i++)
		{
			int key = map.keyAt(i);
			Trigger trigger = map.get(key);
			trigger.stop();
		}
		map.clear();
	}
}
