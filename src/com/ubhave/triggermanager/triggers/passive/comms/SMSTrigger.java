package com.ubhave.triggermanager.triggers.passive.comms;

import android.content.Context;

import com.ubhave.sensormanager.ESException;
import com.ubhave.sensormanager.config.Constants;
import com.ubhave.sensormanager.data.SensorData;
import com.ubhave.sensormanager.data.pushsensor.SmsData;
import com.ubhave.sensormanager.logs.ESLogger;
import com.ubhave.sensormanager.sensors.SensorUtils;
import com.ubhave.triggermanager.TriggerReceiver;
import com.ubhave.triggermanager.triggers.passive.PassiveTrigger;

public class SMSTrigger extends PassiveTrigger
{
	private final static String LOG_TAG = "SMSTrigger";

	public SMSTrigger(Context context, TriggerReceiver listener) throws ESException
	{
		super(context, listener, SensorUtils.SENSOR_TYPE_SMS, LOG_TAG, 1.0);
	}

	@Override
	public void onDataSensed(SensorData sensorData)
	{
		SmsData sms = (SmsData) sensorData;
		if (sms.wasReceived())
		{
			if (Constants.TEST_MODE)
			{
				ESLogger.log(LOG_TAG, "onDataSensed(): SMS received");
			}
			callForSurvey(ADHERE_TO_CAP);
		}
		else if (Constants.TEST_MODE)
		{
			ESLogger.log(LOG_TAG, "onDataSensed(): SMS not received");
		}
	}
}
