import java.util.ArrayList;
import java.util.List;

import com.ryanantkowiak.jOptionsHouseAPI.OhLogin;
import com.ryanantkowiak.jOptionsHouseAPI.OhLogout;
import com.ryanantkowiak.jOptionsHouseAPI.OhQuote;


/**
 * This example shows how to obtain stock quotes using OptionsHouse API
 * 
 * @author Ryan Antkowiak 
 */
public class QuoteExample
{
	public static void main(String[] args)
	{
		// Replace these with your OptionsHouse login credentials
		String loginName = "your_login";
		String password = "password123";
		
		// Create the login object
		OhLogin login = new OhLogin(loginName, password);

		// Execute the login (send the login message to the OptionsHouse server)
		login.execute();
		
		// Check if the login failed
		if (login.loginSuccess() == false)
		{
			System.out.println("Login Failure!");
			System.exit(0);
		}
		else
		{
			// The login succeeded
			
			// Grab the authorization token. The authToken must be used in all
			// subsequent communications with OptionsHouse for this session.
			String authToken = login.getAuthToken();
			
			// Must wait 1 second (1000ms) between sending messages to OptionsHouse
			waitOneSecond();
			
			// Create a list of symbols for which quote data will be retrieved
			List<String> symbolList = new ArrayList<String>();
			symbolList.add("SPY");
			symbolList.add("GE");
			symbolList.add("AAPL");
			
			// Create the quote object (and use the authToken from the login)
			OhQuote quote = new OhQuote(authToken, symbolList);
			quote.execute();
			
			// Print out quote data for each symbol
			for (String sym : symbolList)
			{
				System.out.println(sym + ":" +
						" last=" + quote.getLast(sym) +
						" low=" + quote.getLow(sym) +
						" high=" + quote.getHigh(sym) +
						" bid=" + quote.getBid(sym) +
						" ask=" + quote.getAsk(sym) +
						" vol=" + quote.getVolume(sym));
			}
			
			// Must wait 1 second (1000ms) between sending messages to OptionsHouse
			waitOneSecond();
			
			// Create the logout object (and use the authToken from the login)
			OhLogout logout = new OhLogout(authToken);
			
			// Execute the logout (send the logout message to the OptionsHouse server)
			logout.execute();
		}
	}
	
	/*
	 * Delay for one second. Per the API agreement with OptionsHouse, API messages should
	 * only be sent no more frequently than one per second.
	 */
	private static void waitOneSecond()
	{
		try
		{
			Thread.sleep(1000);
		}
		catch (Exception e)
		{
		}
	}
}
