package com.ubhave.triggermanager.triggers.clockbased;

import java.util.Calendar;

import android.content.Context;

import com.ubhave.triggermanager.TriggerException;
import com.ubhave.triggermanager.TriggerReceiver;
import com.ubhave.triggermanager.config.Constants;

public class OneTimeTrigger extends ClockTrigger
{
	private final Calendar surveyDate;

	public OneTimeTrigger(Context context, TriggerReceiver listener, Calendar surveyDate) throws TriggerException
	{
		super(context, listener);
		this.surveyDate = surveyDate;
		initialise();
	}
	
	protected void initialise() throws TriggerException
	{
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
}
