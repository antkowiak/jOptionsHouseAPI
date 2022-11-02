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
import com.ryanantkowiak.jOptionsHouseAPI.OhMsgOrderHistoryRsp.EZMessage_.data_.order_history_;

/**
 * This class will retrieve history events associated with one order from
 * OptionsHouse. An order can under-go multiple events. (Such as several
 * partial-fills.)
 * 
 * @author Ryan Antkowiak 
 */
public class OhOrderHistory extends IOh
{
	/**
	 * This class contains all the data associated with one history event of an
	 * order.
	 * 
	 * @author Ryan Antkowiak 
	 */
	public class OhOrderHistoryEvent
	{
		/** order event transaction */
		public String m_transaction;

		/** date of the activity associated with this event */
		public String m_activityDate;

		/** description of this event */
		public String m_description;

		/** quantity associated with this event */
		public long m_quantity;

		/** price associated with this event */
		public double m_price;

		/** underlying stock symbol associated with this event */
		public String m_underlyingStockSymbol;

		/** the event */
		public String m_event;
	}

	/** authorization token for the session with OptionsHouse API */
	private String m_authToken;

	/** the account id for which the order history will be requested */
	private String m_accountId;

	/** the order id of the order for which history will be requested */
	private String m_orderId;

	/** contains the request JSON message for order history */
	private OhMsgOrderHistoryReq m_request;

	/** contains the response JSON message for order history */
	private OhMsgOrderHistoryRsp m_response;

