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

package com.ubhave.triggermanager.config;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import com.ubhave.triggermanager.TriggerException;

public class GlobalState
{
	private final static String CURRENT_DAY = "currentDay";
	private final static String NOTIFICATIONS_SENT = "notifications";
	private final static String NOTIFICATION_CAP = "cap";
	private final static String TRIGGERS_ENABLED = "allTriggersEnabled";
	
	private static GlobalState globalState;
	private static final Object lock = new Object();

	public static GlobalState getGlobalState(Context c) throws TriggerException
	{
		if (c == null)
		{
			throw new TriggerException(TriggerException.NO_CONTEXT, "Context is null");
		}
		if (globalState == null)
		{
			synchronized (lock)
			{
				if (globalState == null)
				{
					globalState = new GlobalState(c);
				}
			}
		}
		return globalState;
	}
	
	private final SharedPreferences preferences;
	
	public GlobalState(final Context context) throws TriggerException
	{
		preferences = context.getSharedPreferences(TriggerManagerConstants.GLOBAL_STATE, Context.MODE_PRIVATE);
	}
	
	public void incrementNotificationsSent()
	{
		int notifications = getNotificationsSent() + 1;
		SharedPreferences.Editor editor = preferences.edit();
		editor.putInt(NOTIFICATIONS_SENT, notifications);
		editor.commit();
	}
	
	@SuppressLint("SimpleDateFormat")
	public void reset()
	{
		DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		String dateKey = formatter.format(System.currentTimeMillis());
		reset(dateKey);
	}
	
	private void reset(String dateKey)
	{
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString(CURRENT_DAY, dateKey);
		editor.putInt(NOTIFICATIONS_SENT, 0);
		editor.commit();
	}
	
	@SuppressLint("SimpleDateFormat")
	public int getNotificationsSent()
	{
		DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		String dateKey = formatter.format(System.currentTimeMillis());
		String currentDate = preferences.getString(CURRENT_DAY, null);
		if (currentDate == null || !dateKey.equals(currentDate))
		{
			reset(dateKey);
		}
		return preferences.getInt(NOTIFICATIONS_SENT, 0);
	}
	
	public boolean areNotificationsAllowed()
	{
		return preferences.getBoolean(TRIGGERS_ENABLED, TriggerManagerConstants.DEFAULT_TRIGGER_ENABLED);
	}
	
	public void setNotificationsAllowed(boolean value)
	{
		SharedPreferences.Editor editor = preferences.edit();
		editor.putBoolean(TRIGGERS_ENABLED, value);
		editor.commit();
	}
	
	public int getNotificationCap()
	{
		return preferences.getInt(NOTIFICATION_CAP, TriggerManagerConstants.DEFAULT_DAILY_NOTIFICATION_CAP);
	}
	
	public void setNotificationCap(int value)
	{
		SharedPreferences.Editor editor = preferences.edit();
		editor.putInt(NOTIFICATION_CAP, value);
		editor.commit();
	}
}
