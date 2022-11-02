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
 * Enumeration of the side of an OptionsHouse order (Buy-side or Sell-side)
 * 
 * @author Ryan Antkowiak 
 */
public enum Side
{
	/**
	 * Buy (go long, or cover) with an order
	 */
	Buy,

	/**
	 * Sell (sell to close, or short) with an order
	 */
	Sell
}
