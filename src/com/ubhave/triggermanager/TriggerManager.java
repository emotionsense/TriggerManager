package com.ubhave.triggermanager;

import java.util.Calendar;

import android.content.Context;

import com.ubhave.triggermanager.preferences.SurveyLimiter;
import com.ubhave.triggermanager.preferences.UserPreferences;

public class TriggerManager implements TriggerManagerInterface
{

	public static final String SURVEY_INTENT = "com.lathia.experiencesense.triggers.START_SURVEY";
	public static final String END_SURVEYS_INTENT = "com.lathia.experiencesense.triggers.END_SURVEYS";
	public static final String RELOAD_INTENT = "com.lathia.experiencesense.triggers.RELOAD_TRIGGERS";
	
	private static TriggerManager triggerManager;
	private static Object lock = new Object();
	
	private final UserPreferences preferences;

	public static TriggerManager getSensorManager(Context context)
	{
		if (triggerManager == null)
		{
			synchronized (lock)
			{
				triggerManager = new TriggerManager(context);
			}
		}
		return triggerManager;
	}

	private TriggerManager(final Context appContext)
	{
		preferences = new UserPreferences(appContext);
	}
	
	public UserPreferences getPreferences()
	{
		return preferences;
	}

	@Override
	public void setDoNotDisturbBefore(int hour)
	{
		preferences.setBeforeTime(hour);
	}

	@Override
	public void setDoNotDisturbAfter(int hour)
	{
		preferences.setAfterTime(hour);
	}

	@Override
	public void setMaximumDailySurveys(int cap)
	{
		preferences.setSurveyCap(cap);
	}
	
	public void trigger()
	{
		if (SurveyLimiter.surveyAllowed(preferences))
		{
			preferences.surveySent(Calendar.getInstance());
		}
	}
}
