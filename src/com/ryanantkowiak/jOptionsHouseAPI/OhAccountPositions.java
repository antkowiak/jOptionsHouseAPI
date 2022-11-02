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
 * This class will retrieve the Positions associated with an account from
 * OptionsHouse
 * 
 * @note There is a bug in the OptionsHouse API spec whereby requesting the JSON
 * data for the account positions will return a different data type if the account
 * only has one position, vs if the account has more than one position.  If the account
 * has only one position, it will return the position as one element of the type
 * that they call "unified" in the JSON.  However, if there is more than one position,
 * OH will return a list of elements of type "unified".  Obviously, it would be more
 * convenient (and would follow their API spec document more accurately) if they returned
 * a list of size 1 when the account only has one position.  Unfortunately, they did not 
 * do this.  As a result, there are a bunch of classes, members, and functions which bear
 * the name "WorkAround" in this file, which "works around" the OH bug.
 * 
 * @author Ryan Antkowiak 
 */
public class OhAccountPositions extends IOh
{
	/**
	 * Defines one Position of an OptionsHouse account
	 * 
	 * @author Ryan Antkowiak 
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
	
	/** contains the response JSON message for account positions (working around the 1-position-bug) **/
	private OhMsgAccountPositionsRspWorkAround m_responseWorkAround;
	

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
	
