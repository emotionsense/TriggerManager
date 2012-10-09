package com.ubhave.triggermanager.triggers.active.movement;

import com.ubhave.sensormanager.ESException;
import com.ubhave.sensormanager.ESSensorManager;
import com.ubhave.sensormanager.classifier.AccelerometerDataClassifier;
import com.ubhave.sensormanager.data.SensorData;
import com.ubhave.sensormanager.sensors.SensorList;
import com.ubhave.triggermanager.TriggerReceiver;
import com.ubhave.triggermanager.triggers.active.ActiveTrigger;

public class AccelerometerTrigger extends ActiveTrigger
{
	
	private int subscriptionId;
	private final AccelerometerDataClassifier classifier;

	public AccelerometerTrigger(TriggerReceiver listener)
	{
		super(listener);
		classifier = new AccelerometerDataClassifier();
	}

	@Override
	protected void sampleForSurvey()
	{
		try
		{
			ESSensorManager sensorManager = ESSensorManager.getSensorManager(manager.getContext());
			subscriptionId = sensorManager.subscribeToSensorData(SensorList.SENSOR_TYPE_ACCELEROMETER, this);
		}
		catch (ESException e)
		{
			// TODO Auto-generated catch block
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
				ESSensorManager sensorManager = ESSensorManager.getSensorManager(manager.getContext());
				sensorManager.unsubscribeFromSensorData(subscriptionId);
				callForSurvey(true);
			}
			catch (ESException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

}