	/**
	 * Constructor sets up the input values for retrieving order history
	 * 
	 * @param authToken
	 *            the authorization token for the session with OptionsHouse
	 * @param accountId
	 *            the account id under which order history will be requested
	 * @param orderId
	 *            the id of the order for which history will be requested
	 */
	public OhOrderHistory(String authToken, String accountId, String orderId)
	{
		m_authToken = authToken;
		m_accountId = accountId;
		m_orderId = orderId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ryanantkowiak.jOptionsHouseAPI.IOh#getRequest()
	 */
	@Override
	protected OhMsgOrderHistoryReq getRequest()
	{
		return m_request;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ryanantkowiak.jOptionsHouseAPI.IOh#getResponse()
	 */
	@Override
	protected OhMsgOrderHistoryRsp getResponse()
	{
		return m_response;
	}

	/**
	 * Sets up the input values for retrieving order history. Also sends the
	 * request to the OptionsHouse server.
	 * 
	 * @param authToken
	 *            the authorization token for the session with OptionsHouse
	 * @param accountId
	 *            the account id under which order history will be requested
	 * @param orderId
	 *            the id of the order for which history will be requested
	 */
	public void execute(String authToken, String accountId, String orderId)
	{
		m_authToken = authToken;
		m_accountId = accountId;
		m_orderId = orderId;

		execute();
	}

	/**
	 * Send the request to the OptionsHouse API server and retrieve the
	 * response.
	 */
	@Override
	public void execute()
	{
		m_request = new OhMsgOrderHistoryReq(m_authToken, m_accountId,
				m_orderId);
		m_httpRequest = new OptionsHouseHttpRequest(m_request.getJsonString(),
				m_request.getPage());
		m_httpRequest.sendRequest();
		m_response = OhMsgOrderHistoryRsp.build(m_httpRequest.getResponse());

		super.execute();
	}

	/**
	 * Returns the timestamp that the order history was retrieved
	 * 
	 * @return the timestamp that the order history was retrieved
	 */
	public String getTimestamp()
	{
		if (null != getData() && null != getData().timestamp)
		{
			return getData().timestamp;
		}

		return "";
	}

	/**
	 * Returns the number of history events associated with the order
	 * 
	 * @return the number of history events
	 */
	public int getNumHistoryEvents()
	{
		if (null != getOrderHistoryList())
		{
			return getOrderHistoryList().size();
		}

		return 0;
	}

	/**
	 * Returns a list of all of the history events associated with the order
	 * 
	 * @return list of all history events
	 */
	public List<OhOrderHistoryEvent> getHistoryEvents()
	{
		List<OhOrderHistoryEvent> events = new ArrayList<OhOrderHistoryEvent>();

		for (int i = 0; i < getNumHistoryEvents(); ++i)
		{
			OhOrderHistoryEvent e = new OhOrderHistoryEvent();
			e.m_transaction = getOrderHistoryEvent(i).transaction;
			e.m_activityDate = getOrderHistoryEvent(i).activity_date;
			e.m_description = getOrderHistoryEvent(i).description;

			e.m_quantity = 0;
			try
			{
				e.m_quantity = Long.parseLong(getOrderHistoryEvent(i).quantity);
			} catch (Exception ex)
			{
			}

			e.m_price = 0;
			try
			{
				e.m_price = Double.parseDouble(getOrderHistoryEvent(i).price);
			} catch (Exception ex)
			{
			}

			e.m_underlyingStockSymbol = getOrderHistoryEvent(i).underlying_stock_symbol;
			e.m_event = getOrderHistoryEvent(i).event;
			events.add(e);
		}

		return events;
	}

	/**
	 * Returns the total quantity of all of the order history events.
	 * 
	 * @return the total quantity of all order history events
	 */
	public long getTotalQuantity()
	{
		long qty = 0;

		List<OhOrderHistoryEvent> events = getHistoryEvents();

		for (int i = 0; i < events.size(); ++i)
		{
			qty += events.get(i).m_quantity;
		}

		return qty;
	}

	/**
	 * Calculates and returns the average price, aggregated from all the order
	 * history events.
	 * 
	 * @return the average price of the order
	 */
	public double getAveragePrice()
	{
		double qty = 0;
		double total = 0;

		List<OhOrderHistoryEvent> events = getHistoryEvents();

		for (int i = 0; i < events.size(); ++i)
		{
			qty += events.get(i).m_quantity;
			total = total + (qty * events.get(i).m_price);
		}

		if (qty == 0)
		{
			return 0;
		}

		return (total / qty);
	}

	/**
	 * Internal helper method to get data from the response message
	 * 
	 * @return the data object
	 */
	private OhMsgOrderHistoryRsp.EZMessage_.data_ getData()
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
	 * @return the order history list
	 */
	private List<OhMsgOrderHistoryRsp.EZMessage_.data_.order_history_> getOrderHistoryList()
	{
		if (null != getData() && null != getData().order_history)
		{
			return getData().order_history;
		}

		return null;
	}

	/**
	 * Returns the order_history_ internal data structure representing a history
	 * event at a given index.
	 * 
	 * @param index
	 *            the index of the order history event
	 * @return the order_history_ internal data type
	 */
	private order_history_ getOrderHistoryEvent(int index)
	{
		if (null != getOrderHistoryList()
				&& index < getOrderHistoryList().size())
		{
			return getOrderHistoryList().get(index);
		}

		return null;
	}

}

/**
 * Internal data structure to represent JSON communication with the OptionsHouse
 * API. Specifies the request for the history of an order.
 * 
 * @author Ryan Antkowiak 
 */
class OhMsgOrderHistoryReq extends IOhMsgReq
{
	public OhMsgOrderHistoryReq(String authToken, String account_id,
			String order_id)
	{
		// NOTE - The Rev 9 Spec says it uses "m", but it really needs to be
		// "j" instead
		m_page = "j";

		EZOrderHistoryReq ezReq = new EZOrderHistoryReq();
		ezReq.EZMessage.action = "order.history";
		ezReq.EZMessage.data.authToken = authToken;
		ezReq.EZMessage.data.account_id = account_id;
		ezReq.EZMessage.data.order_history.master_order_view = "current";
		ezReq.EZMessage.data.order_history.master_order_id = order_id;

		m_json = (new GsonBuilder()).create().toJson(ezReq, ezReq.getClass());
	}

	public class EZOrderHistoryReq
	{
		public EZMessage_ EZMessage = new EZMessage_();

		public class EZMessage_
		{
			public String action;

			public data_ data = new data_();

			public class data_
			{
				public String authToken;
				public String account_id;

				public order_history_ order_history = new order_history_();

				public class order_history_
				{
					public String master_order_view;
					public String master_order_id;
				}
			}
		}
	}
}

/**
 * Internal data structure to represent JSON communication with the OptionsHouse
 * API. Specifies the response for the history of an order.
 * 
 * @author Ryan Antkowiak 
 */
class OhMsgOrderHistoryRsp extends IOhMsgRsp
{
	@Override
	public EZMessageBaseRsp getEZ()
	{
		return EZMessage;
	}

	public static OhMsgOrderHistoryRsp build(String str)
	{
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(ErrorMap.class,
				new ErrorMapDeserializer());
		Gson gson = gsonBuilder.create();

		OhMsgOrderHistoryRsp rsp = (gson.fromJson(str,
				OhMsgOrderHistoryRsp.class));
		rsp.m_raw = str;
		return rsp;
	}

	public EZMessage_ EZMessage;

	public class EZMessage_ extends EZMessageBaseRsp
	{
		public data_ data;

		public class data_
		{
			public String master_order_id;
			public String timestamp;

			public List<order_history_> order_history;

			public class order_history_
			{
				public String transaction;
				public String activity_date;
				public String description;
				public String quantity;
				public String price;
				public String underlying_stock_symbol;
				public String event;
			}
		}
	}
}
