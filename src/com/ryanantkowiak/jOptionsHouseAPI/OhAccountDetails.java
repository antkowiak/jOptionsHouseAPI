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
 * This class will retrieve the Account Details from OptionsHouse. Account
 * details include things such as: margin equity, account value, buying power,
 * etc.
 * 
 * @author Ryan Antkowiak (antkowiak@gmail.com)
 */
public class OhAccountDetails extends IOh
{
	/** authorization token for the session with OptionsHouse API */
	private String m_authToken;

	/** the account id for which the account details will be requested */
	private String m_accountId;

	/** contains the request JSON message for account details */
	private OhMsgAccountCashReq m_request;

	/** contains the response JSON message for account details */
	private OhMsgAccountCashRsp m_response;

	/**
	 * Constructor sets up the input values for retrieving the account details
	 * 
	 * @param authToken
	 *            the authorization token for the session with OptionsHouse
	 * @param accountId
	 *            the account id for which account details will be requested
	 */
	public OhAccountDetails(String authToken, String accountId)
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
	protected OhMsgAccountCashReq getRequest()
	{
		return m_request;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ryanantkowiak.jOptionsHouseAPI.IOh#getResponse()
	 */
	@Override
	protected OhMsgAccountCashRsp getResponse()
	{
		return m_response;
	}

	/**
	 * Sets up the input values for retrieving the account details. Also sends
	 * the request to the OptionsHouse server.
	 * 
	 * @param authToken
	 *            the authorization token for the session with OptionsHouse
	 * @param accountId
	 *            the account id for which account details will be requested
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
		m_request = new OhMsgAccountCashReq(m_authToken, m_accountId);
		m_httpRequest = new OptionsHouseHttpRequest(m_request.getJsonString(),
				m_request.getPage());
		m_httpRequest.sendRequest();
		m_response = OhMsgAccountCashRsp.build(m_httpRequest.getResponse());

		super.execute();
	}

	/**
	 * Returns the margin equity for the account
	 * 
	 * @return the margin equity for the account
	 */
	public String getMarginEquity()
	{
		if (null != getData() && null != getData().marginEquity)
		{
			return getData().marginEquity;
		}

		return "";
	}

	/**
	 * Returns the account value change year-to-date
	 * 
	 * @return the account value change YTD
	 */
	public String getAccountValueYearToDate()
	{
		if (null != getData() && null != getData().accountValueYearToDate)
		{
			return getData().accountValueYearToDate;
		}

		return "";
	}

	/**
	 * Returns the cash available to be withdrawn
	 * 
	 * @return the cash available to be withdrawn
	 */
	public String getAvailableToWithdraw()
	{
		if (null != getData() && null != getData().availableToWithdraw)
		{
			return getData().availableToWithdraw;
		}

		return "";
	}

	/**
	 * Returns the account value change month-to-date
	 * 
	 * @return the account value change MTD
	 */
	public String getAccountValueMonthToDate()
	{
		if (null != getData() && null != getData().accountValueMonthToDate)
		{
			return getData().accountValueMonthToDate;
		}

		return "";
	}

	/**
	 * Returns the cost of all pending orders
	 * 
	 * @return the cost of all pending orders
	 */
	public String getPendingOrders()
	{
		if (null != getData() && null != getData().pendingOrders)
		{
			return getData().pendingOrders;
		}

		return "";
	}

	/**
	 * Returns the account value change for the current day
	 * 
	 * @return the account value change for the current day
	 */
	public String getAccountValueDailyChange()
	{
		if (null != getData() && null != getData().accountValueDailyChange)
		{
			return getData().accountValueDailyChange;
		}

		return "";
	}

	/**
	 * Returns the day trading buying power
	 * 
	 * @return the day trading BP
	 */
	public String getDayTradingBuyPower()
	{
		if (null != getData() && null != getData().dayTradingBuyPower)
		{
			return getData().dayTradingBuyPower;
		}

		return "";
	}

	/**
	 * Returns the total account value
	 * 
	 * @return the total account value
	 */
	public String getAccountValue()
	{
		if (null != getData() && null != getData().accountValue)
		{
			return getData().accountValue;
		}

		return "";
	}

	/**
	 * Returns the option buying power
	 * 
	 * @return the option BP
	 */
	public String getOptionBuyingPower()
	{
		if (null != getData() && null != getData().optionBuyingPower)
		{
			return getData().optionBuyingPower;
		}

		return "";
	}

	/**
	 * Returns the stock buying power
	 * 
	 * @return the stock BP
	 */
	public String getStockBuyingPower()
	{
		if (null != getData() && null != getData().stockBuyingPower)
		{
			return getData().stockBuyingPower;
		}

		return "";
	}

	/**
	 * Returns the cash balance
	 * 
	 * @return the cash balance
	 */
	public String getCashBalance()
	{
		if (null != getData() && null != getData().cashBalance)
		{
			return getData().cashBalance;
		}

		return "";
	}

	/**
	 * Returns the available amount to trade
	 * 
	 * @return the available amount to trade
	 */
	public String getAvailableToTrade()
	{
		if (null != getData() && null != getData().availableToTrade)
		{
			return getData().availableToTrade;
		}

		return "";
	}

	/**
	 * Returns the total value of the portfolio
	 * 
	 * @return the total value of the portfolio
	 */
	public String getPortfolioValue()
	{
		if (null != getData() && null != getData().portfolioValue)
		{
			return getData().portfolioValue;
		}

		return "";
	}

	/**
	 * Internal helper method to get data from the response message
	 * 
	 * @return the data object
	 */
	private OhMsgAccountCashRsp.EZMessage_.data_ getData()
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
 * API. Specifies the request for account details data.
 * 
 * @author Ryan Antkowiak (antkowiak@gmail.com)
 */
class OhMsgAccountCashReq extends IOhMsgReq
{
	public OhMsgAccountCashReq(String authToken, String account)
	{
		m_page = "m";

		EZAccountCashReq ezReq = new EZAccountCashReq();
		ezReq.EZMessage.action = "account.cash";
		ezReq.EZMessage.data.authToken = authToken;
		ezReq.EZMessage.data.account = account;
		ezReq.EZMessage.data.portfolio = true;
		ezReq.EZMessage.data.historical = true;
		ezReq.EZMessage.data.fastValues = false;

		m_json = (new GsonBuilder()).create().toJson(ezReq, ezReq.getClass());
	}

	public class EZAccountCashReq
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
				boolean portfolio;
				boolean historical;
				boolean fastValues;
			}
		}
	}
}

