package com.ubhave.triggermanager.triggers.sensorbased;

import android.content.Context;

import com.ubhave.sensormanager.ESException;
import com.ubhave.sensormanager.data.SensorData;
import com.ubhave.sensormanager.data.pushsensor.PhoneStateData;
import com.ubhave.sensormanager.sensors.SensorUtils;
import com.ubhave.triggermanager.TriggerException;
import com.ubhave.triggermanager.TriggerReceiver;

public class CallTrigger extends SensorTrigger
{
	private boolean isInCall;

	public CallTrigger(Context context, TriggerReceiver listener) throws TriggerException, ESException
	{
		super(context, listener, SensorUtils.SENSOR_TYPE_PHONE_STATE);
		isInCall = false;
	}

	@Override
	public void onDataSensed(SensorData sensorData)
	{
		PhoneStateData phone = (PhoneStateData) sensorData;
		if (phone.isOffHook())
		{
			isInCall = true;
		}
		else if (phone.isIdle())
		{
			if (isInCall)
			{
				isInCall = false;
				callForSurvey();
			}
		}
	}
}
