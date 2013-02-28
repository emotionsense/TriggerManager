package com.ubhave.triggermanager.triggers;

import com.ubhave.sensormanager.sensors.SensorUtils;
import com.ubhave.triggermanager.TriggerException;

public class TriggerUtils
{

	public static final int CLOCK_TRIGGER_ONCE = 10000;
	public static final int CLOCK_TRIGGER_ON_INTERVAL = 10001;
	public static final int CLOCK_TRIGGER_DAILY_RANDOM = 10002;
	public static final int SENSOR_TRIGGER_IMMEDIATE = 10003;
	public static final int SENSOR_TRIGGER_DELAYED = 10004;

	public static final int SENSOR_TRIGGER_ACCELEROMETER = SensorUtils.SENSOR_TYPE_ACCELEROMETER;
	public static final int SENSOR_TRIGGER_MICROPHONE = SensorUtils.SENSOR_TYPE_MICROPHONE;
	public static final int SENSOR_TRIGGER_CALLS = SensorUtils.SENSOR_TYPE_PHONE_STATE;
	public static final int SENSOR_TRIGGER_SMS = SensorUtils.SENSOR_TYPE_SMS;
	public static final int SENSOR_TRIGGER_SCREEN = SensorUtils.SENSOR_TYPE_SCREEN;
	
	public static final String CLOCK_TRIGGER_NAME = "Clock Trigger";
	public static final String INTERVAL_TRIGGER_NAME = "Interval Trigger";
	public static final String DAILY_RANDOM_TRIGGER_NAME = "Random Trigger";
	public static final String SENSOR_TRIGGER_IMMEDIATE_NAME = "Sensor Immediate Trigger";
	public static final String SENSOR_TRIGGER_DELAYED_NAME = "Sensor Delayed Trigger";
	
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
		case SENSOR_TRIGGER_IMMEDIATE: return SENSOR_TRIGGER_IMMEDIATE_NAME;
		case SENSOR_TRIGGER_DELAYED: return SENSOR_TRIGGER_DELAYED_NAME;

		default: throw new TriggerException(TriggerException.UNKNOWN_TRIGGER, "Unknown trigger: "+type);
		}
	}
}
