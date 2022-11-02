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
 * This class will retrieve the status of all orders from an OptionsHouse
 * account
 * 
 * @author Ryan Antkowiak 
 */
public class OhAllOrderStatus extends IOh
{
	/**
	 * Defines the status of one order from an OptionsHouse account
	 * 
	 * @author Ryan Antkowiak 
	 */
	public class OhOrderStatusRecord
	{
		public long m_orderId;
		public String m_message;
		public String m_timeInForce;
		public long m_quantity;
		public String m_fillQuantity;
		public String m_transaction;
		public String m_shortDescription;
		public String m_longDescription;
		public String m_status;
		public long m_dateCreatedMs;
		public long m_lastUpdatedMs;
		public String m_dateCreated;
		public String m_lastUpdated;
		public long m_masterOrderId;
		public String m_orderType;
		public String m_priceType;
		public double m_price;
		public boolean m_triggerOrder;
		public boolean m_trailingStopOrder;
		public boolean m_complexOrder;
		public boolean m_modifiable;
		public long m_rootOrderId;
		public boolean m_isSpreadOrder;
		public boolean m_isMutualFundOrder;
		public String m_underlyingStockSymbol;
		public String m_timestamp;
		public boolean m_hasExpiredKeys;
		public String m_securityKeys;
	}

	/** authorization token for the session with OptionsHouse API */
	private String m_authToken;

	/** the account id for which the order status records will be requested */
	private String m_accountId;

	/** contains the request JSON message for the status of all orders */
	private OhMsgAllOrderStatusReq m_request;

	/** contains the response JSON message for the status of all orders */
	private OhMsgAllOrderStatusRsp m_response;

	/**
	 * Constructor sets up the input values for retrieving the status of all
	 * orders
	 * 
	 * @param authToken
	 *            the authorization token for the session with OptionsHouse
	 * @param accountId
	 *            the account id for which the status of all orders will be
	 *            requested
	 */
	public OhAllOrderStatus(String authToken, String accountId)
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
	protected OhMsgAllOrderStatusReq getRequest()
	{
		return m_request;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ryanantkowiak.jOptionsHouseAPI.IOh#getResponse()
	 */
	@Override
	protected OhMsgAllOrderStatusRsp getResponse()
	{
		return m_response;
	}

	/**
	 * Sets up the input values for retrieving all order statuses. Also sends
	 * the request to the OptionsHouse server.
	 * 
	 * @param authToken
	 *            the authorization token for the session with OptionsHouse
	 * @param accountId
	 *            the account id for which all order statuses will be requested
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
		m_request = new OhMsgAllOrderStatusReq(m_authToken, m_accountId);
		m_httpRequest = new OptionsHouseHttpRequest(m_request.getJsonString(),
				m_request.getPage());
		m_httpRequest.sendRequest();
		m_response = OhMsgAllOrderStatusRsp.build(m_httpRequest.getResponse());

		super.execute();
	}

	/**
	 * Return the timestamp of when the order statuses were requested
	 * 
	 * @return timestamp of when order statuses were requested
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
	 * Return the response type
	 * 
	 * @return the response type
	 */
	public String getResponseType()
	{
		if (null != getData() && null != getData().response_type)
		{
			return m_response.EZMessage.data.response_type;
		}

		return "";
	}

	/**
	 * Return the number of order status records retrieved
	 * 
	 * @return the number of order status records
	 */
	public int getNumOrderStatusRecords()
	{
		if (null != getRecordList())
		{
			return getRecordList().size();
		}

		return 0;
	}

	/**
	 * Return a list of all the order status records
	 * 
	 * @return list of all order status records
	 */
	public List<OhOrderStatusRecord> getOrderStatusRecords()
	{
		List<OhOrderStatusRecord> records = new ArrayList<OhOrderStatusRecord>();

		for (int i = 0; i < getNumOrderStatusRecords(); ++i)
		{
			OhOrderStatusRecord r = new OhOrderStatusRecord();

			r.m_orderId = getRecordList().get(i).order_id;
			r.m_message = getRecordList().get(i).message;
			r.m_timeInForce = getRecordList().get(i).time_in_force;
			r.m_quantity = getRecordList().get(i).quantity;
			r.m_fillQuantity = getRecordList().get(i).fill_quantity;
			r.m_transaction = getRecordList().get(i).transaction;
			r.m_shortDescription = getRecordList().get(i).short_description;
			r.m_longDescription = getRecordList().get(i).long_description;
			r.m_status = getRecordList().get(i).status;
			r.m_dateCreatedMs = getRecordList().get(i).date_created_ms;
			r.m_lastUpdatedMs = getRecordList().get(i).last_updated_ms;
			r.m_dateCreated = getRecordList().get(i).date_created;
			r.m_lastUpdated = getRecordList().get(i).last_updated;
			r.m_masterOrderId = getRecordList().get(i).master_order_id;
			r.m_orderType = getRecordList().get(i).order_type;
			r.m_priceType = getRecordList().get(i).price_type;
			r.m_price = getRecordList().get(i).price;
			r.m_triggerOrder = getRecordList().get(i).trigger_order;
			r.m_trailingStopOrder = getRecordList().get(i).trailing_stop_order;
			r.m_complexOrder = getRecordList().get(i).complex_order;
			r.m_modifiable = getRecordList().get(i).modifiable;
			r.m_rootOrderId = getRecordList().get(i).root_order_id;
			r.m_isSpreadOrder = getRecordList().get(i).is_spread_order;
			r.m_isMutualFundOrder = getRecordList().get(i).is_mutual_fund_order;
			r.m_underlyingStockSymbol = getRecordList().get(i).underlying_stock_symbol;
			r.m_timestamp = getRecordList().get(i).timestamp;
			r.m_hasExpiredKeys = getRecordList().get(i).has_expired_keys;
			r.m_securityKeys = getRecordList().get(i).security_keys;

			records.add(r);
		}

		return records;
	}

	/**
	 * Internal helper method to get data from the response message
	 * 
	 * @return the data object
	 */
	private OhMsgAllOrderStatusRsp.EZMessage_.data_ getData()
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
	 * @return the master account orders
	 */
	private OhMsgAllOrderStatusRsp.EZMessage_.data_.master_account_orders_ getMasterAccountOrders()
	{
		if (null != getData() && null != getData().master_account_orders)
		{
			return getData().master_account_orders;
		}

		return null;
	}

	/**
	 * Internal helper method to get data from the response message
	 * 
	 * @return the order records list
	 */
	private List<OhMsgAllOrderStatusRsp.EZMessage_.data_.master_account_orders_.record> getRecordList()
	{
		if (null != getMasterAccountOrders()
				&& null != getMasterAccountOrders().records)
		{
			return getMasterAccountOrders().records;
		}

		return null;

	}

}

/**
 * Internal data structure to represent JSON communication with the OptionsHouse
 * API. Specifies the request for the status of all orders.
 * 
 * @author Ryan Antkowiak 
 */
class OhMsgAllOrderStatusReq extends IOhMsgReq
{
	public OhMsgAllOrderStatusReq(String authToken, String account_id)
	{
		m_page = "j";

		EZAllOrderStatusReq ezReq = new EZAllOrderStatusReq();
		ezReq.EZMessage.action = "master.account.orders";
		ezReq.EZMessage.data.authToken = authToken;
		ezReq.EZMessage.data.account_id = account_id;
		ezReq.EZMessage.data.master_order.page = 0;
		ezReq.EZMessage.data.master_order.page_count = 1;
		ezReq.EZMessage.data.master_order.page_size = 50;
		ezReq.EZMessage.data.master_order.master_order_view = "current";

		m_json = (new GsonBuilder()).create().toJson(ezReq, ezReq.getClass());
	}

