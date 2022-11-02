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
 * This class will retrieve the list of option security symbols for a provided
 * underlying stock symbol, from OptionsHouse.
 * 
 * Depending on the provided parameters to this class, quarterly options and/or
 * weekly options may be included in the results.
 * 
 * @author Ryan Antkowiak 
 */
public class OhViewSeries extends IOh
{
	/**
	 * This class contains an expiration date, and the list of options contracts
	 * that all expire on that date.
	 * 
	 * @author Ryan Antkowiak 
	 */
	public class OhSeriesExpiration
	{
		/** expiration date of the options contracts */
		public String m_expirationDate;

		/** list of option contract security symbol keys */
		public List<String> m_contracts;
	}

	/** authorization token for the session with OptionsHouse API */
	private String m_authToken;

	/** the underlying stock symbol for which series data will be requested */
	private String m_symbol;

	/** flag to indicate if quarterly option data will be retrieved */
	private boolean m_showQuarterlies;

	/** flag to indicate if weekly option data will be retrieved */
	private boolean m_showWeeklies;

	/** contains the request JSON message for option series data */
	private OhMsgViewSeriesReq m_request;

	/** contains the response JSON message for option series data */
	private OhMsgViewSeriesRsp m_response;

	/**
	 * Constructor sets up the input values for retrieving the option series
	 * data. The default behavior will show quarterly option data, but will omit
	 * weekly option data.
	 * 
	 * @param authToken
	 *            the authorization token for the session with OptionsHouse
	 * @param symbol
	 *            the underlying stock symbol upon which the options are based
	 */
	public OhViewSeries(String authToken, String symbol)
	{
		m_authToken = authToken;
		m_symbol = OptionsHouseUtilities
				.extractStockFromKey(OptionsHouseUtilities.createKey(symbol));
		m_showQuarterlies = true;
		m_showWeeklies = false;
	}

