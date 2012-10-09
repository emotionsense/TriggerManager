package com.ubhave.triggermanager.triggers;

import java.util.Calendar;

import com.ubhave.triggermanager.TriggerManager;
import com.ubhave.triggermanager.TriggerReceiver;
import com.ubhave.triggermanager.preferences.SurveyLimiter;
import com.ubhave.triggermanager.preferences.UserPreferences;

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
	private final TriggerReceiver listener;
	
	
	public Trigger(TriggerReceiver listener)
	{
		manager = TriggerManager.getTriggerManager(null);
		this.listener = listener;
	}

	protected void callForSurvey(boolean ignoreCap)
	{
		UserPreferences preferences = manager.getPreferences();
		if (SurveyLimiter.surveyAllowed(preferences))
		{
			listener.onNotificationTriggered();
			preferences.surveySent(Calendar.getInstance());
		}
	}
	
	public abstract void kill();
}
