/*
 * Copyright (c) 2013 Ryan Antkowiak (antkowiak@gmail.com).
 * All rights reserved.
 * It is provided AS-IS, WITHOUT ANY WARRANTY either expressed or implied.
 * You may study, use, and modify it for any non-commercial purpose.
 * You may distribute it non-commercially as long as you retain this notice.
 * For a commercial use license, contact antkowiak@gmail.com.
 */
package com.ryanantkowiak.jOptionsHouseAPI;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * This class will retrieve the Positions associated with an account from
 * OptionsHouse
 * 
 * @author Ryan Antkowiak (antkowiak@gmail.com)
 */
public class OhAccountPositions extends IOh
{
	/**
	 * Defines one Position of an OptionsHouse account
	 * 
	 * @author Ryan Antkowiak (antkowiak@gmail.com)
	 */
	public class OhPosition
	{
		public String m_accountId;
		public double m_shareCostBasis;
		public boolean m_isCustomCostBasis;
		public String m_expString;
		public double m_stock;
		public String m_description;
		public double m_defaultCostBasis;
		public boolean m_isExchangeDelayed;
		public String m_underlying;
		public double m_spc;
		public double m_bid;
		public String m_securityKey;
		public long m_qty;
		public double m_dailyChange;
		public double m_multiplier;
		public double m_gain;
		public double m_mktVal;
		public double m_posValChange;
		public double m_price;
		public String m_strikeString;
		public boolean m_canExercise;
		public double m_costBasis;
		public boolean m_positionNewToday;
		public double m_ask;
	}

	/** authorization token for the session with OptionsHouse API */
	private String m_authToken;

	/** the account id for which the account positions will be requested */
	private String m_accountId;

	/** contains the request JSON message for account positions */
	private OhMsgAccountPositionsReq m_request;

	/** contains the response JSON message for account positions */
	private OhMsgAccountPositionsRsp m_response;

