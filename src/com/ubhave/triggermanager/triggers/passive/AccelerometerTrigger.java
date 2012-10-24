package com.ubhave.triggermanager.triggers.passive;

import android.content.Context;
import android.util.Log;

import com.ubhave.sensormanager.ESException;
import com.ubhave.sensormanager.classifier.AccelerometerDataClassifier;
import com.ubhave.sensormanager.config.SensorConfig;
import com.ubhave.sensormanager.data.SensorData;
import com.ubhave.sensormanager.sensors.SensorUtils;
import com.ubhave.triggermanager.TriggerReceiver;

public class AccelerometerTrigger extends PassiveTrigger
{
	private final static String LOG_TAG = "AccelerometerTrigger";
	private final AccelerometerDataClassifier classifier;

	public AccelerometerTrigger(Context context, TriggerReceiver listener) throws ESException
	{
		super(context, listener, SensorUtils.SENSOR_TYPE_ACCELEROMETER, LOG_TAG, 1.0);
		sensorManager.setSensorConfig(SensorUtils.SENSOR_TYPE_ACCELEROMETER, SensorConfig.SENSE_WINDOW_LENGTH_MILLIS, (long) 5000);
		sensorManager.setSensorConfig(SensorUtils.SENSOR_TYPE_ACCELEROMETER, SensorConfig.SLEEP_WINDOW_LENGTH_MILLIS, (long) 1000);

		classifier = new AccelerometerDataClassifier();
	}

	@Override
	public void onDataSensed(SensorData sensorData)
	{
		Log.d("AccelerometerTrigger", sensorData.toString());
		if (classifier.isInteresting(sensorData))
		{
			Log.d("AccelerometerTrigger", "Movement");
			callForSurvey(true);
		}
		else
		{
			Log.d("AccelerometerTrigger", "No Movement");
		}
	}

}
