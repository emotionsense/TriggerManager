/* **************************************************
 Copyright (c) 2012, University of Cambridge
 Neal Lathia, neal.lathia@cl.cam.ac.uk
 Kiran Rachuri, kiran.rachuri@cl.cam.ac.uk

This library was developed as part of the EPSRC Ubhave (Ubiquitous and
Social Computing for Positive Behaviour Change) Project. For more
information, please visit http://www.emotionsense.org

Permission to use, copy, modify, and/or distribute this software for any
purpose with or without fee is hereby granted, provided that the above
copyright notice and this permission notice appear in all copies.

THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES
WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF
MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY
SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES
WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR
IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 ************************************************** */

package com.ubhave.triggermanager.triggers.clock;

import java.util.Calendar;

import android.app.AlarmManager;
import android.content.Context;
import android.util.Log;

import com.ubhave.triggermanager.TriggerException;
import com.ubhave.triggermanager.TriggerReceiver;
import com.ubhave.triggermanager.config.TriggerConfig;
import com.ubhave.triggermanager.config.TriggerManagerConstants;
import com.ubhave.triggermanager.triggers.TriggerUtils;

public class OneTimeTrigger extends AbstractClockTrigger
{
	private final static String TRIGGER_NAME = "OneTimeTrigger";
	
	public OneTimeTrigger(Context context, int id, TriggerReceiver listener, TriggerConfig parameters) throws TriggerException
	{
		super(context, id, listener, parameters);
	}
	
	@Override
	public String getActionName()
	{
		return TriggerManagerConstants.ACTION_NAME_ONE_TIME_TRIGGER;
	}
	
	@Override
	protected int getRequestCode()
	{
		return TriggerUtils.TYPE_CLOCK_TRIGGER_ONCE;
	}
	
	@Override
	protected void startAlarm() throws TriggerException
	{
		long surveyDate = getSurveyDate();
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(surveyDate);
		
		if (surveyDate > System.currentTimeMillis())
		{
			if (TriggerManagerConstants.LOG_MESSAGES)
			{
				Log.d("OneTimeTrigger", "Scheduled time is: "+calendar.getTime().toString());
			}
			alarmManager.set(AlarmManager.RTC_WAKEUP, surveyDate, pendingIntent);
		}
		else
		{	
			throw new TriggerException(TriggerException.DATE_IN_PAST, "Scheduled time is in the past: "+calendar.getTime().toString());
		}
	}
	
	private long getSurveyDate() throws TriggerException
	{
		if (params.containsKey(TriggerConfig.CLOCK_TRIGGER_DATE_MILLIS))
		{
			return (Long) params.getParameter(TriggerConfig.CLOCK_TRIGGER_DATE_MILLIS);
		}
		else throw new TriggerException(TriggerException.MISSING_PARAMETERS, "Parameters must include TriggerConfig.CLOCK_TRIGGER_DATE");
	}
	
	@Override
	protected String getTriggerTag()
	{
		return TRIGGER_NAME;
	}
}
