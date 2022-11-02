/*
 * Copyright (c) 2013 Ryan Antkowiak .
 * All rights reserved.
 * It is provided AS-IS, WITHOUT ANY WARRANTY either expressed or implied.
 * You may study, use, and modify it for any non-commercial purpose.
 * You may distribute it non-commercially as long as you retain this notice.
 * For a commercial use license, contact .
 */
package com.ryanantkowiak.jOptionsHouseAPI;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * This class will log out of the OptionsHouse API
 * 
 * @author Ryan Antkowiak 
 */

public class OhLogout extends IOh
{
	/** authorization token for the session with OptionsHouse API */
	private String m_authToken;

	/** contains the request JSON message for logging out */
	private OhMsgAuthLogoutReq m_request;

	/** contains the response JSON message for logging out */
	private OhMsgAuthLogoutRsp m_response;

	/**
	 * Constructor sets up the input values for logging out.
	 * 
	 * @param authToken
	 *            the authorization token for the session with OptionsHouse
	 */
	public OhLogout(String authToken)
	{
		m_authToken = authToken;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ryanantkowiak.jOptionsHouseAPI.IOh#getRequest()
	 */
	@Override
	protected OhMsgAuthLogoutReq getRequest()
	{
		return m_request;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ryanantkowiak.jOptionsHouseAPI.IOh#getResponse()
	 */
	@Override
	protected OhMsgAuthLogoutRsp getResponse()
	{
		return m_response;
	}

	/**
	 * Sets up the input values for logging out. Also sends the request to the
	 * OptionsHouse server.
	 * 
	 * @param authToken
	 *            the authorization token for the session with OptionsHouse
	 */
	public void execute(String authToken)
	{
		m_authToken = authToken;
		execute();
	}

	/**
	 * Send the request to the OptionsHouse API server and retrieve the
	 * response.
	 */
	@Override
	public void execute()
	{
		m_request = new OhMsgAuthLogoutReq(m_authToken);
		m_httpRequest = new OptionsHouseHttpRequest(m_request.getJsonString(),
				m_request.getPage());
		m_httpRequest.sendRequest();
		m_response = OhMsgAuthLogoutRsp.build(m_httpRequest.getResponse());

		super.execute();
	}

	/**
	 * Returns true if the logout was successful
	 * 
	 * @return true if success
	 */
	public boolean logoutSuccess()
	{
		return (getAuthToken().isEmpty());
	}

	/**
	 * Returns the authToken. This should be empty after a successful logout.
	 * 
	 * @return the authToken
	 */
	public String getAuthToken()
	{
		String token = m_authToken;

		if (null != m_response && null != m_response.EZMessage
				&& null != m_response.EZMessage.data
				&& null != m_response.EZMessage.data.authToken)
		{
			token = m_response.EZMessage.data.authToken;
		}

		return token;
	}

}

/**
 * Internal data structure to represent JSON communication with the OptionsHouse
 * API. Specifies the request for logging out.
 * 
 * @author Ryan Antkowiak 
 */
class OhMsgAuthLogoutReq extends IOhMsgReq
{
	public OhMsgAuthLogoutReq(String authToken)
	{
		m_page = "m";

		EZLogoutReq ezReq = new EZLogoutReq();
		ezReq.EZMessage.action = "auth.logout";
		ezReq.EZMessage.data.authToken = authToken;

		m_json = (new GsonBuilder()).create().toJson(ezReq, ezReq.getClass());
	}

	public class EZLogoutReq
	{
		public EZMessage_ EZMessage = new EZMessage_();

		public class EZMessage_
		{
			public String action;

			public data_ data = new data_();

			public class data_
			{
				public String authToken;
			}
		}
	}
}

/**
 * Internal data structure to represent JSON communication with the OptionsHouse
 * API. Specifies the response for logging out.
 * 
 * @author Ryan Antkowiak 
 */
class OhMsgAuthLogoutRsp extends IOhMsgRsp
{
	@Override
	public EZMessageBaseRsp getEZ()
	{
		return EZMessage;
	}

	public static OhMsgAuthLogoutRsp build(String str)
	{
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(ErrorMap.class,
				new ErrorMapDeserializer());
		Gson gson = gsonBuilder.create();

		OhMsgAuthLogoutRsp rsp = (gson.fromJson(str, OhMsgAuthLogoutRsp.class));
		rsp.m_raw = str;
		return rsp;
	}

	public EZMessage_ EZMessage;

	public class EZMessage_ extends EZMessageBaseRsp
	{
		public data_ data;

		public class data_
		{
			public String authToken;
		}
	}

}
