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
 * This class will retrieve quote pricing data from OptionsHouse for a security
 * or list of securities.
 * 
 * @author Ryan Antkowiak (antkowiak@gmail.com)
 */
public class OhQuote extends IOh
{
	/** authorization token for the session with OptionsHouse API */
	private String m_authToken;

	/** list of stocks for which quote data will be retrieved */
	private List<String> m_stocks;

	/** list of options for which quote data will be retrieved */
	private List<String> m_options;

	/** contains the request JSON message for quotes */
	private OhMsgViewQuoteListReq m_request;

	/** contains the response JSON message for quotes */
	private OhMsgViewQuoteListRsp m_response;

	/**
	 * Constructor sets up the input values for retrieving quotes
	 * 
	 * @param authToken
	 *            the authorization token for the session with OptionsHouse
	 * @param symbol
	 *            the security symbol for which quotes will be retrieved
	 */
	public OhQuote(String authToken, String symbol)
	{
		m_authToken = authToken;
		m_stocks = new ArrayList<String>();
		m_options = new ArrayList<String>();

		String key = OptionsHouseUtilities.createKey(symbol);
		if (OptionsHouseUtilities.isKeyStock(key))
		{
			m_stocks.add(key);
		} else if (OptionsHouseUtilities.isKeyOption(key))
		{
			m_options.add(key);
		}
	}

