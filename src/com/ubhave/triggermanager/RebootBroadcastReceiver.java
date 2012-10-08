package com.ubhave.triggermanager;

import com.lathia.experiencesense.ServiceAlarmReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class RebootBroadcastReceiver extends BroadcastReceiver
{

	@Override
	public void onReceive(Context context, Intent arg1)
	{
		Intent intent = new Intent(context, SurveyTriggerService.class);
		context.startService(intent);
		
		// start experience sense service
        ServiceAlarmReceiver.startAlarm(context);
	}

}
