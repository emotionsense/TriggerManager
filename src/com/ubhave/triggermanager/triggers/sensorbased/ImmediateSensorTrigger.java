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

import java.util.Random;

import android.content.Context;
import android.util.Log;

import com.ubhave.sensormanager.ESException;
import com.ubhave.sensormanager.ESSensorManager;
import com.ubhave.sensormanager.ESSensorManagerInterface;
import com.ubhave.sensormanager.SensorDataListener;
import com.ubhave.sensormanager.classifier.SensorClassifiers;
import com.ubhave.sensormanager.classifier.SensorDataClassifier;
import com.ubhave.sensormanager.config.SensorConfig;
import com.ubhave.sensormanager.config.SensorManagerConstants;
import com.ubhave.sensormanager.data.SensorData;
import com.ubhave.sensormanager.sensors.SensorUtils;
import com.ubhave.triggermanager.TriggerException;
import com.ubhave.triggermanager.TriggerReceiver;
import com.ubhave.triggermanager.config.TriggerConfig;
import com.ubhave.triggermanager.config.TriggerManagerConstants;
import com.ubhave.triggermanager.triggers.Trigger;

public class ImmediateSensorTrigger extends Trigger implements SensorDataListener
{
	protected ESSensorManagerInterface sensorManager;
	protected SensorDataClassifier classifier;
	private int subscriptionId;

	public ImmediateSensorTrigger(Context context, TriggerReceiver listener, TriggerConfig params) throws TriggerException, ESException
	{
		super(context, listener, params);
	}

	@Override
	protected void initialise() throws TriggerException
	{
		super.initialise();
		int sensorType = getSensorType();
		try
		{
			this.sensorManager = ESSensorManager.getSensorManager(context);
			setupParams(getSensorType(), true);
			this.classifier = SensorClassifiers.getSensorClassifier(sensorType);
			this.subscriptionId = sensorManager.subscribeToSensorData(sensorType, this);
		}
		catch (ESException e)
		{
			throw new TriggerException(TriggerException.UNKNOWN_TRIGGER, "No classifier available for sensor type: "+sensorType);
		}
	}
	
	protected int getSensorType() throws TriggerException
	{
		if (params.containsKey(TriggerConfig.SENSOR_TYPE))
		{
			return (Integer) params.getParameter(TriggerConfig.SENSOR_TYPE);
		}
		else
		{
			throw new TriggerException(TriggerException.MISSING_PARAMETERS, "Sensor Type not specified in parameters.");
		}
	}

	private double notificationProbability()
	{
		if (params.containsKey(TriggerConfig.NOTIFICATION_PROBABILITY))
		{
			return (Double) params.getParameter(TriggerConfig.NOTIFICATION_PROBABILITY);
		}
		else
		{
			return TriggerManagerConstants.DEFAULT_NOTIFICATION_PROBABILITY;
		}
	}

	@Override
	public void onDataSensed(SensorData sensorData)
	{
		if (TriggerManagerConstants.LOG_MESSAGES)
		{
			Log.d("ImmediateSensorTrigger", "onDataSensed() "+sensorData.toString());
		}
		
		if (classifier.isInteresting(sensorData))
		{
			sendNotification();
		}
	}
	
	@Override
	protected void sendNotification()
	{
		double notificationProbability = notificationProbability();
		double p = (new Random()).nextDouble();
		if (p <= notificationProbability)
		{
			super.sendNotification();
		}
	}

	@Override
	public void kill() throws TriggerException
	{
		super.kill();
		try
		{
			setupParams(getSensorType(), false);
			if (sensorManager != null)
			{
				sensorManager.unsubscribeFromSensorData(subscriptionId);
			}
		}
		catch (ESException e)
		{
			throw new TriggerException(TriggerException.INVALID_STATE, "Cannot unsubscribe from sensor subscription.");
		}
	}

	@Override
	public void onCrossingLowBatteryThreshold(boolean isBelowThreshold)
	{
		listener.onCrossingLowBatteryThreshold(isBelowThreshold);
	}

	@Override
	public void pause() throws TriggerException
	{
		super.pause();
		try
		{
			setupParams(getSensorType(), false);
			sensorManager.pauseSubscription(subscriptionId);
		}
		catch (ESException e)
		{
			throw new TriggerException(TriggerException.INVALID_STATE, "Cannot pause sensor subscription.");
		}
	}

	@Override
	public void resume() throws TriggerException
	{
		super.resume();
		try
		{
			setupParams(getSensorType(), true);
			sensorManager.unPauseSubscription(subscriptionId);
		}
		catch (ESException e)
		{
			throw new TriggerException(TriggerException.INVALID_STATE, "Cannot resume sensor subscription.");
		}
	}

	private void setupParams(int sensorType, boolean settingUp) // hack
	{
		if (sensorManager != null)
		{
			try
			{
				switch (sensorType)
				{
				case SensorUtils.SENSOR_TYPE_ACCELEROMETER:
					if (settingUp)
					{
						sensorManager.setSensorConfig(SensorUtils.SENSOR_TYPE_ACCELEROMETER, SensorConfig.POST_SENSE_SLEEP_LENGTH_MILLIS, (long) (1000 * 10));
					}
					else
					{
						sensorManager.setSensorConfig(SensorUtils.SENSOR_TYPE_ACCELEROMETER, SensorConfig.POST_SENSE_SLEEP_LENGTH_MILLIS,
								SensorManagerConstants.ACCELEROMETER_SLEEP_INTERVAL);
					}
					break;
				case SensorUtils.SENSOR_TYPE_MICROPHONE:
					if (settingUp)
					{
						sensorManager.setSensorConfig(SensorUtils.SENSOR_TYPE_ACCELEROMETER, SensorConfig.POST_SENSE_SLEEP_LENGTH_MILLIS, (long) (1000 * 30));
					}
					else
					{
						sensorManager.setSensorConfig(SensorUtils.SENSOR_TYPE_ACCELEROMETER, SensorConfig.POST_SENSE_SLEEP_LENGTH_MILLIS,
								SensorManagerConstants.MICROPHONE_SLEEP_INTERVAL);
					}
					break;
				}
			}
			catch (ESException e)
			{
				e.printStackTrace();
			}
		}
	}

	@Override
	protected String getTriggerTag()
	{
		try
		{
			return "ImmediateSensorTrigger"+SensorUtils.getSensorName(getSensorType());
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return "ImmediateSensorTrigger";
		}
	}
}
