package com.ubhave.triggermanager.triggers.active.fixed;

import com.ubhave.sensormanager.data.SensorData;
import com.ubhave.triggermanager.TriggerReceiver;
import com.ubhave.triggermanager.triggers.active.ActiveTrigger;

public abstract class StaticTrigger extends ActiveTrigger
{

	public StaticTrigger(TriggerReceiver listener)
	{
		super(listener);
	}

	@Override // Unused
	protected void sampleForSurvey(){}

	@Override // Unused
	public void onDataSensed(SensorData sensorData){}

}
