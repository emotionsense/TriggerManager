/* **************************************************
 Copyright (c) 2012, University of Cambridge
 Neal Lathia, neal.lathia@cl.cam.ac.uk
 Kiran Rachuri, kiran.rachuri@cl.cam.ac.uk

This library was developed as part of the EPSRC Ubhave (Ubiquitous and
Social Computing for Positive Behaviour Change) Project. For more
information, please visit http://www.emotionsense.org

Permission to use, copy, modify, and/or distribute this software for any
purpose with or without fee is hereby granted, provided that the above
copyright notice and this permission notice appear in all copies.

THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES
WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF
MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY
SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES
WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR
IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 ************************************************** */

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
