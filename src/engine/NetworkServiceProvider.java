package engine;

import partiePC.CheckRegisterPC;
import data.Account;
import exceptions.AccountNotFoundException;
import exceptions.IllegalFieldException;
import exceptions.NetworkServiceException;
import interfaces.NetworkServiceInterface;

public class NetworkServiceProvider
{

	private static NetworkServiceInterface networkService;
	
	public static void setNetworkService(NetworkServiceInterface networkService)
	{
		if(networkService == null)
			throw new NullPointerException();
		else
			NetworkServiceProvider.networkService = networkService;
	}
	
	public static NetworkServiceInterface getNetworkService()
	{
		return networkService;
	}

	/**************************************/
	/**
	 * Initialize this service to rend it available for network communications.
	 * @throws NetworkServiceException
	 */
	public void initNetworkService() throws NetworkServiceException {
		
	}
	
	
	/**
	 * @param newAccount
	 * @throws IllegalFieldException
	 * @throws NetworkServiceException if there is a network failure.
	 */
	public void createAccount(Account newAccount, String newPassword) throws IllegalFieldException, NetworkServiceException {
		
		CheckRegisterPC.Register("localhost:8080", "Georgette", "Georgette@georgette.com", "Secret");
	}

	/**
	 * 
	 * @param pseudo
	 * @param password
	 * @return
	 * @throws IllegalFieldException
	 * @throws NetworkServiceException
	 */
	public Account authenticate(String pseudo, String password) throws AccountNotFoundException, NetworkServiceException {
		return null;
		
	}
	

}
