package com.ubhave.triggermanager.triggers.active;

import java.util.Timer;

import com.ubhave.sensormanager.SensorDataListener;
import com.ubhave.sensormanager.data.SensorData;
import com.ubhave.triggermanager.TriggerException;
import com.ubhave.triggermanager.triggers.Trigger;

public abstract class ActiveTrigger extends Trigger implements SensorDataListener
{	
	
	protected Timer surveyTimer;
	
	public ActiveTrigger(String survey) throws TriggerException
	{
		super(survey);
		this.surveyTimer = new Timer();
	}
	
	protected abstract void sampleForSurvey();
	
	@Override
	public abstract void onDataSensed(SensorData sensorData);
	
	public void kill()
	{
		if (surveyTimer != null)
		{
			surveyTimer.cancel();
			surveyTimer = null;
		}
	}
	
}
