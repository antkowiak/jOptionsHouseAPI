/*
 * Copyright (c) 2013 Ryan Antkowiak .
 * All rights reserved.
 * It is provided AS-IS, WITHOUT ANY WARRANTY either expressed or implied.
 * You may study, use, and modify it for any non-commercial purpose.
 * You may distribute it non-commercially as long as you retain this notice.
 * For a commercial use license, contact .
 */
package com.ryanantkowiak.jOptionsHouseAPI;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * This class will retrieve the Account Activity from OptionsHouse
 * 
 * @author Ryan Antkowiak 
 */
public class OhAccountActivity extends IOh
{
	/**
	 * Defines one Account Activity event from an OptionsHouse account
	 * 
	 * @author Ryan Antkowiak 
	 */
	public class OhAccountActivityEvent
	{
		/** date of the event */
		public String m_activityDateString;

		/** price associated with the event */
		public double m_price;

		/** account id associated with the event */
		public String m_accountId;

		/** stock or option symbol or security associated with the event */
		public String m_symbol;

		/** transaction type of the event */
		public String m_transaction;

		/** description of the event */
		public String m_description;

		/** quantity involved in the event */
		public double m_quantity;

		/** net amount involved in the event */
		public double m_netAmount;
	}

	/** authorization token for the session with OptionsHouse API */
	private String m_authToken;

	/** the account id for which the account activity will be requested */
	private String m_accountId;

	/** contains the request JSON message for account activity */
	private OhMsgAccountActivityReq m_request;

	/** contains the response JSON message for account activity */
	private OhMsgAccountActivityRsp m_response;

	/**
	 * Constructor sets up the input values for retrieving the account activity
	 * 
	 * @param authToken
	 *            the authorization token for the session with OptionsHouse
	 * @param accountId
	 *            the account id for which account activity will be requested
	 */
	public OhAccountActivity(String authToken, String accountId)
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
	protected OhMsgAccountActivityReq getRequest()
	{
		return m_request;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ryanantkowiak.jOptionsHouseAPI.IOh#getResponse()
	 */
	@Override
	protected OhMsgAccountActivityRsp getResponse()
	{
		return m_response;
	}

	/**
	 * Sets up the input values for retrieving the account activity. Also sends
	 * the request to the OptionsHouse server.
	 * 
	 * @param authToken
	 *            the authorization token for the session with OptionsHouse
	 * @param accountId
	 *            the account id for which account activity will be requested
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
		m_request = new OhMsgAccountActivityReq(m_authToken, m_accountId);
		m_httpRequest = new OptionsHouseHttpRequest(m_request.getJsonString(),
				m_request.getPage());
		m_httpRequest.sendRequest();
		m_response = OhMsgAccountActivityRsp.build(m_httpRequest.getResponse());

		super.execute();
	}

	/**
	 * Returns the timestamp of when account activity is retrieved.
	 * 
	 * @return timestamp of when account activity is retrieved
	 */
	public long getTimestamp()
	{
		if (null != getData())
		{
			return getData().timeStamp;
		}

		return 0;
	}

	/**
	 * Return the "total" field specified by OptionsHouse. Note: The
	 * documentation isn't clear what this value means. Use at your own risk
	 * 
	 * @return the "total" field from OptionsHouse
	 */
	public long getTotal()
	{
		if (null != getData())
		{
			return getData().total;
		}

		return 0;
	}

	/**
	 * Returns the number of account activity events that had been retrieved
	 * 
	 * @return the number of account activity events that had been retrieved
	 */
	public int getNumEvents()
	{
		if (null != getActivityList())
		{
			return getActivityList().size();
		}
		return 0;
	}

	
	/**
	 * Return the account activity event at a specified index into the list.
	 * 
	 * @param index	the index into the list of account activity events
	 * @return the corresponding activity event
	 */
	public OhAccountActivityEvent getActivityEvent(int index)
	{
		if (index >= getNumEvents())
		{
			return null;
		}
		
		return getActivityEvents().get(index);
	}
	
	/**
	 * Returns a list of all the account activity events that had been retrieved
	 * from the OptionsHouse API server.
	 * 
	 * @return list of all account activity events that had been retrieved
	 */
	public List<OhAccountActivityEvent> getActivityEvents()
	{
		List<OhAccountActivityEvent> events = new ArrayList<OhAccountActivityEvent>();

		for (int i = 0; i < getNumEvents(); ++i)
		{
			OhAccountActivityEvent event = new OhAccountActivityEvent();

			event.m_activityDateString = getActivityList().get(i).activityDateStr;
			event.m_price = getActivityList().get(i).price;
			event.m_accountId = getActivityList().get(i).accountId;
			event.m_symbol = getActivityList().get(i).symbol;
			event.m_transaction = getActivityList().get(i).transaction;
			event.m_description = getActivityList().get(i).description;
			event.m_quantity = getActivityList().get(i).qty;
			event.m_netAmount = getActivityList().get(i).netAmount;

			events.add(event);
		}

		return events;
	}

	/**
	 * Internal helper method to get data from the response message
	 * 
	 * @return the data object
	 */
	private OhMsgAccountActivityRsp.EZMessage_.data_ getData()
	{
		if (null != m_response && null != m_response.EZMessage
				&& null != m_response.EZMessage.data)
		{
			return m_response.EZMessage.data;
		}

		return null;
	}

	/**
	 * Internal helper method to get data from the response message
	 * 
	 * @return the activity list
	 */
	private List<OhMsgAccountActivityRsp.EZMessage_.data_.activity_> getActivityList()
	{
		if (null != getData() && null != getData().activity)
		{
			return getData().activity;
		}

		return null;
	}

}

/**
 * Internal data structure to represent JSON communication with the OptionsHouse
 * API. Specifies the request for account activity data.
 * 
 * @author Ryan Antkowiak 
 */
class OhMsgAccountActivityReq extends IOhMsgReq
{
	public OhMsgAccountActivityReq(String authToken, String account)
	{
		m_page = "m";

		EZAccountActivityReq ezReq = new EZAccountActivityReq();
		ezReq.EZMessage.action = "account.activity";
		ezReq.EZMessage.data.authToken = authToken;
		ezReq.EZMessage.data.account = account;

		m_json = (new GsonBuilder()).create().toJson(ezReq, ezReq.getClass());
	}

	public class EZAccountActivityReq
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
 * API. Specifies the response for account activity data.
 * 
 * @author Ryan Antkowiak 
 */
class OhMsgAccountActivityRsp extends IOhMsgRsp
{
	@Override
	public EZMessageBaseRsp getEZ()
	{
		return EZMessage;
	}

	public static OhMsgAccountActivityRsp build(String str)
	{
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(ErrorMap.class,
				new ErrorMapDeserializer());
		Gson gson = gsonBuilder.create();

		OhMsgAccountActivityRsp rsp = (gson.fromJson(str,
				OhMsgAccountActivityRsp.class));
		rsp.m_raw = str;
		return rsp;
	}

	public EZMessage_ EZMessage;

	public class EZMessage_ extends EZMessageBaseRsp
	{
		public data_ data;

		public class data_
		{
			public long total;
			public long timeStamp;
			public List<activity_> activity;

			public class activity_
			{
				public String activityDateStr;
				public double price;
				public String accountId;
				public String symbol;
				public String transaction;
				public String description;
				public double qty;
				public double netAmount;
			}
		}
	}
}
