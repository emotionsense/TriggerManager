package com.ubhave.triggermanager.config;

public class Constants
{
	public static final boolean TEST_MODE = true;
	protected final static boolean IGNORE_CAP = true;
	protected final static boolean ADHERE_TO_CAP = false;
	
	public final static int NOTIFICATION_ID = 901;
	
	public static final int DEFAULT_DO_NOT_DISTURB_BEFORE 		= 8 * 60;
	public static final int DEFAULT_DO_NOT_DISTURB_AFTER 		= 22 * 60;
	
	public static final int DEFAULT_MAXIMUM_DAILY_SURVEYS 		= 2;
	public static final int DEFAULT_MIN_TRIGGER_INTERVAL_MINUTES = 120;
	public static final int DEFAULT_NOTIFICATION_PROBABILITY	= 1;
	public static final int DEFAULT_SENSE_TIME_MINUTES			= 60;
	
	public final static String GLOBAL_PREFERENCES = "global_preferences";
	public final static String TRIGGER_PREFERENCES = "trigger_preferences";
	public final static String GLOBAL_STATE = "trigger_state";
	
}
