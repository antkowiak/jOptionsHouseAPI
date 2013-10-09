/*
 * Copyright (c) 2013 Ryan Antkowiak (antkowiak@gmail.com).
 * All rights reserved.
 * It is provided AS-IS, WITHOUT ANY WARRANTY either expressed or implied.
 * You may study, use, and modify it for any non-commercial purpose.
 * You may distribute it non-commercially as long as you retain this notice.
 * For a commercial use license, contact antkowiak@gmail.com.
 */
package com.ryanantkowiak.jOptionsHouseAPI;

import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ryanantkowiak.jOptionsHouseAPI.OhMsgOrderDetailsRsp.EZMessage_.data_.order_details_.leg;

/**
 * This class will retrieve the details about one order at OptionsHouse. Order
 * details are grouped into "Legs", with each leg containing data such as the
 * security key, the quantity, fill quantity, etc. One order can have multiple
 * legs.
 * 
 * For example, a buy-write covered call order has two legs One leg is holding
 * the shares of the underlying stock. The other leg is the short position on
 * the call option that was written/sold.
 * 
 * @author Ryan Antkowiak (antkowiak@gmail.com)
 */
public class OhOrderDetails extends IOh
{
	/** authorization token for the session with OptionsHouse API */
	private String m_authToken;

	/** the account id under which the order details will be requested */
	private String m_accountId;

	/** the order id for which details will be requested */
	private String m_orderId;

	/** contains the request JSON message for order details */
	private OhMsgOrderDetailsReq m_request;

	/** contains the response JSON message for order details */
	private OhMsgOrderDetailsRsp m_response;