	public class EZAllOrderStatusReq
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

				public master_order_ master_order = new master_order_();

				public class master_order_
				{
					public long page;
					public long page_count;
					public long page_size;
					public String master_order_view;
				}
			}
		}
	}
}

/**
 * Internal data structure to represent JSON communication with the OptionsHouse
 * API. Specifies the response for the status of all orders.
 * 
 * @author Ryan Antkowiak 
 */
class OhMsgAllOrderStatusRsp extends IOhMsgRsp
{
	@Override
	public EZMessageBaseRsp getEZ()
	{
		return EZMessage;
	}

	public static OhMsgAllOrderStatusRsp build(String str)
	{
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(ErrorMap.class,
				new ErrorMapDeserializer());
		Gson gson = gsonBuilder.create();

		OhMsgAllOrderStatusRsp rsp = gson.fromJson(str,
				OhMsgAllOrderStatusRsp.class);
		rsp.m_raw = str;
		return rsp;
	}

	public EZMessage_ EZMessage;

	public class EZMessage_ extends EZMessageBaseRsp
	{
		public data_ data;

		public class data_
		{
			public String timestamp;
			public String response_type;

			public master_account_orders_ master_account_orders;

			public class master_account_orders_
			{
				public long page;
				public long page_size;
				public long total_records;
				public List<record> records;

				public class record
				{
					public long order_id;
					public String message;
					public String time_in_force;
					public long quantity;
					public String fill_quantity;
					public String transaction;
					public String short_description;
					public String long_description;
					public String status;
					public long date_created_ms;
					public long last_updated_ms;
					public String date_created;
					public String last_updated;
					public long master_order_id;
					public String order_type;
					public String price_type;
					public double price;
					public boolean trigger_order;
					public boolean trailing_stop_order;
					public boolean complex_order;
					public boolean modifiable;
					public long root_order_id;
					public boolean is_spread_order;
					public boolean is_mutual_fund_order;
					public String underlying_stock_symbol;
					public String timestamp;
					public boolean has_expired_keys;
					public String security_keys;
				}
			}
		}
	}

}
