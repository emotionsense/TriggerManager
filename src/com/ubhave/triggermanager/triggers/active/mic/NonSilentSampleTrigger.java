package com.ubhave.triggermanager.triggers.active.mic;

import android.content.Context;

import com.ubhave.sensormanager.data.SensorData;
import com.ubhave.sensormanager.data.pullsensor.MicrophoneData;
import com.ubhave.sensormanager.logs.ESLogger;
import com.ubhave.triggermanager.TriggerReceiver;
import com.ubhave.triggermanager.config.Constants;
import com.ubhave.triggermanager.triggers.sensorbased.MicrophoneBasedTrigger;

public class NonSilentSampleTrigger extends MicrophoneBasedTrigger
{
	/*
	 * NonSilentSampleTrigger
	 * Will call for a survey immediately after a non-silent sample is sensed from the user's microphone,
	 * or if the number of silent samples == Constants.TRIGGER_MAX_CYCLES
	 */
	
	public NonSilentSampleTrigger(Context context, TriggerReceiver listener)
	{
		super(context, listener);
	}

	private final static String LOG_TAG = "NonSilentSampleTrigger";

	@Override
	public void onDataSensed(SensorData sensorData)
	{
		if (Constants.TEST_MODE)
		{
			ESLogger.log(LOG_TAG, "onDataSensed() "+senseCycle);
		}
		
		MicrophoneData recording = (MicrophoneData) sensorData;
		
		if (!isSilent(recording.getAmplitudeString()) || senseCycle == TRIGGER_MAX_CYCLES)
		{
			if (Constants.TEST_MODE)
			{
				ESLogger.log(LOG_TAG, "Trigger for notification");
			}
			
			unregisterFromMicrophone();
			callForSurvey(ADHERE_TO_CAP);
		}
		else if (Constants.TEST_MODE)
		{
			ESLogger.log(LOG_TAG, "Sample "+senseCycle+" was silent.");
		}
		senseCycle++;
	}
	
}
