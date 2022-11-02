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
import java.util.Date;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * This class will create a simple one-legged order on OptionsHouse
 * 
 * @author Ryan Antkowiak 
 */
public class OhSimpleOrder extends IOh
{
	/** authorization token for the session with OptionsHouse API */
	private String m_authToken;

	/** the account id under which the order creation will be requested */
	private String m_accountId;

	/** the security symbol (stock or option) of this order */
	private String m_symbol;

	/** the quantity of this order */
	private long m_quantity;

	/** the limit price of the order */
	private double m_limitPrice;

	/** the side of the order (buy or sell) */
	private Side m_side;

	/** the position type of the order (open or close) */
	private PositionType m_positionType;

	/** the time-in-force of this order (Day or GTC) */
	private TimeInForce m_tif;

	/** contains the request JSON message for placing the order */
	private OhMsgCreateSimpleOrderReq m_request;

	/** contains the response JSON message for placing the order */
	private OhMsgCreateSimpleOrderRsp m_response;

	/**
	 * Constructor sets up the input values for placing an order.
	 * 
	 * @param authToken
	 *            the authorization token for the session with OptionsHouse
	 * @param accountId
	 *            the account id under which the order will be placed
	 * @param symbol
	 *            the security symbol (stock or option) of this order
	 * @param quantity
	 *            the quantity of this order
	 * @param limitPrice
	 *            the limit price of this order
	 * @param side
	 *            the side of the order (buy or sell)
	 * @param positionType
	 *            the position type of the order (open or close)
	 * @param tif
	 *            the time-in-force of this order (Day or GTC or EXT)
	 */
	public OhSimpleOrder(String authToken, String accountId, String symbol,
			long quantity, double limitPrice, Side side,
			PositionType positionType, TimeInForce tif)
	{
		m_authToken = authToken;
		m_accountId = accountId;
		m_symbol = OptionsHouseUtilities.createKey(symbol);
		m_quantity = quantity;
		m_limitPrice = limitPrice;
		m_side = side;
		m_positionType = positionType;
		m_tif = tif;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ryanantkowiak.jOptionsHouseAPI.IOh#getRequest()
	 */
	@Override
	protected OhMsgCreateSimpleOrderReq getRequest()
	{
		return m_request;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ryanantkowiak.jOptionsHouseAPI.IOh#getResponse()
	 */
	@Override
	protected OhMsgCreateSimpleOrderRsp getResponse()
	{
		return m_response;
	}

	/**
	 * Sets up the input values for placing an order. Also sends the request to
	 * the OptionsHouse server.
	 * 
	 * @param authToken
	 *            the authorization token for the session with OptionsHouse
	 * @param accountId
	 *            the account id under which the order will be placed
	 * @param symbol
	 *            the security symbol (stock or option) of this order
	 * @param quantity
	 *            the quantity of this order
	 * @param limitPrice
	 *            the limit price of this order
	 * @param side
	 *            the side of the order (buy or sell)
	 * @param positionType
	 *            the position type of the order (open or close)
	 * @param tif
	 *            the time-in-force of this order (Day or GTC)
	 */
	public void execute(String authToken, String accountId, String symbol,
			long quantity, double limitPrice, Side side,
			PositionType positionType, TimeInForce tif)
	{
		m_authToken = authToken;
		m_accountId = accountId;
		m_symbol = OptionsHouseUtilities.createKey(symbol);
		m_quantity = quantity;
		m_limitPrice = limitPrice;
		m_side = side;
		m_positionType = positionType;
		m_tif = tif;

		execute();
	}

	/**
	 * Send the request to the OptionsHouse API server and retrieve the
	 * response.
	 */
	@Override
	public void execute()
	{
		m_request = new OhMsgCreateSimpleOrderReq(m_authToken, m_accountId,
				m_symbol, m_quantity, m_limitPrice, m_side, m_positionType,
				m_tif);
		m_httpRequest = new OptionsHouseHttpRequest(m_request.getJsonString(),
				m_request.getPage());
		m_httpRequest.sendRequest();
		m_response = OhMsgCreateSimpleOrderRsp.build(m_httpRequest
				.getResponse());

		super.execute();
	}

	/**
	 * Returns true if the order was successfully created.
	 * 
	 * @return true if the order was created
	 */
	public boolean wasCreated()
	{
		if (null != getData())
		{
			return getData().created;
		}

		return false;
	}

	/**
	 * Returns the id string of the newly created order.
	 * 
	 * @return the id string of the order
	 */
	public String getOrderId()
	{
		if (null != getData() && null != getData().id)
		{
			return getData().id;
		}

		return "";
	}

	/**
	 * Internal helper method to get data from the response message
	 * 
	 * @return the data object
	 */
	private OhMsgCreateSimpleOrderRsp.EZMessage_.data_ getData()
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
 * API. Specifies the request for creating a simple one-legged order.
 * 
 * @author Ryan Antkowiak 
 */
class OhMsgCreateSimpleOrderReq extends IOhMsgReq
{
	public OhMsgCreateSimpleOrderReq(String authToken, String account,
			String symbol, long quantity, double limitPrice, Side side,
			PositionType positionType, TimeInForce tif)
	{
		String key = OptionsHouseUtilities.createKey(symbol);
		String underlying = OptionsHouseUtilities.extractStockFromKey(key);
		String securityType = "";

		if (OptionsHouseUtilities.isKeyStock(key))
		{
			securityType = "stock";
		}

		if (OptionsHouseUtilities.isKeyOption(key))
		{
			securityType = "option";
		}

		String tifStr = "";
		switch (tif)
		{
			case Day:
			{
				tifStr = "day";
				break;
			}
			case GTC:
			{
				tifStr = "good_till_cancel";
				break;
			}
			case EXT:
			{
				tifStr = "ext_trading";
				break;
			}
		}

		String sideStr = "buy";
		if (side == Side.Sell)
		{
			sideStr = "sell";
		}

		String posTypeStr = "opening";
		if (positionType == PositionType.Close)
		{
			posTypeStr = "closing";
		}

		Date d = new Date();
		long timestamp = d.getTime();

		m_page = "j";

		EZCreateSimpleOrderReq ezReq = new EZCreateSimpleOrderReq();
		ezReq.EZMessage.action = "order.create.json";
		ezReq.EZMessage.data.authToken = authToken;
		ezReq.EZMessage.data.account = account;
		ezReq.EZMessage.data.order.order_type = "regular";
		ezReq.EZMessage.data.order.order_id = false;
		ezReq.EZMessage.data.order.m_order_id = 1;
		ezReq.EZMessage.data.order.order_subtype = "single";
		ezReq.EZMessage.data.order.price_type = "limit";
		ezReq.EZMessage.data.order.time_in_force = tifStr;
		ezReq.EZMessage.data.order.alias = "(A) Order 1";
		ezReq.EZMessage.data.order.price = "" + limitPrice;
		ezReq.EZMessage.data.order.underlying_stock_symbol = underlying;
		ezReq.EZMessage.data.order.allOrNone = false;
		ezReq.EZMessage.data.order.source = "API";
		ezReq.EZMessage.data.order.client_id = timestamp;
		ezReq.EZMessage.data.order.preferred_destination = "BEST";

		ezReq.EZMessage.data.order.addLeg(0, sideStr, securityType, quantity,
				key, 1, posTypeStr);

		m_json = (new GsonBuilder()).create().toJson(ezReq, ezReq.getClass());

	}

	public class EZCreateSimpleOrderReq
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

				public order_ order = new order_();

				public class order_
				{
					public String order_type;
					public boolean order_id;
					public long m_order_id;
					public String order_subtype;
					public String price_type;
					public String time_in_force;
					public String alias;
					public String price;
					public String underlying_stock_symbol;
					public boolean allOrNone;
					public String source;
					public long client_id;
					public String preferred_destination;

					public class leg_
					{
						public long index;
						public String side;
						public String security_type;
						public long quantity;
						public String key;
						public long multiplier;
						public String position_type;
					}

					public List<leg_> legs = new ArrayList<leg_>();

					public void addLeg(long index_, String side_,
							String security_type_, long quantity_, String key_,
							long multiplier_, String position_type_)
					{
						leg_ lg = new leg_();
						lg.index = index_;
						lg.side = side_;
						lg.security_type = security_type_;
						lg.quantity = quantity_;
						lg.key = key_;
						lg.multiplier = multiplier_;
						lg.position_type = position_type_;
						legs.add(lg);
					}
				}
			}
		}
	}
}

/**
 * Internal data structure to represent JSON communication with the OptionsHouse
 * API. Specifies the response for creating a simple one-legged order.
 * 
 * @author Ryan Antkowiak 
 */
class OhMsgCreateSimpleOrderRsp extends IOhMsgRsp
{
	@Override
	public EZMessageBaseRsp getEZ()
	{
		return EZMessage;
	}

	public static OhMsgCreateSimpleOrderRsp build(String str)
	{
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(ErrorMap.class,
				new ErrorMapDeserializer());
		Gson gson = gsonBuilder.create();

		OhMsgCreateSimpleOrderRsp rsp = (gson.fromJson(str,
				OhMsgCreateSimpleOrderRsp.class));
		rsp.m_raw = str;
		return rsp;
	}

	public EZMessage_ EZMessage;

	public class EZMessage_ extends EZMessageBaseRsp
	{
		public data_ data;

		public class data_
		{
			public boolean created;
			public String id;
		}
	}
}
