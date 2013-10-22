package com.ubhave.triggermanager.triggers;

import com.ubhave.sensormanager.sensors.SensorUtils;
import com.ubhave.triggermanager.TriggerException;
import com.ubhave.triggermanager.config.TriggerManagerConstants;

public class TriggerUtils
{

	public static final int TYPE_CLOCK_TRIGGER_ONCE 			= 10000;
	public static final int TYPE_CLOCK_TRIGGER_ON_INTERVAL 		= 10001;
	public static final int TYPE_CLOCK_TRIGGER_DAILY_RANDOM 	= 10002;
	public static final int TYPE_SENSOR_TRIGGER_IMMEDIATE 		= 10003;
	public static final int TYPE_SENSOR_TRIGGER_DELAYED 		= 10004;
	public static final int TYPE_CLOCK_TRIGGER_DAY_INTERVAL		= 10005;

	public static final int SENSOR_TRIGGER_ACCELEROMETER 	= SensorUtils.SENSOR_TYPE_ACCELEROMETER;
	public static final int SENSOR_TRIGGER_MICROPHONE 		= SensorUtils.SENSOR_TYPE_MICROPHONE;
	public static final int SENSOR_TRIGGER_CALLS 			= SensorUtils.SENSOR_TYPE_PHONE_STATE;
	public static final int SENSOR_TRIGGER_SMS 				= SensorUtils.SENSOR_TYPE_SMS;
	public static final int SENSOR_TRIGGER_SCREEN 			= SensorUtils.SENSOR_TYPE_SCREEN;
	
	public static final String NAME_CLOCK_TRIGGER_ONCE 			= "type_clock_once";
	public static final String NAME_CLOCK_TRIGGER_ON_INTERVAL 	= "type_clock_interval";
	public static final String NAME_CLOCK_TRIGGER_DAILY			= "type_clock_daily";
	public static final String NAME_CLOCK_TRIGGER_DAILY_RANDOM 	= "type_clock_random";
	public static final String NAME_SENSOR_TRIGGER_IMMEDIATE 	= "type_sensor_immediate";
	public static final String NAME_SENSOR_TRIGGER_DELAYED 		= "type_sensor_delayed";
	
	
	private static final String[] ALL_NAMES = new String[]{
		NAME_CLOCK_TRIGGER_ONCE,
		NAME_CLOCK_TRIGGER_ON_INTERVAL,
		NAME_CLOCK_TRIGGER_DAILY_RANDOM,
		NAME_SENSOR_TRIGGER_IMMEDIATE,
		NAME_SENSOR_TRIGGER_DELAYED,
		NAME_CLOCK_TRIGGER_DAILY
	};
	
	private static final int[] ALL_IDS = new int[]{
		TYPE_CLOCK_TRIGGER_ONCE,
		TYPE_CLOCK_TRIGGER_ON_INTERVAL,
		TYPE_CLOCK_TRIGGER_DAILY_RANDOM,
		TYPE_SENSOR_TRIGGER_IMMEDIATE,
		TYPE_SENSOR_TRIGGER_DELAYED,
		TYPE_CLOCK_TRIGGER_DAY_INTERVAL
	};
	
	private static final String[] ALL_ACTIONS = new String[]{
		TriggerManagerConstants.ACTION_NAME_ONE_TIME_TRIGGER,
		TriggerManagerConstants.ACTION_NAME_INTERVAL_TRIGGER,
		TriggerManagerConstants.ACTION_NAME_RANDOM_DAY_TRIGGER,
		TriggerManagerConstants.ACTION_NAME_SENSOR_TRIGGER_IMMEDIATE,
		TriggerManagerConstants.ACTION_NAME_SENSOR_TRIGGER_DELAYED,
		TriggerManagerConstants.ACTION_NAME_DAY_INTERVAL_TRIGGER
	};
	
	public static String getTriggerName(int type) throws TriggerException
	{
		switch (type)
		{
		case TYPE_CLOCK_TRIGGER_ONCE: return NAME_CLOCK_TRIGGER_ONCE;
		case TYPE_CLOCK_TRIGGER_ON_INTERVAL: return NAME_CLOCK_TRIGGER_ON_INTERVAL;
		case TYPE_CLOCK_TRIGGER_DAILY_RANDOM: return NAME_CLOCK_TRIGGER_DAILY_RANDOM;
		case TYPE_SENSOR_TRIGGER_IMMEDIATE: return NAME_SENSOR_TRIGGER_IMMEDIATE;
		case TYPE_SENSOR_TRIGGER_DELAYED: return NAME_SENSOR_TRIGGER_DELAYED;
		case TYPE_CLOCK_TRIGGER_DAY_INTERVAL: return NAME_CLOCK_TRIGGER_DAILY;

		default: throw new TriggerException(TriggerException.UNKNOWN_TRIGGER, "Unknown trigger: "+type);
		}
	}
	
	public static int getTriggerType(final String triggerName) throws TriggerException
	{
		for (int i=0; i<ALL_NAMES.length; i++)
		{
			if (ALL_NAMES[i].equals(triggerName))
			{
				return ALL_IDS[i];
			}
		}
		throw new TriggerException(TriggerException.UNKNOWN_TRIGGER, "Unknown trigger: "+triggerName);
	}
	
	public static String getTriggerActionName(final String triggerType) throws TriggerException
	{
		for (int i=0; i<ALL_NAMES.length; i++)
		{
			if (ALL_NAMES[i].equals(triggerType))
			{
				return ALL_ACTIONS[i];
			}
		}
		throw new TriggerException(TriggerException.UNKNOWN_TRIGGER, "Unknown trigger: "+triggerType);
	}
}
