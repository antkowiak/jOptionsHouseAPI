/*
 * Copyright (c) 2013 Ryan Antkowiak (antkowiak@gmail.com).
 * All rights reserved.
 * It is provided AS-IS, WITHOUT ANY WARRANTY either expressed or implied.
 * You may study, use, and modify it for any non-commercial purpose.
 * You may distribute it non-commercially as long as you retain this notice.
 * For a commercial use license, contact antkowiak@gmail.com.
 */
package com.ryanantkowiak.jOptionsHouseAPI;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * This class will retrieve the list of accounts from OptionsHouse
 * 
 * @author Ryan Antkowiak (antkowiak@gmail.com)
 */
public class OhAccountList extends IOh
{
	/** authorization token for the session with OptionsHouse API */
	private String m_authToken;

	/** contains the request JSON message for account into */
	private OhMsgAccountInfoReq m_request;

	/** contains the response JSON message for account into */
	private OhMsgAccountInfoRsp m_response;

	/**
	 * Constructor sets up the input values for retrieving the account list info
	 * 
	 * @param authToken
	 *            the authorization token for the session with OptionsHouse
	 */
	public OhAccountList(String authToken)
	{
		m_authToken = authToken;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ryanantkowiak.jOptionsHouseAPI.IOh#getRequest()
	 */
	@Override
	protected OhMsgAccountInfoReq getRequest()
	{
		return m_request;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ryanantkowiak.jOptionsHouseAPI.IOh#getResponse()
	 */
	@Override
	protected OhMsgAccountInfoRsp getResponse()
	{
		return m_response;
	}

	/**
	 * Sets up the input values for retrieving the account list info. Also sends
	 * the request to the OptionsHouse server.
	 * 
	 * @param authToken
	 *            the authorization token for the session with OptionsHouse
	 */
	public void execute(String authToken)
	{
		m_authToken = authToken;
		execute();
	}

	/**
	 * Send the request to the OptionsHouse API server and retrieve the
	 * response.
	 */
	@Override
	public void execute()
	{
		m_request = new OhMsgAccountInfoReq(m_authToken);
		m_httpRequest = new OptionsHouseHttpRequest(m_request.getJsonString(),
				m_request.getPage());
		m_httpRequest.sendRequest();
		m_response = OhMsgAccountInfoRsp.build(m_httpRequest.getResponse());

		super.execute();
	}

	/**
	 * Returns a list of account IDs associated with the current session
	 * 
	 * @return list of account IDs associated with the current session
	 */
	public List<String> getAccountIdList()
	{
		List<String> accountIds = new ArrayList<String>();

		if (null != getAccountList())
		{
			for (int i = 0; i < getAccountList().size(); ++i)
			{
				accountIds.add(getAccountList().get(i).accountId);
			}
		}

		return accountIds;
	}

	/**
	 * Returns a list of account names associated with the current session
	 * 
	 * @return list of account names associated with the current session
	 */
	public List<String> getAccountNameList()
	{
		List<String> accountNames = new ArrayList<String>();

		if (null != getAccountList())
		{
			for (int i = 0; i < getAccountList().size(); ++i)
			{
				accountNames.add(getAccountList().get(i).accountName);
			}
		}

		return accountNames;
	}

	/**
	 * Returns the account name associated with a given accountId
	 * 
	 * @param accountId
	 *            the account id for which a name will be returned
	 * @return the account name associated with accountId
	 */
	public String getAccountNameForAccountId(String accountId)
	{
		if (null != getAccountList())
		{
			for (int i = 0; i < getAccountList().size(); ++i)
			{
				if (getAccountList().get(i).accountId.equals(accountId))
				{
					return (getAccountList().get(i).accountName);
				}
			}
		}

		return "";
	}

	/**
	 * Returns the account ID associated with a given accountName
	 * 
	 * @param accountName
	 *            the account name for which an ID will be returned
	 * @return the account ID associated with accountName
	 */
	public String getAccountIdForAccountName(String accountName)
	{
		if (null != getAccountList())
		{
			for (int i = 0; i < getAccountList().size(); ++i)
			{
				if (getAccountList().get(i).accountName.equals(accountName))
				{
					return (getAccountList().get(i).accountId);
				}
			}
		}

		return "";
	}

	/**
	 * Returns the number of accounts associated with this session
	 * 
	 * @return the number of accounts
	 */
	public int getNumAccounts()
	{
		if (null != getAccountList())
		{
			return (getAccountList().size());
		}

		return 0;
	}

	/**
	 * Return the account ID at a given index of the array of accounts
	 * 
	 * @param index
	 *            the index into an array of accounts
	 * @return the account ID for the provided index
	 */
	public String getAccountIdAtIndex(int index)
	{
		if (index < getNumAccounts())
		{
			return getAccountIdList().get(index);
		}

		return "";
	}

	/**
	 * Return the account name at a given index of the array of accounts
	 * 
	 * @param index
	 *            the index into an array of accounts
	 * @return the account name for the provided index
	 */
	public String getAccountNameAtIndex(int index)
	{
		if (index < getNumAccounts())
		{
			return getAccountNameList().get(index);
		}

		return "";
	}
	
	/**
	 * Returns the inactivity timeout
	 * 
	 * @return	the inactivity timeout
	 */
	public String getInactivityTimeout()
	{
		if (null != getData())
		{
			return getData().inactivityTimeout;
		}
		
		return "";
	}
	
	/**
	 * Returns a flag that indicates if this login requires account creation
	 * 
	 * @return	true if this login requires account creation
	 */
	public boolean getRequiresAccountCreation()
	{
		if (null != getData())
		{
			return getData().requiresAccountCreation;
		}
		
		return false;
	}
	
	/**
	 * Returns the last name
	 * 
	 * @return	the last name
	 */
	public String getLastName()
	{
		if (null != getDataLogin())
		{
			return getDataLogin().lastName;
		}
		
		return "";
	}
	
	/**
	 * Returns the account mode.  (Note: The spec doesn't specify what this is.)
	 * 
	 * @return the account mode
	 */
	public String getAccountMode()
	{
		if (null != getDataLogin())
		{
			return getDataLogin().accountMode;
		}
		
		return "";
	}
	
	/**
	 * Returns the RFQ Warning.  (Note: The spec doesn't specify what this is.)
	 * 
	 * @return	the RFQ warning
	 */
	public boolean getRfqWarning()
	{
		if (null != getDataLogin())
		{
			return getDataLogin().rfqWarning;
		}
		
		return false;
	}
	
	/**
	 * Returns the tools warning.  (Note: The spec doesn't specify what this is.)
	 * 
	 * @return	the tools warning
	 */
	public boolean getToolsWarning()
	{
		if (null != getDataLogin())
		{
			return getDataLogin().toolsWarning;
		}
		
		return false;
	}
	
	/**
	 * Returns the login count
	 * 
	 * @return	the login count
	 */
	public String getLoginCount()
	{
		if (null != getDataLogin())
		{
			return getDataLogin().loginCount;
		}
		
		return "";
	}
	
	/**
	 * Returns the first name
	 * 
	 * @return	the first name
	 */
	public String getFirstName()
	{
		if (null != getDataLogin())
		{
			return getDataLogin().firstName;
		}
		
		return "";
	}
	
	/**
	 * Returns the tools warning version.  (Note: The spec doesn't specify what this is.)
	 * 
	 * @return	the tools warning version
	 */
	public String getToolsWarningVersion()
	{
		if (null != getDataLogin())
		{
			return getDataLogin().toolsWarningVersion;
		}
		
		return "";
	}
	
	/**
	 * Returns the default symbol
	 * 
	 * @return	the default symbol
	 */
	public String getDefaultSymbol()
	{
		if (null != getDataLogin())
		{
			return getDataLogin().defaultSymbol;
		}
		
		return "";
	}
	
	/**
	 * Returns the UI mode.  (Note: The spec doesn't specify what this is.)
	 * 
	 * @return the UI mode
	 */
	public String getUiMode()
	{
		if (null != getDataLogin())
		{
			return getDataLogin().uiMode;
		}
		
		return "";
	}
	
	
	/**
	 * Given the provided account id, returns the value of: canChangeCommissionSchedule
	 * 
	 * @param accountId	the account id for which the data field is returned
	 * 
	 * @return the requested field
	 */
	public boolean getCanChangeCommissionSchedule(String accountId)
	{
		OhMsgAccountInfoRsp.EZMessage_.data_.account_ a = getDataAccount(accountId);
	
		if (null != a)
		{
			return a.canChangeCommissionSchedule;
		}
		
		return false;
	}
	
	/**
	 * Given the provided account id, returns the value of: nextCommissionSchedule
	 * 
	 * @param accountId	the account id for which the data field is returned
	 * 
	 * @return the requested field
	 */
	public String getNextCommissionSchedule(String accountId)
	{
		OhMsgAccountInfoRsp.EZMessage_.data_.account_ a = getDataAccount(accountId);
	
		if (null != a)
		{
			return a.nextCommissionSchedule;
		}
		
		return "";
	}
	
	
	/**
	 * Given the provided account id, returns the value of: isVirtual
	 * 
	 * @param accountId	the account id for which the data field is returned
	 * 
	 * @return the requested field
	 */
	public boolean getIsVirtual(String accountId)
	{
		OhMsgAccountInfoRsp.EZMessage_.data_.account_ a = getDataAccount(accountId);
	
		if (null != a)
		{
			return a.isVirtual;
		}
		
		return false;
	}
	
	/**
	 * Given the provided account id, returns the value of: riskMaxDollarsPerOrder
	 * 
	 * @param accountId	the account id for which the data field is returned
	 * 
	 * @return the requested field
	 */
	public String getRiskMaxDollarsPerOrder(String accountId)
	{
		OhMsgAccountInfoRsp.EZMessage_.data_.account_ a = getDataAccount(accountId);
	
		if (null != a)
		{
			return a.riskMaxDollarsPerOrder;
		}
		
		return "";
	}
	
	/**
	 * Given the provided account id, returns the value of: riskMaxSharesPerOrder
	 * 
	 * @param accountId	the account id for which the data field is returned
	 * 
	 * @return the requested field
	 */
	public String getRiskMaxSharesPerOrder(String accountId)
	{
		OhMsgAccountInfoRsp.EZMessage_.data_.account_ a = getDataAccount(accountId);
	
		if (null != a)
		{
			return a.riskMaxSharesPerOrder;
		}
		
		return "";
	}
	
	/**
	 * Given the provided account id, returns the value of: accountDesc
	 * 
	 * @param accountId	the account id for which the data field is returned
	 * 
	 * @return the requested field
	 */
	public String getAccountDesc(String accountId)
	{
		OhMsgAccountInfoRsp.EZMessage_.data_.account_ a = getDataAccount(accountId);
	
		if (null != a)
		{
			return a.accountDesc;
		}
		
		return "";
	}
	
	/**
	 * Given the provided account id, returns the value of: accountName
	 * 
	 * @param accountId	the account id for which the data field is returned
	 * 
	 * @return the requested field
	 */
	public String getAccountName(String accountId)
	{
		OhMsgAccountInfoRsp.EZMessage_.data_.account_ a = getDataAccount(accountId);
	
		if (null != a)
		{
			return a.accountName;
		}
		
		return "";
	}
	
	/**
	 * Given the provided account id, returns the value of: yearAccountOpened
	 * 
	 * @param accountId	the account id for which the data field is returned
	 * 
	 * @return the requested field
	 */
	public String getYearAccountOpened(String accountId)
	{
		OhMsgAccountInfoRsp.EZMessage_.data_.account_ a = getDataAccount(accountId);
	
		if (null != a)
		{
			return a.yearAccountOpened;
		}
		
		return "";
	}
	
	/**
	 * Given the provided account id, returns the value of: riskMaxContractsPerOrder
	 * 
	 * @param accountId	the account id for which the data field is returned
	 * 
	 * @return the requested field
	 */
	public String getRiskMaxContractsPerOrder(String accountId)
	{
		OhMsgAccountInfoRsp.EZMessage_.data_.account_ a = getDataAccount(accountId);
	
		if (null != a)
		{
			return a.riskMaxContractsPerOrder;
		}
		
		return "";
	}
	
	/**
	 * Given the provided account id, returns the value of: optionsWarning
	 * 
	 * @param accountId	the account id for which the data field is returned
	 * 
	 * @return the requested field
	 */
	public boolean getOptionsWarning(String accountId)
	{
		OhMsgAccountInfoRsp.EZMessage_.data_.account_ a = getDataAccount(accountId);
	
		if (null != a)
		{
			return a.optionsWarning;
		}
		
		return false;
	}
	
	/**
	 * Given the provided account id, returns the value of: partnerCode
	 * 
	 * @param accountId	the account id for which the data field is returned
	 * 
	 * @return the requested field
	 */
	public String getPartnerCode(String accountId)
	{
		OhMsgAccountInfoRsp.EZMessage_.data_.account_ a = getDataAccount(accountId);
	
		if (null != a)
		{
			return a.partnerCode;
		}
		
		return "";
	}
	
	/**
	 * Given the provided account id, returns the value of: accountType
	 * 
	 * @param accountId	the account id for which the data field is returned
	 * 
	 * @return the requested field
	 */
	public String getAccountType(String accountId)
	{
		OhMsgAccountInfoRsp.EZMessage_.data_.account_ a = getDataAccount(accountId);
	
		if (null != a)
		{
			return a.accountType;
		}
		
		return "";
	}
	
	/**
	 * Given the provided account id, returns the value of: canAccountACH
	 * 
	 * @param accountId	the account id for which the data field is returned
	 * 
	 * @return the requested field
	 */
	public boolean getCanAccountACH(String accountId)
	{
		OhMsgAccountInfoRsp.EZMessage_.data_.account_ a = getDataAccount(accountId);
	
		if (null != a)
		{
			return a.canAccountACH;
		}
		
		return false;
	}
	
	/**
	 * Given the provided account id, returns the value of: accountTypeId
	 * 
	 * @param accountId	the account id for which the data field is returned
	 * 
	 * @return the requested field
	 */
	public String getAccountTypeId(String accountId)
	{
		OhMsgAccountInfoRsp.EZMessage_.data_.account_ a = getDataAccount(accountId);
	
		if (null != a)
		{
			return a.accountTypeId;
		}
		
		return "";
	}
	
	/**
	 * Given the provided account id, returns the value of: currentCommissionSchedule
	 * 
	 * @param accountId	the account id for which the data field is returned
	 * 
	 * @return the requested field
	 */
	public String getCurrentCommissionSchedule(String accountId)
	{
		OhMsgAccountInfoRsp.EZMessage_.data_.account_ a = getDataAccount(accountId);
	
		if (null != a)
		{
			return a.currentCommissionSchedule;
		}
		
		return "";
	}
	

	/**
	 * Internal helper method to get data from the response message
	 * 
	 * @return the data object
	 */
	private OhMsgAccountInfoRsp.EZMessage_.data_ getData()
	{
		if (null != m_response && null != m_response.EZMessage
				&& null != m_response.EZMessage.data)
		{
			return m_response.EZMessage.data;
		}

		return null;
	}
	
	/**
	 * Internal helper method to get data from the response message
	 * 
	 * @return the login data object
	 */
	private OhMsgAccountInfoRsp.EZMessage_.data_.login_ getDataLogin()
	{
		if (null != getData())
		{
			return getData().login;
		}
		
		return null;
	}
	
	/**
	 * Internal helper method to get the data from the response message
	 * 
	 * @param accountId the account id of the account data to retrieve
	 * @return	the account data
	 */
	private OhMsgAccountInfoRsp.EZMessage_.data_.account_ getDataAccount(String accountId)
	{
		if (null != getData() &&
				null != getData().account)
		{
			int index = getIndexOfAccountId(accountId);
			
			if (index > 0 && index < getData().account.size())
			{
				return getData().account.get(index);
			}
		}
		
		return null;
	}
	
	/**
	 * Internal helper method to get the index of an account id
	 * 
	 * @param accountId the account id
	 * @return the index of the account id, or -1 if not found
	 */
	private int getIndexOfAccountId(String accountId)
	{
		List<String> accounts = getAccountIdList();
		
		if (null != accounts)
		{
			for (int i = 0 ; i < accounts.size() ; ++i)
			{
				if (accountId.equals(accounts.get(i)))
				{
					return i;
				}
			}
		}
		
		return -1;
	}

	/**
	 * Internal helper method to get data from the response message
	 * 
	 * @return the account list
	 */
	private List<OhMsgAccountInfoRsp.EZMessage_.data_.account_> getAccountList()
	{
		if (null != getData() && null != getData().account)
		{
			return getData().account;
		}

		return null;
	}

}

/**
 * Internal data structure to represent JSON communication with the OptionsHouse
 * API. Specifies the request for account info data.
 * 
 * @author Ryan Antkowiak (antkowiak@gmail.com)
 */
class OhMsgAccountInfoReq extends IOhMsgReq
{
	public OhMsgAccountInfoReq(String authToken)
	{
		m_page = "m";

		EZAccountInfoReq ezReq = new EZAccountInfoReq();
		ezReq.EZMessage.action = "account.info";
		ezReq.EZMessage.data.authToken = authToken;

		m_json = (new GsonBuilder()).create().toJson(ezReq, ezReq.getClass());
	}

	public class EZAccountInfoReq
	{
		public EZMessage_ EZMessage = new EZMessage_();

		public class EZMessage_
		{
			public String action;

			public data_ data = new data_();

			public class data_
			{
				public String authToken;
			}
		}
	}
}

/**
 * Internal data structure to represent JSON communication with the OptionsHouse
 * API. Specifies the response for account info data.
 * 
 * @author Ryan Antkowiak (antkowiak@gmail.com)
 */
class OhMsgAccountInfoRsp extends IOhMsgRsp
{
	@Override
	public EZMessageBaseRsp getEZ()
	{
		return EZMessage;
	}

	public static OhMsgAccountInfoRsp build(String str)
	{
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(ErrorMap.class,
				new ErrorMapDeserializer());
		Gson gson = gsonBuilder.create();

		OhMsgAccountInfoRsp rsp = (gson
				.fromJson(str, OhMsgAccountInfoRsp.class));
		rsp.m_raw = str;
		return rsp;
	}

	public EZMessage_ EZMessage;

	public class EZMessage_ extends EZMessageBaseRsp
	{
		public data_ data;

		public class data_
		{
			public List<account_> account;

			public class account_
			{
				public boolean canChangeCommissionSchedule;
				public String accountId;
				public String nextCommissionSchedule;
				public boolean isVirtual;
				public String riskMaxDollarsPerOrder;
				public String riskMaxSharesPerOrder;
				public String accountDesc;
				public String accountName;
				public String yearAccountOpened;
				public String riskMaxContractsPerOrder;
				public boolean optionsWarning;
				public String partnerCode;
				public String accountType;
				public String account;
				public boolean canAccountACH;
				public String accountTypeId;
				public String currentCommissionSchedule;
			}

			public login_ login;

			public class login_
			{
				public String lastName;
				public String accountMode;
				public boolean rfqWarning;
				public boolean toolsWarning;
				public String loginCount;
				public String firstName;
				public String toolsWarningVersion;
				public String defaultSymbol;
				public String uiMode;
			}

			public String inactivityTimeout;
			public boolean requiresAccountCreation;
		}
	}

}
