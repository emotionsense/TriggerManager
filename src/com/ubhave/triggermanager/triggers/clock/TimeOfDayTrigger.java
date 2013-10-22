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

import android.content.Context;

import com.ubhave.triggermanager.TriggerException;
import com.ubhave.triggermanager.TriggerReceiver;
import com.ubhave.triggermanager.config.TriggerConfig;
import com.ubhave.triggermanager.config.TriggerManagerConstants;
import com.ubhave.triggermanager.triggers.TriggerUtils;

public class TimeOfDayTrigger extends IntervalTrigger
{
	private final static String TRIGGER_NAME = "TimeOfDayTrigger";

	public TimeOfDayTrigger(Context context, int id, final TriggerReceiver listener, final TriggerConfig parameters) throws TriggerException
	{
		super(context, id, listener, parameters);
	}
	
	@Override
	public String getActionName()
	{
		return TriggerManagerConstants.ACTION_NAME_DAY_INTERVAL_TRIGGER;
	}
	
	@Override
	protected int getRequestCode()
	{
		return TriggerUtils.TYPE_CLOCK_TRIGGER_DAY_INTERVAL;
	}
	
	@Override
	protected String getTriggerTag()
	{
		return TRIGGER_NAME;
	}
	
	@Override
	protected long getStartDelay() throws TriggerException
	{
		if (params.containsKey(TriggerConfig.DAILY_HOUR) && params.containsKey(TriggerConfig.DAILY_MINUTE))
		{
			Calendar calendar = Calendar.getInstance();
			calendar.set(Calendar.HOUR_OF_DAY, ((Long) params.getParameter(TriggerConfig.DAILY_HOUR)).intValue());
			calendar.set(Calendar.MINUTE, ((Long) params.getParameter(TriggerConfig.DAILY_MINUTE)).intValue());
			if (calendar.getTimeInMillis() < System.currentTimeMillis())
			{
				calendar.add(Calendar.DATE, 1);
			}
			return calendar.getTimeInMillis() - System.currentTimeMillis();
		}
		else
		{
			throw new TriggerException(TriggerException.MISSING_PARAMETERS, "Parameters must include TriggerConfig.DAILY_HOUR and TriggerConfig.DAILY_MINUTE");
		}
	}
	
	@Override
	protected long getIntervalLength() throws TriggerException
	{
		return (1000L * 60 * 60 * 24);
	}
}
