package com.ubhave.triggermanager.triggers.passive;

import java.util.Random;

import android.content.Context;

import com.ubhave.sensormanager.ESException;
import com.ubhave.sensormanager.SensorDataListener;
import com.ubhave.sensormanager.config.Constants;
import com.ubhave.sensormanager.data.SensorData;
import com.ubhave.sensormanager.logs.ESLogger;
import com.ubhave.triggermanager.TriggerReceiver;
import com.ubhave.triggermanager.triggers.Trigger;

public abstract class PassiveTrigger extends Trigger implements SensorDataListener
{	
	
	private final int sensorType, subscriptionId;
	private final String LOG_TAG;
	private final double SAMPLE_PROBABILITY;
	
	public PassiveTrigger(Context context, TriggerReceiver listener, int sensorType, String LOG_TAG, double p) throws ESException
	{
		super(context, listener);
		this.sensorType = sensorType;
		this.LOG_TAG = LOG_TAG;
		this.SAMPLE_PROBABILITY = p;
		subscriptionId = registerWithManager();
	}
	
	private int registerWithManager() throws ESException
	{
		if (Constants.TEST_MODE)
		{
			ESLogger.log(LOG_TAG, "Registering with sensor: "+sensorType);
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
			ESLogger.error(LOG_TAG, e);
		}
	}
	
	@Override
	protected void callForSurvey(boolean adhereToCap)
	{
		double sampleProbability = (new Random()).nextDouble();
		if (sampleProbability <= SAMPLE_PROBABILITY)
		{
			ESLogger.log(LOG_TAG, "Calling for survey!");
			super.callForSurvey(adhereToCap);
		}
		else if (Constants.TEST_MODE)
		{
			ESLogger.log(LOG_TAG, "Not calling for survey: P(sample) = "+sampleProbability);
		}
	}
	
	@Override
	public abstract void onDataSensed(SensorData sensorData);
	
}
