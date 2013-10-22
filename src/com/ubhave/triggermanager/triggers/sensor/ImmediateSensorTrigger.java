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

import java.util.Random;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.ubhave.sensormanager.ESException;
import com.ubhave.sensormanager.ESSensorManager;
import com.ubhave.sensormanager.ESSensorManagerInterface;
import com.ubhave.sensormanager.SensorDataListener;
import com.ubhave.sensormanager.classifier.SensorClassifiers;
import com.ubhave.sensormanager.classifier.SensorDataClassifier;
import com.ubhave.sensormanager.config.sensors.pull.AccelerometerConfig;
import com.ubhave.sensormanager.config.sensors.pull.MicrophoneConfig;
import com.ubhave.sensormanager.config.sensors.pull.PullSensorConfig;
import com.ubhave.sensormanager.data.SensorData;
import com.ubhave.sensormanager.sensors.SensorUtils;
import com.ubhave.triggermanager.TriggerException;
import com.ubhave.triggermanager.TriggerReceiver;
import com.ubhave.triggermanager.config.TriggerConfig;
import com.ubhave.triggermanager.config.TriggerManagerConstants;
import com.ubhave.triggermanager.triggers.Trigger;
import com.ubhave.triggermanager.triggers.TriggerUtils;

public class ImmediateSensorTrigger extends Trigger implements SensorDataListener
{
	/*
	 * WARNING -- Obsolete
	 */
	
	protected ESSensorManagerInterface sensorManager;
	protected SensorDataClassifier classifier;
	private int subscriptionId;

	public ImmediateSensorTrigger(Context context, int id, TriggerReceiver listener, TriggerConfig params) throws TriggerException, ESException
	{
		super(context, id, listener, params);
	}
	
	@Override
	public String getActionName()
	{
		return TriggerManagerConstants.ACTION_NAME_SENSOR_TRIGGER_IMMEDIATE;
	}

	@Override
	public void start() throws TriggerException
	{
		super.start();
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
	public void stop() throws TriggerException
	{
		super.stop();
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
						sensorManager.setSensorConfig(SensorUtils.SENSOR_TYPE_ACCELEROMETER, PullSensorConfig.POST_SENSE_SLEEP_LENGTH_MILLIS, (long) (1000 * 10));
					}
					else
					{
						sensorManager.setSensorConfig(SensorUtils.SENSOR_TYPE_ACCELEROMETER, PullSensorConfig.POST_SENSE_SLEEP_LENGTH_MILLIS,
								AccelerometerConfig.DEFAULT_SLEEP_INTERVAL);
					}
					break;
				case SensorUtils.SENSOR_TYPE_MICROPHONE:
					if (settingUp)
					{
						sensorManager.setSensorConfig(SensorUtils.SENSOR_TYPE_ACCELEROMETER, PullSensorConfig.POST_SENSE_SLEEP_LENGTH_MILLIS, (long) (1000 * 30));
					}
					else
					{
						sensorManager.setSensorConfig(SensorUtils.SENSOR_TYPE_ACCELEROMETER, PullSensorConfig.POST_SENSE_SLEEP_LENGTH_MILLIS,
								MicrophoneConfig.DEFAULT_SLEEP_INTERVAL);
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

	@Override
	protected PendingIntent getPendingIntent()
	{
		int requestCode = TriggerUtils.TYPE_CLOCK_TRIGGER_ONCE;
		Intent intent = new Intent(TriggerManagerConstants.ACTION_NAME_ONE_TIME_TRIGGER);
		return PendingIntent.getBroadcast(context, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
	}

	@Override
	protected void startAlarm() throws TriggerException
	{
		// TODO Auto-generated method stub
		
	}
}
