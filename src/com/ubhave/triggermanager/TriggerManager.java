package com.ubhave.triggermanager;

import java.util.ArrayList;
import java.util.Calendar;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.ubhave.triggermanager.preferences.SurveyLimiter;
import com.ubhave.triggermanager.preferences.UserPreferences;
import com.ubhave.triggermanager.triggers.Trigger;

public class TriggerManager implements TriggerManagerInterface
{

	public static final String END_SURVEYS_INTENT = "com.lathia.experiencesense.triggers.END_SURVEYS";
	public static final String RELOAD_INTENT = "com.lathia.experiencesense.triggers.RELOAD_TRIGGERS";
	
	private static TriggerManager triggerManager;
	public final static int NOTIFICATION_ID = 901;
	private static Object lock = new Object();
	
	private final UserPreferences preferences;
	private final Context context;
	private ArrayList<Trigger> triggers;

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
		triggers = new ArrayList<Trigger>();
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
	
	public void endAllTriggers()
	{
		for (Trigger trigger : triggers)
		{
			trigger.kill();
		}
		triggers.clear();
	}
	
	public void reloadAllTriggers()
	{
		endAllTriggers();
//		triggers = TriggerLoader.loadTriggers();
	}
	
	public void trigger(Intent notificationIntent, String title, String text)
	{
		if (SurveyLimiter.surveyAllowed(preferences))
		{
			NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

			Notification notification = new Notification(android.R.drawable.ic_dialog_alert, text, System.currentTimeMillis());
			notification.defaults |= Notification.DEFAULT_SOUND;
			notification.defaults |= Notification.DEFAULT_VIBRATE;
			notification.defaults |= Notification.DEFAULT_LIGHTS;

//			Intent notificationIntent = new Intent(this, SurveyActivity.class);
//			notificationIntent.putExtra(SurveyActivity.FROM_NOTIFICATION, true);
//			notificationIntent.putExtra(Trigger.TARGET_SURVEY, targetSurvey);
			
			PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
			notification.setLatestEventInfo(context, title, text, contentIntent);
			mNotificationManager.notify(NOTIFICATION_ID, notification);
			preferences.surveySent(Calendar.getInstance());
		}
	}

	@Override
	public int addTrigger(int triggerType)
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void removeTrigger(int triggerId)
	{
		// TODO Auto-generated method stub
		
	}
}