	/**
	 * Constructor sets up the input values for retrieving the order details
	 * 
	 * @param authToken
	 *            the authorization token for the session with OptionsHouse
	 * @param accountId
	 *            the account id under which order details will be requested
	 * @param orderId
	 *            the order id for which order details will be requested
	 */
	public OhOrderDetails(String authToken, String accountId, String orderId)
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
	protected OhMsgOrderDetailsReq getRequest()
	{
		return m_request;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ryanantkowiak.jOptionsHouseAPI.IOh#getResponse()
	 */
	@Override
	protected OhMsgOrderDetailsRsp getResponse()
	{
		return m_response;
	}

	/**
	 * Sets up the input values for retrieving the order details. Also sends the
	 * request to the OptionsHouse server.
	 * 
	 * @param authToken
	 *            the authorization token for the session with OptionsHouse
	 * @param accountId
	 *            the account id under which order details will be requested
	 * @param orderId
	 *            the order id for which order details will be requested
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
		m_request = new OhMsgOrderDetailsReq(m_authToken, m_accountId,
				m_orderId);
		m_httpRequest = new OptionsHouseHttpRequest(m_request.getJsonString(),
				m_request.getPage());
		m_httpRequest.sendRequest();
		m_response = OhMsgOrderDetailsRsp.build(m_httpRequest.getResponse());

		super.execute();
	}

	/**
	 * Returns the number of legs associated with the order
	 * 
	 * @return the number of legs associated with the order
	 */
	public int getNumLegs()
	{
		if (null != getLegList())
		{
			return getLegList().size();
		}

		return 0;
	}

	/**
	 * Returns the status of the order. Ex: "Filled" or "Open" or "Canceled"
	 * 
	 * @return the status of the order
	 */
	public String getStatus()
	{
		if (null != getOrderDetails() && null != getOrderDetails().status)
		{
			return getOrderDetails().status;
		}

		return "";
	}

	/**
	 * Returns true if the order was fully filled
	 * 
	 * @return true if the order was fully filled
	 */
	public boolean isFullyFilled()
	{
		return getStatus().equals("Filled");
	}

	/**
	 * Returns the total quantity filled
	 * 
	 * @return the total quantity filled
	 */
	public long getTotalFillQuantity()
	{
		long total = 0;

		for (int i = 0; i < getNumLegs(); ++i)
		{
			if (null != getLeg(i))
			{
				total += getLeg(i).quantity_filled;
			}
		}

		return total;
	}

	/**
	 * Returns the security key of the leg at the provided index. Returns an
	 * empty string if an invalid index is provided.
	 * 
	 * @param index
	 *            the index of the leg for which the security key will be
	 *            returned
	 * @return the security key
	 */
	public String getLegKey(int index)
	{
		leg l = getLeg(index);

		if (null != l)
		{
			return l.key;
		}

		return "";
	}

	/**
	 * Returns the index of the leg that corresponds to the provided security
	 * key. Returns -1 if there is no corresponding leg.
	 * 
	 * @param symbol
	 *            the provided symbol or security key
	 * @return the index of the corresponding leg
	 */
	public int getLegIndex(String symbol)
	{
		String key = OptionsHouseUtilities.createKey(symbol);

		List<leg> legs = getLegList();
		if (null != legs)
		{
			for (int i = 0; i < legs.size(); ++i)
			{
				if (OptionsHouseUtilities.areKeysEqual(key, legs.get(i).key))
				{
					return i;
				}
			}
		}

		return -1;
	}

	/**
	 * Returns the leg multiplier of the leg at the given index
	 * 
	 * @param index
	 *            the index of the leg
	 * @return the leg multiplier
	 */
	public long getLegMultiplier(int index)
	{
		leg l = getLeg(index);
		if (l != null)
			return l.multiplier;
		return 0;
	}

	/**
	 * Returns the leg multiplier of the leg with the given security key symbol
	 * 
	 * @param symbol
	 *            security key symbol of the leg
	 * @return the leg multiplier
	 */
	public long getLegMultiplier(String symbol)
	{
		leg l = getLeg(symbol);
		if (l != null)
			return l.multiplier;
		return 0;

	}

	/**
	 * Returns the leg ratio quantity of the leg at the given index
	 * 
	 * @param index
	 *            the index of the leg
	 * @return the leg ratio quantity
	 */
	public long getLegRatioQuantity(int index)
	{
		leg l = getLeg(index);
		if (l != null)
			return l.ratio_quantity;
		return 0;
	}

	/**
	 * Returns the leg ratio quantity of the leg with the given security key
	 * symbol
	 * 
	 * @param symbol
	 *            security key symbol of the leg
	 * @return the leg ratio quantity
	 */
	public long getLegRatioQuantity(String symbol)
	{
		leg l = getLeg(symbol);
		if (l != null)
			return l.ratio_quantity;
		return 0;
	}

	/**
	 * Returns the leg position type of the leg at the given index
	 * 
	 * @param index
	 *            the index of the leg
	 * @return the leg position type
	 */
	public String getLegPositionType(int index)
	{
		leg l = getLeg(index);
		if (l != null)
			return l.position_type;
		return "";
	}

	/**
	 * Returns the leg position type of the leg with the given security key
	 * symbol
	 * 
	 * @param symbol
	 *            security key symbol of the leg
	 * @return the leg position type
	 */
	public String getLegPositionType(String symbol)
	{
		leg l = getLeg(symbol);
		if (l != null)
			return l.position_type;
		return "";
	}

	/**
	 * Returns the leg quantity of the leg at the given index
	 * 
	 * @param index
	 *            the index of the leg
	 * @return the leg quantity
	 */
	public long getLegQuantity(int index)
	{
		leg l = getLeg(index);

		if (l != null)
		{
			return l.quantity;
		}

		return 0;
	}

	/**
	 * Returns the leg quantity of the leg with the given security key symbol
	 * 
	 * @param symbol
	 *            security key symbol of the leg
	 * @return the leg quantity
	 */
	public long getLegQuantity(String symbol)
	{
		leg l = getLeg(symbol);

		if (l != null)
		{
			return l.quantity;
		}

		return 0;
	}

	/**
	 * Returns the leg quantity filled of the leg at the given index
	 * 
	 * @param index
	 *            the index of the leg
	 * @return the leg quantity filled
	 */
	public long getLegQuantityFilled(int index)
	{
		leg l = getLeg(index);

		if (l != null)
		{
			return l.quantity_filled;
		}

		return 0;
	}

	/**
	 * Returns the leg quantity filled of the leg with the given security key
	 * symbol
	 * 
	 * @param symbol
	 *            security key symbol of the leg
	 * @return the leg quantity filled
	 */
	public long getLegQuantityFilled(String symbol)
	{
		leg l = getLeg(symbol);

		if (l != null)
		{
			return l.quantity_filled;
		}

		return 0;
	}

	/**
	 * Returns the leg transaction of the leg at the given index
	 * 
	 * @param index
	 *            the index of the leg
	 * @return the leg transaction
	 */
	public String getLegTransaction(int index)
	{
		leg l = getLeg(index);

		if (l != null)
		{
			return l.transaction;
		}

		return "";
	}

	/**
	 * Returns the leg transaction of the leg with the given security key symbol
	 * 
	 * @param symbol
	 *            security key symbol of the leg
	 * @return the leg transaction
	 */
	public String getLegTransaction(String symbol)
	{
		leg l = getLeg(symbol);

		if (l != null)
		{
			return l.transaction;
		}

		return "";
	}

	/**
	 * Returns the last updated timestamp of the leg at the given index
	 * 
	 * @param index
	 *            the index of the leg
	 * @return the last updated timestamp
	 */
	public String getLegLastUpdated(int index)
	{
		leg l = getLeg(index);

		if (l != null)
		{
			return l.last_updated;
		}

		return "";
	}

	/**
	 * Returns the last updated timestamp of the leg with the given security key
	 * symbol
	 * 
	 * @param symbol
	 *            security key symbol of the leg
	 * @return the last updated timestamp
	 */
	public String getLegLastUpdated(String symbol)
	{
		leg l = getLeg(symbol);

		if (l != null)
		{
			return l.last_updated;
		}

		return "";
	}

	/**
	 * Internal helper method to get data from the response message
	 * 
	 * @return the data object
	 */
	private OhMsgOrderDetailsRsp.EZMessage_.data_ getData()
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
	 * @return the order details object
	 */
	private OhMsgOrderDetailsRsp.EZMessage_.data_.order_details_ getOrderDetails()
	{
		if (null != getData() && null != getData().order_details)
		{
			return m_response.EZMessage.data.order_details;
		}

		return null;
	}

	/**
	 * Returns a list of all of the legs in the order
	 * 
	 * @return list of all the legs
	 */
	private List<leg> getLegList()
	{
		if (null != getOrderDetails())
		{
			return getOrderDetails().legs;
		}

		return null;
	}

	/**
	 * Returns the 'leg' data type at the given index
	 * 
	 * @param index
	 *            the index of the leg
	 * @return the internal leg data type corresponding to the given index
	 */
	private leg getLeg(int index)
	{
		if (getLegList() == null || index > getNumLegs() || index < 0)
		{
			return null;
		}

		return getLegList().get(index);
	}

	/**
	 * Returns the 'leg' data type with the given security key symbol
	 * 
	 * @param symbol
	 *            the security key symbol of the leg
	 * @return the internal leg data type corresponding to the given security
	 *         key symbol
	 */
	private leg getLeg(String symbol)
	{
		List<leg> legs = getLegList();

		if (legs == null)
		{
			return null;
		}

		String key = OptionsHouseUtilities.createKey(symbol);

		for (int i = 0; i < legs.size(); ++i)
		{
			if (OptionsHouseUtilities.areKeysEqual(key, legs.get(i).key))
			{
				return legs.get(i);
			}
		}

		return null;
	}

}

/**
 * Internal data structure to represent JSON communication with the OptionsHouse
 * API. Specifies the request for the details of an order.
 * 
 * @author Ryan Antkowiak (antkowiak@gmail.com)
 */
class OhMsgOrderDetailsReq extends IOhMsgReq
{
	public OhMsgOrderDetailsReq(String authToken, String account_id,
			String order_id)
	{
		// NOTE - The Rev 9 Spec says it uses "m", but it really needs to be
		// "j" instead
		m_page = "j";

		EZOrderDetailsReq ezReq = new EZOrderDetailsReq();
		ezReq.EZMessage.action = "order.details";
		ezReq.EZMessage.data.authToken = authToken;
		ezReq.EZMessage.data.account_id = account_id;
		ezReq.EZMessage.data.order_details.master_order_view = "current";
		ezReq.EZMessage.data.order_details.master_order_id = order_id;

		m_json = (new GsonBuilder()).create().toJson(ezReq, ezReq.getClass());
	}

