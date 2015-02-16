package tests;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import data.Account;
import data.Profile;
import data.Tag;
import engine.FieldVerifier;
import exceptions.AccountNotFoundException;
import exceptions.IllegalFieldException;
import exceptions.NetworkServiceException;
import exceptions.NotAuthenticatedException;
import exceptions.TagAlreadyUsedException;
import exceptions.TagNotFoundException;
import interfaces.NetworkServiceInterface;

public class NetworkServiceEmulator implements NetworkServiceInterface
{
	private List<Account> accounts;
	private List<String> passwords;
	private Set<Tag> tags;
	
	private Account authenticatedAccount;
	
	private static NetworkServiceEmulator emulator = new NetworkServiceEmulator();
	private int authenticatedAccountIndex;
	
	private NetworkServiceEmulator()
	{
		accounts = new ArrayList<>();
		passwords = new ArrayList<>();
		tags = new HashSet<>();
		
		authenticatedAccount = null;
		
		
		Account testAccount = new Account("jdupon", "Jean", "Dupont", "jean.dupont@gmail.com");

		Tag tag1 = new Tag("a1bef83a374", "Home keys");
		Tag tag2 = new Tag("bd3496e342c", "Car key");
		Tag tag3 = new Tag("aefd573fc3e", "Bag");
		Tag tag4 = new Tag("f53ebc87298", "Wallet");
		Tag tag5 = new Tag("e68fa3547cb", "Android tablet");
		
		tags.add(tag1);
		tags.add(tag2);
		tags.add(tag3);
		tags.add(tag4);
		tags.add(tag5);

		testAccount.getTags().add(tag1);
		testAccount.getTags().add(tag2);
		testAccount.getTags().add(tag3);
		testAccount.getTags().add(tag4);
		testAccount.getTags().add(tag5);
		
		
		accounts.add(testAccount);
		passwords.add("123456");
	}
	
	public void initNetworkService() throws NetworkServiceException
	{
		
	}

