package com.ubhave.triggermanager.triggers.clockbased;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;

import com.ubhave.triggermanager.TriggerException;
import com.ubhave.triggermanager.TriggerReceiver;
import com.ubhave.triggermanager.config.Constants;
import com.ubhave.triggermanager.triggers.Trigger;

public class OneTimeTrigger extends Trigger
{
	protected Timer surveyTimer;

	private class SurveyNotification extends TimerTask
	{
		@Override
		public void run()
		{
			callForSurvey();
		}
	}

	public OneTimeTrigger(Context context, TriggerReceiver listener, Calendar surveyDate) throws TriggerException
	{
		super(context, listener);
		this.surveyTimer = new Timer();
		
		long waitTime = surveyDate.getTimeInMillis() - System.currentTimeMillis();
		if (waitTime > 0)
		{
			surveyTimer.schedule(new SurveyNotification(), waitTime);
		}
		else if (Constants.TEST_MODE)
		{
			throw new TriggerException(TriggerException.DATE_IN_PAST, "Scheduled time is in the past: "+surveyDate.getTime().toString());
		}
	}
	
	@Override
	public void kill()
	{
		surveyTimer.cancel();
	}

}
