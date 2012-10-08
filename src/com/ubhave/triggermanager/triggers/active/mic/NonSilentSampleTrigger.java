package com.ubhave.triggermanager.triggers.active.mic;

import com.lathia.experiencesense.SurveyApplication;
import com.lathia.experiencesense.log.ESLogger;
import com.lathia.experiencesense.util.Constants;
import com.ubhave.sensormanager.ESException;
import com.ubhave.sensormanager.ESSensorManager;
import com.ubhave.sensormanager.data.SensorData;
import com.ubhave.sensormanager.data.pullsensor.MicrophoneData;
import com.ubhave.sensormanager.sensors.SensorList;

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
		try {
			if (Constants.TEST_MODE)
			{
				ESLogger.log(LOG_TAG, "onDataSensed() "+senseCycle);
			}
			
			MicrophoneData recording = (MicrophoneData) sensorData;
			if (!recording.isSilent() || senseCycle == Constants.TRIGGER_MAX_CYCLES)
			{
				if (Constants.TEST_MODE)
				{
					ESLogger.log(LOG_TAG, "Trigger for notification");
				}
				
				TriggerManager.getSensorManager(SurveyApplication.getContext()).unregisterSensorDataListener(SensorList.SENSOR_TYPE_MICROPHONE, this);
				callForSurvey(ADHERE_TO_CAP);
			}
			else if (Constants.TEST_MODE)
			{
				ESLogger.log(LOG_TAG, "Sample "+senseCycle+" was silent.");
			}
			senseCycle++;
		}
		catch(ESException e)
		{
			ESLogger.error(LOG_TAG, e);
		}
	}
	
	

}