	public class EZOrderDetailsReq
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

				public order_details_ order_details = new order_details_();

				public class order_details_
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
 * API. Specifies the response for the details of an order.
 * 
 * @author Ryan Antkowiak (antkowiak@gmail.com)
 */
class OhMsgOrderDetailsRsp extends IOhMsgRsp
{
	@Override
	public EZMessageBaseRsp getEZ()
	{
		return EZMessage;
	}

	public static OhMsgOrderDetailsRsp build(String str)
	{
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(ErrorMap.class,
				new ErrorMapDeserializer());
		Gson gson = gsonBuilder.create();

		OhMsgOrderDetailsRsp rsp = (gson.fromJson(str,
				OhMsgOrderDetailsRsp.class));
		rsp.m_raw = str;
		return rsp;
	}

	public EZMessage_ EZMessage;

	public class EZMessage_ extends EZMessageBaseRsp
	{
		public data_ data;

		public class data_
		{
			public order_details_ order_details;

			public class order_details_
			{
				public String order_id;
				public String order_subtype;
				public String order_type;

				public transaction_time_ transaction_time;

				public class transaction_time_
				{
					public String raw;
					public String pretty;
				}

				public customer_ customer;

				public class customer_
				{
					public String account;

					public stock_clearing_firm_ stock_clearing_firm;

					public class stock_clearing_firm_
					{
						public String name;
						public String account;
						public String mpid;
					}

					public String org;

					public option_clearing_firm_ option_clearing_firm;

					public class option_clearing_firm_
					{
						public String name;
						public String account;
						public String mpid;
					}
				}

				public String order_creator_id;
				public String preferred_destination;
				public String fix_symbol;
				public long quantity;
				public String price_type;
				public double price;
				public String time_in_force;
				public boolean allOrNone;

				public List<leg> legs;

				public class leg
				{
					public String side;
					public long index;
					public String security_type;
					public String key;
					public long multiplier;
					public long ratio_quantity;
					public String position_type;
					public String leg_description;
					public long quantity;
					public long quantity_filled;
					public String transaction;
					public String last_updated;
				}

				public String order_title;
				public String status;
				public String date_created;
				public String date_modified;
				public long master_order_id;
				public String time_in_force_desc;

			}

			public String master_order_view;
			public String timestamp;
		}
	}

}
