package com.ubhave.triggermanager.triggers;

import com.ubhave.triggermanager.TriggerManager;

public abstract class Trigger
{	
//	private static final String LOG_TAG = "Trigger";
	
	protected final static boolean IGNORE_CAP = true;
	protected final static boolean ADHERE_TO_CAP = false;

	// JSON tags
	public static final String TYPE = "type";
	public static final String LAST_DATE = "finish";
	public static final String TARGET_SURVEY = "survey";
	protected static final String ALL = "*";

	protected final TriggerManager manager;
	
	public Trigger()
	{
		manager = TriggerManager.getSensorManager(null);
	}

	protected void callForSurvey(boolean ignoreCap)
	{
		manager.trigger();
	}
	
	public abstract void kill();
}
