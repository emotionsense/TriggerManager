package com.ubhave.triggermanager;

import android.content.Context;

import com.ubhave.sensormanager.ESException;
import com.ubhave.triggermanager.config.GlobalConfig;
import com.ubhave.triggermanager.triggers.Trigger;
import com.ubhave.triggermanager.triggers.TriggerList;

public class TriggerManager implements TriggerManagerInterface
{
	private static TriggerManager triggerManager;
	private static final Object lock = new Object();

	private final GlobalConfig config;
	private final Context context;
	private final TriggerList triggers;

	public static TriggerManager getTriggerManager(Context context) throws TriggerException
	{
		if (triggerManager == null)
		{
			synchronized (lock)
			{
				triggerManager = new TriggerManager(context);
			}
		}
		return triggerManager;
	}

	private TriggerManager(final Context appContext) throws TriggerException
	{
		context = appContext;
		config = GlobalConfig.getGlobalConfig(appContext);
		triggers = new TriggerList();
	}

	@Override
	public int addTrigger(int triggerType, TriggerReceiver listener) throws ESException, TriggerException
	{
		Trigger trigger = TriggerList.createTrigger(context, triggerType, listener);
		return triggers.addTrigger(trigger, listener);
	}

	@Override
	public void removeTrigger(int triggerId)
	{
		triggers.removeTrigger(triggerId);
	}

	@Override
	public void setGlobalConfig(String configKey, Object configValue)
	{
		config.setParameter(configKey, configValue);
	}

	@Override
	public Object getGlobalConfigValue(String configKey) throws TriggerException
	{
		return config.getParameter(configKey);
	}
}
