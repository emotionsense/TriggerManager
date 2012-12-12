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

package com.ubhave.triggermanager.triggers.sensorbased;

import android.content.Context;

import com.ubhave.sensormanager.ESException;
import com.ubhave.sensormanager.ESSensorManager;
import com.ubhave.sensormanager.ESSensorManagerInterface;
import com.ubhave.sensormanager.SensorDataListener;
import com.ubhave.sensormanager.classifier.SensorClassifiers;
import com.ubhave.sensormanager.classifier.SensorDataClassifier;
import com.ubhave.sensormanager.data.SensorData;
import com.ubhave.triggermanager.TriggerException;
import com.ubhave.triggermanager.TriggerReceiver;
import com.ubhave.triggermanager.config.TriggerConfig;
import com.ubhave.triggermanager.triggers.Trigger;

public class SensorTrigger extends Trigger implements SensorDataListener
{	
	private final ESSensorManagerInterface sensorManager;
	private final int subscriptionId;
	private boolean isDataInteresting;
	
	private Thread waitThread;
	private long waitTimeInMillis;
	
	protected final SensorDataClassifier classifier;
	
	
	public SensorTrigger(Context context, TriggerReceiver listener, int sensorType, TriggerConfig params) throws TriggerException, ESException
	{
		super(context, listener);
		sensorManager = ESSensorManager.getSensorManager(context);
		
		classifier = SensorClassifiers.getSensorClassifier(sensorType);
		subscriptionId = sensorManager.subscribeToSensorData(sensorType, this);
		
		if (params.containsKey(TriggerConfig.POST_SENSE_WAIT_INTERVAL_MILLIS))
		{
			waitTimeInMillis = (Long) params.getParameter(TriggerConfig.POST_SENSE_WAIT_INTERVAL_MILLIS);
		}
		else waitTimeInMillis = 0;
	}
	
	@Override
	public void onDataSensed(SensorData sensorData)
	{
		isDataInteresting = classifier.isInteresting(sensorData);
		if (!isDataInteresting)
		{
			if (waitThread != null)
			{
				waitThread.interrupt();
			}
		}
		else
		{
			if (waitThread == null)
			{
				startWaiting();
			}

		}
	}
	
	private void startWaiting()
	{
		if (this.waitTimeInMillis > 0)
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
							int waited = 0;

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
		else doneWaiting();
	}
	
	private void doneWaiting()
	{
		if (isDataInteresting)
		{
			callForSurvey();
		}
	}

	@Override
	public void kill()
	{
		try
		{
			sensorManager.unsubscribeFromSensorData(subscriptionId);
		}
		catch (ESException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void onCrossingLowBatteryThreshold(boolean isBelowThreshold)
	{
		listener.onCrossingLowBatteryThreshold(isBelowThreshold);
	}

	@Override
	public void pause()
	{
		try
		{
			sensorManager.pauseSubscription(subscriptionId);
		}
		catch (ESException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void resume()
	{
		try
		{
			sensorManager.unPauseSubscription(subscriptionId);
		}
		catch (ESException e)
		{
			e.printStackTrace();
		}
	}
}
