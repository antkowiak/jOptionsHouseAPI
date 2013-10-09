/*
 * Copyright (c) 2013 Ryan Antkowiak (antkowiak@gmail.com).
 * All rights reserved.
 * It is provided AS-IS, WITHOUT ANY WARRANTY either expressed or implied.
 * You may study, use, and modify it for any non-commercial purpose.
 * You may distribute it non-commercially as long as you retain this notice.
 * For a commercial use license, contact antkowiak@gmail.com.
 */
package com.ryanantkowiak.jOptionsHouseAPI;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

// TODO OVERALL - Add accessors for all data fields
// TODO Add new message for order preview
// TODO Add new message for complex order
// TODO Add new message combining OrderDetails and OrderHistory

/**
 * Base class for other classes that wrap functionality for communication with
 * OptionsHouse API.
 * 
 * @author Ryan Antkowiak (antkowiak@gmail.com)
 */
public abstract class IOh
{
	/** object to make the HTTP request to OptionsHouse API server */
	protected OptionsHouseHttpRequest m_httpRequest;

	/** Returns the request object */
	protected abstract IOhMsgReq getRequest();

	/** Returns the response object */
	protected abstract IOhMsgRsp getResponse();

	/**
	 * Returns true if the JSON response contained an alert string. Alert
	 * strings are not errors. They are informative messages that describe
	 * situations such as notifying customers when the OptionsHouse servers are
	 * currently experiencing latency, or notifying customers when certain days
	 * are an expiration date of options.
	 * 
	 * @return true if there is an alert string
	 */
	public boolean hasAlert()
	{
		return (!(getAlert().isEmpty()));
	}

	/**
	 * Returns true if the JSON response contained error messages
	 * 
	 * @return true if there were error messages
	 */
	public boolean hasErrors()
	{
		return (!(getErrors().isEmpty()));
	}

	/**
	 * Returns the alert text string, if applicable
	 * 
	 * @return the alert text string
	 */
	public String getAlert()
	{
		if (null != getResponse() && null != getResponse().getEZ()
				&& null != getResponse().getEZ().alert)
		{
			return getResponse().getEZ().alert;
		}

		return "";
	}

	/**
	 * Returns a map of all errors that occurred
	 * 
	 * @return a map of all error messages
	 */
	public Map<String, String> getErrors()
	{
		if (null != getResponse() && null != getResponse().getEZ()
				&& null != getResponse().getEZ().errors
				&& null != getResponse().getEZ().errors.m_errors)
		{
			return getResponse().getEZ().errors.m_errors;
		}

		return new HashMap<String, String>();
	}

	/**
	 * Returns the action string for this response
	 * 
	 * @return the action string
	 */
	public String getAction()
	{
		if (null != getResponse() && null != getResponse().getEZ()
				&& null != getResponse().getEZ().action)
		{
			return getResponse().getEZ().action;
		}

		return "";
	}

	/**
	 * Send the request to the OptionsHouse API server and retrieve the
	 * response. This base class method will simply print debug tracing, if
	 * required.
	 */
	public void execute()
	{
		if (OptionsHouseUtilities.DEBUG_MSG_TRACING)
		{
			if (null != getRequest())
			{
				getRequest().debugPrint();
				JsonUtilities.printJson(getRequest().getJsonString());
			}

			if (null != getResponse())
			{
				getResponse().debugPrint();
				JsonUtilities.printJson(getResponse().toString());
			}

			if (hasAlert())
			{
				System.out.println("ALERT='" + getAlert() + "'");
			}
			if (hasErrors())
			{
				System.out.println("Errors:");

				Iterator<String> iterator = getErrors().keySet().iterator();

				while (iterator.hasNext())
				{
					String key = iterator.next().toString();
					String value = getErrors().get(key).toString();

					System.out.println("KEY='" + key + "' VALUE='" + value
							+ "'");
				}
			}
			
			System.out.println();
		}
	}

}