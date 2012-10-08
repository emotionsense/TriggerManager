package com.ubhave.triggermanager.triggers.passive;

import java.util.Random;

import com.ubhave.sensormanager.ESException;
import com.ubhave.sensormanager.config.Constants;
import com.ubhave.sensormanager.data.SensorData;
import com.ubhave.sensormanager.data.pushsensor.ScreenData;
import com.ubhave.sensormanager.logs.ESLogger;
import com.ubhave.sensormanager.sensors.SensorList;
import com.ubhave.triggermanager.TriggerException;
import com.ubhave.triggermanager.TriggerReceiver;

public class ScreenActivityTrigger extends PassiveTrigger
{
	private final static String LOG_TAG = "ScreenActivityTrigger";
	private boolean screenOn;
	private Thread waitThread;

	public ScreenActivityTrigger(TriggerReceiver listener) throws TriggerException, ESException
	{
		super(listener, SensorList.SENSOR_TYPE_SCREEN, LOG_TAG, 0.3);
	}

	@Override
	public void onDataSensed(SensorData sensorData)
	{
		ScreenData screen = (ScreenData) sensorData;
		if (screen.isOn())
		{
			if (Constants.TEST_MODE)
			{
				ESLogger.log(LOG_TAG, "Screen turned on, starting to wait");
			}
			screenOn = true;
			startWaiting();
		}
		else
		{
			if (Constants.TEST_MODE)
			{
				ESLogger.log(LOG_TAG, "Screen turned off");
			}
			screenOn = false;
		}
	}

	private void startWaiting()
	{
		if (waitThread == null)
		{
			waitThread = new Thread()
			{
				@Override
				public void run()
				{
					try
					{
						int waited = 0;
						int wait_time = ((new Random()).nextInt(60)+30)*1000;
						if (Constants.TEST_MODE)
						{
							ESLogger.log(LOG_TAG, "Waiting for: "+wait_time);
						}
						while (waited < wait_time)
						{
							sleep(100);
							waited += 100;
						}
					}
					catch (InterruptedException e)
					{}
					finally
					{
						doneWaiting();
					}
				}
			};
			waitThread.start();
		}
	}

	private void doneWaiting()
	{
		waitThread = null;
		if (Constants.TEST_MODE)
		{
			ESLogger.log(LOG_TAG, "Wait thread finished");
		}
		if (screenOn)
		{
			if (Constants.TEST_MODE)
			{
				ESLogger.log(LOG_TAG, "Calling for survey");
			}
			callForSurvey(ADHERE_TO_CAP);
		}
		else if (Constants.TEST_MODE)
		{
			ESLogger.log(LOG_TAG, "Screen turned off, not calling for survey");
		}
	}
}
