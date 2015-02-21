package com.app_client.implementation;

import java.util.List;

import org.apache.http.Header;

import com.app_client.data.Account;
import com.app_client.data.Profile;
import com.app_client.data.Tag;

import com.app_client.exceptions.AccountNotFoundException;
import com.app_client.exceptions.IllegalFieldException;
import com.app_client.exceptions.NetworkServiceException;
import com.app_client.exceptions.NotAuthenticatedException;
import com.app_client.interfaces.NetworkServiceInterface;

import com.loopj.android.http.*;

public class NetworkService implements NetworkServiceInterface {
	
	private static AsyncHttpClient client;
	
	@Override
	public void initNetworkService() throws NetworkServiceException 
	{
		NetworkService.client = new AsyncHttpClient();
	}

	@Override
	public void createAccount(Account newAccount, String newPassword)
			throws IllegalFieldException, NetworkServiceException 
	{
		RequestParams params = new RequestParams();
		params.put("pseudo", newAccount.getPseudo());
		params.put("password", newPassword);
		params.put("first_name", newAccount.getFirstName());
		params.put("last_name", newAccount.getLastName());
		params.put("email", newAccount.getEMailAddress());
				
		client.get("http://www.localhost:9999/dologin/login/", params, new AsyncHttpResponseHandler() {	

		    @Override
		    public void onSuccess(int statusCode, Header[] headers, byte[] response) {
		        // called when response HTTP status is "200 OK"
		    }

		    @Override
		    public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
		        // called when response HTTP status is "4XX" (eg. 401, 403, 404)
		    }
		    			
		});	
	}

	@Override
	public Account authenticate(String pseudo, String password)
			throws AccountNotFoundException, NetworkServiceException {
		// TODO Auto-generated method stub
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
	public void modifyEMailAddress(String emailAddress)
			throws NotAuthenticatedException, IllegalFieldException,
			NetworkServiceException {
		// TODO Auto-generated method stub

	}

	@Override
	public void modifyPassword(String newPassword)
			throws NotAuthenticatedException, IllegalFieldException,
			NetworkServiceException {
		// TODO Auto-generated method stub

	}

	@Override
	public List<Tag> getTags() throws NotAuthenticatedException,
			NetworkServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Tag addTag(Tag tag) throws NotAuthenticatedException,
			IllegalFieldException, NetworkServiceException {
		// TODO Auto-generated method stub
		return null;
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
	public void removeTag(Tag tag) throws NotAuthenticatedException,
			IllegalFieldException, NetworkServiceException {
		// TODO Auto-generated method stub

	}

	@Override
	public Profile createProfile(String profileName)
			throws NotAuthenticatedException, IllegalFieldException,
			NetworkServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Profile addTagToProfile(Profile profile, Tag tag)
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
	public Profile removeTagFromProfile(Profile profile, Tag tag)
			throws NotAuthenticatedException, IllegalFieldException,
			NetworkServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Profile removeAllFromProfile(Profile profile)
			throws NotAuthenticatedException, IllegalFieldException,
			NetworkServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Profile replaceTagListOfProfile(Profile profile, List<Tag> tagList)
			throws NotAuthenticatedException, IllegalFieldException,
			NetworkServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Profile replaceTagListOfProfile(Profile profile, Tag[] tagList)
			throws NotAuthenticatedException, IllegalFieldException,
			NetworkServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Profile getProfile(String profileName)
			throws NotAuthenticatedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Profile> getProfiles() throws NotAuthenticatedException,
			NetworkServiceException {
		// TODO Auto-generated method stub
		return null;
	}

}
