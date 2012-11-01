package com.ubhave.triggermanager.triggers.sensorbased;

import android.content.Context;

import com.ubhave.sensormanager.ESException;
import com.ubhave.sensormanager.data.SensorData;
import com.ubhave.triggermanager.TriggerException;
import com.ubhave.triggermanager.TriggerReceiver;
import com.ubhave.triggermanager.config.Constants;
import com.ubhave.triggermanager.config.GlobalConfig;

public class TwoStageSensorTrigger extends ImmediateSensorTrigger
{
	private final GlobalConfig config;

	private boolean isDataInteresting;
	private Thread waitThread;

	public TwoStageSensorTrigger(Context context, TriggerReceiver listener, int sensorType) throws TriggerException, ESException
	{
		super(context, listener, sensorType);
		config = GlobalConfig.getGlobalConfig(context);
		isDataInteresting = false;
	}

	@Override
	public void onDataSensed(SensorData sensorData)
	{
		isDataInteresting = classifier.isInteresting(sensorData);
		if (!isDataInteresting)
		{
			if (waitThread != null)
			{
				waitThread.interrupt();
			}
		}
		else
		{
			if (waitThread == null)
			{
				int wait_time;
				try
				{
					wait_time = (Integer) config.getParameter(GlobalConfig.INTER_EVENT_WAIT_TIME_MILLIES);
				}
				catch (TriggerException e)
				{
					wait_time = Constants.DEFAULT_WAIT_TIME_MILLIES;
				}
				startWaiting(wait_time);
			}

		}
	}

	private void doneWaiting()
	{
		if (isDataInteresting)
		{
			callForSurvey();
		}
	}

	private void startWaiting(final int wait_time)
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

						while (waited < wait_time)
						{
							sleep(1000);
							waited += 1000;
						}

						doneWaiting();
					}
					catch (InterruptedException e)
					{
					}
				}
			};
			waitThread.start();
		}
	}

}
