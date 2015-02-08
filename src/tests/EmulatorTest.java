package tests;

import java.util.Date;

import data.Account;
import engine.NetworkServiceProvider;
import exceptions.AccountNotFoundException;
import exceptions.IllegalFieldException;
import exceptions.NetworkServiceException;

public class EmulatorTest
{
	@SuppressWarnings("deprecation")
	public static void main(String[] args)
	{
		NetworkServiceProvider.setNetworkService(NetworkServiceEmulator.getInstance());
		
		Account account;
		try {
			account = new Account("nth", "Nicolas", "Thierce", new Date(1993, 10, 20), "nth@gmail.com");
			try {
				NetworkServiceProvider.getNetworkService().createAccount(account, "notOk");
				System.out.println("account created.");
			} catch (IllegalFieldException e) {
				System.out.println("error for account creation : " + e.getMessage());
			} catch (NetworkServiceException e) {
				e.printStackTrace();
			}
			try {
				NetworkServiceProvider.getNetworkService().createAccount(account, "correctPassword");
				System.out.println("account created.");
			} catch (IllegalFieldException e) {
				System.out.println("error for account creation : " + e.getMessage());
			} catch (NetworkServiceException e) {
				e.printStackTrace();
			}
			
			// will fail because pseudo already used.
			try {
				NetworkServiceProvider.getNetworkService().createAccount(account, "correctPassword");
				System.out.println("account created.");
			} catch (IllegalFieldException e) {
				System.out.println("error for account creation : " + e.getMessage());
			} catch (NetworkServiceException e) {
				e.printStackTrace();
			}
			
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		try {
			NetworkServiceProvider.getNetworkService().authenticate("invalid", "invalid");
			System.out.println("Authentication 1 done.");
			NetworkServiceProvider.getNetworkService().logOut();
		} catch (AccountNotFoundException e) {
			System.out.println("account not found.");
		} catch (NetworkServiceException e) {
			e.printStackTrace();
		}

		try {
			NetworkServiceProvider.getNetworkService().authenticate("nth", "correctPassword");
			System.out.println("Authentication 2 done.");
		} catch (AccountNotFoundException e) {
			System.out.println("account not found.");
		} catch (NetworkServiceException e) {
			e.printStackTrace();
		}
		
	}

}
