package com.ubhave.triggermanager.triggers.active.fixed;

import java.util.Calendar;
import java.util.TimerTask;

import org.json.simple.JSONObject;

import com.ubhave.sensormanager.config.Constants;
import com.ubhave.sensormanager.logs.ESLogger;
import com.ubhave.triggermanager.TriggerException;
import com.ubhave.triggermanager.triggers.Trigger;

public class IntervalTrigger extends StaticTrigger
{
	private final static String LOG_TAG = "FixedTimeTrigger";

	private final static String MINUTE = "minute";
	private final static String HOUR = "hour";
	private final static String DAY = "dayofweek";

	private final boolean[] minutes;
	private final boolean[] hours;
	private final boolean[] days;

	private class SurveyNotification extends TimerTask
	{
		@Override
		public void run()
		{
			callForSurvey();
		}
	}

	public IntervalTrigger(JSONObject data) throws TriggerException
	{
		minutes = init((String) data.get(MINUTE), 60);
		hours = init((String) data.get(HOUR), 24);
		days = init((String) data.get(DAY), 7);

		surveyTimer.schedule(new SurveyNotification(), getNextTime());
	}

	public void callForSurvey()
	{
		super.callForSurvey(Trigger.IGNORE_CAP);
		surveyTimer.schedule(new SurveyNotification(), getNextTime());
	}

	private boolean[] init(String value, int length)
	{
		boolean[] data = new boolean[length];
		if (value.equals(ALL) || value.length() == 0)
		{
			for (int i = 0; i < data.length; i++)
			{
				data[i] = true;
			}
		}
		else
		{
			String[] values = value.split(",");
			for (String v : values)
			{
				data[Integer.valueOf(v)] = true;
			}
		}
		return data;
	}

	private long getNextTime()
	{
		Calendar calendar = Calendar.getInstance();
		long now = calendar.getTimeInMillis();
		int minute, hour, day;
		do {
			calendar.add(Calendar.MINUTE, 1);
			minute = calendar.get(Calendar.MINUTE);
			hour = calendar.get(Calendar.HOUR_OF_DAY);
			day = calendar.get(Calendar.DAY_OF_WEEK) - 1;
		} while (!(minutes[minute] && hours[hour] && days[day]));

		if (Constants.TEST_MODE)
		{
			ESLogger.log(LOG_TAG, "Next survey is: " + calendar.getTime().toString());
		}
		return calendar.getTimeInMillis() - now;
	}
	
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
