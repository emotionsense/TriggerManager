package com.ubhave.triggermanager;

public interface TriggerManagerInterface
{
	
	public void setDoNotDisturbBefore(int hour);
	public void setDoNotDisturbAfter(int hour);
	public void setMaximumDailySurveys(int cap);

}
