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
	// Clock based Triggers
	public final static String CLOCK_TRIGGER_DATE_MILLIS = "clockTriggerDate";
	public final static String INTERVAL_TRIGGER_TIME_MILLIS = "intervalTriggerTime";
	public final static String INTERVAL_TRIGGER_START_DELAY = "intervalTriggerStartDelay";
	
	// Sensor Based Triggers
	public static final String NOTIFICATION_PROBABILITY = "notificationProb";
	public final static String POST_SENSE_WAIT_INTERVAL_MILLIS = "postSenseWait";
	
	// Hybrid Triggers
	public static final String SENSOR_TRIGGER_WINDOW_MILLIS = "sensorTriggerWindow";
	
	private final HashMap<String, Object> parameters;
	
	public TriggerConfig()
	{
		parameters = new HashMap<String, Object>();
	}
	
	public void addParameter(String key, Object value)
	{
		parameters.put(key, value);
	}
	
	public Object getParameter(String key)
	{
		return parameters.get(key);
	}
	
	public boolean containsKey(String key)
	{
		return parameters.containsKey(key);
	}
}
