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
import com.ubhave.sensormanager.sensors.SensorUtils;
import com.ubhave.triggermanager.AbstractSubscriptionList;
import com.ubhave.triggermanager.TriggerException;
import com.ubhave.triggermanager.TriggerReceiver;
import com.ubhave.triggermanager.config.TriggerConfig;
import com.ubhave.triggermanager.triggers.clockbased.IntervalTrigger;
import com.ubhave.triggermanager.triggers.clockbased.OneTimeTrigger;
import com.ubhave.triggermanager.triggers.clockbased.RandomFrequencyTrigger;
import com.ubhave.triggermanager.triggers.hybrid.HybridTrigger;
import com.ubhave.triggermanager.triggers.sensorbased.SensorTrigger;

public class TriggerList extends AbstractSubscriptionList<Trigger>
{

	public static Trigger createTrigger(Context context, int type, TriggerReceiver listener, TriggerConfig params) throws ESException, TriggerException
	{
		switch (type)
		{
		case TriggerUtils.CLOCK_TRIGGER_ONCE:
			return new OneTimeTrigger(context, listener, params);
		case TriggerUtils.CLOCK_TRIGGER_ON_INTERVAL:
			return new IntervalTrigger(context, listener, params);
		case TriggerUtils.CLOCK_TRIGGER_DAILY_RANDOM:
			return new RandomFrequencyTrigger(context, listener, params);

		case TriggerUtils.HYBRID_RANDOM_MICROPHONE:
			return new HybridTrigger(context, TriggerUtils.CLOCK_TRIGGER_DAILY_RANDOM, TriggerUtils.SENSOR_TRIGGER_MICROPHONE, listener, params);
		case TriggerUtils.HYBRID_RANDOM_ACCELEROMETER:
			return new HybridTrigger(context, TriggerUtils.CLOCK_TRIGGER_DAILY_RANDOM, TriggerUtils.SENSOR_TRIGGER_ACCELEROMETER, listener, params);

		default:
			try
			{
				SensorUtils.getSensorName(type);
				try
				{
					SensorTrigger trigger = new SensorTrigger(context, listener, type, params);
					return trigger;
				}
				catch (ESException e)
				{
					throw new TriggerException(TriggerException.UNABLE_TO_ALLOCATE, "Cannot subscribe to sensor. Do you have battery permissions?");
				}
			}
			catch (ESException e)
			{
				e.printStackTrace();
				throw new TriggerException(TriggerException.INVALID_STATE, "Type unknown: " + type);
			}
		}
	}

	@Override
	public void remove(int triggerId)
	{
		Trigger s = map.get(triggerId);
		if (s != null)
		{
			s.kill();
			map.delete(triggerId);
		}
	}
}
