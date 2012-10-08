package com.ubhave.triggermanager.triggers.active.mic;

import com.lathia.experiencesense.SurveyApplication;
import com.lathia.experiencesense.log.ESLogger;
import com.lathia.experiencesense.util.Constants;
import com.ubhave.sensormanager.ESException;
import com.ubhave.sensormanager.ESSensorManager;
import com.ubhave.sensormanager.data.SensorData;
import com.ubhave.sensormanager.data.pullsensor.MicrophoneData;

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

	public PostSampleSilenceTrigger(String targetSurvey)
	{
		super(targetSurvey);
	}

	@Override
	protected void sampleForSurvey()
	{
		super.sampleForSurvey();
		sampleObtained = false;
	}

	@Override
	public void onDataSensed(SensorData sensorData)
	{
		try
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

					ESSensorManager.getSensorManager(SurveyApplication.getContext()).unregisterSensorDataListener(Constants.SENSOR_TYPE_MICROPHONE, this);
					callForSurvey(ADHERE_TO_CAP);
				}
			}
			else if (!recording.isSilent() || senseCycle == Constants.TRIGGER_MAX_CYCLES)
			{
				sampleObtained = true;
				postSampleSilentCycles = 0;
			}
		}
		catch (ESException e)
		{
			ESLogger.error(LOG_TAG, e);
		}
	}
}
