/*
 * Copyright (c) 2013 Ryan Antkowiak .
 * All rights reserved.
 * It is provided AS-IS, WITHOUT ANY WARRANTY either expressed or implied.
 * You may study, use, and modify it for any non-commercial purpose.
 * You may distribute it non-commercially as long as you retain this notice.
 * For a commercial use license, contact .
 */
package com.ryanantkowiak.jOptionsHouseAPI;

import java.util.Map;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

/**
 * Collection of static utility methods for using JSON strings and objects.
 * 
 * @author Ryan Antkowiak 
 */
public class JsonUtilities
{

	/**
	 * Recursively print the contents of a JSON string
	 * 
	 * @param text the JSON string
	 */
	public static void printJson(String text)
	{
		JsonParser parser = new JsonParser();
		JsonElement element = parser.parse(text);
		printJson(element, "[root]");
	}
	
	/**
	 * Recursively print the contents of a GSON JSON element.
	 * 
	 * @param element	the GSON JSON element to be printed
	 * @param prefix	output will be prefixed with this string
	 */
	public static void printJson(JsonElement element, String prefix)
	{
		if (null == prefix || prefix.isEmpty())
		{
			prefix = "";
		}
		
		if (null == element || element.isJsonNull())
		{
			System.out.println(prefix + " [null]");
			return;
		}
		
		else if (element.isJsonPrimitive())
		{
			JsonPrimitive p = element.getAsJsonPrimitive();
			if (p.isBoolean())
			{
				System.out.println(prefix + " [bool=" + p.getAsBoolean() + "]");
			}
			else if (p.isString())
			{
				System.out.println(prefix + " [string='" + p.getAsString() + "']");
			}
			else if (p.isNumber())
			{
				System.out.println(prefix + " [number=" + p.getAsDouble() + "]");
			}
		}
		
		else if (element.isJsonArray())
		{
			System.out.println(prefix + " [array]");
			for (int i = 0 ; i < element.getAsJsonArray().size() ; ++i)
			{
				String newPrefix = prefix + "[" + i + "]";
				printJson(element.getAsJsonArray().get(i), newPrefix);
			}
		}
		
		else if (element.isJsonObject())
		{
			JsonObject obj = element.getAsJsonObject();

			for (Map.Entry<String,JsonElement> entry : obj.entrySet())
			{
			    String key = entry.getKey();
			    JsonElement value = entry.getValue();
			    String newPrefix = prefix + "." + key;
			    printJson(value, newPrefix);
			}
		}

	}

}
