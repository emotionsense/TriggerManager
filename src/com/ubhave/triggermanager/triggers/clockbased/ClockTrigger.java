package com.ubhave.triggermanager.triggers.clockbased;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;

import com.ubhave.triggermanager.TriggerException;
import com.ubhave.triggermanager.TriggerReceiver;
import com.ubhave.triggermanager.triggers.Trigger;

public abstract class ClockTrigger extends Trigger
{
	protected Timer surveyTimer;

	protected class SurveyNotification extends TimerTask
	{
		@Override
		public void run()
		{
			callForSurvey();
		}
	}

	public ClockTrigger(Context context, TriggerReceiver listener) throws TriggerException
	{
		super(context, listener);
		surveyTimer = new Timer();
	}
	
	protected abstract void initialise() throws TriggerException;
	
	@Override
	public void kill()
	{
		surveyTimer.cancel();
	}

	@Override
	public void pause()
	{
		kill();
	}

	@Override
	public void resume()
	{
		try {
			initialise();
		}
		catch (TriggerException e)
		{
			e.printStackTrace();
		}
		
	}
}
