package com.ubhave.triggermanager.triggers.hybrid;

import android.content.Context;

import com.ubhave.sensormanager.ESException;
import com.ubhave.triggermanager.TriggerException;
import com.ubhave.triggermanager.TriggerReceiver;
import com.ubhave.triggermanager.triggers.Trigger;
import com.ubhave.triggermanager.triggers.TriggerList;

public class SensorTriggerListener implements TriggerReceiver
{
	
	private TriggerReceiver hybridListener;
	private final Trigger sensorTrigger;
	
	public SensorTriggerListener(Context context, int sensorType, TriggerReceiver hybridListener) throws TriggerException, ESException
	{
		this.hybridListener = hybridListener;
		this.sensorTrigger = TriggerList.createTrigger(context, sensorType, this);
		
		sensorTrigger.pause();
	}
	
	public void resume()
	{
		sensorTrigger.resume();
	}
	
	public void pause()
	{
		sensorTrigger.pause();
	}
	
	public void kill()
	{
		sensorTrigger.kill();
	}
	
	@Override
	public void onNotificationTriggered()
	{
		hybridListener.onNotificationTriggered();
		sensorTrigger.pause();
	}

	@Override
	public void onCrossingLowBatteryThreshold(boolean isBelowThreshold)
	{
		hybridListener.onCrossingLowBatteryThreshold(isBelowThreshold);
	}

}
