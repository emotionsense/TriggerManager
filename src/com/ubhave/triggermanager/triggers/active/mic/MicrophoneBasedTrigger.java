package com.ubhave.triggermanager.triggers.active.mic;

import com.ubhave.sensormanager.ESException;
import com.ubhave.sensormanager.ESSensorManager;
import com.ubhave.sensormanager.data.SensorData;
import com.ubhave.triggermanager.triggers.active.RandomFrequencyTrigger;

public abstract class MicrophoneBasedTrigger extends RandomFrequencyTrigger
{

//	private final static String LOG_TAG = "MicrophoneBasedTrigger";
	protected final static int TRIGGER_MAX_CYCLES = 60;
	private int subscriptionId;
	
//	private SensorConfig getSensorConfig()
//	{
//		SensorConfig sensorConfig = AbstractSensor.getDefaultSensorConfig(SensorList.SENSOR_TYPE_MICROPHONE);
//		sensorConfig.set(SensorConfig.SENSOR_SLEEP_INTERVAL, Constants.MICROPHONE_SAMPLING_WINDOW_SIZE_MILLIS);
//		return sensorConfig;
//	}
	
	@Override
	protected void sampleForSurvey()
	{
//		try
//		{
//			if (Constants.TEST_MODE)
//			{
//				ESLogger.log(LOG_TAG, "Pre-survey mic sampling starting");
//			}
//			
//			senseCycle = 0;
//			ESSensorManager sensorManager = ESSensorManager.getSensorManager(manager.getContext());
//			subscriptionId = sensorManager.subscribeToSensorData(SensorList.SENSOR_TYPE_MICROPHONE, this);
//			
//			if (Constants.TEST_MODE)
//			{
//				ESLogger.log(LOG_TAG, "Registered for mic updates");
//			}
//		}
//		catch (ESException e)
//		{
//			ESLogger.log(LOG_TAG, "Exception, sleeping for 10 seconds");	
//			Thread waitThread = new Thread()
//			{
//				@Override
//				public void run()
//				{
//					try
//					{
//						int waited = 0;
//						while (waited < 1000 * 10)
//						{
//							sleep(100);
//							waited += 100;
//						}
//					}
//					catch (InterruptedException e)
//					{
//						// do nothing
//					}
//					finally
//					{
//						sampleForSurvey();
//					}
//				}
//			};
//			waitThread.start();
//		}
	}
	
	protected void unregisterFromMicrophone()
	{
		try {
			ESSensorManager sensorManager = ESSensorManager.getSensorManager(manager.getContext());
			sensorManager.unsubscribeFromSensorData(subscriptionId);
		}
		catch(ESException e)
		{
			// TODO
		}
		
	}

	@Override
	public abstract void onDataSensed(SensorData sensorData);

}
