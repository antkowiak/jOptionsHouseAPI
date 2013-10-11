import java.util.List;

import com.ryanantkowiak.jOptionsHouseAPI.OhAccountList;
import com.ryanantkowiak.jOptionsHouseAPI.OhLogin;
import com.ryanantkowiak.jOptionsHouseAPI.OhLogout;


/**
 * This example shows how see an account list using OptionsHouse API
 * 
 * @author Ryan Antkowiak (antkowiak@gmail.com)
 */
public class AccountListExample
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
			
			// Iterate over the account IDs, printing information about each one
			for (String id : accountIds)
			{
				System.out.println("-------------------------------");
				System.out.println("Account ID:          " + id);
				System.out.println("Account Name:        " + accountList.getAccountName(id));
				System.out.println("Account Description: " + accountList.getAccountDesc(id));
				System.out.println("Is Virtual Account?: " + accountList.getIsVirtual(id));
				System.out.println("Account Type:        " + accountList.getAccountType(id));
				System.out.println("Year Account Opened: " + accountList.getYearAccountOpened(id));
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
