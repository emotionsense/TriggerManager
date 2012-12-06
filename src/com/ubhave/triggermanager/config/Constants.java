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

public class Constants
{
	public final static boolean LOG_MESSAGES = true;
	
	
	protected final static boolean IGNORE_CAP = true;
	protected final static boolean ADHERE_TO_CAP = false;

	public final static int NOTIFICATION_ID = 901;

	public static final int DEFAULT_DO_NOT_DISTURB_BEFORE = 8 * 60;
	public static final int DEFAULT_DO_NOT_DISTURB_AFTER = 22 * 60;

	public static final int DEFAULT_MAXIMUM_DAILY_SURVEYS = 2;
	public static final int DEFAULT_MIN_TRIGGER_INTERVAL_MILLIES = 120 * 60 * 1000;
	public static final int DEFAULT_NOTIFICATION_PROBABILITY = 1;

	public static final int DEFAULT_WAIT_TIME_MILLIES = 5 * 60 * 1000;
	public static final int DEFAULT_SENSE_TIME_MILLIES = 60 * 60 * 1000;

	public final static String GLOBAL_PREFERENCES = "global_preferences";
	public final static String TRIGGER_PREFERENCES = "trigger_preferences";
	public final static String GLOBAL_STATE = "trigger_state";

}
