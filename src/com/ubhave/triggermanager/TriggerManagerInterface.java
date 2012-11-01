package com.ubhave.triggermanager;

import com.ubhave.sensormanager.ESException;

public interface TriggerManagerInterface
{
	
	/*
	 * Trigger
	 */
	public int addTrigger(int triggerType, TriggerReceiver listener) throws ESException, TriggerException;
	public void removeTrigger(int triggerId);
	
	public void pauseTrigger(int triggerId);
	public void unPauseTrigger(int triggerId);
	
	/*
	 * User Preferences
	 */
	public void setGlobalConfig(String configKey, Object configValue);
	public Object getGlobalConfigValue(String configKey) throws TriggerException;

}
