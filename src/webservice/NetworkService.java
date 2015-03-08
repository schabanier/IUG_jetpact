  package webservice;

import java.net.*;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import data.Account;
import data.Profile;
import data.Tag;
import exceptions.AccountNotFoundException;
import exceptions.IllegalFieldException;
import exceptions.NetworkServiceException;
import exceptions.NotAuthenticatedException;
import interfaces.NetworkServiceInterface;

public class NetworkService implements NetworkServiceInterface {

	private static String adressHost;
	private static Account userAccount;
	private static String userPassword;
	private static ArrayList<Exception> exceptionTable;
	
	public NetworkService() {
		
	}
	
	@Override
	public void initNetworkService() throws NetworkServiceException {
		adressHost = "92.222.33.38:8080/app_server/ns";
		
		/* S'il y a une erreur côté serveur et base de donnée, le json renvoyé contiendra une key "IntError", 
		 * qui est l'indice dans ExceptionTable de l'exception concernée. 
		 * 
		 * Cette partie requiert d'être mise à jour à chaque modification des Exceptions côté Serveur
		 */
		NetworkServiceException e0 = new NetworkServiceException("Wrong combination pseudo/password");
		NetworkServiceException e1 = new NetworkServiceException("Error with database");
		NetworkServiceException e2 = new NetworkServiceException("You are already registered");
		NetworkServiceException e3 = new NetworkServiceException("Special Characters are not allowed in Pseudo and Password");
		
		exceptionTable.set(0, e0 );
		exceptionTable.set(1, e1 );
		exceptionTable.set(2, e2 );
		exceptionTable.set(3, e3 );

	}

