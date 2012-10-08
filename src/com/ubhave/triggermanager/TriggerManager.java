package com.ubhave.triggermanager;

import java.util.ArrayList;
import java.util.Calendar;

import android.content.Context;
import android.content.Intent;

import com.ubhave.triggermanager.preferences.SurveyLimiter;
import com.ubhave.triggermanager.preferences.UserPreferences;
import com.ubhave.triggermanager.triggers.Trigger;
import com.ubhave.triggermanager.triggers.TriggerList;

public class TriggerManager implements TriggerManagerInterface
{

	public static final String END_SURVEYS_INTENT = "com.lathia.experiencesense.triggers.END_SURVEYS";
	public static final String RELOAD_INTENT = "com.lathia.experiencesense.triggers.RELOAD_TRIGGERS";
	
	private static TriggerManager triggerManager;
	public final static int NOTIFICATION_ID = 901;
	private static Object lock = new Object();
	
	private final UserPreferences preferences;
	private final Context context;
	private final TriggerList triggers;

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
		context = appContext;
		preferences = new UserPreferences(appContext);
		triggers = new TriggerList();
	}
	
	public UserPreferences getPreferences()
	{
		return preferences;
	}
	
	public Context getContext()
	{
		return context;
	}
	
	@Override
	public int getNotificationId()
	{
		return NOTIFICATION_ID;
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
	
	@Override
	public int getDoNotDisturbBefore()
	{
		return preferences.getBeforeTime();
	}
	
	@Override
	public int getDoNotDisturbAfter()
	{
		return preferences.getAfterTime();
	}
	
	@Override
	public int getMaximumDailySurveys()
	{
		return preferences.getSurveyCap();
	}
	
	@Override
	public void endAllTriggers()
	{
		triggers.endAllTriggers();
	}
	
//	public void reloadAllTriggers()
//	{
//		endAllTriggers();
////		triggers = TriggerLoader.loadTriggers();
//	}
	
	public void trigger()
	{
		if (SurveyLimiter.surveyAllowed(preferences))
		{
			// TODO
			preferences.surveySent(Calendar.getInstance());
		}
	}

	@Override
	public int addTrigger(int triggerType, TriggerReceiver listener)
	{
		return triggers.addTrigger(s)
	}

	@Override
	public void removeTrigger(int triggerId)
	{
		// TODO Auto-generated method stub
		
	}
}
