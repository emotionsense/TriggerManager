package com.ubhave.triggermanager;

public class TriggerException extends Exception
{
	private static final long serialVersionUID = -6952859423645368705L;
	
	public static final int NO_CONTEXT = 8000;
	public static final int INVALID_STATE = 8001;
	public static final int INVALID_CONFIG_KEY = 8002;
	public static final int DATE_IN_PAST = 8003;

	private int errorCode;
	private String message;

	public TriggerException(int errorCode, String message)
	{
		super(message);
		this.errorCode = errorCode;
		this.message = message;
	}
	
	public int getErrorCode()
	{
		return errorCode;
	}
	
	public String getMessage()
	{
		return message;
	}

}
