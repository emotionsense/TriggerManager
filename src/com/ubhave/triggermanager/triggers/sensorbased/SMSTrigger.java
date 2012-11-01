package com.ubhave.triggermanager.triggers.sensorbased;

import android.content.Context;

import com.ubhave.sensormanager.ESException;
import com.ubhave.sensormanager.data.SensorData;
import com.ubhave.sensormanager.data.pushsensor.SmsData;
import com.ubhave.sensormanager.sensors.SensorUtils;
import com.ubhave.triggermanager.TriggerException;
import com.ubhave.triggermanager.TriggerReceiver;

public class SMSTrigger extends SensorTrigger
{

	public SMSTrigger(Context context, TriggerReceiver listener) throws TriggerException, ESException
	{
		super(context, listener, SensorUtils.SENSOR_TYPE_SMS);
	}

	@Override
	public void onDataSensed(SensorData sensorData)
	{
		SmsData sms = (SmsData) sensorData;
		if (sms.wasReceived())
		{
			callForSurvey();
		}
	}

}
