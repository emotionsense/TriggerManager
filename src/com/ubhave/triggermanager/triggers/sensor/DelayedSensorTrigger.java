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

package com.ubhave.triggermanager.triggers.sensor;

import android.content.Context;

import com.ubhave.sensormanager.ESException;
import com.ubhave.sensormanager.data.SensorData;
import com.ubhave.sensormanager.sensors.SensorUtils;
import com.ubhave.triggermanager.TriggerException;
import com.ubhave.triggermanager.TriggerReceiver;
import com.ubhave.triggermanager.config.TriggerConfig;
import com.ubhave.triggermanager.config.TriggerManagerConstants;

public class DelayedSensorTrigger extends ImmediateSensorTrigger
{
	private Thread waitThread;
	private boolean isDataInteresting;

	public DelayedSensorTrigger(Context context, int id, TriggerReceiver listener, TriggerConfig params) throws TriggerException, ESException
	{
		super(context, id, listener, params);
	}

	private long postSenseWaitInterval()
	{
		if (params.containsKey(TriggerConfig.POST_SENSE_WAIT_INTERVAL_MILLIS))
		{
			return (Long) params.getParameter(TriggerConfig.POST_SENSE_WAIT_INTERVAL_MILLIS);
		}
		else
		{
			return 0;
		}
	}

	@Override
	public void onDataSensed(SensorData sensorData)
	{
		isDataInteresting = classifier.isInteresting(sensorData);
		if (isDataInteresting)
		{
			if (waitThread == null)
			{
				startWaiting();
			}
		}
		else
		{
			if (waitThread != null)
			{
				waitThread.interrupt();
			}
		}
	}

	private void startWaiting()
	{
		final long waitTimeInMillis = postSenseWaitInterval();
		if (waitTimeInMillis > 0)
		{
			if (waitThread == null)
			{
				waitThread = new Thread()
				{
					@Override
					public void run()
					{
						try
						{
							long waited = 0;
							while (waited < waitTimeInMillis)
							{
								sleep(1000);
								waited += 1000;
							}
							doneWaiting();
						}
						catch (InterruptedException e)
						{
						}
					}
				};
				waitThread.start();
			}
		}
		else
		{
			doneWaiting();
		}	
	}

	private void doneWaiting()
	{
		if (isDataInteresting)
		{
			waitThread = null;
			sendNotification();
		}
	}

	@Override
	protected String getTriggerTag()
	{
		try
		{
			return "DelayedSensorTrigger"+SensorUtils.getSensorName(getSensorType());
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return "DelayedSensorTrigger";
		}
	}
	
	@Override
	public String getActionName()
	{
		return TriggerManagerConstants.ACTION_NAME_SENSOR_TRIGGER_DELAYED;
	}
}
