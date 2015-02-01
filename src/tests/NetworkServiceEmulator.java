package tests;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import data.Account;
import data.Profile;
import data.Tag;
import exceptions.AccountNotFoundException;
import exceptions.IllegalFieldException;
import exceptions.NetworkServiceException;
import exceptions.NotAuthenticatedException;
import interfaces.NetworkServiceInterface;

public class NetworkServiceEmulator implements NetworkServiceInterface
{
	private List<Account> accounts;
	private List<String> passwords;
	
	private Account authenticatedAccount;
	
	private static NetworkServiceEmulator emulator = new NetworkServiceEmulator();
	
	private NetworkServiceEmulator()
	{
		@SuppressWarnings("deprecation")
		Date date = new Date(1965, 2, 24);
		Account testAccount = new Account("jdupon", "Jean", "Dupont", date, "jean.dupont@gmail.com");
		
		accounts = new ArrayList<>();
		accounts.add(testAccount);
		
		passwords = new ArrayList<>();
		passwords.add("123456");
		
		authenticatedAccount = null;
	}
	
	public void initNetworkService() throws NetworkServiceException
	{
		
	}

	public void createAccount(Account newAccount, String newPassword) throws IllegalFieldException, NetworkServiceException
	{
		if(newPassword == null)
			throw new IllegalArgumentException("parameter newPassword can't be null.");
		
		if(newPassword.length() < 6)
			throw new IllegalFieldException(IllegalFieldException.PASSWORD, "password must contain 6 characters or more.");
		
		for(Account tmp : accounts)
			if(tmp.getPseudo().equals(newAccount.getPseudo()))
				throw new IllegalFieldException(IllegalFieldException.PSEUDO, "pseudo already used.");
		
		accounts.add(newAccount);
		passwords.add(newPassword);
	}

	public Account authenticate(String pseudo, String password) throws AccountNotFoundException, NetworkServiceException
	{
		Account tmp;
		
		for(int i=0; i < accounts.size(); i++)
		{
			tmp = accounts.get(i);
			if(tmp.getPseudo().equals(pseudo))
			{
				if(passwords.get(i).equals(password))
				{
					authenticatedAccount = tmp;
					return tmp;
				}
				else
					throw new AccountNotFoundException();
			}
		}
		
		throw new AccountNotFoundException();
	}

	public void logOut()
	{
		authenticatedAccount = null;
	}

	public Account getCurrentAccount() throws NotAuthenticatedException
	{
		if(authenticatedAccount == null)
			throw new NotAuthenticatedException();
		return authenticatedAccount;
	}

	public Account modifyBirthDate(Date birthDate)
	{
		return null;
	}

	public Account modifyMailAddress(String mailAddress)
	{
		return null;
	}

	public void modifyPassword(String newPassword)
	{
	}

	public void addTag(Tag tag)
	{
	}

	public void modifyTag(Tag tag)
	{
	}

	public void removeTag(Tag tag)
	{
	}

	public Profile createProfile(String profileName)
	{
		return null;
	}

	public Profile addTagToProfile(Profile profile, Tag tag)
	{
		return null;
	}

	public Profile removeTagFromProfile(Profile profile, Tag tag)
	{
		return null;
	}

	public Profile removeAllFromProfile(Profile profile)
	{
		return null;
	}

	public Profile replaceTagListOfProfile(Profile profile, List<Tag> tagList)
	{
		return null;
	}

	public Profile replaceTagListOfProfile(Profile profile, Tag[] tagList)
	{
		return null;
	}

	public Profile getProfile(String profileName)
	{
		return null;
	}
	
	public NetworkServiceEmulator getInstance()
	{
		return emulator;
	}
}
