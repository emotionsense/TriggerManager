package com.ubhave.triggermanager.triggers;

import java.util.Random;

import android.util.SparseArray;

import com.ubhave.sensormanager.ESException;
import com.ubhave.triggermanager.TriggerException;
import com.ubhave.triggermanager.TriggerReceiver;
import com.ubhave.triggermanager.triggers.active.movement.AccelerometerTrigger;
import com.ubhave.triggermanager.triggers.passive.ScreenActivityTrigger;
import com.ubhave.triggermanager.triggers.passive.comms.CallTrigger;
import com.ubhave.triggermanager.triggers.passive.comms.SMSTrigger;

public class TriggerList
{
//	private static final int TRIGGER_MIC_IMMEDIATE = 10101;
//	private static final int TRIGGER_MIC_WAIT_FOR_SILENCE = 10102;
	public static final int TRIGGER_CALL_STATE = 20101;
	public static final int TRIGGER_SMS_RECEIVED = 20201;
	public static final int TRIGGER_PHONE_SCREEN_ON = 20301;
//	private static final int TRIGGER_FIXED_INTERVAL = 30001;
//	private static final int TRIGGER_FINAL_SURVEY = 30002;
	public static final int TRIGGER_ACCELEROMETER = 4001;
	
	public static Trigger createTrigger(int type, TriggerReceiver listener) throws ESException, TriggerException
	{
		switch(type)
		{
//		case MIC_IMMEDIATE: return new NonSilentSampleTrigger();
//		case MIC_WAIT_FOR_SILENCE: return new PostSampleSilenceTrigger();
		case TRIGGER_CALL_STATE: return new CallTrigger(listener);
		case TRIGGER_SMS_RECEIVED: return new SMSTrigger(listener);
		case TRIGGER_PHONE_SCREEN_ON: return new ScreenActivityTrigger(listener);
		case TRIGGER_ACCELEROMETER: return new AccelerometerTrigger(listener);
//		case FIXED_INTERVAL: return new IntervalTrigger(); // needs config
//		case FINAL_SURVEY: return new OneTimeTrigger(); // needs config
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
	
	public void endAllTriggers()
	{
		for (int i=0; i<triggerMap.size(); i++)
		{
			triggerMap.get(triggerMap.keyAt(i)).kill();
		}
		triggerMap.clear();
	}
}
