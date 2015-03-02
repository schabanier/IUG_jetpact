package com.app_client.implementation;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.app_client.data.Account;
import com.app_client.data.Profile;
import com.app_client.data.Tag;
import com.app_client.engine.FieldVerifier;
import com.app_client.exceptions.AccountNotFoundException;
import com.app_client.exceptions.IllegalFieldException;
import com.app_client.exceptions.NetworkServiceException;
import com.app_client.exceptions.NotAuthenticatedException;
import com.app_client.interfaces.NetworkServiceInterface;
import com.loopj.android.http.*;

public class NetworkService implements NetworkServiceInterface {
	
	private static AsyncHttpClient client;
	private Account currentAccount = null;
	private String currentPassword = null;
	@Override
	public void initNetworkService() throws NetworkServiceException 
	{
		NetworkService.client = new AsyncHttpClient();
	}

	@Override
	public void createAccount(Account newAccount, String newPassword)
			throws IllegalFieldException, NetworkServiceException 
	{	// We first check the validity of the arguments to create the parameters
		RequestParams params = new RequestParams();
		if (FieldVerifier.verifyName(newAccount.getPseudo())){
			params.put("pseudo", newAccount.getPseudo());}
		else { throw new IllegalFieldException(IllegalFieldException.PSEUDO,IllegalFieldException.REASON_VALUE_INCORRECT, newAccount.getPseudo());}
		
		if (FieldVerifier.verifyPassword(newPassword)){
			params.put("password", newPassword);}
		else { throw new IllegalFieldException(IllegalFieldException.PASSWORD,IllegalFieldException.REASON_VALUE_INCORRECT, newPassword);}
		
		if (FieldVerifier.verifyName(newAccount.getFirstName())){
			params.put("first_name", newAccount.getFirstName());}
		else { throw new IllegalFieldException(IllegalFieldException.FIRSTNAME,IllegalFieldException.REASON_VALUE_INCORRECT, newAccount.getFirstName());}
		
		if (FieldVerifier.verifyName(newAccount.getLastName())){
			params.put("last_name", newAccount.getLastName());}
		else { throw new IllegalFieldException(IllegalFieldException.LASTNAME,IllegalFieldException.REASON_VALUE_INCORRECT, newAccount.getLastName());}
		
		if (FieldVerifier.verifyEMailAddress(newAccount.getEMailAddress())){
			params.put("email", newAccount.getEMailAddress());}
		else { throw new IllegalFieldException(IllegalFieldException.EMAIL_ADDRESS,IllegalFieldException.REASON_VALUE_INCORRECT, newAccount.getEMailAddress());}
		
				
		client.get("http://www.localhost:8080/ns/doregister", params, new AsyncHttpResponseHandler() {	

			// When the response returned by REST has Http response code '200'
            @Override
            public void onSuccess(String response) {
                try {
                         // JSON Object
                        JSONObject obj = new JSONObject(response);
                        // When the JSON response has status boolean value assigned with true
                        if(obj.getBoolean("status")){
                            //
                        } 
                        // Else display error message
                        else{
                           //
                        }
                } catch (JSONException e) {
                    // "Error Occured [Server's JSON response might be invalid]!"
                    e.printStackTrace();

                }
            }

		 // When the response returned by REST has Http response code other than '200'
            @Override
            public void onFailure(int statusCode, Throwable error,
                String content) {             
                // When Http response code is '404'
                if(statusCode == 404){
                  try {
					throw new NetworkServiceException("Requested resource not found");
				} catch (NetworkServiceException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                } 
                // When Http response code is '500'
                else if(statusCode == 500){
                   try {
					throw new NetworkServiceException("Something went wrong at server end");
				} catch (NetworkServiceException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                } 
                // When Http response code other than 404, 500
                else{
                 try {
					throw new NetworkServiceException("Unexpected Error occcured! [Most common Error: Device might not be connected to Internet or remote server is not up and running]");
				} catch (NetworkServiceException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                }
            }
		    			
		});	
	}
	
	
	@Override
	public Account authenticate(String pseudo, String password)
		throws AccountNotFoundException, NetworkServiceException 
			{
				// We first check the validity of the arguments to create the parameters
					RequestParams params = new RequestParams();
					if (FieldVerifier.verifyName(pseudo)){
						params.put("pseudo", pseudo);}
					else { throw new IllegalFieldException(IllegalFieldException.PSEUDO, IllegalFieldException.REASON_VALUE_INCORRECT, pseudo);}
					
					if (FieldVerifier.verifyPassword(password))
						{
							params.put("password",password);
							currentPassword = password;
						}
					else { throw new IllegalFieldException(IllegalFieldException.PASSWORD,IllegalFieldException.REASON_VALUE_INCORRECT, password);}
					
					
					client.get("http://www.localhost:8080/ns/dologin", params, new AsyncHttpResponseHandler() {	

						// When the response returned by REST has Http response code '200'
			            @Override
			            public void onSuccess(String response) {
			                try {
			                         // JSON Object
			                        JSONObject obj = new JSONObject(response);
			                        // When the JSON response has status boolean value assigned with true
			                        if(obj.getBoolean("status")){
			                            currentAccount = new Account(obj.getString("pseudo"),obj.getString("fist_name"),obj.getString("last_name"),obj.getString("email"));	
			                            
			                        } 
			                        // Else display error message
			                        else{
			                        		currentPassword = null;
			                        	}
			                } catch (JSONException e) {
			                    // TODO Auto-generated catch block
			                    // "Error Occured [Server's JSON response might be invalid]!"
			                    e.printStackTrace();

			                }				              
			            }

					 // When the response returned by REST has Http response code other than '200'
			            @Override
			            public void onFailure(int statusCode, Throwable error,
			                String content) {             
			                // When Http response code is '404'
			                if(statusCode == 404){
			                  try {
								throw new NetworkServiceException("Requested resource not found");
							} catch (NetworkServiceException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
			                } 
			                // When Http response code is '500'
			                else if(statusCode == 500){
			                   try {
								throw new NetworkServiceException("Something went wrong at server end");
							} catch (NetworkServiceException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
			                } 
			                // When Http response code other than 404, 500
			                else{
			                 try {
								throw new NetworkServiceException("Unexpected Error occcured! [Most common Error: Device might not be connected to Internet or remote server is not up and running]");
							} catch (NetworkServiceException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
			                }
			            }
					    			
					});	
					
		return currentAccount;
	}

	@Override
	public void logOut() {
		// TODO Auto-generated method stub

	}

	@Override
	public Account getCurrentAccount() throws NotAuthenticatedException {
		// TODO Auto-generated method stub
		// if account = null on lève une exception sinon on renvoie account.
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
