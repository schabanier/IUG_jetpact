package webservice;

import java.net.URL;
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

	@Override
	public void initNetworkService() throws NetworkServiceException {
		/* variables à initialiser (paramètres serveur...*/

	}

	@Override
	public void createAccount(Account newAccount, String newPassword)
			throws IllegalFieldException, NetworkServiceException {
		
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

	@Override
	public Account authenticate(String pseudo, String password)
			throws AccountNotFoundException, NetworkServiceException {

		String adressHost;
		//voir avec Philippe construction URL//
		URL loginURL = new URL("http://"+adressHost+"/doregister?name="+name+"&username="+username+"&password="+password);
		
		String reponse = HTTPLoader.getTextFile(loginURL);

		JSONObject obj = (JSONObject) JSONValue.parse(reponse);
		
		//voir avec philippe retour serveur
		System.out.println("tag : " + obj.get("tag"));
		System.out.println("status : " + obj.get("status"));
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
		// TODO Auto-generated method stub

	}

	@Override
	public void modifyTag(Tag tag) {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeTag(Tag tag) {
		// TODO Auto-generated method stub

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

