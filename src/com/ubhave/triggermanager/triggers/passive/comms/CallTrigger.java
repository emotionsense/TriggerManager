package com.ubhave.triggermanager.triggers.passive.comms;

import com.ubhave.sensormanager.ESException;
import com.ubhave.sensormanager.config.Constants;
import com.ubhave.sensormanager.data.SensorData;
import com.ubhave.sensormanager.data.pushsensor.PhoneStateData;
import com.ubhave.sensormanager.logs.ESLogger;
import com.ubhave.sensormanager.sensors.SensorList;
import com.ubhave.triggermanager.triggers.passive.PassiveTrigger;

public class CallTrigger extends PassiveTrigger
{
	private final static String LOG_TAG = "CallTrigger";
	private boolean isInCall;

	public CallTrigger() throws ESException
	{
		super(SensorList.SENSOR_TYPE_PHONE_STATE, LOG_TAG, 1.1);
		isInCall = false;
	}

	@Override
	public void onDataSensed(SensorData sensorData)
	{
		PhoneStateData phone = (PhoneStateData) sensorData;
		if (phone.isOffHook())
		{
			if (Constants.TEST_MODE)
			{
				ESLogger.log(LOG_TAG, "Phone is off hook");
			}
			isInCall = true;
		}
		else if (phone.isIdle())
		{
			if (Constants.TEST_MODE)
			{
				ESLogger.log(LOG_TAG, "Phone is idle");
			}
			if (isInCall)
			{
				if (Constants.TEST_MODE)
				{
					ESLogger.log(LOG_TAG, "Phone call is ending");
				}
				isInCall = false;
				callForSurvey(ADHERE_TO_CAP);
			}
		}
	}
}
