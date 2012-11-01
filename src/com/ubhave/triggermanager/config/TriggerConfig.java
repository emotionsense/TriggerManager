package com.ubhave.triggermanager.config;


public class TriggerConfig
{
	/*
	 * 1. Waiting time after an event
	 * 
		sensorManager.setSensorConfig(SensorUtils.SENSOR_TYPE_ACCELEROMETER, SensorConfig.SENSE_WINDOW_LENGTH_MILLIS, (long) 5000);
		sensorManager.setSensorConfig(SensorUtils.SENSOR_TYPE_ACCELEROMETER, SensorConfig.POST_SENSE_SLEEP_LENGTH_MILLIS, (long) 1000);
	 */

//	private static TriggerConfig triggerConfig;
//	private static final Object lock = new Object();
//
//	public static TriggerConfig getTriggerConfig(Context c) throws TriggerException
//	{
//		if (c == null)
//		{
//			throw new TriggerException(TriggerException.NO_CONTEXT, "Context is null");
//		}
//		if (triggerConfig == null)
//		{
//			synchronized (lock)
//			{
//				if (triggerConfig == null)
//				{
//					triggerConfig = new TriggerConfig(c);
//				}
//			}
//		}
//		return triggerConfig;
//	}
//
//	private final SharedPreferences preferences;
//
//	public TriggerConfig(Context context)
//	{
//		preferences = context.getSharedPreferences(Constants.TRIGGER_PREFERENCES, Context.MODE_PRIVATE);
//	}

}
