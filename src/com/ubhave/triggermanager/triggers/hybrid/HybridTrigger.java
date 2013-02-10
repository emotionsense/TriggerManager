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
import com.ubhave.triggermanager.config.TriggerConfig;
import com.ubhave.triggermanager.triggers.Trigger;
import com.ubhave.triggermanager.triggers.TriggerList;

public class HybridTrigger extends Trigger implements TriggerReceiver
{
	private final Trigger clockTrigger;
	private final SensorTriggerListener sensorListener;

	private Thread waitThread;

	public HybridTrigger(Context context, int clockType, int sensorType, TriggerReceiver listener, TriggerConfig params) throws TriggerException, ESException
	{
		super(context, listener);
		this.clockTrigger = TriggerList.createTrigger(context, clockType, this, params);
		sensorListener = new SensorTriggerListener(context, sensorType, this, params);
	}

	@Override
	public void onNotificationTriggered()
	{
		if (waitThread == null)
		{
//			int wait_time;
//			try
//			{
//				wait_time = (Integer) config.getParameter(GlobalConfig.SENSE_CYCLE_TOTAL_TIME_MILLIES);
//			}
//			catch (TriggerException e)
//			{
//				wait_time = Constants.DEFAULT_WAIT_TIME_MILLIES;
//			}
//			sensorListener.resume();
//			startWaiting(wait_time);
		}
		else
		{
			waitThread.interrupt();
			sendNotification();
		}
	}

	@Override
	public void onCrossingLowBatteryThreshold(boolean isBelowThreshold)
	{
		if (isBelowThreshold)
		{
			clockTrigger.pause();
		}
		else
		{
			clockTrigger.resume();
		}
	}

//	private void startWaiting(final int wait_time)
//	{
//		if (waitThread == null)
//		{
//			waitThread = new Thread()
//			{
//				@Override
//				public void run()
//				{
//					try
//					{
//						int waited = 0;
//
//						while (waited < wait_time)
//						{
//							sleep(1000);
//							waited += 1000;
//						}
//						callForSurvey();
//					}
//					catch (InterruptedException e)
//					{
//					}
//				}
//			};
//			waitThread.start();
//		}
//	}

	@Override
	public void kill()
	{
		clockTrigger.kill();
		sensorListener.kill();
	}

	@Override
	public void pause()
	{
		clockTrigger.pause();
		sensorListener.pause();
	}

	@Override
	public void resume()
	{
		clockTrigger.resume();
	}
}
