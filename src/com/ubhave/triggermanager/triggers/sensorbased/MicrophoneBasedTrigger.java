package com.ubhave.triggermanager.triggers.sensorbased;

import android.content.Context;

import com.ubhave.sensormanager.ESException;
import com.ubhave.sensormanager.classifier.MicrophoneDataClassifier;
import com.ubhave.sensormanager.data.SensorData;
import com.ubhave.sensormanager.sensors.SensorUtils;
import com.ubhave.triggermanager.TriggerException;
import com.ubhave.triggermanager.TriggerReceiver;

public abstract class MicrophoneBasedTrigger extends SensorTrigger
{
	private final MicrophoneDataClassifier classifier;
	
	public MicrophoneBasedTrigger(Context context, TriggerReceiver listener) throws TriggerException, ESException
	{
		super(context, listener, SensorUtils.SENSOR_TYPE_MICROPHONE);
		classifier = new MicrophoneDataClassifier();
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