	/**
	 * Determine if this class needs to work around a bug in the OptionsHouse API
	 * 
	 * @note This method works around a bug in the OptionsHouse API whereby the "AccountPositions"
	 * request will not send a list of the "unified" type, and will instead only send one "unified"
	 * typed object if there is exactly one position. 
	 *
	 * @return boolean - true if this class must work around the OH API bug
	 */
	private boolean isWorkAround()
	{
		return (m_response == null);
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
	protected IOhMsgRsp getResponse()
	{
		if (isWorkAround())
		{
			return m_responseWorkAround;
		}
		else
		{
			return m_response;
		}
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
		
		m_response = null;
		m_responseWorkAround = null;
		try
		{
			m_response = OhMsgAccountPositionsRsp
				.build(m_httpRequest.getResponse());
		}
		catch (Exception e)
		{
			m_response = null;
			m_responseWorkAround = OhMsgAccountPositionsRspWorkAround
					.build(m_httpRequest.getResponse());
		}

		super.execute();
	}

	/**
	 * Returns the timestamp at which the positions were retrieved
	 * 
	 * @return the timestamp when the positions were retrieved
	 */
	public String getTimestamp()
	{
		if (isWorkAround())
		{
			if (null != getDataWorkAround() && null != getDataWorkAround().timeStamp)
			{
				return m_responseWorkAround.EZMessage.data.timeStamp;
			}
		}
		else
		{
			if (null != getData() && null != getData().timeStamp)
			{
				return m_response.EZMessage.data.timeStamp;
			}
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
		if (isWorkAround())
		{
			if (null != getUnifiedListWorkAround())
			{
				return getUnifiedListWorkAround().size();
			}
		}
		else
		{
			if (null != getUnifiedList())
			{
				return getUnifiedList().size();
			}
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
		if (isWorkAround())
		{
			List<OhPosition> positions = new ArrayList<OhPosition>();

			for (int i = 0; i < getNumPositions(); ++i)
			{
				OhPosition pos = new OhPosition();

				pos.m_accountId = 			getUnifiedListWorkAround().get(i).accountId;
				pos.m_shareCostBasis = 		getUnifiedListWorkAround().get(i).shareCostBasis;
				pos.m_isCustomCostBasis = 	getUnifiedListWorkAround().get(i).isCustomCostBasis;
				pos.m_expString = 			getUnifiedListWorkAround().get(i).expString;
				pos.m_stock = 				getUnifiedListWorkAround().get(i).stock;
				pos.m_description = 		getUnifiedListWorkAround().get(i).description;
				pos.m_defaultCostBasis = 	getUnifiedListWorkAround().get(i).defaultCostBasis;
				pos.m_isExchangeDelayed = 	getUnifiedListWorkAround().get(i).isExchangeDelayed;
				pos.m_underlying = 			getUnifiedListWorkAround().get(i).underlying;
				pos.m_spc = 				getUnifiedListWorkAround().get(i).spc;
				pos.m_bid = 				getUnifiedListWorkAround().get(i).bid;
				pos.m_securityKey = 		getUnifiedListWorkAround().get(i).securityKey;
				pos.m_qty = 				getUnifiedListWorkAround().get(i).qty;
				pos.m_dailyChange = 		getUnifiedListWorkAround().get(i).dailyChange;
				pos.m_multiplier = 			getUnifiedListWorkAround().get(i).multiplier;
				pos.m_gain = 				getUnifiedListWorkAround().get(i).gain;
				pos.m_mktVal = 				getUnifiedListWorkAround().get(i).mktVal;
				pos.m_posValChange = 		getUnifiedListWorkAround().get(i).posValChange;
				pos.m_price = 				getUnifiedListWorkAround().get(i).price;
				pos.m_strikeString = 		getUnifiedListWorkAround().get(i).strikeString;
				pos.m_canExercise = 		getUnifiedListWorkAround().get(i).canExercise;
				pos.m_costBasis = 			getUnifiedListWorkAround().get(i).costBasis;
				pos.m_positionNewToday = 	getUnifiedListWorkAround().get(i).positionNewToday;
				pos.m_ask = 				getUnifiedListWorkAround().get(i).ask;

				positions.add(pos);
			}

			return positions;
		}
		else
		{
			List<OhPosition> positions = new ArrayList<OhPosition>();

			for (int i = 0; i < getNumPositions(); ++i)
			{
				OhPosition pos = new OhPosition();

				pos.m_accountId = 			getUnifiedList().get(i).accountId;
				pos.m_shareCostBasis = 		getUnifiedList().get(i).shareCostBasis;
				pos.m_isCustomCostBasis = 	getUnifiedList().get(i).isCustomCostBasis;
				pos.m_expString = 			getUnifiedList().get(i).expString;
				pos.m_stock = 				getUnifiedList().get(i).stock;
				pos.m_description = 		getUnifiedList().get(i).description;
				pos.m_defaultCostBasis = 	getUnifiedList().get(i).defaultCostBasis;
				pos.m_isExchangeDelayed = 	getUnifiedList().get(i).isExchangeDelayed;
				pos.m_underlying = 			getUnifiedList().get(i).underlying;
				pos.m_spc = 				getUnifiedList().get(i).spc;
				pos.m_bid = 				getUnifiedList().get(i).bid;
				pos.m_securityKey = 		getUnifiedList().get(i).securityKey;
				pos.m_qty = 				getUnifiedList().get(i).qty;
				pos.m_dailyChange = 		getUnifiedList().get(i).dailyChange;
				pos.m_multiplier = 			getUnifiedList().get(i).multiplier;
				pos.m_gain = 				getUnifiedList().get(i).gain;
				pos.m_mktVal = 				getUnifiedList().get(i).mktVal;
				pos.m_posValChange = 		getUnifiedList().get(i).posValChange;
				pos.m_price = 				getUnifiedList().get(i).price;
				pos.m_strikeString = 		getUnifiedList().get(i).strikeString;
				pos.m_canExercise = 		getUnifiedList().get(i).canExercise;
				pos.m_costBasis = 			getUnifiedList().get(i).costBasis;
				pos.m_positionNewToday = 	getUnifiedList().get(i).positionNewToday;
				pos.m_ask = 				getUnifiedList().get(i).ask;

				positions.add(pos);
			}

			return positions;
		}
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
	 * @return the data object
	 */
	private OhMsgAccountPositionsRspWorkAround.EZMessage_.data_ getDataWorkAround()
	{
		if (null != m_responseWorkAround && null != m_responseWorkAround.EZMessage
				&& null != m_responseWorkAround.EZMessage.data)
		{
			return m_responseWorkAround.EZMessage.data;
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
	
	/**
	 * Internal helper method to get data from the response message
	 * 
	 * @note This method works around a bug in the OptionsHouse API whereby the "AccountPositions"
	 * request will not send a list of the "unified" type, and will instead only send one "unified"
	 * typed object if there is exactly one position. 
	 * 
	 * @return the unified list
	 */
	private List<OhMsgAccountPositionsRspWorkAround.EZMessage_.data_.unified_> getUnifiedListWorkAround()
	{
		if (null != getDataWorkAround() && null != getDataWorkAround().unified)
		{
			// Create a temporary list of "unified" type
			
			List<OhMsgAccountPositionsRspWorkAround.EZMessage_.data_.unified_> tmpList =
					new ArrayList<OhMsgAccountPositionsRspWorkAround.EZMessage_.data_.unified_>();
			
			// Add the single "unified" position to the list
			tmpList.add(getDataWorkAround().unified);
			
			return tmpList;
		}

		return null;
	}

}

/**
 * Internal data structure to represent JSON communication with the OptionsHouse
 * API. Specifies the request for account positions data.
 * 
 * @author Ryan Antkowiak 
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
 * @author Ryan Antkowiak 
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
				public List<String> sortHint;
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

/**
 * Internal data structure to represent JSON communication with the OptionsHouse
 * API. Specifies the response for account positions data.
 * 
 * @note This class works around a bug in the OptionsHouse API whereby the "AccountPositions"
 * request will not send a list of the "unified" type, and will instead only send one "unified"
 * typed object if there is exactly one position. 
 * 
 * @author Ryan Antkowiak 
 */
class OhMsgAccountPositionsRspWorkAround extends IOhMsgRsp
{
	@Override
	public EZMessageBaseRsp getEZ()
	{
		return EZMessage;
	}

	public static OhMsgAccountPositionsRspWorkAround build(String str)
	{
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(ErrorMap.class,
				new ErrorMapDeserializer());
		Gson gson = gsonBuilder.create();

		OhMsgAccountPositionsRspWorkAround rsp = (gson.fromJson(str,
				OhMsgAccountPositionsRspWorkAround.class));
			
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
				public List<String> sortHint;
				public double mktVal;
				public double posValChange;
				public double price;
				public String strikeString;
				public boolean canExercise;
				public double costBasis;
				public boolean positionNewToday;
				public double ask;
			}
			
			public unified_ unified;
		}
	}

}

