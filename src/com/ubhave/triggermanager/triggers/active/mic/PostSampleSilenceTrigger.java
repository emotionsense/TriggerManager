package com.ubhave.triggermanager.triggers.active.mic;

import com.ubhave.sensormanager.config.Constants;
import com.ubhave.sensormanager.data.SensorData;
import com.ubhave.sensormanager.data.pullsensor.MicrophoneData;
import com.ubhave.sensormanager.logs.ESLogger;

public class PostSampleSilenceTrigger extends MicrophoneBasedTrigger
{
	/*
	 * SilentSampleTrigger 1) Waits for a non-silent sample of mic data 2) Then
	 * waits for a silent sample and calls for the survey Will call for a survey
	 * immediately after a non-silent sample is sensed from the user's
	 * microphone, or if the number of silent samples ==
	 * Constants.TRIGGER_MAX_CYCLES
	 */

	private final static String LOG_TAG = "SilentSampleTrigger";
	private final static int MIN_SILENCE_CYCLES = 3; // 3x5 seconds in 3 minutes
	
	private boolean sampleObtained;
	private int postSampleSilentCycles;

	@Override
	protected void sampleForSurvey()
	{
		super.sampleForSurvey();
		sampleObtained = false;
	}

	@Override
	public void onDataSensed(SensorData sensorData)
	{
		if (Constants.TEST_MODE)
		{
			ESLogger.log(LOG_TAG, "onDataSensed() " + senseCycle);
		}

		senseCycle++;
		MicrophoneData recording = (MicrophoneData) sensorData;
		if (sampleObtained)
		{
			if (recording.isSilent())
			{
				postSampleSilentCycles++;
			}
			
			if (postSampleSilentCycles == MIN_SILENCE_CYCLES)
			{
				if (Constants.TEST_MODE)
				{
					ESLogger.log(LOG_TAG, "Trigger for notification");
				}

				super.unregisterFromMicrophone();
				callForSurvey(ADHERE_TO_CAP);
			}
		}
		else if (!recording.isSilent() || senseCycle == TRIGGER_MAX_CYCLES)
		{
			sampleObtained = true;
			postSampleSilentCycles = 0;
		}
	}
}
