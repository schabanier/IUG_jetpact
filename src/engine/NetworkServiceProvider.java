package engine;


import java.net.URL;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import data.HTTPLoader;
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
		/* ??? configuration host .. ?*/
	}
	
	
	/**
	 * @param newAccount
	 * @throws IllegalFieldException
	 * @throws NetworkServiceException if there is a network failure.
	 */
	public void createAccount(Account newAccount, String newPassword) throws IllegalFieldException, NetworkServiceException {
		
		String adressHost;
		//voir avec Philippe construction URL//
		URL registerURL = new URL("http://"+adressHost+"/doregister?name="+name+"&username="+username+"&password="+password);
		
		String reponse = HTTPLoader.getTextFile(registerURL);

		JSONObject obj = (JSONObject) JSONValue.parse(reponse);
		
		//voir avec philippe retour serveur
		System.out.println("tag : " + obj.get("tag"));
		System.out.println("status : " + obj.get("status"));
		System.out.println("error_msg : " + obj.get("error_msg"));
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
		
		String adressHost;
		//voir avec Philippe construction URL//
		URL loginURL = new URL("http://"+adressHost+"/doregister?name="+name+"&username="+username+"&password="+password);
		
		String reponse = HTTPLoader.getTextFile(loginURL);

		JSONObject obj = (JSONObject) JSONValue.parse(reponse);
		
		//voir avec philippe retour serveur
		System.out.println("tag : " + obj.get("tag"));
		System.out.println("status : " + obj.get("status"));
		System.out.println("error_msg : " + obj.get("error_msg"));
	}
	

}
