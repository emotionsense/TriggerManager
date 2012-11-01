package com.ubhave.triggermanager;

public interface TriggerReceiver
{
	public void onNotificationTriggered();
	
	public void onCrossingLowBatteryThreshold(boolean isBelowThreshold);
}
