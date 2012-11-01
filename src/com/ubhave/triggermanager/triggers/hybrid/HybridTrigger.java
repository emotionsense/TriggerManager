package com.ubhave.triggermanager.triggers.hybrid;

import android.content.Context;

import com.ubhave.sensormanager.ESException;
import com.ubhave.triggermanager.TriggerException;
import com.ubhave.triggermanager.TriggerReceiver;
import com.ubhave.triggermanager.config.Constants;
import com.ubhave.triggermanager.config.GlobalConfig;
import com.ubhave.triggermanager.triggers.Trigger;
import com.ubhave.triggermanager.triggers.TriggerList;

public class HybridTrigger extends Trigger implements TriggerReceiver
{
	private final Trigger clockTrigger;
	private final SensorTriggerListener sensorListener;

	private final GlobalConfig config;

	private Thread waitThread;

	public HybridTrigger(Context context, int clockType, int sensorType, TriggerReceiver listener) throws TriggerException, ESException
	{
		super(context, listener);
		this.clockTrigger = TriggerList.createTrigger(context, clockType, this);
		sensorListener = new SensorTriggerListener(context, sensorType, this);

		this.config = GlobalConfig.getGlobalConfig(context);
	}

	@Override
	public void onNotificationTriggered()
	{
		if (waitThread == null)
		{
			int wait_time;
			try
			{
				wait_time = (Integer) config.getParameter(GlobalConfig.SENSE_CYCLE_TOTAL_TIME_MILLIES);
			}
			catch (TriggerException e)
			{
				wait_time = Constants.DEFAULT_WAIT_TIME_MILLIES;
			}
			sensorListener.resume();
			startWaiting(wait_time);
		}
		else
		{
			waitThread.interrupt();
			callForSurvey();
		}
	}

	@Override
	public void onCrossingLowBatteryThreshold(boolean isBelowThreshold)
	{
		if (isBelowThreshold)
		{
			clockTrigger.pause();
		}
		else
		{
			clockTrigger.resume();
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
						callForSurvey();
					}
					catch (InterruptedException e)
					{
					}
				}
			};
			waitThread.start();
		}
	}

	@Override
	public void kill()
	{
		clockTrigger.kill();
		sensorListener.kill();
	}

	@Override
	public void pause()
	{
		clockTrigger.pause();
		sensorListener.pause();
	}

	@Override
	public void resume()
	{
		clockTrigger.resume();
	}
}
