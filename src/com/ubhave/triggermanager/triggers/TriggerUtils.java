package com.ubhave.triggermanager.triggers;

import com.ubhave.triggermanager.TriggerException;

public class TriggerUtils
{

	public static final int CLOCK_TRIGGER_ONCE = 10000;
	public static final int CLOCK_TRIGGER_ON_INTERVAL = 10001;
	public static final int CLOCK_TRIGGER_DAILY_RANDOM = 10002;

	public static final int SENSOR_TRIGGER_ACCELEROMETER = 20000;
	public static final int SENSOR_TRIGGER_MICROPHONE = 20001;
	public static final int SENSOR_TRIGGER_CALLS = 20002;
	public static final int SENSOR_TRIGGER_SMS = 20003;
	public static final int SENSOR_TRIGGER_SCREEN = 20004;

	public static final int HYBRID_RANDOM_MICROPHONE = 30000;
	public static final int HYBRID_RANDOM_ACCELEROMETER = 30001;
	
	public static final String CLOCK_TRIGGER_NAME = "Clock Trigger";
	public static final String INTERVAL_TRIGGER_NAME = "Interval Trigger";
	public static final String DAILY_RANDOM_TRIGGER_NAME = "Random Trigger";
	
	public static final String ACCELEROMETER_TRIGGER_NAME = "Accelerometer Trigger";
	public static final String MICROPHONE_TRIGGER_NAME = "Microphone Trigger";
	public static final String CALL_STATE_TRIGGER_NAME = "Phone Call Trigger";
	public static final String SMS_TRIGGER_NAME = "SMS Trigger";
	public static final String SCREEN_TRIGGER_NAME = "Screen Trigger";
	
	public static String getTriggerName(int type) throws TriggerException
	{
		switch (type)
		{
		case CLOCK_TRIGGER_ONCE: return CLOCK_TRIGGER_NAME;
		case CLOCK_TRIGGER_ON_INTERVAL: return INTERVAL_TRIGGER_NAME;
		case CLOCK_TRIGGER_DAILY_RANDOM: return DAILY_RANDOM_TRIGGER_NAME;
		
		case SENSOR_TRIGGER_ACCELEROMETER: return ACCELEROMETER_TRIGGER_NAME;
		case SENSOR_TRIGGER_MICROPHONE: return MICROPHONE_TRIGGER_NAME;
		case SENSOR_TRIGGER_CALLS: return CALL_STATE_TRIGGER_NAME;
		case SENSOR_TRIGGER_SMS: return SMS_TRIGGER_NAME;
		case SENSOR_TRIGGER_SCREEN: return SCREEN_TRIGGER_NAME;
		default: throw new TriggerException(TriggerException.UNKNOWN_TRIGGER, "Unknown trigger: "+type);
		}
	}
}
