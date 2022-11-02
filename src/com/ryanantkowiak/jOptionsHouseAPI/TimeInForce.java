/*
 * Copyright (c) 2013 Ryan Antkowiak .
 * All rights reserved.
 * It is provided AS-IS, WITHOUT ANY WARRANTY either expressed or implied.
 * You may study, use, and modify it for any non-commercial purpose.
 * You may distribute it non-commercially as long as you retain this notice.
 * For a commercial use license, contact .
 */
package com.ryanantkowiak.jOptionsHouseAPI;

/**
 * Enumerates the Time In Force values that can be attributed to an order on
 * OptionsHouse
 * 
 * @author Ryan Antkowiak 
 */
public enum TimeInForce
{
	/**
	 * "Good-Til-Day" (Day) Orders are canceled automatically at the end of the
	 * trading day, if not already filled
	 */
	Day,

	/**
	 * "Good-Til-Cancel" (GTC) Orders will remain open (even multiple days)
	 * until they are filled or canceled
	 */
	GTC,

	/**
	 * "Extended Trading" (EXT) Orders are allowed in the extended session after
	 * normal market hours have closed
	 */
	EXT
}
