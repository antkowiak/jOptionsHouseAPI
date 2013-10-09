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
 * Enumeration of the types of positions in an OptionsHouse account.
 * Positions can be initiated ("Opened") or disposed of ("Closed").
 * 
 * For example: If you buy 100 shares of SPY, you have "Opened" a position.
 * If you later sold those 100 shares, you have "Closed" the position.
 * 
 * Another example: If you have sold short 100 shares of SPY, you have also "Opened"
 * a position. When you buy the shares back to cover your short position, you have now
 * "Closed" your position.
 * </pre>
 * 
 * @author Ryan Antkowiak (antkowiak@gmail.com)
 */
public enum PositionType
{
	/**
	 * Initiate ("Open") a new position
	 */
	Open,

	/**
	 * Dispose of ("Close") an existing position
	 */
	Close
}
