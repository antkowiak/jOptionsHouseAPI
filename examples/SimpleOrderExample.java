import java.util.List;

import com.ryanantkowiak.jOptionsHouseAPI.OhAccountList;
import com.ryanantkowiak.jOptionsHouseAPI.OhCancelOrder;
import com.ryanantkowiak.jOptionsHouseAPI.OhLogin;
import com.ryanantkowiak.jOptionsHouseAPI.OhLogout;
import com.ryanantkowiak.jOptionsHouseAPI.OhOrderDetails;
import com.ryanantkowiak.jOptionsHouseAPI.OhSimpleOrder;
import com.ryanantkowiak.jOptionsHouseAPI.PositionType;
import com.ryanantkowiak.jOptionsHouseAPI.Side;
import com.ryanantkowiak.jOptionsHouseAPI.TimeInForce;


/**
 * This example shows how to create and then cancel a simple order using OptionsHouse API
 * 
 * @author Ryan Antkowiak (antkowiak@gmail.com)
 */
public class SimpleOrderExample
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
			
			// Create the accountList object (and use the authToken from the login)
			OhAccountList accountList = new OhAccountList(authToken);
			
			// Execute the accountList (send the message to the OptionsHouse server)
			accountList.execute();
			
			// Get the list of account IDs
			List<String> accountIds = accountList.getAccountIdList();
			
			String virtualAccountId = "";
			
			// Iterate over the account IDs, looking for a virtual account.
			// This example code is intentionally only going to try to work with
			// a virtual account.  If no virtual account is found, then no order will
			// be created.
			for (String accountId : accountIds)
			{
				// Since this is an example, we will only look at virtual accounts
				if (accountList.getIsVirtual(accountId))
				{
					virtualAccountId = accountId;
					break;
				}
			}
						
			// If no virtual account was found, log out and exit
			if (virtualAccountId.isEmpty())
			{
				waitOneSecond();
				OhLogout logout = new OhLogout(authToken);
				logout.execute();
				System.out.println("No virtual account found.  Existing.");
				System.exit(0);
			}
			
			System.out.println("Using virtual account with ID: " + virtualAccountId);


			// Must wait 1 second (1000ms) between sending messages to OptionsHouse
			waitOneSecond();
			
			// Create the order object with the following parameters:
			// 1. The authorization token we received when logging in
			// 2. The account ID for which the order should be submitted.
			//    (in this case, the virtual account we previously searched for.)
			// 3. The symbol of the stock (or option contract)
			// 4. The quantity of the order
			// 5. The limit price of the order
			// 6. The side (buy or sell)
			// 7. The type of order (open or close -- e.g. "buy to open", "sell to close")
			// 8. The time in force of the order (good til day, good til cancelled, extended hours)
			OhSimpleOrder order = new OhSimpleOrder(
					authToken,
					virtualAccountId,
					"SPY",
					1,
					25.00,
					Side.Buy,
					PositionType.Open,
					TimeInForce.Day);
					
			// Send the order message to OptionsHouse
			order.execute();
			
			// Must wait 1 second (1000ms) between sending messages to OptionsHouse
			waitOneSecond();
			
			// Get the order id
			String orderId = order.getOrderId();
			
			// Check if the order was created
			if (order.wasCreated() == false)
			{
				OhLogout logout = new OhLogout(authToken);
				logout.execute();
				System.out.println("The order was not able to be created! Exiting.");
				System.exit(0);
			}
				
			System.out.println("Order created with ID: " + orderId);
			
			// Get the details of the order
			OhOrderDetails orderDetails = new OhOrderDetails(authToken, virtualAccountId, orderId);
			orderDetails.execute();
			System.out.println("The status of the order is: " + orderDetails.getStatus());
			waitOneSecond();
			
			// Cancel the order
			System.out.println("Now attempting to cancel the order.");
			OhCancelOrder cancelOrder = new OhCancelOrder(authToken, virtualAccountId, orderId);
			cancelOrder.execute();
			waitOneSecond();
			
			// Get the details of the order again
			orderDetails.execute();
			System.out.println("Now the status of the order is: " + orderDetails.getStatus());
			
			// Logout
			waitOneSecond();
			OhLogout logout = new OhLogout(authToken);
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
