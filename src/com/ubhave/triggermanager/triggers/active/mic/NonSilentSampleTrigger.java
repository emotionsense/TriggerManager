package com.ubhave.triggermanager.triggers.active.mic;

import com.ubhave.sensormanager.config.Constants;
import com.ubhave.sensormanager.data.SensorData;
import com.ubhave.sensormanager.data.pullsensor.MicrophoneData;
import com.ubhave.sensormanager.logs.ESLogger;

public class NonSilentSampleTrigger extends MicrophoneBasedTrigger
{
	/*
	 * NonSilentSampleTrigger
	 * Will call for a survey immediately after a non-silent sample is sensed from the user's microphone,
	 * or if the number of silent samples == Constants.TRIGGER_MAX_CYCLES
	 */
	
	private final static String LOG_TAG = "NonSilentSampleTrigger";
	
	public NonSilentSampleTrigger(String targetSurvey)
	{
		super(targetSurvey);
	}

	@Override
	public void onDataSensed(SensorData sensorData)
	{
		if (Constants.TEST_MODE)
		{
			ESLogger.log(LOG_TAG, "onDataSensed() "+senseCycle);
		}
		
		MicrophoneData recording = (MicrophoneData) sensorData;
		if (!recording.isSilent() || senseCycle == TRIGGER_MAX_CYCLES)
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
