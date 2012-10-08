package com.ubhave.triggermanager.triggers;

import com.ubhave.triggermanager.TriggerException;
import com.ubhave.triggermanager.TriggerManager;

public abstract class Trigger
{	
	private static final String LOG_TAG = "Trigger";
	
	protected final static boolean IGNORE_CAP = true;
	protected final static boolean ADHERE_TO_CAP = false;

	// JSON tags
	public static final String TYPE = "type";
	public static final String LAST_DATE = "finish";
	public static final String TARGET_SURVEY = "survey";
	protected static final String ALL = "*";

	private final String targetSurvey;
	protected final TriggerManager manager;
	
	public Trigger(String survey) throws TriggerException
	{
		this.targetSurvey = survey;
		manager = TriggerManager.getSensorManager(null);
		if (manager == null)
		{
			throw new TriggerException(TriggerException.NO_CONTEXT, "Trigger Manager not instantiated!");
		}
	}

	protected void callForSurvey(boolean ignoreCap)
	{
		// TODO
		manager.trigger(null, "Title", "Text");
	}
	
	public abstract void kill();
}
