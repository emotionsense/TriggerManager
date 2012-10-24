package com.ubhave.triggermanager.triggers.active.fixedtime;

import android.content.Context;

import com.ubhave.sensormanager.data.SensorData;
import com.ubhave.triggermanager.TriggerReceiver;
import com.ubhave.triggermanager.triggers.active.ActiveTrigger;

public abstract class StaticTrigger extends ActiveTrigger
{

	public StaticTrigger(Context context, TriggerReceiver listener)
	{
		super(context, listener);
	}

	@Override // Unused
	protected void sampleForSurvey(){}

	@Override // Unused
	public void onDataSensed(SensorData sensorData){}

}
