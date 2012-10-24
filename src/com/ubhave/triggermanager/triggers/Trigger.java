package com.ubhave.triggermanager.triggers;

import java.util.Calendar;

import android.content.Context;

import com.ubhave.sensormanager.ESException;
import com.ubhave.sensormanager.ESSensorManager;
import com.ubhave.sensormanager.ESSensorManagerInterface;
import com.ubhave.triggermanager.TriggerReceiver;
import com.ubhave.triggermanager.preferences.SurveyLimiter;
import com.ubhave.triggermanager.preferences.UserPreferences;

public abstract class Trigger
{

	protected final static boolean IGNORE_CAP = true;
	protected final static boolean ADHERE_TO_CAP = false;

	// JSON tags
	public static final String TYPE = "type";
	public static final String LAST_DATE = "finish";
	public static final String TARGET_SURVEY = "survey";
	protected static final String ALL = "*";

	private final TriggerReceiver listener;
	protected final Context context;
	protected ESSensorManagerInterface sensorManager;
	protected final UserPreferences preferences;
	
	public Trigger(Context context, TriggerReceiver listener)
	{
		this.context = context;
		this.listener = listener;
		this.preferences = new UserPreferences(context);
		try
		{
			sensorManager = ESSensorManager.getSensorManager();
		}
		catch (ESException e)
		{
			e.printStackTrace();
		}
	}

	protected void callForSurvey(boolean ignoreCap)
	{
		if (SurveyLimiter.surveyAllowed(preferences))
		{
			listener.onNotificationTriggered();
			preferences.surveySent(Calendar.getInstance());
		}
	}

	public abstract void kill();
}
