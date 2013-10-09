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
 * This class manages the cancellation of an OptionsHouse order
 * 
 * @author Ryan Antkowiak (antkowiak@gmail.com)
 */
public class OhCancelOrder extends IOh
{
	/** authorization token for the session with OptionsHouse API */
	private String m_authToken;

	/** the account id for which the account activity will be requested */
	private String m_accountId;

	/** the order id for the order to be cancelled */
	private String m_orderId;

	/** contains the request JSON message for canceling an order */
	private OhMsgCancelOrderReq m_request;

	/** contains the response JSON message for canceling an order */
	private OhMsgCancelOrderRsp m_response;

	/**
	 * Constructor sets up the input values for canceling an order
	 * 
	 * @param authToken
	 *            the authorization token for the session with OptionsHouse
	 * @param accountId
	 *            the account id for which an order will be canceled
	 * @param orderId
	 *            the id of the order to be canceled
	 */
	public OhCancelOrder(String authToken, String accountId, String orderId)
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
	protected OhMsgCancelOrderReq getRequest()
	{
		return m_request;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ryanantkowiak.jOptionsHouseAPI.IOh#getResponse()
	 */
	@Override
	protected OhMsgCancelOrderRsp getResponse()
	{
		return m_response;
	}

	/**
	 * Sets up the input values for canceling an order. Also sends the request
	 * to the OptionsHouse server.
	 * 
	 * @param authToken
	 *            the authorization token for the session with OptionsHouse
	 * @param accountId
	 *            the account id for which an order will be canceled
	 * @param orderId
	 *            the id of the order to be canceled
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
		m_request = new OhMsgCancelOrderReq(m_authToken, m_accountId, m_orderId);
		m_httpRequest = new OptionsHouseHttpRequest(m_request.getJsonString(),
				m_request.getPage());
		m_httpRequest.sendRequest();
		m_response = OhMsgCancelOrderRsp.build(m_httpRequest.getResponse());

		super.execute();
	}

	/**
	 * Returns true if the order was successfully cancelled
	 * 
	 * @return true if the order was cancelled
	 */
	public boolean wasCanceled()
	{
		if (null != getData())
		{
			return getData().canceled;
		}

		return false;
	}

	/**
	 * Returns true if the order was successfully cancelled
	 * 
	 * @return true if the order was cancelled
	 */
	public boolean wasCancelled()
	{
		return wasCanceled();
	}

	/**
	 * Returns the ID of an order that was cancelled
	 * 
	 * @return the ID of the order that was cancelled
	 */
	public String getId()
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
	private OhMsgCancelOrderRsp.EZMessage_.data_ getData()
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
 * API. Specifies the request for canceling an order.
 * 
 * @author Ryan Antkowiak (antkowiak@gmail.com)
 */
class OhMsgCancelOrderReq extends IOhMsgReq
{
	public OhMsgCancelOrderReq(String authToken, String account, String order_id)
	{
		m_page = "j";

		EZCancelOrderReq ezReq = new EZCancelOrderReq();
		ezReq.EZMessage.action = "order.cancel.json";
		ezReq.EZMessage.data.authToken = authToken;
		ezReq.EZMessage.data.account = account;
		ezReq.EZMessage.data.order_id = order_id;

		m_json = (new GsonBuilder()).create().toJson(ezReq, ezReq.getClass());
	}

	public class EZCancelOrderReq
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
				public String order_id;
			}
		}
	}
}

/**
 * Internal data structure to represent JSON communication with the OptionsHouse
 * API. Specifies the response for canceling an order.
 * 
 * @author Ryan Antkowiak (antkowiak@gmail.com)
 */
class OhMsgCancelOrderRsp extends IOhMsgRsp
{
	@Override
	public EZMessageBaseRsp getEZ()
	{
		return EZMessage;
	}

	public static OhMsgCancelOrderRsp build(String str)
	{
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(ErrorMap.class,
				new ErrorMapDeserializer());
		Gson gson = gsonBuilder.create();

		OhMsgCancelOrderRsp rsp = (gson
				.fromJson(str, OhMsgCancelOrderRsp.class));
		rsp.m_raw = str;
		return rsp;
	}

	public EZMessage_ EZMessage;

	public class EZMessage_ extends EZMessageBaseRsp
	{
		public data_ data;

		public class data_
		{
			public boolean canceled;
			public String id;
		}
	}
}
