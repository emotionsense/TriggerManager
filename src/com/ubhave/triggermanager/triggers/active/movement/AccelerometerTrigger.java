package com.ubhave.triggermanager.triggers.active.movement;

import android.content.Context;

import com.ubhave.sensormanager.ESException;
import com.ubhave.sensormanager.ESSensorManager;
import com.ubhave.sensormanager.classifier.AccelerometerDataClassifier;
import com.ubhave.sensormanager.data.SensorData;
import com.ubhave.sensormanager.sensors.SensorUtils;
import com.ubhave.triggermanager.TriggerReceiver;
import com.ubhave.triggermanager.triggers.active.ActiveTrigger;

public class AccelerometerTrigger extends ActiveTrigger
{

	private int subscriptionId;
	private final AccelerometerDataClassifier classifier;

	public AccelerometerTrigger(Context context, TriggerReceiver listener)
	{
		super(context, listener);
		classifier = new AccelerometerDataClassifier();
	}

	@Override
	protected void sampleForSurvey()
	{
		try
		{
			ESSensorManager sensorManager = ESSensorManager.getSensorManager();
			subscriptionId = sensorManager.subscribeToSensorData(SensorUtils.SENSOR_TYPE_ACCELEROMETER, this);
		}
		catch (ESException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void onDataSensed(SensorData sensorData)
	{
		if (classifier.isInteresting(sensorData))
		{
			try
			{
				ESSensorManager sensorManager = ESSensorManager.getSensorManager();
				sensorManager.unsubscribeFromSensorData(subscriptionId);
				callForSurvey(true);
			}
			catch (ESException e)
			{
				e.printStackTrace();
			}
		}
	}

}
