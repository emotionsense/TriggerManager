package com.ubhave.triggermanager;

public interface TriggerManagerInterface
{
	
	public void setDoNotDisturbBefore(int hour);
	public void setDoNotDisturbAfter(int hour);
	public void setMaximumDailySurveys(int cap);
	
	public int getDoNotDisturbBefore();
	public int getDoNotDisturbAfter();
	public int getMaximumDailySurveys();
	
	public int getNotificationId();
	
	public int addTrigger(int triggerType);
	public void removeTrigger(int triggerId);

}
