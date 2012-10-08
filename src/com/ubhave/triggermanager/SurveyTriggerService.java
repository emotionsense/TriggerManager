package com.ubhave.triggermanager;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

import com.ubhave.sensormanager.config.Constants;
import com.ubhave.sensormanager.logs.ESLogger;

public class SurveyTriggerService extends Service
{
	private final static String LOG_TAG = "SurveyTriggerService";
	
	private static SurveyTriggerService triggerService = null;
	private static final Object lock = new Object();
	
	private TriggerManager triggerManager;
	private SurveyBroadCastReceiver receiver;

	private class SurveyBroadCastReceiver extends BroadcastReceiver
	{
		@Override
		public void onReceive(Context context, Intent intent)
		{
			String action = intent.getAction();
			if (action.equals(TriggerManager.END_SURVEYS_INTENT))
			{
				if (Constants.TEST_MODE)
				{
					ESLogger.log(LOG_TAG, "Receiving: " + action);
				}
				triggerManager.endAllTriggers();
			}
			else if (action.equals(TriggerManager.RELOAD_INTENT))
			{
				if (Constants.TEST_MODE)
				{
					ESLogger.log(LOG_TAG, "Receiving: " + action);
				}
				triggerManager.reloadAllTriggers();
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
					triggerManager = TriggerManager.getSensorManager(this);
					
					receiver = new SurveyBroadCastReceiver();
					IntentFilter filter = new IntentFilter();
					filter.addAction(TriggerManager.RELOAD_INTENT);
					filter.addAction(TriggerManager.END_SURVEYS_INTENT);
					this.registerReceiver(receiver, filter);
					triggerService = this;
				}
			}
		}
	}

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

}
