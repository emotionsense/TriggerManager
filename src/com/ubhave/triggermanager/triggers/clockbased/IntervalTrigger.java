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

package com.ubhave.triggermanager.triggers.clockbased;

import android.content.Context;

import com.ubhave.triggermanager.TriggerException;
import com.ubhave.triggermanager.TriggerReceiver;
import com.ubhave.triggermanager.config.TriggerConfig;

public class IntervalTrigger extends ClockTrigger
{

	public IntervalTrigger(Context context, TriggerReceiver listener, TriggerConfig parameters) throws TriggerException
	{
		super(context, listener, parameters);
	}

	@Override
	protected void initialise() throws TriggerException
	{
		super.initialise();
		long startDelayInMillis = getStartDelay();
		long intervalLengthInMillis = getIntervalLength();
		try
		{
			surveyTimer.schedule(new SurveyNotification(), startDelayInMillis, intervalLengthInMillis);
		}
		catch (IllegalArgumentException e)
		{
			throw new TriggerException(TriggerException.INVALID_STATE, "Illegal arguments in interval trigger. Start delay: "+startDelayInMillis+", Interval: "+intervalLengthInMillis);
		}
	}
	
	private long getStartDelay() throws TriggerException
	{
		if (params.containsKey(TriggerConfig.INTERVAL_TRIGGER_START_DELAY))
		{
			return (Long) params.getParameter(TriggerConfig.INTERVAL_TRIGGER_START_DELAY);
		}
		else
		{
			throw new TriggerException(TriggerException.MISSING_PARAMETERS, "Parameters must include TriggerConfig.INTERVAL_TRIGGER_START_DELAY");
		}
	}
	
	private long getIntervalLength() throws TriggerException
	{
		if (params.containsKey(TriggerConfig.INTERVAL_TRIGGER_TIME_MILLIS))
		{
			return (Long) params.getParameter(TriggerConfig.INTERVAL_TRIGGER_TIME_MILLIS);
		}
		else
		{
			throw new TriggerException(TriggerException.MISSING_PARAMETERS, "Parameters must include TriggerConfig.INTERVAL_TRIGGER_TIME_MILLIS");
		}
	}
	
	@Override
	protected String getTriggerTag()
	{
		return "IntervalTrigger";
	}
}
