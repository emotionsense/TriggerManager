package com.ubhave.triggermanager.triggers;

import java.util.Random;

import android.content.Context;

import com.ubhave.triggermanager.TriggerException;
import com.ubhave.triggermanager.TriggerReceiver;
import com.ubhave.triggermanager.config.Constants;
import com.ubhave.triggermanager.config.GlobalConfig;
import com.ubhave.triggermanager.config.GlobalState;

public abstract class Trigger
{

	protected final TriggerReceiver listener;
	protected final GlobalState globalState;
	protected final GlobalConfig globalConfig;

	public Trigger(Context context, TriggerReceiver listener) throws TriggerException
	{
		this.listener = listener;
		this.globalState = GlobalState.getGlobalState(context);
		this.globalConfig = GlobalConfig.getGlobalConfig(context);
	}

	protected void callForSurvey()
	{
		double sampleProbability;
		try
		{
			sampleProbability = (Float) globalConfig.getParameter(GlobalConfig.NOTIFICATION_PROBABILITY);
		}
		catch (TriggerException e)
		{
			sampleProbability = Constants.DEFAULT_NOTIFICATION_PROBABILITY;
		}

		double currentProbability = (new Random()).nextDouble();
		if (currentProbability <= sampleProbability)
		{
			listener.onNotificationTriggered();
			globalState.incrementNotificationsSent();
		}

		// else if (Constants.TEST_MODE)
		// {
		// ESLogger.log(LOG_TAG,
		// "Not calling for survey: P(sample) = "+sampleProbability);
		// }
		// if (SurveyLimiter.surveyAllowed())
		{

		}
		// else
		// {
		// Log.d("Trigger", "Notification not allowed");
		// }
	}

	public abstract void kill();

	public abstract void pause();

	public abstract void resume();

}
