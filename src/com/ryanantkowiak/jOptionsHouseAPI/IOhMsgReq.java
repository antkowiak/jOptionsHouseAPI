/*
 * Copyright (c) 2013 Ryan Antkowiak (antkowiak@gmail.com).
 * All rights reserved.
 * It is provided AS-IS, WITHOUT ANY WARRANTY either expressed or implied.
 * You may study, use, and modify it for any non-commercial purpose.
 * You may distribute it non-commercially as long as you retain this notice.
 * For a commercial use license, contact antkowiak@gmail.com.
 */
package com.ryanantkowiak.jOptionsHouseAPI;

/**
 * Base class for other classes that define a JSON representation of
 * OptionsHouse API requests.
 * 
 * @author Ryan Antkowiak (antkowiak@gmail.com)
 */
class IOhMsgReq
{
	/** Defines the page to request (must be "j" or "m") */
	protected String m_page;

	/** Contains JSON string of the request to OptionsHouse */
	protected String m_json;

	/**
	 * Default constructor
	 */
	protected IOhMsgReq()
	{
		m_page = "";
		m_json = "";
	}

	/**
	 * Retrieve the type of page to request (must be "j" or "m")
	 * 
	 * @return the type of page to request
	 */
	public String getPage()
	{
		return m_page;
	}

	/**
	 * Retrieve the JSON string of the request to OptionsHouse
	 * 
	 * @return the JSON string
	 */
	public String getJsonString()
	{
		return m_json;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("m_page=");
		sb.append(m_page);
		sb.append("\n");
		sb.append("m_json=");
		sb.append(m_json);
		sb.append("\n");

		return sb.toString();
	}

	/**
	 * Prints out debugging information for the request object
	 */
	public void debugPrint()
	{
		System.out.println(getClass().getName() + " [" + m_page + "]");
		System.out.println(m_json);
	}

}
