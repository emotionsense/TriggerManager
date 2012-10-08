package com.ubhave.triggermanager.triggers.passive;

import java.util.Random;

import com.lathia.experiencesense.SurveyApplication;
import com.lathia.experiencesense.log.ESLogger;
import com.lathia.experiencesense.userprefs.UserPreferences;
import com.lathia.experiencesense.util.Constants;
import com.ubhave.sensormanager.ESException;
import com.ubhave.sensormanager.ESSensorManager;
import com.ubhave.sensormanager.config.SensorConfig;
import com.ubhave.sensormanager.data.SensorData;
import com.ubhave.sensormanager.sensors.AbstractSensor;
import com.ubhave.sensormanager.sensors.SensorDataListener;
import com.ubhave.triggermanager.triggers.Trigger;

public abstract class PassiveTrigger extends Trigger implements SensorDataListener
{	
	
	private final static long SURVEY_INTERVAL = 1000 * 60 * 60 * 2;
	
	private final int sensorType;
	private final String LOG_TAG;
	private final double SAMPLE_PROBABILITY;
	
	public PassiveTrigger(String survey, int sensorType, String LOG_TAG, double p)
	{
		super(survey);
		this.sensorType = sensorType;
		this.LOG_TAG = LOG_TAG;
		this.SAMPLE_PROBABILITY = p;
		registerWithManager();
	}
	
	private void registerWithManager()
	{
		try
		{
			if (Constants.TEST_MODE)
			{
				ESLogger.log(LOG_TAG, "Registering with sensor: "+sensorType);
			}
			ESSensorManager manager = ESSensorManager.getSensorManager(SurveyApplication.getContext());
			SensorConfig config = AbstractSensor.getDefaultSensorConfig(sensorType);
			manager.registerSensorDataListener(sensorType, config, this);
		}
		catch (ESException e)
		{
			if (Constants.TEST_MODE)
			{
				ESLogger.log(LOG_TAG, "Exception, wating 10 seconds ("+sensorType+")");
			}
			Thread waitThread = new Thread()
			{
				@Override
				public void run()
				{
					try
					{
						int waited = 0;
						while (waited < 1000 * 10)
						{
							sleep(100);
							waited += 100;
						}
					}
					catch (InterruptedException e)
					{
						// do nothing
					}
					finally
					{
						registerWithManager();
					}
				}
			};
			waitThread.start();
		}
	}
	
	@Override
	public void kill()
	{
		try
		{
			ESSensorManager manager = ESSensorManager.getSensorManager(SurveyApplication.getContext());
			manager.unregisterSensorDataListener(sensorType, this);
		}
		catch (ESException e)
		{
			ESLogger.error(LOG_TAG, e);
		}
	}
	
	@Override
	protected void callForSurvey(boolean adhereToCap)
	{
		long lastNotification = UserPreferences.lastNotification();
		if (Math.abs(System.currentTimeMillis() - lastNotification) > SURVEY_INTERVAL)
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
		else if (Constants.TEST_MODE)
		{
			ESLogger.log(LOG_TAG, "Not proceeding to survey (min inter-survey interval not met)");
		}
	}
	
	@Override
	public abstract void onDataSensed(SensorData sensorData);
	
}
