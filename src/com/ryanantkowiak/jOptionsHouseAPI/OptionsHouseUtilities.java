/*
 * Copyright (c) 2013 Ryan Antkowiak (antkowiak@gmail.com).
 * All rights reserved.
 * It is provided AS-IS, WITHOUT ANY WARRANTY either expressed or implied.
 * You may study, use, and modify it for any non-commercial purpose.
 * You may distribute it non-commercially as long as you retain this notice.
 * For a commercial use license, contact antkowiak@gmail.com.
 */
package com.ryanantkowiak.jOptionsHouseAPI;

/**
 * <pre>
 * This class contains some utility methods that are useful when
 * dealing with OptionsHouse symbols and keys. Internally, many of the
 * OptionsHouse API JSON messages are required to contain the keys for
 * stocks and ETFs in a similar format how options contracts are formatted.
 * 
 * For example, option contracts look like: "IBM:20110716:1600000:C" and
 * contain 4 pieces of data, separated by a colon ":" character. The
 * four pieces of data in order are:
 * 1) Underlying stock symbol [ex: "IBM"]
 * 2) Expiration date (YYYYMMDD) [ex: "20110716"]
 * 3) Strike Price in hundredths of a cent [ex: "1600000"] for $160.00
 * 4) "C" or "P" for a Call option or Put Option [ex: "C"]
 * 
 * For stocks, the OptionsHouse API internally refers to the security
 * as follows:  "IBM:::S"
 * 
 * Where the first part of it is the stock symbol,
 * followed by three colons, and then finally a capital letter "S" (for "Stock")
 * </pre>
 * 
 * @author Ryan Antkowiak (antkowiak@gmail.com)
 */
public class OptionsHouseUtilities
{
	/**
	 * Boolean flag to enable debug message tracing output. Warning: This will
	 * cause all JSON message data to be printed in clear-text INCLUDING login
	 * and password credentials! Only enable this in a secure environment!
	 */
	public static boolean DEBUG_MSG_TRACING = false;

	/**
	 * Sets a flag that indicates whether or not all JSON message data should be
	 * printed to standard out.
	 * 
	 * @param msgTracing
	 *            true enables tracing, false disables tracing
	 */
	public static void setDebugMsgTracing(boolean msgTracing)
	{
		DEBUG_MSG_TRACING = msgTracing;
	}

	/**
	 * Normalize a stock symbol by removing all characters except alphabetic
	 * characters. Also convert it to all upper-case characters.
	 * 
	 * @param symbol
	 *            the symbol to be normalized
	 * @return the normalized value of the stock symbol
	 */
	public static String normalizeStockSymbol(String symbol)
	{
		String s = symbol;
		s = s.replaceAll("[^A-Za-z]", "");
		s = s.toUpperCase();
		return s;
	}

	/**
	 * Normalize a key used by OptionsHouse API to specify a particular
	 * security. This can be a stock or an option, and must contain
	 * colon-separated fields. It will remove all characters except alphanumeric
	 * and colons. Also convert it to all upper-case characters.
	 * 
	 * @param key
	 *            the key of either a stock or option security to be normalized
	 * @return the normalized value of the key
	 */
	public static String normalizeKey(String key)
	{
		String s = key;
		s = s.replaceAll("[^A-Za-z0-9:]", "");
		s = s.toUpperCase();
		return s;
	}

	/**
	 * Converts a simple stock symbol to an OptionsHouse API security key. Ex:
	 * Converts "IBM" to "IBM:::S"
	 * 
	 * @param symbol
	 *            the symbol to be converted to a security key
	 * @return the security key
	 */
	public static String convertStockToKey(String symbol)
	{
		String s = normalizeStockSymbol(symbol);
		String key = s + ":::S";
		return key;
	}

	/**
	 * Checks to see if the input value has already been converted to a security
	 * key.
	 * 
	 * @param key
	 *            the string to check whether or not is a key
	 * @return true if the input is already a key
	 */
	public static boolean isAlreadyKey(String key)
	{
		return key.contains(":");
	}

	/**
	 * Returns the underlying stock symbol portion of an OptionsHouse API
	 * security key
	 * 
	 * @param key
	 *            the string representing the security key (ex:
	 *            "IBM:20110716:1600000:C")
	 * @return the string of the underlying security (ex: "IBM")
	 */
	public static String extractStockFromKey(String key)
	{
		String s = normalizeKey(key);
		s = s.replaceAll(":.*", "");
		return s;
	}

	/**
	 * Checks to see if the input is already a security key. If it isn't, it
	 * will create a security key out of the normalized stock symbol, and return
	 * it
	 * 
	 * @param input
	 *            string containing a stock or option security key
	 * @return The normalized key that corresponds to the given input string
	 */
	public static String createKey(String input)
	{
		String key = "";

		if (isAlreadyKey(input))
		{
			key = normalizeKey(input);
		} else
		{
			key = convertStockToKey(normalizeStockSymbol(input));
		}

		return key;
	}

	/**
	 * Check if the given input is a security key that corresponds to a stock
	 * security
	 * 
	 * @param key
	 *            the input to be checked
	 * @return true if the input security key is for a stock
	 */
	public static boolean isKeyStock(String key)
	{
		String k = createKey(key);

		return k.endsWith("S");
	}

	/**
	 * Check if the given input is a security key that corresponds to an option
	 * security
	 * 
	 * @param key
	 *            the input to be checked
	 * @return true if the input security key is for an option contract
	 */
	public static boolean isKeyOption(String key)
	{
		String k = createKey(key);

		return (k.endsWith("C") || k.endsWith("P"));
	}

	/**
	 * Normalize two security keys and compare them for equality
	 * 
	 * @param key1
	 *            one of the security keys to be compared for equality
	 * @param key2
	 *            another one of the security keys to be compared
	 * @return true if both input keys are equal
	 */
	public static boolean areKeysEqual(String key1, String key2)
	{
		String k1 = createKey(key1);
		String k2 = createKey(key2);
		return k1.equals(k2);
	}

}
