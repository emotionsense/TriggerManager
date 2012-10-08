package com.ubhave.triggermanager.triggers;

import java.util.Random;

import android.util.SparseArray;

import com.ubhave.sensormanager.ESException;
import com.ubhave.triggermanager.TriggerException;
import com.ubhave.triggermanager.triggers.active.mic.NonSilentSampleTrigger;
import com.ubhave.triggermanager.triggers.active.mic.PostSampleSilenceTrigger;
import com.ubhave.triggermanager.triggers.passive.ScreenActivityTrigger;
import com.ubhave.triggermanager.triggers.passive.comms.CallTrigger;
import com.ubhave.triggermanager.triggers.passive.comms.SMSTrigger;

public class TriggerList
{
	private static final int MIC_IMMEDIATE = 10101;
	private static final int MIC_WAIT_FOR_SILENCE = 10102;
	private static final int CALL_STATE = 20101;
	private static final int SMS_RECEIVED = 20201;
	private static final int PHONE_SCREEN_ON = 20301;
//	private static final int FIXED_INTERVAL = 30001;
//	private static final int FINAL_SURVEY = 30002;
	
	public static Trigger createTrigger(int type) throws ESException, TriggerException
	{
		switch(type)
		{
		case MIC_IMMEDIATE: return new NonSilentSampleTrigger();
		case MIC_WAIT_FOR_SILENCE: return new PostSampleSilenceTrigger();
		case CALL_STATE: return new CallTrigger();
		case SMS_RECEIVED: return new SMSTrigger();
		case PHONE_SCREEN_ON: return new ScreenActivityTrigger();
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

	public int addTrigger(Trigger s) throws TriggerException
	{
		int triggerId = randomKey();
		triggerMap.append(triggerId, s);
		return triggerId;
	}

	public void removeTrigger(int triggerId)
	{
		Trigger s = triggerMap.get(triggerId);
		s.kill();
		triggerMap.delete(triggerId);
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
