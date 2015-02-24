  package webservice;

import java.net.*;
import java.util.Date;
import java.util.List;

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
	private static String user_pseudo;
	private static String user_password;
	
	public NetworkService() {
		
	}
	
	@Override
	public void initNetworkService() throws NetworkServiceException {
		/* variables à initialiser (paramètres serveur...*/
		adressHost = "local:8080"; //pour tests : localhost:xxxx (pour Henri 8080)
		
	}

	@Override
	public void createAccount(Account newAccount, String newPassword)
			throws IllegalFieldException, NetworkServiceException {
		
		/*  adapter au serveur choisi */
		URL registerURL;
		try {
			registerURL = new URL("http://"+adressHost+"/ns/doregister?pseudo="+newAccount.getPseudo()+"&password="+newPassword+"&first_name="+newAccount.getFirstName()+"&last_name="+newAccount.getLastName()+"&email="+newAccount.getMailAddress());
		// http://localhost:8080/app_server/ns/doregister?pseudo=pqrs&password=abc&first_name=xyz&last_name=cdf&email=hij
		
		String reponse = HTTPLoader.getTextFile(registerURL);

		JSONObject obj = (JSONObject) JSONValue.parse(reponse);
		
		System.out.println("tag : " + obj.get("tag"));
		System.out.println("status : " + obj.get("status"));
		System.out.println("error_msg : " + obj.get("error_msg"));
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public Account authenticate(String pseudo, String password)
			throws AccountNotFoundException, NetworkServiceException, MalformedURLException {

		URL loginURL = new URL("http://"+adressHost+"/ns/dologin?e="+pseudo+"&password="+password);
// http://localhost:8080/app_server/ns/dologin?pseudo=abc&password=xyz
		
		String reponse = HTTPLoader.getTextFile(loginURL);
		JSONObject obj = (JSONObject) JSONValue.parse(reponse);
		
		if ((boolean) obj.get("status")) {
			user_pseudo = pseudo;
			user_password = password;
		}
		System.out.println("tag : " + obj.get("tag"));
		System.out.println("status : " + obj.get("status"));
		System.out.println("pseudo : " + obj.get("pseudo"));
		System.out.println("first_name : " + obj.get("first_name"));
		System.out.println("last_name : " + obj.get("last_name"));
		System.out.println("email : " + obj.get("email"));
		
		System.out.println("error_msg : " + obj.get("error_msg"));
		
		return null;
	}

	@Override
	public void logOut() {
		// TODO Auto-generated method stub

	}

	@Override
	public Account getCurrentAccount() throws NotAuthenticatedException {
		// TODO Auto-generated method stub
		//id authenticate
		return null;
	}

	@Override
	public Account modifyBirthDate(Date birthDate) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Account modifyMailAddress(String mailAddress) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void modifyPassword(String newPassword) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addTag(Tag tag) {

		URL addTagURL;
		try {
			// http://localhost/<appln-folder-name>/tag/addtag?pseudo=abc&password=abc&object_name=xyz&picture=url				
			addTagURL = new URL("http://"+adressHost+"/tag/addtag?e="+user_pseudo+"&password="+user_password+"&object_name="+tag.getObjectName()+"&picture="+tag.getObjectImage());
			String reponse = HTTPLoader.getTextFile(addTagURL);
			JSONObject obj = (JSONObject) JSONValue.parse(reponse);
			System.out.println("tag : " + obj.get("tag"));
			System.out.println("status : " + obj.get("status"));
			System.out.println("error_msg : " + obj.get("error_msg"));
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}				
	}

	@Override
	public void modifyTag(Tag tag) {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeTag(Tag tag) {
		URL deleteTagURL;
		try {
			// http://localhost/<appln-folder-name>/tag/deletetag?pseudo=abc&password=abc&object_name=xyz
			deleteTagURL = new URL("http://"+adressHost+"/tag/deletetag?"+user_pseudo+"&password="+user_password+"&object_name="+tag.getObjectName());
			String reponse = HTTPLoader.getTextFile(deleteTagURL);
			JSONObject obj = (JSONObject) JSONValue.parse(reponse);
			System.out.println("tag : " + obj.get("tag"));
			System.out.println("status : " + obj.get("status"));
			System.out.println("error_msg : " + obj.get("error_msg"));
			
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

}

