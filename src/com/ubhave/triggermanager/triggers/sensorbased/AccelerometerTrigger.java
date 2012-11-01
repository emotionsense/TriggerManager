package com.ubhave.triggermanager.triggers.sensorbased;

import android.content.Context;

import com.ubhave.sensormanager.ESException;
import com.ubhave.sensormanager.classifier.AccelerometerDataClassifier;
import com.ubhave.sensormanager.data.SensorData;
import com.ubhave.sensormanager.sensors.SensorUtils;
import com.ubhave.triggermanager.TriggerException;
import com.ubhave.triggermanager.TriggerReceiver;

public class AccelerometerTrigger extends SensorTrigger
{
	private final AccelerometerDataClassifier classifier;

	public AccelerometerTrigger(Context context, TriggerReceiver listener) throws TriggerException, ESException
	{
		super(context, listener, SensorUtils.SENSOR_TYPE_ACCELEROMETER);
		classifier = new AccelerometerDataClassifier();
	}

	@Override
	public void onDataSensed(SensorData sensorData)
	{
		if (classifier.isInteresting(sensorData))
		{
			callForSurvey();
		}
	}

}
