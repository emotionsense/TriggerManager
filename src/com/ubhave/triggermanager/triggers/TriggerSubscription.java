package com.ubhave.triggermanager.triggers;

import com.ubhave.triggermanager.TriggerReceiver;

public class TriggerSubscription
{

	private final Trigger trigger;
	private final TriggerReceiver receiver;
	
	public TriggerSubscription(Trigger t, TriggerReceiver r)
	{
		trigger = t;
		receiver = r;
	}
	
}
