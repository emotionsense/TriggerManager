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

public class TriggerManagerConstants
{
	public final static boolean LOG_MESSAGES = true;
	
	public static final boolean DEFAULT_TRIGGER_ENABLED = true;
	public static final boolean DEFAULT_IS_TRIGGER_UNCAPPED = false;
	public static final boolean DEFAULT_IS_SYSTEM_TRIGGER = false;
	
	public static final int DEFAULT_DO_NOT_DISTURB_BEFORE_MINUTES = 8 * 60;
	public static final int DEFAULT_DO_NOT_DISTURB_AFTER_MINUTES = 22 * 60;
	public static final int DEFAULT_MIN_TRIGGER_INTERVAL_MINUTES = 120;
	public static final int DEFAULT_DAILY_NOTIFICATION_CAP = 2;
	public static final int DEFAULT_NUMBER_OF_NOTIFICATIONS = 2;

	public static final int DEFAULT_MAXIMUM_DAILY_SURVEYS = 2;
	public static final int DEFAULT_NOTIFICATION_PROBABILITY = 1;

	public final static String GLOBAL_PREFERENCES = "global_preferences";
	public final static String TRIGGER_PREFERENCES = "trigger_preferences";
	public final static String GLOBAL_STATE = "triggerManager_state";
	
	private final static String ACTION_NAME_ROOT = "com.ubhave.triggermanager.triggers";
	public final static String ACTION_NAME_ONE_TIME_TRIGGER = ACTION_NAME_ROOT+".clockbased.ONE_TIME_TRIGGER";
	public final static String ACTION_NAME_INTERVAL_TRIGGER = ACTION_NAME_ROOT+".clockbased.INTERVAL_TRIGGER";
	public final static String ACTION_NAME_RANDOM_DAY_TRIGGER = ACTION_NAME_ROOT+".clockbased.RANDOM_TRIGGER";
	public final static String ACTION_NAME_DAY_INTERVAL_TRIGGER = ACTION_NAME_ROOT+".clockbased.DAY_INTERVAL_TRIGGER";
	
	public final static String ACTION_NAME_SENSOR_TRIGGER_IMMEDIATE = ACTION_NAME_ROOT+".sensorbased.SENSOR_IMMEDIATE";
	public final static String ACTION_NAME_SENSOR_TRIGGER_DELAYED = ACTION_NAME_ROOT+".sensorbased.SENSOR_DELAYED";

}
