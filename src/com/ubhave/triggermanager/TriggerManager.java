package com.ubhave.triggermanager;

import android.content.Context;
import android.widget.Toast;

import com.ubhave.sensormanager.ESException;
import com.ubhave.sensormanager.ESSensorManager;
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

	public static TriggerManager getTriggerManager(Context context)
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
		try
		{
			ESSensorManager.startSensorManager(appContext);
		}
		catch (ESException e)
		{
			e.printStackTrace();
		}
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

	// public void reloadAllTriggers()
	// {
	// endAllTriggers();
	// // triggers = TriggerLoader.loadTriggers();
	// }

	@Override
	public int addTrigger(int triggerType, TriggerReceiver listener)
	{
		try
		{
			Trigger trigger = TriggerList.createTrigger(context, triggerType, listener);
			return triggers.addTrigger(trigger, listener);
		}
		catch (ESException e)
		{
			e.printStackTrace();
			Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
			return 0;
		}
		catch (TriggerException e)
		{
			e.printStackTrace();
			Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
			return 0;
		}
	}

	@Override
	public void removeTrigger(int triggerId)
	{
		triggers.removeTrigger(triggerId);
	}
}
