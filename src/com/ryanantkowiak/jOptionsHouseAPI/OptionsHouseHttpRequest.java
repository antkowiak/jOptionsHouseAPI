/*
 * Copyright (c) 2013 Ryan Antkowiak .
 * All rights reserved.
 * It is provided AS-IS, WITHOUT ANY WARRANTY either expressed or implied.
 * You may study, use, and modify it for any non-commercial purpose.
 * You may distribute it non-commercially as long as you retain this notice.
 * For a commercial use license, contact .
 */
package com.ryanantkowiak.jOptionsHouseAPI;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * This class takes care of making a request to one of the OptionsHouse API
 * servers. Requests must be in the JSON format. Requests must be directed at
 * one of two pages ("j" or "m").
 * 
 * @author Ryan Antkowiak 
 */

class OptionsHouseHttpRequest
{
	/** The JSON request that will be sent to the OptionsHouse API */
	private String m_query;

	/** The type of page to request ("j" or "m") */
	private String m_page;

	/** The JSON response from the OptionsHouse API */
	private String m_response;

	/** Contains any error text as a result of attempting to send the request */
	private String m_errorMessage;

	/**
	 * Flag that indicates whether or not the request to OptionsHouse API has
	 * been attempted yet
	 */
	private boolean m_requestAttempted;

	/** Flag that indicates the success of the last request to OptionsHouse API */
	private boolean m_success;

	/**
	 * Unused default constructor
	 */
	@SuppressWarnings("unused")
	private OptionsHouseHttpRequest()
	{
	}

	/**
	 * Construct a request that can be sent to OptionsHouse API. The constructor
	 * only sets up the object. It will not actually send the request.
	 * 
	 * @param query
	 *            The JSON text string for the request to OptionsHouse API
	 * @param page
	 *            The page to request that corresponds to the type of request.
	 *            Must be one of two values: "j" or "m".
	 */
	public OptionsHouseHttpRequest(String query, String page)
	{
		m_query = query;
		m_page = "m";
		if (page == "j")
		{
			m_page = "j";
		}

		m_response = "";
		m_errorMessage = "";
		m_requestAttempted = false;
		m_success = false;
	}

	/**
	 * Returns true if the last request was successfully send and a response
	 * successfully received.
	 * 
	 * @return true on success
	 */
	public boolean getSuccess()
	{
		return m_success;
	}

	/**
	 * Returns the JSON string response from the OptionsHouse API server, as a
	 * result of a prior request.
	 * 
	 * @return JSON string response
	 */
	public String getResponse()
	{
		return m_response;
	}

	/**
	 * Returns a string containing an error message, as a result of a prior
	 * request to the OptionsHouse API server.
	 * 
	 * @return string containing error message
	 */
	public String getErrorMessage()
	{
		return m_errorMessage;
	}

	/**
	 * Send the request to the OptionsHouse API server and retrieve the
	 * response. Returns true on success.
	 * 
	 * @return true on success
	 */
	public boolean sendRequest()
	{
		if (!m_requestAttempted)
		{
			m_requestAttempted = true;

			m_success = true;

			String httpsURL = "https://api.optionshouse.com/" + m_page;

			StringBuilder response = new StringBuilder();
			URL url = null;
			try
			{
				url = new URL(httpsURL);
			} catch (MalformedURLException e)
			{
				handleFailure(e);
			}

			if (m_success)
			{
				HttpsURLConnection conn = null;
				try
				{
					conn = (HttpsURLConnection) url.openConnection();
					try
					{
						conn.setRequestMethod("POST");
						conn.setDoOutput(true);
						conn.setDoInput(true);
						conn.setUseCaches(false);
						conn.setAllowUserInteraction(false);
						conn.setRequestProperty("Content-Type", "text/xml");
					} catch (ProtocolException e)
					{
						handleFailure(e);
					}

					if (m_success)
					{
						OutputStream out = conn.getOutputStream();
						OutputStreamWriter wr = null;
						try
						{
							wr = new OutputStreamWriter(out);
							wr.write(m_query);
						} catch (IOException e)
						{
							handleFailure(e);
						} finally
						{
							if (wr != null)
							{
								wr.close();
							}
						}
					}

					if (m_success)
					{
						InputStream in = conn.getInputStream();
						BufferedReader rd = null;
						try
						{
							rd = new BufferedReader(new InputStreamReader(in));
							String responseSingle = null;
							while ((responseSingle = rd.readLine()) != null)
							{
								response.append(responseSingle);
							}
							m_response = response.toString();
						} catch (IOException e)
						{
							handleFailure(e);
						} finally
						{
							if (rd != null)
							{
								rd.close();
							}
						}
					}
				} catch (IOException e)
				{
					handleFailure(e);
				} finally
				{
					if (conn != null)
					{
						conn.disconnect();
					}
				}
			}
		}

		return m_success;
	}

	/**
	 * Handles a failure when attempting to send a request to the OptionsHouse
	 * API server. Sets the "success" flag to false. Sets the error message for
	 * later retrieval, if desired.
	 * 
	 * @param e
	 *            the exception used for retrieving the stack trace for the
	 *            error message
	 */
	private void handleFailure(Exception e)
	{
		m_success = false;
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		m_errorMessage += sw.toString() + "\n";
	}

}
