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

import android.content.Context;
import android.content.SharedPreferences;
import android.util.SparseArray;

import com.ubhave.triggermanager.TriggerException;

public class GlobalConfig
{
	public static final String DO_NOT_DISTURB_BEFORE = "beforeHour";
	public static final String DO_NOT_DISTURB_AFTER = "afterHour";
	public static final String MAX_DAILY_NOTIFICATION_CAP = "dailyCap";
	public static final String TRIGGERS_ENABLED = "triggersEnabled";

	public static final String MIN_TRIGGER_INTERVAL_MILLIES = "minInterval";
	public static final String SENSE_CYCLE_TOTAL_TIME_MILLIES = "senseTime";

	private static GlobalConfig globalConfig;
	private static final Object lock = new Object();

	public static GlobalConfig getGlobalConfig(Context c) throws TriggerException
	{
		if (c == null)
		{
			throw new TriggerException(TriggerException.NO_CONTEXT, "Context is null");
		}
		if (globalConfig == null)
		{
			synchronized (lock)
			{
				if (globalConfig == null)
				{
					globalConfig = new GlobalConfig(c);
				}
			}
		}
		return globalConfig;
	}

	private final SharedPreferences preferences;
	private final ConfigListenerList listeners;

	public GlobalConfig(Context context)
	{
		preferences = context.getSharedPreferences(Constants.GLOBAL_PREFERENCES, Context.MODE_PRIVATE);
		listeners = new ConfigListenerList();
	}

	public void setParameter(String parameterName, Object parameterValue)
	{
		synchronized (lock)
		{
			SharedPreferences.Editor editor = preferences.edit();
			if (parameterName.equals(TRIGGERS_ENABLED))
			{
				editor.putBoolean(TRIGGERS_ENABLED, (Boolean) parameterValue);
			}
			else if (parameterName.equals(MIN_TRIGGER_INTERVAL_MILLIES))
			{
				editor.putLong(MIN_TRIGGER_INTERVAL_MILLIES, (Long) parameterValue);
			}
			else
			{
				editor.putInt(parameterName, (Integer) parameterValue);
			}
			editor.commit();
			notifyListeners();
		}
	}

	public int addConfigListener(ConfigChangeListener listener)
	{
		int value = -1;
		try
		{
			value = listeners.add(listener);
		}
		catch (TriggerException e)
		{
			e.printStackTrace();
		}
		return value;
	}
	
	public void removeConfigListener(int id)
	{
		listeners.remove(id);
	}

	private void notifyListeners()
	{
		SparseArray<ConfigChangeListener> listenerList = listeners.getAll();
		for (int i = 0; i < listenerList.size(); i++)
		{
			listenerList.valueAt(i).onGlobalConfigChanged();
		}
	}

	public Object getParameter(String parameterName) throws TriggerException
	{
		synchronized (lock)
		{
			if (parameterName.equals(TRIGGERS_ENABLED))
			{
				return preferences.getBoolean(TRIGGERS_ENABLED, Constants.DEFAULT_TRIGGERS_ENABLED);
			}
			else if (parameterName.equals(MIN_TRIGGER_INTERVAL_MILLIES))
			{
				return preferences.getLong(MIN_TRIGGER_INTERVAL_MILLIES, Constants.DEFAULT_MIN_TRIGGER_INTERVAL_MILLIES);
			}
			else
			{
				return preferences.getInt(parameterName, getDefault(parameterName));
			}
		}
	}

	private int getDefault(String key) throws TriggerException
	{
		if (key.equals(DO_NOT_DISTURB_BEFORE))
			return Constants.DEFAULT_DO_NOT_DISTURB_BEFORE;
		else if (key.equals(DO_NOT_DISTURB_AFTER))
			return Constants.DEFAULT_DO_NOT_DISTURB_AFTER;
		else if (key.equals(MIN_TRIGGER_INTERVAL_MILLIES))
			return Constants.DEFAULT_MIN_TRIGGER_INTERVAL_MILLIES;
		else if (key.equals(SENSE_CYCLE_TOTAL_TIME_MILLIES))
			return Constants.DEFAULT_SENSE_TIME_MILLIES;
		else if (key.equals(MAX_DAILY_NOTIFICATION_CAP))
			return Constants.DEFAULT_DAILY_NOTIFICATION_CAP;
		else
			throw new TriggerException(TriggerException.INVALID_CONFIG_KEY, "Key: " + key + " does not exist");
	}

}