	/**
	 * Constructor sets up the input values for retrieving quotes
	 * 
	 * @param authToken
	 *            the authorization token for the session with OptionsHouse
	 * @param symbols
	 *            a list of security symbols for which quotes will be retrieved
	 */
	public OhQuote(String authToken, List<String> symbols)
	{
		m_authToken = authToken;
		m_stocks = new ArrayList<String>();
		m_options = new ArrayList<String>();

		for (int i = 0; i < symbols.size(); ++i)
		{
			String key = OptionsHouseUtilities.createKey(symbols.get(i));
			if (OptionsHouseUtilities.isKeyStock(key))
			{
				m_stocks.add(key);
			} else if (OptionsHouseUtilities.isKeyOption(key))
			{
				m_options.add(key);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ryanantkowiak.jOptionsHouseAPI.IOh#getRequest()
	 */
	@Override
	protected OhMsgViewQuoteListReq getRequest()
	{
		return m_request;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ryanantkowiak.jOptionsHouseAPI.IOh#getResponse()
	 */
	@Override
	protected OhMsgViewQuoteListRsp getResponse()
	{
		return m_response;
	}

	/**
	 * Sets up the input values for retrieving quotes. Also sends the request to
	 * the OptionsHouse server.
	 * 
	 * @param authToken
	 *            the authorization token for the session with OptionsHouse
	 * @param symbol
	 *            the security symbol for which quotes will be retrieved
	 */
	public void execute(String authToken, String symbol)
	{
		m_authToken = authToken;
		m_stocks = new ArrayList<String>();
		m_options = new ArrayList<String>();

		String key = OptionsHouseUtilities.createKey(symbol);
		if (OptionsHouseUtilities.isKeyStock(key))
		{
			m_stocks.add(key);
		} else if (OptionsHouseUtilities.isKeyOption(key))
		{
			m_options.add(key);
		}
	}

	/**
	 * Sets up the input values for retrieving quotes. Also sends the request to
	 * the OptionsHouse server.
	 * 
	 * @param authToken
	 *            the authorization token for the session with OptionsHouse
	 * @param symbols
	 *            a list of security symbols for which quotes will be retrieved
	 */
	public void execute(String authToken, List<String> symbols)
	{
		m_authToken = authToken;
		m_stocks = new ArrayList<String>();
		m_options = new ArrayList<String>();

		for (int i = 0; i < symbols.size(); ++i)
		{
			String key = OptionsHouseUtilities.createKey(symbols.get(i));
			if (OptionsHouseUtilities.isKeyStock(key))
			{
				m_stocks.add(key);
			} else if (OptionsHouseUtilities.isKeyOption(key))
			{
				m_options.add(key);
			}
		}
		
		execute();
	}

	/**
	 * Send the request to the OptionsHouse API server and retrieve the
	 * response.
	 */
	@Override
	public void execute()
	{
		m_request = new OhMsgViewQuoteListReq(m_authToken, m_stocks, m_options);
		m_httpRequest = new OptionsHouseHttpRequest(m_request.getJsonString(),
				m_request.getPage());
		m_httpRequest.sendRequest();
		m_response = OhMsgViewQuoteListRsp.build(m_httpRequest.getResponse());

		super.execute();
	}

	/**
	 * Returns the current bid price for the provided security symbol
	 * 
	 * @param symbol
	 *            the provided security symbol
	 * @return the current bid
	 */
	public double getBid(String symbol)
	{
		OhMsgViewQuoteListRsp.EZMessage_.data_.quote_ quoteObj = findQuote(symbol);
		if (null != quoteObj)
			return quoteObj.bid;
		return 0.0;
	}

	/**
	 * Returns the current ask price for the provided security symbol
	 * 
	 * @param symbol
	 *            the provided security symbol
	 * @return the current ask
	 */
	public double getAsk(String symbol)
	{
		OhMsgViewQuoteListRsp.EZMessage_.data_.quote_ quoteObj = findQuote(symbol);
		if (null != quoteObj)
			return quoteObj.ask;
		return 0.0;
	}

	/**
	 * Returns the current bid size for the provided security symbol
	 * 
	 * @param symbol
	 *            the provided security symbol
	 * @return the current bid size
	 */
	public long getBidSize(String symbol)
	{
		OhMsgViewQuoteListRsp.EZMessage_.data_.quote_ quoteObj = findQuote(symbol);
		if (null != quoteObj)
			return quoteObj.bidSize;
		return 0;
	}

	/**
	 * Returns the current ask size for the provided security symbol
	 * 
	 * @param symbol
	 *            the provided security symbol
	 * @return the current ask size
	 */
	public long getAskSize(String symbol)
	{
		OhMsgViewQuoteListRsp.EZMessage_.data_.quote_ quoteObj = findQuote(symbol);
		if (null != quoteObj)
			return quoteObj.askSize;
		return 0;
	}

	/**
	 * Returns the current volume for the provided security symbol
	 * 
	 * @param symbol
	 *            the provided security symbol
	 * @return the current volume
	 */
	public long getVolume(String symbol)
	{
		OhMsgViewQuoteListRsp.EZMessage_.data_.quote_ quoteObj = findQuote(symbol);
		if (null != quoteObj)
			return quoteObj.volume;
		return 0;
	}

	/**
	 * Returns the price change for the provided security symbol
	 * 
	 * @param symbol
	 *            the provided security symbol
	 * @return the price change
	 */
	public double getChange(String symbol)
	{
		OhMsgViewQuoteListRsp.EZMessage_.data_.quote_ quoteObj = findQuote(symbol);
		if (null != quoteObj)
			return quoteObj.change;
		return 0;
	}

	/**
	 * Returns the price change percentage for the provided security symbol
	 * 
	 * @param symbol
	 *            the provided security symbol
	 * @return the price change percentage
	 */
	public double getChangePercent(String symbol)
	{
		OhMsgViewQuoteListRsp.EZMessage_.data_.quote_ quoteObj = findQuote(symbol);
		if (null != quoteObj)
			return quoteObj.changePercent;
		return 0;
	}

	/**
	 * Returns the current daily high price for the provided security symbol
	 * 
	 * @param symbol
	 *            the provided security symbol
	 * @return the current daily high price
	 */
	public double getHigh(String symbol)
	{
		OhMsgViewQuoteListRsp.EZMessage_.data_.quote_ quoteObj = findQuote(symbol);
		if (null != quoteObj)
			return quoteObj.high;
		return 0;
	}

	/**
	 * Returns the current daily low price for the provided security symbol
	 * 
	 * @param symbol
	 *            the provided security symbol
	 * @return the current daily low price
	 */
	public double getLow(String symbol)
	{
		OhMsgViewQuoteListRsp.EZMessage_.data_.quote_ quoteObj = findQuote(symbol);
		if (null != quoteObj)
			return quoteObj.low;
		return 0;
	}

	/**
	 * Returns the last sale price for the provided security symbol
	 * 
	 * @param symbol
	 *            the provided security symbol
	 * @return the last sale price
	 */
	public double getLast(String symbol)
	{
		OhMsgViewQuoteListRsp.EZMessage_.data_.quote_ quoteObj = findQuote(symbol);
		if (null != quoteObj)
			return quoteObj.last;
		return 0;
	}

	/**
	 * Returns the previous day closing price for the provided security symbol
	 * 
	 * @param symbol
	 *            the provided security symbol
	 * @return the previous day closing price
	 */
	public double getPrevClose(String symbol)
	{
		OhMsgViewQuoteListRsp.EZMessage_.data_.quote_ quoteObj = findQuote(symbol);
		if (null != quoteObj)
			return quoteObj.prevClose;
		return 0;
	}

	/**
	 * Returns the open price for the provided security symbol
	 * 
	 * @param symbol
	 *            the provided security symbol
	 * @return the open price
	 */
	public double getOpen(String symbol)
	{
		OhMsgViewQuoteListRsp.EZMessage_.data_.quote_ quoteObj = findQuote(symbol);
		if (null != quoteObj)
			return quoteObj.open;
		return 0;
	}

	/**
	 * Returns the exchange on which the provided security symbol is listed
	 * 
	 * @param symbol
	 *            the provided security symbol
	 * @return the exchange where the security is listed
	 */
	public String getExchange(String symbol)
	{
		OhMsgViewQuoteListRsp.EZMessage_.data_.quote_ quoteObj = findQuote(symbol);
		if (null != quoteObj && null != quoteObj.exchange)
			return quoteObj.exchange;
		return "";
	}

	/**
	 * Returns the option delta for the provided security symbol
	 * 
	 * @param symbol
	 *            the provided security symbol
	 * @return the option delta
	 */
	public long getDelta(String symbol)
	{
		OhMsgViewQuoteListRsp.EZMessage_.data_.quote_ quoteObj = findQuote(symbol);
		if (null != quoteObj)
			return quoteObj.delta;
		return 0;
	}

	/**
	 * Returns the option gamma for the provided security symbol
	 * 
	 * @param symbol
	 *            the provided security symbol
	 * @return the option gamma
	 */
	public double getGamma(String symbol)
	{
		OhMsgViewQuoteListRsp.EZMessage_.data_.quote_ quoteObj = findQuote(symbol);
		if (null != quoteObj)
			return quoteObj.gamma;
		return 0;
	}

	/**
	 * Returns the option theta for the provided security symbol
	 * 
	 * @param symbol
	 *            the provided security symbol
	 * @return the option theta
	 */
	public double getTheta(String symbol)
	{
		OhMsgViewQuoteListRsp.EZMessage_.data_.quote_ quoteObj = findQuote(symbol);
		if (null != quoteObj)
			return quoteObj.theta;
		return 0;
	}

	/**
	 * Returns the option vega for the provided security symbol
	 * 
	 * @param symbol
	 *            the provided security symbol
	 * @return the option vega
	 */
	public double getVega(String symbol)
	{
		OhMsgViewQuoteListRsp.EZMessage_.data_.quote_ quoteObj = findQuote(symbol);
		if (null != quoteObj)
			return quoteObj.vega;
		return 0;
	}

	/**
	 * Returns the implied volatility for the provided security symbol
	 * 
	 * @param symbol
	 *            the provided security symbol
	 * @return the implied volatility
	 */
	public double getImpliedVol(String symbol)
	{
		OhMsgViewQuoteListRsp.EZMessage_.data_.quote_ quoteObj = findQuote(symbol);
		if (null != quoteObj)
			return quoteObj.ivol;
		return 0;
	}

	/**
	 * Returns the open interest for the provided security symbol
	 * 
	 * @param symbol
	 *            the provided security symbol
	 * @return the open interest
	 */
	public long getOpenInterest(String symbol)
	{
		OhMsgViewQuoteListRsp.EZMessage_.data_.quote_ quoteObj = findQuote(symbol);
		if (null != quoteObj)
			return quoteObj.oi;
		return 0;
	}

	/**
	 * Internal helper method to get data from the response message
	 * 
	 * @return the data object
	 */
	private OhMsgViewQuoteListRsp.EZMessage_.data_ getData()
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
	 * @return the quote list
	 */
	private List<OhMsgViewQuoteListRsp.EZMessage_.data_.quote_> getQuoteList()
	{
		if (null != getData() && null != getData().quote)
		{
			return getData().quote;
		}

		return null;
	}

	/**
	 * Internal helper method to get data from the response message
	 * 
	 * @return the quote
	 */
	private OhMsgViewQuoteListRsp.EZMessage_.data_.quote_ findQuote(
			String symbol)
	{
		if (null != getQuoteList())
		{
			String key = OptionsHouseUtilities.createKey(symbol);

			for (int i = 0; i < getQuoteList().size(); ++i)
			{
				if (OptionsHouseUtilities.areKeysEqual(key,
						getQuoteList().get(i).key))
				{
					return getQuoteList().get(i);
				}
			}
		}

		return null;
	}

}

/**
 * Internal data structure to represent JSON communication with the OptionsHouse
 * API. Specifies the request for quote data.
 * 
 * @author Ryan Antkowiak (antkowiak@gmail.com)
 */
class OhMsgViewQuoteListReq extends IOhMsgReq
{
	public OhMsgViewQuoteListReq(String authToken, List<String> stocks,
			List<String> options)
	{
		m_page = "j";

		EZViewQuoteListReq ezReq = new EZViewQuoteListReq();
		ezReq.EZMessage.action = "view.quote.list";
		ezReq.EZMessage.data.authToken = authToken;
		ezReq.EZMessage.data.addCompanyName = false;

		for (int i = 0; i < stocks.size(); ++i)
		{
			String security = OptionsHouseUtilities.createKey(stocks.get(i));
			ezReq.EZMessage.data.key.add(security);
			ezReq.EZMessage.data.addStockDetails.add(security);
			ezReq.EZMessage.data.addExtended.add(security);
		}

		for (int i = 0; i < options.size(); ++i)
		{
			String security = OptionsHouseUtilities.createKey(options.get(i));
			ezReq.EZMessage.data.key.add(security);
			ezReq.EZMessage.data.addGreeks.add(security);
		}

		m_json = (new GsonBuilder()).create().toJson(ezReq, ezReq.getClass());

	}

	public class EZViewQuoteListReq
	{
		public EZMessage_ EZMessage = new EZMessage_();

		public class EZMessage_
		{
			public String action;

			public data_ data = new data_();

			public class data_
			{
				public String authToken;
				public List<String> key = new ArrayList<String>();
				public List<String> addExtended = new ArrayList<String>();
				public List<String> addStockDetails = new ArrayList<String>();
				public List<String> addGreeks = new ArrayList<String>();
				boolean addCompanyName;
			}
		}
	}
}

/**
 * Internal data structure to represent JSON communication with the OptionsHouse
 * API. Specifies the response for quote data.
 * 
 * @author Ryan Antkowiak (antkowiak@gmail.com)
 */
class OhMsgViewQuoteListRsp extends IOhMsgRsp
{
	@Override
	public EZMessageBaseRsp getEZ()
	{
		return EZMessage;
	}

	public static OhMsgViewQuoteListRsp build(String str)
	{
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(ErrorMap.class,
				new ErrorMapDeserializer());
		Gson gson = gsonBuilder.create();

		OhMsgViewQuoteListRsp rsp = gson.fromJson(str,
				OhMsgViewQuoteListRsp.class);
		rsp.m_raw = str;
		return rsp;
	}

	public EZMessage_ EZMessage;

	public class EZMessage_ extends EZMessageBaseRsp
	{
		public data_ data;

		public class data_
		{
			public String session;

			public List<quote_> quote;

			public class quote_
			{
				public double ask;
				public long askSize;
				public double bid;
				public long bidSize;
				public double change;
				public double changePercent;
				public double dailyChange;
				public long delta;
				public boolean divConfirm;
				public boolean earningsConfirm;
				public String exchange;
				public double extChangeAmount;
				public double extChangePercent;
				public String extChangeTime;
				public double extClose;
				public double extLast;
				public double gamma;
				public boolean hasDividends;
				public boolean hasEarnings;
				public double high;
				public boolean isExchangeDelayed;
				public double ivol;
				public String key;
				public double last;
				public double low;
				public double mark;
				public long oi;
				public double open;
				public long optVol;
				public double prevClose;
				public double stockLast;
				public String symbol;
				public double theta;
				public double vega;
				public long volume;
			}
		}
	}

}
