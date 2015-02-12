package engine;


import java.net.URL;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import webservicePC.HTTPLoader;
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

}
