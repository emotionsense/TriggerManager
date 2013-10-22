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

import java.util.HashMap;

public class TriggerConfig
{	
	/*
	 * Config Keys
	 */
	public static final String IGNORE_USER_PREFERENCES = "ignoreCap";
	public static final String MAX_DAILY_NOTIFICATION_CAP = "limitDailyCap";
	public static final String TRIGGER_ENABLED = "isEnabled";
	
	// Clock based Triggers
	public final static String CLOCK_TRIGGER_DATE_MILLIS = "clockTriggerDate";
	public final static String INTERVAL_TIME_MILLIS = "intervalTriggerTime";
	public final static String INTERVAL_TRIGGER_START_DELAY = "intervalTriggerStartDelay";
	public final static String NUMBER_OF_NOTIFICATIONS = "numberOfNotifications";
	
	public final static String DAILY_HOUR = "dailyHour";
	public final static String DAILY_MINUTE = "dailyMinute";
	
	// Sensor Based Triggers
	public final static String SENSOR_TYPE = "sensorType";
	public final static String NOTIFICATION_PROBABILITY = "notificationProb";
	public final static String POST_SENSE_WAIT_INTERVAL_MILLIS = "postSenseWait";
	
	// Time Boundaries
	public static final String DO_NOT_DISTURB_BEFORE_MINUTES = "limitBeforeHour";
	public static final String DO_NOT_DISTURB_AFTER_MINUTES = "limitAfterHour";
	public static final String MIN_TRIGGER_INTERVAL_MINUTES = "notificationMinInterval";
	
	private final HashMap<String, Object> parameters;
	
	public TriggerConfig()
	{
		parameters = new HashMap<String, Object>();
	}
	
	public void addParameter(final String key, final Object value)
	{
		parameters.put(key, value);
	}
	
	public Object getParameter(final String key)
	{
		if (parameters.containsKey(key))
		{
			return parameters.get(key);
		}
		else
		{
			return defaultValue(key);
		}
	}
	
	private Object defaultValue(final String key)
	{
		if (key.equals(DO_NOT_DISTURB_BEFORE_MINUTES))
		{
			return TriggerManagerConstants.DEFAULT_DO_NOT_DISTURB_BEFORE_MINUTES;
		}
		else if (key.equals(DO_NOT_DISTURB_AFTER_MINUTES))
		{
			return TriggerManagerConstants.DEFAULT_DO_NOT_DISTURB_AFTER_MINUTES;
		}
		else if (key.equals(MIN_TRIGGER_INTERVAL_MINUTES))
		{
			return TriggerManagerConstants.DEFAULT_MIN_TRIGGER_INTERVAL_MINUTES;
		}
		else if (key.equals(MAX_DAILY_NOTIFICATION_CAP))
		{
			return TriggerManagerConstants.DEFAULT_DAILY_NOTIFICATION_CAP;
		}
		else if (key.equals(TRIGGER_ENABLED))
		{
			return TriggerManagerConstants.DEFAULT_TRIGGER_ENABLED;
		}
		else if (key.equals(DO_NOT_DISTURB_BEFORE_MINUTES))
		{
			return TriggerManagerConstants.DEFAULT_DO_NOT_DISTURB_BEFORE_MINUTES;
		}
		else if (key.equals(DO_NOT_DISTURB_AFTER_MINUTES))
		{
			return TriggerManagerConstants.DEFAULT_DO_NOT_DISTURB_AFTER_MINUTES;
		}
		else if (key.equals(MIN_TRIGGER_INTERVAL_MINUTES))
		{
			return TriggerManagerConstants.DEFAULT_MIN_TRIGGER_INTERVAL_MINUTES;
		}
		else if (key.equals(IGNORE_USER_PREFERENCES))
		{
			return TriggerManagerConstants.DEFAULT_IS_SYSTEM_TRIGGER;
		}
		else
		{
			System.err.println("Key not found: "+key);
			return null;
		}
	}
	
	public boolean containsKey(String key)
	{
		return parameters.containsKey(key);
	}
	
	public boolean isSystemTrigger()
	{
		return (Boolean) getParameter(TriggerConfig.IGNORE_USER_PREFERENCES);
	}
	
	public int numberOfNotifications()
	{
		try
		{
			return (Integer) getParameter(TriggerConfig.NUMBER_OF_NOTIFICATIONS);
		}
		catch (java.lang.ClassCastException e)
		{
			return ((Long) getParameter(TriggerConfig.NUMBER_OF_NOTIFICATIONS)).intValue();
		}	
	}
	
	public int getValueInMinutes(String key)
	{
		return (Integer) getParameter(key);
	}
}
