import com.ryanantkowiak.jOptionsHouseAPI.OhLogin;
import com.ryanantkowiak.jOptionsHouseAPI.OhLogout;


/**
 * This class shows an example of how to login and logout from OptionsHouse
 * using the API.
 * 
 * @author Ryan Antkowiak (antkowiak@gmail.com)
 */
public class LoginLogoutExample
{

	public LoginLogoutExample()
	{
	}

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
			System.out.println("Login successful!");
			System.out.println("First Name:            " + login.getFirstName());
			System.out.println("Last Name:             " + login.getLastName());
			System.out.println("Alert String:          " + login.getAlert());
			System.out.println("Professional Account?: " + login.isProfessional());
			System.out.println("Account Funded?:       " + login.isFunded());
			
			// The log in succeeded.  Grab the authorization token
			String authToken = login.getAuthToken();

			// Must wait 1 second (1000ms) between sending messages to OptionsHouse
			waitOneSecond();
			
			// Create the logout object (and use the authToken from the login)
			OhLogout logout = new OhLogout(authToken);
			
			// Execute the logout (send the logout message to the OptionsHouse server)
			logout.execute();	
		}
	}
	
	/*
	 * Delay for one second.  Per the API agreement with OptionsHouse, API messages should
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
