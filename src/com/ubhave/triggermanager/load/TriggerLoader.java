package com.ubhave.triggermanager.load;


public class TriggerLoader extends JSONLoader
{
	private final static String LOG_TAG = "TriggerLoader";
	private final static String TRIGGERS = "triggers";

	// Types of triggers
	// Numbering: [General Type][Sensor][Number]
	// 1. Active
	private static final int MIC_IMMEDIATE = 10101;
	private static final int MIC_WAIT_FOR_SILENCE = 10102;
	
	// 2. Passive
	private static final int CALL_STATE = 20101;
	private static final int SMS_RECEIVED = 20201;
	private static final int PHONE_SCREEN_ON = 20301;
	
	// 3. Static
	private static final int FIXED_INTERVAL = 30001;
	private static final int FINAL_SURVEY = 30002;
	
//	private static void stopAllSensing()
//	{
//		try
//		{
//			ESSensorManager manager = ESSensorManager.getSensorManager(SurveyApplication.getContext());
//			manager.stopAllSensors();
//		}
//		catch (ESException e)
//		{
//			e.printStackTrace();
//			Thread waitThread = new Thread()
//			{
//				@Override
//				public void run()
//				{
//					try
//					{
//						int waited = 0;
//						while (waited < 1000 * 10)
//						{
//							sleep(100);
//							waited += 100;
//						}
//					}
//					catch (InterruptedException e)
//					{}
//					finally
//					{
//						stopAllSensing();
//					}
//				}
//			};
//			waitThread.start();
//		}
//	}

//	public static ArrayList<Trigger> loadTriggers()
//	{
//		try
//		{
//			String rawJSON = loadFileContents(Constants.TRIGGER_JSON_FILE);
//			if (rawJSON != null)
//			{
//				JSONParser p = new JSONParser();
//				JSONObject data = (JSONObject) p.parse(rawJSON);
//
//				ArrayList<Trigger> triggerList = new ArrayList<Trigger>();
//				JSONArray triggers = (JSONArray) data.get(TRIGGERS);
//				for (Object triggerdata : triggers)
//				{
//					JSONObject trigger = (JSONObject) triggerdata;
//					int type = ((Long) trigger.get(Trigger.TYPE)).intValue();
//					String target = (String) trigger.get(Trigger.TARGET_SURVEY);
//					
//					// Active Triggers
//					if (type == MIC_IMMEDIATE)
//					{
//						if (Constants.TEST_MODE) ESLogger.log(LOG_TAG, "Adding: Mic Immediate Trigger");
//						triggerList.add(new NonSilentSampleTrigger(target));
//					}
//					else if (type == MIC_WAIT_FOR_SILENCE)
//					{
//						if (Constants.TEST_MODE) ESLogger.log(LOG_TAG, "Adding: Mic Non-Immediate Trigger");
//						triggerList.add(new PostSampleSilenceTrigger(target));
//					}
//					
//					// Passive Triggers
//					else if (type == CALL_STATE)
//					{
//						if (Constants.TEST_MODE) ESLogger.log(LOG_TAG, "Adding: Call state Trigger");
//						triggerList.add(new CallTrigger(target));
//					}
//					else if (type == SMS_RECEIVED)
//					{
//						if (Constants.TEST_MODE) ESLogger.log(LOG_TAG, "Adding: SMS received Trigger");
//						triggerList.add(new SMSTrigger(target));
//					}
//					else if (type == PHONE_SCREEN_ON)
//					{
//						if (Constants.TEST_MODE) ESLogger.log(LOG_TAG, "Adding: Screen Trigger");
//						triggerList.add(new ScreenActivityTrigger(target));
//					}
//					
//					// Fixed Time Triggers
//					else if (type == FIXED_INTERVAL)
//					{
//						if (Constants.TEST_MODE) ESLogger.log(LOG_TAG, "Adding: Fixed Interval Trigger");
//						triggerList.add(new IntervalTrigger(target, trigger));
//					}
//					else if (type == FINAL_SURVEY)
//					{
//						if (Constants.TEST_MODE) ESLogger.log(LOG_TAG, "Adding: Final Trigger");
//						triggerList.add(new OneTimeTrigger(target, trigger));
//					}
//				}
//				
//				if (triggerList.isEmpty())
//				{
////					stopAllSensing();
//				}
//				else {
////					try {
////						ESSensorManager.getSensorManager(SurveyApplication.getContext()).startAllSensors();
////					}
////					catch(ESException e)
////					{
////						 TODO
////					}
//					
//				}
//				return triggerList;
//			}
//			else if (Constants.TEST_MODE)
//			{
//				Log.d(LOG_TAG, "rawJSON is null!");
//			}
//		}
//		catch (IOException e)
//		{
//			e.printStackTrace();
//		}
//		catch (ParseException e)
//		{
//			e.printStackTrace();
//		}
//		
////		stopAllSensing();
//		return null;
//	}

}
