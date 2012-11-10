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
import com.ubhave.sensormanager.config.SensorConfig;
import com.ubhave.sensormanager.data.SensorData;
import com.ubhave.sensormanager.logs.ESLogger;
import com.ubhave.sensormanager.sensors.SensorUtils;
import com.ubhave.triggermanager.TriggerException;
import com.ubhave.triggermanager.TriggerReceiver;
import com.ubhave.triggermanager.config.Constants;
import com.ubhave.triggermanager.triggers.Trigger;

public class ImmediateSensorTrigger extends Trigger implements SensorDataListener
{
	private final static String LOG_TAG = "SensorTrigger";
	
	protected final ESSensorManagerInterface sensorManager;
	protected final SensorDataClassifier classifier;
	
	private final int sensorType;
	private final int subscriptionId;

	public ImmediateSensorTrigger(Context context, TriggerReceiver listener, int sensorType) throws TriggerException, ESException
	{
		super(context, listener);
		sensorManager = ESSensorManager.getSensorManager(context);
		
		// hack!!
		sensorManager.setSensorConfig(SensorUtils.SENSOR_TYPE_ACCELEROMETER, SensorConfig.SENSE_WINDOW_LENGTH_MILLIS, (long) 5000);
		sensorManager.setSensorConfig(SensorUtils.SENSOR_TYPE_ACCELEROMETER, SensorConfig.POST_SENSE_SLEEP_LENGTH_MILLIS, (long) 1000);
		
		classifier = SensorClassifiers.getSensorClassifier(sensorType);
		
		this.sensorType = sensorType;
		subscriptionId = registerWithManager();
	}
	
	@Override
	public void onDataSensed(SensorData sensorData)
	{
		if (classifier.isInteresting(sensorData))
		{
			callForSurvey();
		}
	}

	private int registerWithManager() throws ESException
	{
		if (Constants.TEST_MODE)
		{
			ESLogger.log(LOG_TAG, "Registering with sensor: " + sensorType);
		}
		return sensorManager.subscribeToSensorData(sensorType, this);
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
