/*
 * Copyright (c) 2013 Ryan Antkowiak (antkowiak@gmail.com).
 * All rights reserved.
 * It is provided AS-IS, WITHOUT ANY WARRANTY either expressed or implied.
 * You may study, use, and modify it for any non-commercial purpose.
 * You may distribute it non-commercially as long as you retain this notice.
 * For a commercial use license, contact antkowiak@gmail.com.
 */
package com.ryanantkowiak.jOptionsHouseAPI;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * This class will send a session KeepAlive request to OptionsHouse. A
 * "KeepAlive" will prevent a session from expiring.  Please note: I have
 * noticed that sometimes sending other (non-KeepAlive) messages to the OptionsHouse
 * server will NOT NECESSARILY preserve your session. It is highly recommended that
 * you send a KeepAlive every few minutes, even if your application is already
 * sending other messages.
 * 
 * @author Ryan Antkowiak (antkowiak@gmail.com)
 */
public class OhKeepAlive extends IOh
{
	/** authorization token for the session with OptionsHouse API */
	private String m_authToken;

	/** the account id for which the KeepAlive will be sent */
	private String m_accountId;

	/** contains the request JSON message for the KeepAlive */
	private OhMsgAuthKeepAliveReq m_request;

	/** contains the response JSON message for the KeepAlive */
	private OhMsgAuthKeepAliveRsp m_response;

	/**
	 * Constructor sets up the input values for requesting the KeepAlive
	 * 
	 * @param authToken
	 *            the authorization token for the session with OptionsHouse
	 * @param accountId
	 *            the account id for which the KeepAlive will be requested
	 */
	public OhKeepAlive(String authToken, String accountId)
	{
		m_authToken = authToken;
		m_accountId = accountId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ryanantkowiak.jOptionsHouseAPI.IOh#getRequest()
	 */
	@Override
	protected OhMsgAuthKeepAliveReq getRequest()
	{
		return m_request;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ryanantkowiak.jOptionsHouseAPI.IOh#getResponse()
	 */
	@Override
	protected OhMsgAuthKeepAliveRsp getResponse()
	{
		return m_response;
	}

	/**
	 * Sets up the input values for requesting the KeepAlive. Also sends the
	 * request to the OptionsHouse server.
	 * 
	 * @param authToken
	 *            the authorization token for the session with OptionsHouse
	 * @param accountId
	 *            the account id for which the KeepAlive will be requested
	 */
	public void execute(String authToken, String accountId)
	{
		m_authToken = authToken;
		m_accountId = accountId;

		execute();
	}

	/**
	 * Send the request to the OptionsHouse API server and retrieve the
	 * response.
	 */
	@Override
	public void execute()
	{
		m_request = new OhMsgAuthKeepAliveReq(m_authToken, m_accountId);
		m_httpRequest = new OptionsHouseHttpRequest(m_request.getJsonString(),
				m_request.getPage());
		m_httpRequest.sendRequest();
		m_response = OhMsgAuthKeepAliveRsp.build(m_httpRequest.getResponse());

		super.execute();
	}
}

/**
 * Internal data structure to represent JSON communication with the OptionsHouse
 * API. Specifies the request for KeepAlive.
 * 
 * @author Ryan Antkowiak (antkowiak@gmail.com)
 */
class OhMsgAuthKeepAliveReq extends IOhMsgReq
{
	public OhMsgAuthKeepAliveReq(String authToken, String account)
	{
		m_page = "m";

		EZAuthKeepAlive ezReq = new EZAuthKeepAlive();
		ezReq.EZMessage.action = "auth.keepAlive";
		ezReq.EZMessage.data.authToken = authToken;
		ezReq.EZMessage.data.account = account;

		m_json = (new GsonBuilder()).create().toJson(ezReq, ezReq.getClass());
	}

	public class EZAuthKeepAlive
	{
		public EZMessage_ EZMessage = new EZMessage_();

		public class EZMessage_
		{
			public String action;

			public data_ data = new data_();

			public class data_
			{
				public String authToken;
				public String account;
			}
		}
	}
}

/**
 * Internal data structure to represent JSON communication with the OptionsHouse
 * API. Specifies the response for KeepAlive.
 * 
 * @author Ryan Antkowiak (antkowiak@gmail.com)
 */
class OhMsgAuthKeepAliveRsp extends IOhMsgRsp
{
	@Override
	public EZMessageBaseRsp getEZ()
	{
		return EZMessage;
	}

	public static OhMsgAuthKeepAliveRsp build(String str)
	{
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(ErrorMap.class,
				new ErrorMapDeserializer());
		Gson gson = gsonBuilder.create();

		OhMsgAuthKeepAliveRsp rsp = (gson.fromJson(str,
				OhMsgAuthKeepAliveRsp.class));
		rsp.m_raw = str;
		return rsp;
	}

	public EZMessage_ EZMessage;

	public class EZMessage_ extends EZMessageBaseRsp
	{
	}

}
