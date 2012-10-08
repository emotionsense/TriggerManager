package com.ubhave.triggermanager.triggers;

import java.util.Calendar;

import android.content.Intent;

import com.lathia.experiencesense.SurveyApplication;
import com.lathia.experiencesense.log.ESLogger;
import com.lathia.experiencesense.userprefs.UserPreferences;
import com.lathia.experiencesense.util.Constants;

public abstract class Trigger
{	
	private static final String LOG_TAG = "Trigger";
	public static final String SURVEY_INTENT = "com.lathia.experiencesense.triggers.START_SURVEY";
	public static final String END_SURVEYS_INTENT = "com.lathia.experiencesense.triggers.END_SURVEYS";
	public static final String RELOAD_INTENT = "com.lathia.experiencesense.triggers.RELOAD_TRIGGERS";
	
	protected final static boolean IGNORE_CAP = true;
	protected final static boolean ADHERE_TO_CAP = false;

	// JSON tags
	public static final String TYPE = "type";
	public static final String LAST_DATE = "finish";
	public static final String TARGET_SURVEY = "survey";
	protected static final String ALL = "*";

	private final String targetSurvey;
	
	public Trigger(String survey)
	{
		this.targetSurvey = survey;
	}
	
	private boolean areSurveysCapped()
	{
		Calendar calendar = Calendar.getInstance();
		return !UserPreferences.aboveCap(calendar.get(Calendar.DAY_OF_MONTH));
	}
	
	private boolean surveyAllowed()
	{
		Calendar calendar = Calendar.getInstance();
		return UserPreferences.userAllowsNotification(calendar);
	}

	protected void callForSurvey(boolean ignoreCap)
	{
		if (areSurveysCapped() || ignoreCap)
		{
			if (surveyAllowed())
			{
				Intent intent = new Intent();
				intent.setAction(SURVEY_INTENT);
				intent.putExtra(TARGET_SURVEY, targetSurvey);
				SurveyApplication.getContext().sendBroadcast(intent);
				UserPreferences.surveySent(Calendar.getInstance());
			}
		}
		else if (Constants.TEST_MODE)
		{
			ESLogger.log(LOG_TAG, "Not sending survey since it is not allowed");
			ESLogger.log(LOG_TAG, "User allows: "+UserPreferences.userAllowsNotification(Calendar.getInstance()));
		}
	}
	
	protected void endAllTriggers()
	{
		Intent intent = new Intent();
		intent.setAction(END_SURVEYS_INTENT);
		SurveyApplication.getContext().sendBroadcast(intent);
	}
	
	public abstract void kill();
}
