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

package com.ubhave.triggermanager.config;

import java.util.Calendar;

import com.ubhave.triggermanager.TriggerException;

import android.content.Context;

public class SurveyLimiter
{

	public boolean surveyAllowedNow(Context c)
	{
		try
		{
			GlobalConfig config = GlobalConfig.getGlobalConfig(c);
			GlobalState state = GlobalState.getGlobalState(c);

			Calendar calendar = Calendar.getInstance();
			int maxSurveys = (Integer) config.getParameter(GlobalConfig.MAXIMUM_DAILY_SURVEYS);
			if (state.getNotificationsSent() < maxSurveys)
			{
				if (userAllowsNotification(calendar, config))
				{
					long lastNotification = state.getLastNotificationTime();
					long interval = (Integer) config.getParameter(GlobalConfig.MIN_TRIGGER_INTERVAL_MILLIES);
					if (Math.abs(System.currentTimeMillis() - lastNotification) > interval)
					{
						return true;
					}
				}
			}
		}
		catch (TriggerException e)
		{
			e.printStackTrace();
		}
		return false;
	}

	// TODO
	public boolean userAllowsNotification(Calendar time, GlobalConfig config) throws TriggerException
	{
		int minTime = (Integer) config.getParameter(GlobalConfig.DO_NOT_DISTURB_BEFORE);
		int minHour = (minTime / 2);
		int minMinute;
		if (minHour % 2 == 0)
		{
			minMinute = 0;
		}
		else
		{
			minMinute = 30;
		}

		int maxTime = (Integer) config.getParameter(GlobalConfig.DO_NOT_DISTURB_AFTER);
		int maxHour = (maxTime / 2);
		int maxMinute;
		if (maxHour % 2 == 0)
		{
			maxMinute = 0;
		}
		else
		{
			maxMinute = 30;
		}
		int hour = time.get(Calendar.HOUR_OF_DAY);
		int minute = time.get(Calendar.MINUTE);

		if (hour < minHour || (hour == minHour && minute <= minMinute))
		{
			return false;
		}
		else if (hour > maxHour || (hour == maxHour && minute > maxMinute))
		{
			return false;
		}
		else
		{
			return true;
		}
	}

}