/**
 * Internal data structure to represent JSON communication with the OptionsHouse
 * API. Specifies the response for account details data.
 * 
 * @author Ryan Antkowiak (antkowiak@gmail.com)
 */
class OhMsgAccountCashRsp extends IOhMsgRsp
{
	@Override
	public EZMessageBaseRsp getEZ()
	{
		return EZMessage;
	}

	public static OhMsgAccountCashRsp build(String str)
	{
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(ErrorMap.class,
				new ErrorMapDeserializer());
		Gson gson = gsonBuilder.create();

		OhMsgAccountCashRsp rsp = gson.fromJson(str, OhMsgAccountCashRsp.class);
		rsp.m_raw = str;
		return rsp;
	}

	public EZMessage_ EZMessage;

	public class EZMessage_ extends EZMessageBaseRsp
	{
		public data_ data;

		public class data_
		{
			public String marginEquity;
			public String accountValueYearToDate;
			public String availableToWithdraw;
			public String accountValueMonthToDate;
			public String pendingOrders;
			public String accountValueDailyChange;
			public String dayTradingBuyPower;
			public String accountValue;
			public String optionBuyingPower;
			public String stockBuyingPower;
			public String cashBalance;
			public String availableToTrade;
			public String portfolioValue;
		}
	}
}