	/**
	 * Constructor sets up the input values for retrieving the account positions
	 * 
	 * @param authToken
	 *            the authorization token for the session with OptionsHouse
	 * @param accountId
	 *            the account id for which account positions will be requested
	 */
	public OhAccountPositions(String authToken, String accountId)
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
	protected OhMsgAccountPositionsReq getRequest()
	{
		return m_request;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ryanantkowiak.jOptionsHouseAPI.IOh#getResponse()
	 */
	@Override
	protected OhMsgAccountPositionsRsp getResponse()
	{
		return m_response;
	}

	/**
	 * Sets up the input values for retrieving the account positions. Also sends
	 * the request to the OptionsHouse server.
	 * 
	 * @param authToken
	 *            the authorization token for the session with OptionsHouse
	 * @param accountId
	 *            the account id for which account positions will be requested
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
		m_request = new OhMsgAccountPositionsReq(m_authToken, m_accountId);
		m_httpRequest = new OptionsHouseHttpRequest(m_request.getJsonString(),
				m_request.getPage());
		m_httpRequest.sendRequest();
		m_response = OhMsgAccountPositionsRsp
				.build(m_httpRequest.getResponse());

		super.execute();
	}

	/**
	 * Returns the timestamp at which the positions were retrieved
	 * 
	 * @return the timestamp when the positions were retrieved
	 */
	public String getTimestamp()
	{
		if (null != getData() && null != getData().timeStamp)
		{
			return m_response.EZMessage.data.timeStamp;
		}

		return "";
	}

	/**
	 * Returns the number of positions that were retrieved
	 * 
	 * @return the number of positions
	 */
	public int getNumPositions()
	{
		if (null != getUnifiedList())
		{
			return getUnifiedList().size();
		}
		return 0;
	}

	/**
	 * Returns a list of all positions associated with the account
	 * 
	 * @return list of positions
	 */
	public List<OhPosition> getPositions()
	{
		List<OhPosition> positions = new ArrayList<OhPosition>();

		for (int i = 0; i < getNumPositions(); ++i)
		{
			OhPosition pos = new OhPosition();

			pos.m_accountId = getUnifiedList().get(i).accountId;
			pos.m_shareCostBasis = getUnifiedList().get(i).shareCostBasis;
			pos.m_isCustomCostBasis = getUnifiedList().get(i).isCustomCostBasis;
			pos.m_expString = getUnifiedList().get(i).expString;
			pos.m_stock = getUnifiedList().get(i).stock;
			pos.m_description = getUnifiedList().get(i).description;
			pos.m_defaultCostBasis = getUnifiedList().get(i).defaultCostBasis;
			pos.m_isExchangeDelayed = getUnifiedList().get(i).isExchangeDelayed;
			pos.m_underlying = getUnifiedList().get(i).underlying;
			pos.m_spc = getUnifiedList().get(i).spc;
			pos.m_bid = getUnifiedList().get(i).bid;
			pos.m_securityKey = getUnifiedList().get(i).securityKey;
			pos.m_qty = getUnifiedList().get(i).qty;
			pos.m_dailyChange = getUnifiedList().get(i).dailyChange;
			pos.m_multiplier = getUnifiedList().get(i).multiplier;
			pos.m_gain = getUnifiedList().get(i).gain;
			pos.m_mktVal = getUnifiedList().get(i).mktVal;
			pos.m_posValChange = getUnifiedList().get(i).posValChange;
			pos.m_price = getUnifiedList().get(i).price;
			pos.m_strikeString = getUnifiedList().get(i).strikeString;
			pos.m_canExercise = getUnifiedList().get(i).canExercise;
			pos.m_costBasis = getUnifiedList().get(i).costBasis;
			pos.m_positionNewToday = getUnifiedList().get(i).positionNewToday;
			pos.m_ask = getUnifiedList().get(i).ask;

			positions.add(pos);
		}

		return positions;
	}
	
	/**
	 * Returns the position data associated with the provided index of the list
	 * 
	 * @param index	index of the position data
	 * @return	the position data
	 */
	public OhPosition getPosition(int index)
	{
		List<OhPosition> p = getPositions();
		
		if (null != p &&
				index >= 0 &&
				index < p.size())
		{
			return p.get(index);
		}
		
		return null;
	}

	/**
	 * Internal helper method to get data from the response message
	 * 
	 * @return the data object
	 */
	private OhMsgAccountPositionsRsp.EZMessage_.data_ getData()
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
	 * @return the unified list
	 */
	private List<OhMsgAccountPositionsRsp.EZMessage_.data_.unified_> getUnifiedList()
	{
		if (null != getData() && null != getData().unified)
		{
			return getData().unified;
		}

		return null;
	}

}

/**
 * Internal data structure to represent JSON communication with the OptionsHouse
 * API. Specifies the request for account positions data.
 * 
 * @author Ryan Antkowiak (antkowiak@gmail.com)
 */
class OhMsgAccountPositionsReq extends IOhMsgReq
{
	public OhMsgAccountPositionsReq(String authToken, String account)
	{
		m_page = "m";

		EZAccountPositionsReq ezReq = new EZAccountPositionsReq();
		ezReq.EZMessage.action = "account.positions";
		ezReq.EZMessage.data.authToken = authToken;
		ezReq.EZMessage.data.account = account;

		m_json = (new GsonBuilder()).create().toJson(ezReq, ezReq.getClass());
	}

	public class EZAccountPositionsReq
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
 * API. Specifies the response for account positions data.
 * 
 * @author Ryan Antkowiak (antkowiak@gmail.com)
 */
class OhMsgAccountPositionsRsp extends IOhMsgRsp
{
	@Override
	public EZMessageBaseRsp getEZ()
	{
		return EZMessage;
	}

	public static OhMsgAccountPositionsRsp build(String str)
	{
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(ErrorMap.class,
				new ErrorMapDeserializer());
		Gson gson = gsonBuilder.create();

		OhMsgAccountPositionsRsp rsp = (gson.fromJson(str,
				OhMsgAccountPositionsRsp.class));
		rsp.m_raw = str;
		return rsp;
	}

	public EZMessage_ EZMessage;

	public class EZMessage_ extends EZMessageBaseRsp
	{
		public data_ data;

		public class data_
		{
			public String timeStamp;

			public List<unified_> unified;

			public class unified_
			{
				public String accountId;
				public double shareCostBasis;
				public boolean isCustomCostBasis;
				public String expString;
				public double stock;
				public String description;
				public double defaultCostBasis;
				public boolean isExchangeDelayed;
				public String underlying;
				public double spc;
				public double bid;
				public String securityKey;
				public long qty;
				public double dailyChange;
				public double multiplier;
				public double gain;
				List<String> sortHint;
				public double mktVal;
				public double posValChange;
				public double price;
				public String strikeString;
				public boolean canExercise;
				public double costBasis;
				public boolean positionNewToday;
				public double ask;
			}
		}
	}

}