	@Override
	public void createAccount(Account newAccount, String newPassword)
			throws IllegalFieldException, NetworkServiceException {
		
		/*  adapter au serveur choisi */
		URL registerURL;
		try {
			registerURL = new URL("http://"+adressHost+"/doregister?pseudo="+newAccount.getPseudo()+"&password="+newPassword+"&first_name="+newAccount.getFirstName()+"&last_name="+newAccount.getLastName()+"&email="+newAccount.getEMailAddress());
		// http://localhost:8080/app_server/ns/doregister?pseudo=pqrs&password=abc&first_name=xyz&last_name=cdf&email=hij
		
			String reponse = HTTPLoader.getTextFile(registerURL);
			JSONObject obj = (JSONObject) JSONValue.parse(reponse);
		
			if (!(boolean) obj.get("status")){
				try {
					throw exceptionTable.get((int) obj.get("IntError"));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					System.out.println("JSONException");
				}
			}
				

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public Account authenticate(String pseudo, String password)
			throws AccountNotFoundException, NetworkServiceException {

		URL loginURL;
		try {
			// http://localhost:8080/app_server/ns/dologin?pseudo=abc&password=xyz
			loginURL = new URL("http://"+adressHost+"/dologin?pseudo="+pseudo+"&password="+password);
			String reponse = HTTPLoader.getTextFile(loginURL);
			JSONObject obj = (JSONObject) JSONValue.parse(reponse);
			if ((boolean) obj.get("status")) {
				userAccount = new Account((String)obj.get("pseudo"), (String)obj.get("first_name"), (String)obj.get("last_name"), (String)obj.get("email"));
				userPassword = password;
			}else{
				try {
					throw exceptionTable.get((int) obj.get("IntError"));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					System.out.println("JSONException");
				}
			}
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return userAccount;
	}

	@Override
	public void logOut() {
		// TODO Auto-generated method stub

	}

	@Override
	public Account getCurrentAccount() throws NotAuthenticatedException {
		URL loginURL;
		try {
			// http://localhost:8080/app_server/ns/dologin?pseudo=abc&password=xyz
			loginURL = new URL("http://"+adressHost+"/dologin?pseudo="+userAccount.getPseudo()+"&password="+userPassword);
			String reponse = HTTPLoader.getTextFile(loginURL);
			JSONObject obj = (JSONObject) JSONValue.parse(reponse);
			if ((boolean) obj.get("status")) {
				userAccount = new Account((String)obj.get("pseudo"), (String)obj.get("first_name"), (String)obj.get("last_name"), (String)obj.get("email"));
			}else{
				try {
					throw exceptionTable.get((int) obj.get("IntError"));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					System.out.println("JSONException");
				}
			}
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return userAccount;
	}

	//return = argument ?? -> différencier modifyTag et addTag
	@Override
	public Tag addTag(Tag tag) {

		URL addTagURL;
		try {
			// http://localhost/<appln-folder-name>/tag/addtag?pseudo=abc&password=abc&object_name=xyz&picture=url				
			addTagURL = new URL("http://"+adressHost+"/tag/addtag?e="+userAccount.getPseudo()+"&password="+userPassword+"&object_name="+tag.getObjectName()+"&picture="+tag.getObjectImageName());
			String reponse = HTTPLoader.getTextFile(addTagURL);
			JSONObject obj = (JSONObject) JSONValue.parse(reponse);

			if (!(boolean) obj.get("status")){
				try {
					throw exceptionTable.get((int) obj.get("IntError"));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					System.out.println("JSONException");
				}
			}
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return tag;				
	}

	@Override
	public void removeTag(Tag tag) {
		URL deleteTagURL;
		try {
			// http://localhost/<appln-folder-name>/tag/deletetag?pseudo=abc&password=abc&object_name=xyz
			deleteTagURL = new URL("http://"+adressHost+"/tag/deletetag?"+userAccount.getPseudo()+"&password="+userPassword+"&object_name="+tag.getObjectName());
			String reponse = HTTPLoader.getTextFile(deleteTagURL);
			JSONObject obj = (JSONObject) JSONValue.parse(reponse);

			if (!(boolean) obj.get("status")){
				try {
					throw exceptionTable.get((int) obj.get("IntError"));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					System.out.println("JSONException");
				}
			}
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}			
	}

	@Override
	public Profile createProfile(String profileName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Profile addTagToProfile(Profile profile, Tag tag) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Profile removeTagFromProfile(Profile profile, Tag tag) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Profile removeAllFromProfile(Profile profile) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Profile replaceTagListOfProfile(Profile profile, List<Tag> tagList) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Profile replaceTagListOfProfile(Profile profile, Tag[] tagList) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Profile getProfile(String profileName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Tag> getTags() throws NotAuthenticatedException,
			NetworkServiceException {
		
		URL getTagsURL;
		List<Tag> ListTag = new ArrayList<Tag>();

		try {
			// http://localhost/<appln-folder-name>/tag/retrievetags?pseudo=abc&password=abc			
			getTagsURL = new URL("http://"+adressHost+"/tag/retrievetags?"+userAccount.getPseudo()+"&password="+userPassword);
			String reponse = HTTPLoader.getTextFile(getTagsURL);
			JSONObject obj = (JSONObject) JSONValue.parse(reponse);
			
			if ((boolean) obj.get("status")) {
				JSONArray arrayOfJsonTags = (JSONArray) obj.get("arrayOfJsonTags");
				int n = arrayOfJsonTags.size();
				for(int i=0; i<n; i++){
					JSONObject tagJson = new JSONObject();
					tagJson = (JSONObject) arrayOfJsonTags.get(i);
					Tag tag = new Tag((String)tagJson.get("tagID"), (String)tagJson.get("nameTag"), (String)tagJson.get("picture"));
					ListTag.add(tag);	
				}
			}else{
				try {
					throw exceptionTable.get((int) obj.get("IntError"));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					System.out.println("JSONException");
				}
			}
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}			
		
		return ListTag;
	}

	@Override
	public Tag modifyObjectName(Tag tag, String newObjectName)
			throws NotAuthenticatedException, IllegalFieldException,
			NetworkServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Tag modifyObjectImage(Tag tag, String newImageFileName)
			throws NotAuthenticatedException, IllegalFieldException,
			NetworkServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Profile addTagsToProfile(Profile profile, List<Tag> tags)
			throws NotAuthenticatedException, IllegalFieldException,
			NetworkServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Profile> getProfiles() throws NotAuthenticatedException,
			NetworkServiceException {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public void modifyEMailAddress(String newEMailAddress) throws NotAuthenticatedException, IllegalFieldException, NetworkServiceException {			
		URL modifyEMailAddressURL;
		try {
			// http://localhost:8080/app_server/modifyEMailAddress?pseudo=abc&password=xyz&newEMailAdress=abc
			modifyEMailAddressURL = new URL("http://"+adressHost+"/modifyEMailAddress?pseudo="+userAccount.getPseudo()+"&password="+userPassword+"&newEMailAddress="+newEMailAddress);
			String reponse = HTTPLoader.getTextFile(modifyEMailAddressURL);
			JSONObject obj = (JSONObject) JSONValue.parse(reponse);
			if ((boolean) obj.get("status")) {
				userAccount.setMailAddress(newEMailAddress);
			} else {
				try {
					throw exceptionTable.get((int) obj.get("IntError"));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					System.out.println("JSONException");
				}
			}
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	

	@Override
	public void modifyPassword(String newPassword) throws NotAuthenticatedException, IllegalFieldException, NetworkServiceException {			
		URL modifyPasswordURL;
		try {
			// http://localhost:8080/app_server/modifyPassword?pseudo=abc&password=xyz&newPassword=abc
			modifyPasswordURL = new URL("http://"+adressHost+"/modifyPassword?pseudo="+userAccount.getPseudo()+"&password="+userPassword+"&newPassword="+newPassword);
			String reponse = HTTPLoader.getTextFile(modifyPasswordURL);
			JSONObject obj = (JSONObject) JSONValue.parse(reponse);
			if ((boolean) obj.get("status")) {
				userPassword = newPassword;
			} else {
				try {
					throw exceptionTable.get((int) obj.get("IntError"));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					System.out.println("JSONException");
				}
			}
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
}
	