	public void createAccount(Account newAccount, String newPassword) throws IllegalFieldException, NetworkServiceException
	{
		if(newPassword == null)
			throw new IllegalArgumentException("parameter newPassword can't be null.");
		
		if(! FieldVerifier.verifyPassword(newPassword))
			throw new IllegalFieldException(IllegalFieldException.PASSWORD, "Password must contain 6 characters or more.");
		
		if(! FieldVerifier.verifyEMailAddress(newAccount.getEMailAddress()))
			throw new IllegalFieldException(IllegalFieldException.EMAIL_ADDRESS, "Email is incorrect");
		
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
					authenticatedAccountIndex = i;
					
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


	public void modifyEMailAddress(String emailAddress) throws NotAuthenticatedException, IllegalFieldException, NetworkServiceException
	{
		if(authenticatedAccount == null)
			throw new NotAuthenticatedException();
		
		if(! FieldVerifier.verifyEMailAddress(emailAddress))
			throw new IllegalFieldException(IllegalFieldException.EMAIL_ADDRESS, "Email is incorrect");
		
		authenticatedAccount.setMailAddress(emailAddress);
	}

	public void modifyPassword(String newPassword) throws NotAuthenticatedException, IllegalFieldException, NetworkServiceException
	{
		if(authenticatedAccount == null)
			throw new NotAuthenticatedException();

		if(! FieldVerifier.verifyPassword(newPassword))
			throw new IllegalFieldException(IllegalFieldException.PASSWORD, "Password must contain 6 characters or more.");
		
		passwords.set(authenticatedAccountIndex, newPassword);
	}
	
	

	public Tag addTag(Tag tag) throws NotAuthenticatedException, IllegalFieldException, TagAlreadyUsedException, NetworkServiceException
	{
		if(authenticatedAccount == null)
			throw new NotAuthenticatedException();
		
		if(! FieldVerifier.verifyTagUID(tag.getUid()))
			throw new IllegalFieldException(IllegalFieldException.TAG_UID, "Tag uid is incorrect");
		
		if(! FieldVerifier.verifyTagName(tag.getObjectName()))
			throw new IllegalFieldException(IllegalFieldException.TAG_OBJECT_NAME, "Tag name is incorrect");
		
		if(tag.getObjectImageName() != null && FieldVerifier.verifyImageFileName(tag.getObjectImageName()) == false)
			throw new IllegalFieldException(IllegalFieldException.TAG_OBJECT_IMAGE, "Image filename is incorrect");
		
		for(Tag tmp : authenticatedAccount.getTags())
			if(tmp.getObjectName().equals(tag.getObjectName()))
				throw new IllegalFieldException(IllegalFieldException.TAG_OBJECT_NAME, IllegalFieldException.REASON_VALUE_ALREADY_USED, "this name is already used for another tag.");

		if(! tags.add(tag)) // because the method add() returns true if this operation is successful, false if there is already a tag with the specifed tag UID. 
			throw new IllegalFieldException(IllegalFieldException.TAG_UID, IllegalFieldException.REASON_VALUE_ALREADY_USED, "this name is already used for another tag.");
		
		authenticatedAccount.getTags().add(tag);
		return tag;
		
	}

	public Tag modifyObjectName(Tag tag, String newObjectName) throws NotAuthenticatedException, IllegalFieldException, TagNotFoundException, NetworkServiceException
	{
		if(authenticatedAccount == null)
			throw new NotAuthenticatedException();
		

		if(! FieldVerifier.verifyTagUID(tag.getUid()))
			throw new IllegalFieldException(IllegalFieldException.TAG_UID, "Tag uid is incorrect");
		
		if(! FieldVerifier.verifyTagName(newObjectName))
			throw new IllegalFieldException(IllegalFieldException.TAG_OBJECT_NAME, "Tag name is incorrect");

		for(Tag tmp : authenticatedAccount.getTags())
			if(tmp.getObjectName().equals(newObjectName) && ! tmp.getUid().equals(tag.getUid()))
				throw new IllegalFieldException(IllegalFieldException.TAG_OBJECT_NAME, IllegalFieldException.REASON_VALUE_ALREADY_USED, "this name is already used for another tag.");
		
		
		int index = authenticatedAccount.getTags().indexOf(tag);
		
		if(index < 0)
			throw new TagNotFoundException(tag.getUid());
		else
		{
			authenticatedAccount.getTags().get(index).setObjectName(newObjectName);
			return authenticatedAccount.getTags().get(index);
		}
	}

	public Tag modifyObjectImage(Tag tag, String newImageFileName) throws NotAuthenticatedException, IllegalFieldException, TagNotFoundException, NetworkServiceException
	{
		if(authenticatedAccount == null)
			throw new NotAuthenticatedException();
		

		if(! FieldVerifier.verifyTagUID(tag.getUid()))
			throw new IllegalFieldException(IllegalFieldException.TAG_UID, "Tag uid is incorrect");

		if(tag.getObjectImageName() != null && FieldVerifier.verifyImageFileName(newImageFileName) == false)
			throw new IllegalFieldException(IllegalFieldException.TAG_OBJECT_IMAGE, "Image filename is incorrect");
		
		
		int index = authenticatedAccount.getTags().indexOf(tag);
		
		if(index < 0)
			throw new TagNotFoundException(tag.getUid());
		else
		{
			authenticatedAccount.getTags().get(index).setObjectImageName(newImageFileName);
			return authenticatedAccount.getTags().get(index);
		}
	}
	
	public void removeTag(Tag tag) throws NotAuthenticatedException, IllegalFieldException, TagNotFoundException, NetworkServiceException
	{
		if(authenticatedAccount == null)
			throw new NotAuthenticatedException();


		if(! FieldVerifier.verifyTagUID(tag.getUid()))
			throw new IllegalFieldException(IllegalFieldException.TAG_UID, "Tag uid is incorrect");
		

		int index = authenticatedAccount.getTags().indexOf(tag);
		
		if(index < 0)
			throw new TagNotFoundException(tag.getUid());
		else
		{
			authenticatedAccount.getTags().remove(index);
			tags.remove(tag);
		}
	}
	
	
	

	public Profile createProfile(String profileName) throws NotAuthenticatedException
	{
		if(authenticatedAccount == null)
			throw new NotAuthenticatedException();
		
		return null;
	}

	public Profile addTagToProfile(Profile profile, Tag tag) throws NotAuthenticatedException
	{
		if(authenticatedAccount == null)
			throw new NotAuthenticatedException();
		
		return null;
	}

	public Profile removeTagFromProfile(Profile profile, Tag tag) throws NotAuthenticatedException
	{
		if(authenticatedAccount == null)
			throw new NotAuthenticatedException();
		
		return null;
	}

	public Profile removeAllFromProfile(Profile profile) throws NotAuthenticatedException
	{
		if(authenticatedAccount == null)
			throw new NotAuthenticatedException();
		
		return null;
	}

	public Profile replaceTagListOfProfile(Profile profile, List<Tag> tagList) throws NotAuthenticatedException
	{
		if(authenticatedAccount == null)
			throw new NotAuthenticatedException();
		
		return null;
	}

	public Profile replaceTagListOfProfile(Profile profile, Tag[] tagList) throws NotAuthenticatedException
	{
		if(authenticatedAccount == null)
			throw new NotAuthenticatedException();
		
		return null;
	}

	public Profile getProfile(String profileName) throws NotAuthenticatedException
	{
		if(authenticatedAccount == null)
			throw new NotAuthenticatedException();
		
		return null;
	}

	public static NetworkServiceEmulator getInstance()
	{
		return emulator;
	}
}
