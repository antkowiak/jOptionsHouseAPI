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
 * This class will log into the OptionsHouse API. It will maintain an
 * "authToken" that will need to be used in any subsequent messages to the
 * OptionsHouse API during the session.
 * 
 * @author Ryan Antkowiak (antkowiak@gmail.com)
 */
public class OhLogin extends IOh
{
	/** login name for authentication with OptionsHouse API server */
	private String m_login;

	/** password for authentication with OptionsHouse API server */
	private String m_password;

	/** contains the request JSON message for logging in */
	private OhMsgAuthLoginReq m_request;

	/** contains the response JSON message for logging in */
	private OhMsgAuthLoginRsp m_response;

	/**
	 * Constructor sets up the input values for logging into OptionsHouse.
	 * 
	 * @param login
	 *            login name for authentication with OptionsHouse
	 * @param password
	 *            password for authentication with OptionsHouse
	 */
	public OhLogin(String login, String password)
	{
		m_login = login;
		m_password = password;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ryanantkowiak.jOptionsHouseAPI.IOh#getRequest()
	 */
	@Override
	protected OhMsgAuthLoginReq getRequest()
	{
		return m_request;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ryanantkowiak.jOptionsHouseAPI.IOh#getResponse()
	 */
	@Override
	protected OhMsgAuthLoginRsp getResponse()
	{
		return m_response;
	}

	/**
	 * Sets up the input values for logging into OptionsHouse. Also sends the
	 * request to the OptionsHouse server.
	 * 
	 * @param login
	 *            login name for authentication with OptionsHouse
	 * @param password
	 *            password for authentication with OptionsHouse
	 */
	public void execute(String login, String password)
	{
		m_login = login;
		m_password = password;
		execute();
	}

	/**
	 * Send the request to the OptionsHouse API server and retrieve the
	 * response.
	 */
	@Override
	public void execute()
	{
		m_request = new OhMsgAuthLoginReq(m_login, m_password);
		m_httpRequest = new OptionsHouseHttpRequest(m_request.getJsonString(),
				m_request.getPage());
		m_httpRequest.sendRequest();
		m_response = OhMsgAuthLoginRsp.build(m_httpRequest.getResponse());

		super.execute();
	}

	/**
	 * Returns true if the login attempt was successful with the provided
	 * credentials
	 * 
	 * @return true if the login attempt was a success
	 */
	public boolean loginSuccess()
	{
		return (!getAuthToken().isEmpty());
	}

	/**
	 * Returns the authToken for use with any subsequent communication with the
	 * OptionsHouse API server for this session.
	 * 
	 * @return the authToken
	 */
	public String getAuthToken()
	{
		String token = "";

		if (null != getData() && null != getData().authToken)
		{
			token = getData().authToken;
		}

		return token;
	}

	/**
	 * Returns the first name of the person associated with the provided
	 * OptionsHouse credentials.
	 * 
	 * @return the first name
	 */
	public String getFirstName()
	{
		if (null != getData() && null != getData().firstName)
		{
			return getData().firstName;
		}
		return "";
	}

	/**
	 * Returns the last name of the person associated with the provided
	 * OptionsHouse credentials.
	 * 
	 * @return the last name
	 */
	public String getLastName()
	{
		if (null != getData() && null != getData().lastName)
		{
			return getData().lastName;
		}
		return "";
	}

	/**
	 * Returns true if the owner of the account has funded it
	 * 
	 * @return true if the account is funded
	 */
	public boolean isFunded()
	{
		if (null != getData())
		{
			return getData().funded;
		}
		return false;
	}

	/**
	 * Returns true if the account owner will receive delayed quotes
	 * 
	 * @return true if the quotes are delayed
	 */
	public boolean areQuotesDelayed()
	{
		if (null != getData())
		{
			return getData().delayedQuotes;
		}

		return false;
	}

	/**
	 * Returns the access string
	 * 
	 * @return access string
	 */
	public String getAccessString()
	{
		if (null != getData() && null != getData().access)
		{
			return getData().access;
		}
		return "";
	}

	/**
	 * Returns true if this is a professional account
	 * 
	 * @return true if the account is professional
	 */
	public boolean isProfessional()
	{
		if (null != getData())
		{
			return getData().professional;
		}
		return false;
	}

	/**
	 * Internal helper method to get data from the response message
	 * 
	 * @return the data object
	 */
	private OhMsgAuthLoginRsp.EZMessage_.data_ getData()
	{
		if (null != m_response && null != m_response.EZMessage
				&& null != m_response.EZMessage.data)
		{
			return m_response.EZMessage.data;
		}

		return null;
	}

}

/**
 * Internal data structure to represent JSON communication with the OptionsHouse
 * API. Specifies the request for account logging in.
 * 
 * @author Ryan Antkowiak (antkowiak@gmail.com)
 */
class OhMsgAuthLoginReq extends IOhMsgReq
{
	public OhMsgAuthLoginReq(String userName, String password)
	{
		m_page = "m";

		EZLoginReq ezReq = new EZLoginReq();
		ezReq.EZMessage.action = "auth.login";
		ezReq.EZMessage.data.userName = userName;
		ezReq.EZMessage.data.password = password;

		m_json = (new GsonBuilder()).create().toJson(ezReq, ezReq.getClass());
	}

	public class EZLoginReq
	{
		public EZMessage_ EZMessage = new EZMessage_();

		public class EZMessage_
		{
			public String action;

			public data_ data = new data_();

			public class data_
			{
				public String userName;
				public String password;
			}
		}
	}

}

/**
 * Internal data structure to represent JSON communication with the OptionsHouse
 * API. Specifies the response for logging in.
 * 
 * @author Ryan Antkowiak (antkowiak@gmail.com)
 */
class OhMsgAuthLoginRsp extends IOhMsgRsp
{
	@Override
	public EZMessageBaseRsp getEZ()
	{
		return EZMessage;
	}

	public static OhMsgAuthLoginRsp build(String str)
	{
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(ErrorMap.class,
				new ErrorMapDeserializer());
		Gson gson = gsonBuilder.create();

		OhMsgAuthLoginRsp rsp = (gson.fromJson(str, OhMsgAuthLoginRsp.class));
		rsp.m_raw = str;
		return rsp;
	}

	public EZMessage_ EZMessage;

	public class EZMessage_ extends EZMessageBaseRsp
	{
		public data_ data;

		public class data_
		{
			public String lastName;
			public boolean funded;

			public nasdaq_ nasdaq;

			class nasdaq_
			{
				public boolean professional;
				public boolean agree;
			}

			public String authToken;

			public nyse_ nyse;

			class nyse_
			{
				public boolean professional;
				public boolean agree;
			}

			public opera_ opera;

			class opera_
			{
				public boolean professional;
				public boolean agree;
			}

			public boolean delayedQuotes;
			public String firstName;
			public String access;
			public boolean requiresAccountCreation;
			public boolean professional;
		}
	}

}
