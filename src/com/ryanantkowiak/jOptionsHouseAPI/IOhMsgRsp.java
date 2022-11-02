/*
 * Copyright (c) 2013 Ryan Antkowiak .
 * All rights reserved.
 * It is provided AS-IS, WITHOUT ANY WARRANTY either expressed or implied.
 * You may study, use, and modify it for any non-commercial purpose.
 * You may distribute it non-commercially as long as you retain this notice.
 * For a commercial use license, contact .
 */
package com.ryanantkowiak.jOptionsHouseAPI;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

/**
 * Base class for other classes that define a JSON representation of
 * OptionsHouse API responses.
 * 
 * @author Ryan Antkowiak 
 */
abstract class IOhMsgRsp
{
	/** the raw JSON string of the response */
	protected String m_raw;

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return m_raw;
	}

	/**
	 * Prints out debugging information for the request object
	 */
	public void debugPrint()
	{
		System.out.println(getClass().getName());
		System.out.println(m_raw);
	}

	public abstract EZMessageBaseRsp getEZ();

}

/**
 * Contains a map of error strings that can occur in an OptionsHouse JSON
 * message response.
 * 
 * @author Ryan Antkowiak 
 */
class ErrorMap
{
	/** the map of error messages */
	public Map<String, String> m_errors;

	/**
	 * Default constructor
	 */
	public ErrorMap()
	{
		m_errors = new HashMap<String, String>();
	}

	/**
	 * Constructor that sets up the map with json messages
	 * 
	 * @param json
	 */
	@SuppressWarnings("unchecked")
	public ErrorMap(String json)
	{
		m_errors = new HashMap<String, String>();
		if (null != json && !json.isEmpty())
		{
			Gson gson = new Gson();
			m_errors = gson.fromJson(json, m_errors.getClass());
		}
	}
}

/**
 * This class deserializes error messages from OptionsHouse into an instance of
 * the ErrorMap class.
 * 
 * @author Ryan Antkowiak 
 */
class ErrorMapDeserializer implements JsonDeserializer<ErrorMap>
{
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.google.gson.JsonDeserializer#deserialize(com.google.gson.JsonElement,
	 * java.lang.reflect.Type, com.google.gson.JsonDeserializationContext)
	 */
	public ErrorMap deserialize(JsonElement json, Type typeOfT,
			JsonDeserializationContext context) throws JsonParseException
	{
		return new ErrorMap(json.toString());
	}
}

/**
 * Base class for EZMessage response JSON structures
 * 
 * @author Ryan Antkowiak 
 */
class EZMessageBaseRsp
{
	/** map of error messages */
	public ErrorMap errors;

	/** alert message */
	public String alert;

	/** the message action */
	public String action;
}