	/**
	 * Constructor sets up the input values for retrieving the option series
	 * data. The caller is allowed to specify whether quarterly and/or weekly
	 * option data should be included.
	 * 
	 * @param authToken
	 *            the authorization token for the session with OptionsHouse
	 * @param symbol
	 *            the underlying stock symbol upon which the options are based
	 * @param showQuarterlies
	 *            flag indicating if quarterly option data should be included
	 * @param showWeeklies
	 *            flag indicating if weekly option data should be included
	 */
	public OhViewSeries(String authToken, String symbol,
			boolean showQuarterlies, boolean showWeeklies)
	{
		m_authToken = authToken;
		m_symbol = OptionsHouseUtilities
				.extractStockFromKey(OptionsHouseUtilities.createKey(symbol));
		m_showQuarterlies = showQuarterlies;
		m_showWeeklies = showWeeklies;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ryanantkowiak.jOptionsHouseAPI.IOh#getRequest()
	 */
	@Override
	protected OhMsgViewSeriesReq getRequest()
	{
		return m_request;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ryanantkowiak.jOptionsHouseAPI.IOh#getResponse()
	 */
	@Override
	protected OhMsgViewSeriesRsp getResponse()
	{
		return m_response;
	}

	/**
	 * Sets up the input values for retrieving the option series data. The
	 * default behavior will show quarterly option data, but will omit weekly
	 * option data. Also sends the request to the OptionsHouse server.
	 * 
	 * @param authToken
	 *            the authorization token for the session with OptionsHouse
	 * @param symbol
	 *            the underlying stock symbol upon which the options are based
	 */
	public void execute(String authToken, String accountId, String symbol)
	{
		m_authToken = authToken;
		m_symbol = OptionsHouseUtilities
				.extractStockFromKey(OptionsHouseUtilities.createKey(symbol));
		m_showQuarterlies = true;
		m_showWeeklies = false;

		execute();
	}

	/**
	 * Sets up the input values for retrieving the option series data. The
	 * caller is allowed to specify whether quarterly and/or weekly option data
	 * should be included. Also sends the request to the OptionsHouse server.
	 * 
	 * @param authToken
	 *            the authorization token for the session with OptionsHouse
	 * @param symbol
	 *            the underlying stock symbol upon which the options are based
	 * @param showQuarterlies
	 *            flag indicating if quarterly option data should be included
	 * @param showWeeklies
	 *            flag indicating if weekly option data should be included
	 */
	public void execute(String authToken, String accountId, String symbol,
			boolean showQuarterlies, boolean showWeeklies)
	{
		m_authToken = authToken;
		m_symbol = OptionsHouseUtilities
				.extractStockFromKey(OptionsHouseUtilities.createKey(symbol));
		m_showQuarterlies = showQuarterlies;
		m_showWeeklies = showWeeklies;

		execute();
	}

	/**
	 * Send the request to the OptionsHouse API server and retrieve the
	 * response.
	 */
	@Override
	public void execute()
	{
		m_request = new OhMsgViewSeriesReq(m_authToken, m_symbol,
				m_showQuarterlies, m_showWeeklies);
		m_httpRequest = new OptionsHouseHttpRequest(m_request.getJsonString(),
				m_request.getPage());
		m_httpRequest.sendRequest();
		m_response = OhMsgViewSeriesRsp.build(m_httpRequest.getResponse());

		super.execute();
	}

	/**
	 * Returns the value of the "q" parameter returned by OptionsHouse. It may
	 * refer to the current quote price of the underlying security. However, the
	 * API reference does not specify. Use this value at your own risk.
	 * 
	 * @return the value of "q" returned by OptionsHouse
	 */
	public String getQ()
	{
		if (null != getData() && null != getData().q)
		{
			return getData().q;
		}

		return "";
	}

	/**
	 * Returns the list of option contract series data.
	 * 
	 * @return the list of option contract data
	 */
	public List<OhSeriesExpiration> getSeriesData()
	{
		List<OhSeriesExpiration> data = new ArrayList<OhSeriesExpiration>();

		if (null != getSeriesList())
		{
			for (int i = 0; i < m_response.EZMessage.data.s.size(); ++i)
			{
				OhSeriesExpiration exp = new OhSeriesExpiration();
				exp.m_expirationDate = getSeriesList().get(i).e;
				exp.m_contracts = new ArrayList<String>();
				for (int j = 0; j < getSeriesList().get(i).k.size(); ++j)
				{
					exp.m_contracts.add(getSeriesList().get(i).k.get(j));
				}
			}
		}

		return data;
	}

	/**
	 * Internal helper method to get data from the response message
	 * 
	 * @return the data object
	 */
	private OhMsgViewSeriesRsp.EZMessage_.data_ getData()
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
	 * @return the series list
	 */
	private List<OhMsgViewSeriesRsp.EZMessage_.data_.s_> getSeriesList()
	{
		if (null != getData() && null != getData().s)
		{
			return getData().s;
		}

		return null;
	}

}

/**
 * Internal data structure to represent JSON communication with the OptionsHouse
 * API. Specifies the request for retrieving a series of option contract data.
 * 
 * @author Ryan Antkowiak 
 */
class OhMsgViewSeriesReq extends IOhMsgReq
{
	public OhMsgViewSeriesReq(String authToken, String symbol,
			boolean quarterlies, boolean weeklies)
	{
		m_page = "m";

		EZViewSeriesReq ezReq = new EZViewSeriesReq();
		ezReq.EZMessage.action = "view.series";
		ezReq.EZMessage.data.authToken = authToken;
		ezReq.EZMessage.data.symbol = symbol;
		ezReq.EZMessage.data.quarterlies = quarterlies;
		ezReq.EZMessage.data.weeklies = weeklies;

		m_json = (new GsonBuilder()).create().toJson(ezReq, ezReq.getClass());
	}

	public class EZViewSeriesReq
	{
		public EZMessage_ EZMessage = new EZMessage_();

		public class EZMessage_
		{
			public String action;

			public data_ data = new data_();

			public class data_
			{
				public String authToken;
				public String symbol;
				boolean quarterlies;
				boolean weeklies;
			}
		}
	}
}

/**
 * Internal data structure to represent JSON communication with the OptionsHouse
 * API. Specifies the response for receiving a series of option contract data.
 * 
 * @author Ryan Antkowiak 
 */
class OhMsgViewSeriesRsp extends IOhMsgRsp
{
	@Override
	public EZMessageBaseRsp getEZ()
	{
		return EZMessage;
	}

	public static OhMsgViewSeriesRsp build(String str)
	{
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(ErrorMap.class,
				new ErrorMapDeserializer());
		Gson gson = gsonBuilder.create();

		OhMsgViewSeriesRsp rsp = (gson.fromJson(str, OhMsgViewSeriesRsp.class));
		rsp.m_raw = str;
		return rsp;
	}

	public EZMessage_ EZMessage;

	public class EZMessage_ extends EZMessageBaseRsp
	{
		public data_ data;

		public class data_
		{
			public List<s_> s;

			public class s_
			{
				public String e;
				public List<String> k;
			}

			public String q;
		}
	}

}