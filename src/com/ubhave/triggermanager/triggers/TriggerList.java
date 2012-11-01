package com.ubhave.triggermanager.triggers;

import java.util.Random;

import android.content.Context;
import android.util.SparseArray;

import com.ubhave.sensormanager.ESException;
import com.ubhave.sensormanager.sensors.SensorUtils;
import com.ubhave.triggermanager.TriggerException;
import com.ubhave.triggermanager.TriggerReceiver;
import com.ubhave.triggermanager.triggers.clockbased.IntervalTrigger;
import com.ubhave.triggermanager.triggers.clockbased.RandomFrequencyTrigger;
import com.ubhave.triggermanager.triggers.hybrid.HybridTrigger;
import com.ubhave.triggermanager.triggers.sensorbased.SensorTrigger;

public class TriggerList
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
	
	public static Trigger createTrigger(Context context, int type, TriggerReceiver listener) throws ESException, TriggerException
	{
		switch (type)
		{
//		case CLOCK_TRIGGER_ONCE: return new OneTimeTrigger(context, listener, date); // TODO
		case CLOCK_TRIGGER_ON_INTERVAL: return new IntervalTrigger(context, listener);
		case CLOCK_TRIGGER_DAILY_RANDOM: return new RandomFrequencyTrigger(context, listener);
		
		case SENSOR_TRIGGER_ACCELEROMETER: return new SensorTrigger(context, listener, SensorUtils.SENSOR_TYPE_ACCELEROMETER);
		case SENSOR_TRIGGER_CALLS: return new SensorTrigger(context, listener, SensorUtils.SENSOR_TYPE_PHONE_STATE);
		case SENSOR_TRIGGER_MICROPHONE: return new SensorTrigger(context, listener, SensorUtils.SENSOR_TYPE_MICROPHONE);
		case SENSOR_TRIGGER_SMS: return new SensorTrigger(context, listener, SensorUtils.SENSOR_TYPE_SMS);
		case SENSOR_TRIGGER_SCREEN: return new SensorTrigger(context, listener, SensorUtils.SENSOR_TYPE_SCREEN);
		
		case HYBRID_RANDOM_MICROPHONE: return new HybridTrigger(context, CLOCK_TRIGGER_DAILY_RANDOM, SENSOR_TRIGGER_MICROPHONE, listener);
		case HYBRID_RANDOM_ACCELEROMETER: return new HybridTrigger(context, CLOCK_TRIGGER_DAILY_RANDOM, SENSOR_TRIGGER_ACCELEROMETER, listener);
		
		default: throw new TriggerException(TriggerException.INVALID_STATE, "Type unknown: "+type);
		}
	}
	
	
	private final SparseArray<Trigger> triggerMap;
	private final Random keyGenerator;

	public TriggerList()
	{
		triggerMap = new SparseArray<Trigger>();
		keyGenerator = new Random();
	}

	public int addTrigger(Trigger s, TriggerReceiver l) throws TriggerException
	{
		int triggerId = randomKey();
		triggerMap.append(triggerId, s);
		return triggerId;
	}

	public void removeTrigger(int triggerId)
	{
		Trigger s = triggerMap.get(triggerId);
		if (s != null)
		{
			s.kill();
			triggerMap.delete(triggerId);
		}
	}
	
	public Trigger getTrigger(int triggerId)
	{
		return triggerMap.get(triggerId);
	}

	private int randomKey() throws TriggerException
	{
		int triggerId = keyGenerator.nextInt();
		int loopCount = 0;
		while (triggerMap.get(triggerId) != null)
		{
			if (loopCount > 1000)
				throw new TriggerException(TriggerException.INVALID_STATE, "Listener map >1000 key conflicts.");
			triggerId = keyGenerator.nextInt();
			loopCount++;
		}
		return triggerId;
	}
}
