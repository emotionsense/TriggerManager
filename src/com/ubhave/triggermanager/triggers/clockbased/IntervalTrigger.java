package com.ubhave.triggermanager.triggers.clockbased;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;

import com.ubhave.sensormanager.logs.ESLogger;
import com.ubhave.triggermanager.TriggerException;
import com.ubhave.triggermanager.TriggerReceiver;
import com.ubhave.triggermanager.config.Constants;
import com.ubhave.triggermanager.triggers.Trigger;

public class IntervalTrigger extends Trigger
{
	private final static String LOG_TAG = "FixedTimeTrigger";

	private final boolean[] minutes;
	private final boolean[] hours;
	private final boolean[] days;

	protected Timer surveyTimer;

	private class SurveyNotification extends TimerTask
	{
		@Override
		public void run()
		{
			callForSurvey();
		}
	}

	public IntervalTrigger(Context context, TriggerReceiver listener) throws TriggerException
	{
		super(context, listener);
		this.surveyTimer = new Timer();
		// minutes = init((String) data.get(MINUTE), 60);
		// hours = init((String) data.get(HOUR), 24);
		// days = init((String) data.get(DAY), 7);

		minutes = null;
		hours = null;
		days = null;

		surveyTimer.schedule(new SurveyNotification(), getNextTime());
	}

	public void callForSurvey()
	{
		super.callForSurvey();
		surveyTimer.schedule(new SurveyNotification(), getNextTime());
	}

	// private boolean[] init(String value, int length)
	// {
	// boolean[] data = new boolean[length];
	// if (value.equals("*") || value.length() == 0)
	// {
	// for (int i = 0; i < data.length; i++)
	// {
	// data[i] = true;
	// }
	// }
	// else
	// {
	// String[] values = value.split(",");
	// for (String v : values)
	// {
	// data[Integer.valueOf(v)] = true;
	// }
	// }
	// return data;
	// }

	private long getNextTime()
	{
		Calendar calendar = Calendar.getInstance();
		long now = calendar.getTimeInMillis();
		int minute, hour, day;
		do
		{
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
		surveyTimer.cancel();
	}
}
