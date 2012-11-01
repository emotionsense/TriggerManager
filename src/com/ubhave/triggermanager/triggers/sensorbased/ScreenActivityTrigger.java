package com.ubhave.triggermanager.triggers.sensorbased;

import android.content.Context;

import com.ubhave.sensormanager.ESException;
import com.ubhave.sensormanager.data.SensorData;
import com.ubhave.sensormanager.data.pushsensor.ScreenData;
import com.ubhave.sensormanager.sensors.SensorUtils;
import com.ubhave.triggermanager.TriggerException;
import com.ubhave.triggermanager.TriggerReceiver;

public class ScreenActivityTrigger extends SensorTrigger
{
	private boolean screenOn;
	private Thread waitThread;

	public ScreenActivityTrigger(Context context, TriggerReceiver listener) throws TriggerException, ESException
	{
		super(context, listener, SensorUtils.SENSOR_TYPE_SCREEN);
	}

	@Override
	public void onDataSensed(SensorData sensorData)
	{
		ScreenData screen = (ScreenData) sensorData;
		if (screen.isOn())
		{
			screenOn = true;
			startWaiting();
		}
		else
		{
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
						int wait_time = 5000;//((new Random()).nextInt(60)+30)*1000;
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
		if (screenOn)
		{
			callForSurvey();
		}
	}
}
