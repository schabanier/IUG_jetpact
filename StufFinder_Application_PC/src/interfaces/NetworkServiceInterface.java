package interfaces;

import java.util.Date;
import java.util.List;

import data.Account;
import data.Profile;
import data.Tag;

/**
 * This interface makes the link between the network service (in this case, this is the client web service) and the engine of the application.
 * It defines all the needed methods to use this service.
 * @author Nicolas Thierce
 *
 */
public interface NetworkServiceInterface
{
	/**
	 * Initialize this service to rend it available for network communications.
	 */
	public void initNetworkService();
	
	/**
	 * Creates a new account on server.
	 * @param newAccount the account to be created.
	 */
	public void createAccount(Account newAccount);
	
	/**
	 * authenticate 
	 * @param pseudo
	 * @param password
	 * @return the account matching with the login/password
	 */
	public Account authenticate(String login, String password);
	
	public void logOut();
	
	/**
	 * to get the current accohnt of the session.
	 * @return the current account if the authentication is done, null otherwise.
	 */
	public Account getCurrentAccount();

	/**
	 * Modify the field birthDate of the current Account.
	 * @param birthDate the new birth date.
	 * @return the account modified (maybe the same object).
	 */
	public Account modifyBirthDate(Date birthDate);
	
	/**
	 * Modify the e-mail of the current account.
	 * @param mailAddress the new e-mail.
	 * @return the account modified (maybe the same object).
	 */
	public Account modifyMailAddress(String mailAddress);
	
	/**
	 * modify the password of the current account.
	 * @param newPassword
	 */
	public void modifyPassword(String newPassword);
	
	/**
	 * Add a tag to the current account. 
	 * @param tag
	 */
	public void addTag(Tag tag);
	
	/**
	 * Modify the tag which has the uid contained in the object tag.
	 * @param tag
	 */
	public void modifyTag(Tag tag);
	
	/**
	 * 
	 * @param tag
	 */
	public void removeTag(Tag tag);
	
	
	/**
	 * 
	 * @param profileName
	 * @return
	 */
	public Profile createProfile(String profileName);
	
	
	/**
	 * @param profile
	 * @param tag
	 * @return
	 */
	public Profile addTagToProfile(Profile profile, Tag tag);
	
	/**
	 * @param profile
	 * @param tag
	 * @return
	 */
	public Profile removeTagFromProfile(Profile profile, Tag tag);
	
	/**
	 * @param profile
	 * @return
	 */
	public Profile removeAllFromProfile(Profile profile);
	
	/**
	 * @param profile
	 * @param tagList
	 * @return
	 */
	public Profile replaceTagListOfProfile(Profile profile, List<Tag> tagList);
	
	/**
	 * @param profil
	 * @param tagList
	 * @return
	 */
	public Profile replaceTagListOfProfile(Profile profile, Tag[] tagList);
	
	/**
	 * @param profilName
	 * @return
	 */
	public Profile getProfile(String profileName);
}