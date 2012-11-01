package com.ubhave.triggermanager.triggers.hybrid;

import android.content.Context;

import com.ubhave.sensormanager.ESException;
import com.ubhave.triggermanager.TriggerException;
import com.ubhave.triggermanager.TriggerReceiver;
import com.ubhave.triggermanager.config.Constants;
import com.ubhave.triggermanager.config.GlobalConfig;
import com.ubhave.triggermanager.config.GlobalState;
import com.ubhave.triggermanager.triggers.Trigger;
import com.ubhave.triggermanager.triggers.TriggerList;

public class HybridTrigger extends Trigger implements TriggerReceiver
{
	private final Trigger clockTrigger;
	private final Trigger sensorTrigger;
	private final GlobalConfig config;
	private final GlobalState state;
	
	private Thread waitThread;
	private int wait_time;

	public HybridTrigger(Context context, int clockType, int sensorType, TriggerReceiver listener) throws TriggerException, ESException
	{
		super(context, listener);
		this.clockTrigger = TriggerList.createTrigger(context, clockType, this);
		this.sensorTrigger = TriggerList.createTrigger(context, sensorType, listener);
		this.config = GlobalConfig.getGlobalConfig(context);
		this.state = GlobalState.getGlobalState(context);
		
		sensorTrigger.pause();
	}

	@Override
	public void onNotificationTriggered()
	{
		try
		{
			wait_time = (Integer) config.getParameter(GlobalConfig.SENSE_TIME) * (60 * 1000);
		}
		catch (TriggerException e)
		{
			wait_time = Constants.DEFAULT_SENSE_TIME_MINUTES * (60 * 1000);
		}
		
		startWaiting(wait_time);
		sensorTrigger.resume();
	}

	private void doneWaiting()
	{
		sensorTrigger.pause();
		long time = state.getLastNotificationTime();
		if (Math.abs(System.currentTimeMillis() - time) > wait_time)
		{
			// event did not happen during waiting time
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
					}
					catch (InterruptedException e)
					{
					}
					finally
					{
						doneWaiting();
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
		sensorTrigger.kill();
	}

	@Override
	public void pause()
	{
		clockTrigger.pause();
		sensorTrigger.pause();
	}

	@Override
	public void resume()
	{
		clockTrigger.resume();
		sensorTrigger.resume();
	}
}
