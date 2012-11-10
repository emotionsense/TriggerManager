/* **************************************************
 Copyright (c) 2012, University of Cambridge
 Neal Lathia, neal.lathia@cl.cam.ac.uk
 Kiran Rachuri, kiran.rachuri@cl.cam.ac.uk

This library was developed as part of the EPSRC Ubhave (Ubiquitous and
Social Computing for Positive Behaviour Change) Project. For more
information, please visit http://www.emotionsense.org

Permission to use, copy, modify, and/or distribute this software for any
purpose with or without fee is hereby granted, provided that the above
copyright notice and this permission notice appear in all copies.

THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES
WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF
MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY
SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES
WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR
IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 ************************************************** */

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
