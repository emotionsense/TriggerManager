package com.ubhave.triggermanager.triggers.active.fixed;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimerTask;

import org.json.simple.JSONObject;

import com.ubhave.sensormanager.config.Constants;
import com.ubhave.sensormanager.logs.ESLogger;
import com.ubhave.triggermanager.TriggerException;
import com.ubhave.triggermanager.triggers.Trigger;

public class OneTimeTrigger extends StaticTrigger
{
	private final static String LOG_TAG = "OneTimeTrigger";

	private final static String TIME = "time";

	private class SurveyNotification extends TimerTask
	{
		@Override
		public void run()
		{
			callForSurvey(Trigger.IGNORE_CAP);
		}
	}

	public OneTimeTrigger(String target, JSONObject data) throws TriggerException
	{
		super(target);
		try
		{
			SimpleDateFormat format = new SimpleDateFormat("HH:mm dd/MM/yyyy");
			Calendar surveyDate = Calendar.getInstance();
			surveyDate.setTime(format.parse((String) data.get(TIME)));
			
			if (Constants.TEST_MODE)
			{
				ESLogger.log(LOG_TAG, "Final survey set for: "+surveyDate.getTime().toString());
			}
			
			long waitTime = surveyDate.getTimeInMillis() - System.currentTimeMillis();
			if (waitTime > 0)
			{
				surveyTimer.schedule(new SurveyNotification(), waitTime);
			}
			else if (Constants.TEST_MODE)
			{
				ESLogger.log(LOG_TAG, "Warning: final survey NOT scheduled as is in the past: "+surveyDate.getTime().toString());
			}
		}
		catch (ParseException e)
		{
			ESLogger.error(LOG_TAG, e);
		}
	}
	
//	protected void callForSurvey()
//	{
//		super.callForSurvey(Trigger.IGNORE_CAP);
//		try
//		{
//			ESSensorManager manager = ESSensorManager.getSensorManager(SurveyApplication.getContext());
//			manager.stopAllSensors();
//			super.endAllTriggers();
//		}
//		catch (ESException e)
//		{
//			e.printStackTrace();
//		}
//	}
	
	@Override
	public void kill()
	{
		super.kill();
		if (Constants.TEST_MODE)
		{
			ESLogger.log(LOG_TAG, "Killing: "+LOG_TAG);
		}
	}

}
