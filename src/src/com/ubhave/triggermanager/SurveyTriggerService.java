package com.ubhave.triggermanager;

import java.util.ArrayList;
import java.util.Date;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.os.IBinder;

import com.lathia.experiencesense.R;
import com.lathia.experiencesense.SurveyActivity;
import com.lathia.experiencesense.json.TriggerLoader;
import com.lathia.experiencesense.log.DataLogger;
import com.lathia.experiencesense.log.ESLogger;
import com.lathia.experiencesense.util.Constants;
import com.ubhave.triggermanager.triggers.Trigger;

public class SurveyTriggerService extends Service
{
	public final static int NOTIFICATION_ID = 901;
	private final static String LOG_TAG = "SurveyTrigger";
	
	private static SurveyTriggerService triggerService = null;
	private static final Object lock = new Object();

	private class SurveyBroadCastReceiver extends BroadcastReceiver
	{
		@Override
		public void onReceive(Context context, Intent intent)
		{
			String action = intent.getAction();
			if (action.equals(Trigger.SURVEY_INTENT))
			{
				if (Constants.TEST_MODE)
				{
					ESLogger.log(LOG_TAG, "Receiving: " + action);
				}
				showNotification(intent.getStringExtra(Trigger.TARGET_SURVEY));
			}
			else if (action.equals(Trigger.END_SURVEYS_INTENT))
			{
				if (Constants.TEST_MODE)
				{
					ESLogger.log(LOG_TAG, "Receiving: " + action);
				}
				endAllTriggers();
			}
			else if (action.equals(Trigger.RELOAD_INTENT))
			{
				if (Constants.TEST_MODE)
				{
					ESLogger.log(LOG_TAG, "Receiving: " + action);
				}
				reloadAllTriggers();
			}
		}
	}
	
	@Override
	public void onCreate()
	{
		if (triggerService != null)
		{
			ESLogger.log(LOG_TAG, "Trigger service already started");
		}
		else
		{
			synchronized (lock)
			{
				if (triggerService == null)
				{
					ESLogger.log(LOG_TAG, "Trigger service starting");
					super.onCreate();
					triggers = TriggerLoader.loadTriggers();
					receiver = new SurveyBroadCastReceiver();

					IntentFilter filter = new IntentFilter();
					filter.addAction(Trigger.SURVEY_INTENT);
					filter.addAction(Trigger.END_SURVEYS_INTENT);
					this.registerReceiver(receiver, filter);
					triggerService = this;
				}
			}
		}
	}

	private ArrayList<Trigger> triggers;
	private SurveyBroadCastReceiver receiver;

	@Override
	public IBinder onBind(Intent arg0)
	{
		return null;
	}

	@Override
	public void onDestroy()
	{
		super.onDestroy();
		this.unregisterReceiver(receiver);
	}
	
	private void endAllTriggers()
	{
		for (Trigger trigger : triggers)
		{
			trigger.kill();
		}
	}
	
	private void reloadAllTriggers()
	{
		endAllTriggers();
		triggers.clear();
		triggers = TriggerLoader.loadTriggers();
	}

	private void showNotification(String targetSurvey)
	{
		if (Constants.TEST_MODE)
		{
			ESLogger.log(LOG_TAG, "Notification firing: " + (new Date()).toString());
		}
			
		NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

		Resources resources = getResources();
		Notification notification = new Notification(android.R.drawable.ic_dialog_alert, resources.getString(R.string.notificationTickerText), System.currentTimeMillis());
		notification.defaults |= Notification.DEFAULT_SOUND;
		notification.defaults |= Notification.DEFAULT_VIBRATE;
		notification.defaults |= Notification.DEFAULT_LIGHTS;

		Intent notificationIntent = new Intent(this, SurveyActivity.class);
		notificationIntent.putExtra(SurveyActivity.FROM_NOTIFICATION, true);
		notificationIntent.putExtra(Trigger.TARGET_SURVEY, targetSurvey);
		
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		notification.setLatestEventInfo(getApplicationContext(), resources.getString(R.string.notificationTitle), resources.getString(R.string.notificationText), contentIntent);
		mNotificationManager.notify(NOTIFICATION_ID, notification);
		ESLogger.log(LOG_TAG, "Sending notification");
		DataLogger.getDataLogger().logData("SURVEY", "Notification "+System.currentTimeMillis());
	}

}
