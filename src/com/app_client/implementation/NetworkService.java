package com.app_client.implementation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;


import com.app_client.data.Account;
import com.app_client.data.Profile;
import com.app_client.data.Tag;
import com.app_client.engine.FieldVerifier;
import com.app_client.exceptions.AccountNotFoundException;
import com.app_client.exceptions.IllegalFieldException;
import com.app_client.exceptions.NetworkServiceException;
import com.app_client.exceptions.NotAuthenticatedException;
import com.app_client.interfaces.NetworkServiceInterface;

public class NetworkService implements NetworkServiceInterface {
	
	private static HttpClient client;
	private Account currentAccount = null;
	private String currentPassword = null;
	@Override
	public void initNetworkService() throws NetworkServiceException 
	{
		NetworkService.client = new DefaultHttpClient();
	}

	@Override
	public void createAccount(Account newAccount, String newPassword)
			throws IllegalFieldException, NetworkServiceException 
	{	// We first check the validity of the arguments to create the parameters		
		if (FieldVerifier.verifyName(newAccount.getPseudo())){}
		else { throw new IllegalFieldException(IllegalFieldException.PSEUDO,IllegalFieldException.REASON_VALUE_INCORRECT, newAccount.getPseudo());}
		
		if (FieldVerifier.verifyPassword(newPassword)){}
		else { throw new IllegalFieldException(IllegalFieldException.PASSWORD,IllegalFieldException.REASON_VALUE_INCORRECT, newPassword);}
		
		if (FieldVerifier.verifyName(newAccount.getFirstName())){}
		else { throw new IllegalFieldException(IllegalFieldException.FIRSTNAME,IllegalFieldException.REASON_VALUE_INCORRECT, newAccount.getFirstName());}
		
		if (FieldVerifier.verifyName(newAccount.getLastName())){}
		else { throw new IllegalFieldException(IllegalFieldException.LASTNAME,IllegalFieldException.REASON_VALUE_INCORRECT, newAccount.getLastName());}
		
		if (FieldVerifier.verifyEMailAddress(newAccount.getEMailAddress())){}
		else { throw new IllegalFieldException(IllegalFieldException.EMAIL_ADDRESS,IllegalFieldException.REASON_VALUE_INCORRECT, newAccount.getEMailAddress());}
		
		 InputStream inputStream = null;
         String result = "";
         try {                	
             // make GET request to the given URL
             HttpResponse httpResponse = client.execute(new HttpGet("http://92.222.33.38:8080/app_server/ns/register?pseudo="+ newAccount.getPseudo() +"&password="+ newPassword +"&first_name="+ newAccount.getFirstName() + "&last_name="+ newAccount.getLastName() +"&email=" +newAccount.getEMailAddress()));
             StatusLine statusLine = httpResponse.getStatusLine();
             int statusCode = statusLine.getStatusCode();
             if (statusCode == 200) {
             	// receive response as inputStream
             	HttpEntity entity =  httpResponse.getEntity();
             	inputStream = entity.getContent();
		                // convert inputstream to string
		                if(inputStream != null)
		                    {result = convertInputStreamToString(inputStream);	                
			                try {
				                // creation JSON Object
				                JSONObject obj = new JSONObject(result);
				                int returnCode = obj.getInt("returncode");
					                if( returnCode == 0){
					                    currentAccount = new Account(obj.getString("pseudo"),obj.getString("first_name"),obj.getString("last_name"),obj.getString("email"));}
					            // Else display error message
					                else if(returnCode == 1){
					                    currentPassword = null;
					                    throw new IllegalFieldException(IllegalFieldException.PROFILE_NAME,IllegalFieldException.REASON_VALUE_ALREADY_USED);
					                }
					                else {
					                	currentPassword = null;
					                	throw new IllegalFieldException(IllegalFieldException.PROFILE_NAME,IllegalFieldException.REASON_VALUE_INCORRECT);
					                }
				            } catch (JSONException e) {
				                // TODO Auto-generated catch block
				                // "Error Occured [Server's JSON response might be invalid]!"
				                e.printStackTrace();
				            	                
			                } catch (Exception e) {
			                	e.printStackTrace();
				            }
		                    }
		                else {
		                	throw new NetworkServiceException("Connection issue with ther server, null imput stream");
		                }
             }
		                            
             // When Http response code is '404'
             else if(statusCode == 404){
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
         }catch(Exception e) {
         	// TODO Auto-generated catch block
				e.printStackTrace();
         }
                         				
	} 
	
	
	 @Override
	    public Account authenticate(String pseudo, String password)
	        throws AccountNotFoundException, NetworkServiceException 
	            {
	                // We first check the validity of the arguments to create the parameters
	                if (FieldVerifier.verifyName(pseudo)){}
	                else { throw new IllegalFieldException(IllegalFieldException.PSEUDO, IllegalFieldException.REASON_VALUE_INCORRECT, pseudo);}

	                if (FieldVerifier.verifyPassword(password)){currentPassword = password;}
	                else { throw new IllegalFieldException(IllegalFieldException.PASSWORD,IllegalFieldException.REASON_VALUE_INCORRECT, password);}	                
	                InputStream inputStream = null;
	                String result = "";
	                
	                try {                	
		                // make GET request to the given URL
		                HttpResponse httpResponse = client.execute(new HttpGet("http://92.222.33.38:8080/app_server/ns/login?pseudo="+ pseudo +"&password="+ password));
		                StatusLine statusLine = httpResponse.getStatusLine();
		                int statusCode = statusLine.getStatusCode();
		                if (statusCode == 200) {
		                	// receive response as inputStream
		                	HttpEntity entity =  httpResponse.getEntity();
		                	inputStream = entity.getContent();
				                // convert inputstream to string
				                if(inputStream != null)
				                    {result = convertInputStreamToString(inputStream);	                
					                try {
						                // creation JSON Object
						                JSONObject obj = new JSONObject(result);
						                int returnCode = obj.getInt("returncode");
							                if(returnCode == 0){
							                    currentAccount = new Account(obj.getString("pseudo"),obj.getString("first_name"),obj.getString("last_name"),obj.getString("email"));}
							            // Else display error message
							                else if(returnCode == 1){
							                    currentPassword = null;
							                    throw new AccountNotFoundException();
							                }
						            } catch (JSONException e) {
						                // TODO Auto-generated catch block
						                // "Error Occured [Server's JSON response might be invalid]!"
						                e.printStackTrace();
						            	                
					                } catch (Exception e) {
						                e.printStackTrace();
						            }
				                    }
				                else {
				                	throw new NetworkServiceException("Connection issue with ther server, null imput stream");
				                }
		                }
				               
		     	                
		                // When Http response code is '404'
		                else if(statusCode == 404){
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
	                }catch(Exception e) {
	                	// TODO Auto-generated catch block
						e.printStackTrace();
	                }
		                            	                	         
	        return currentAccount;
	    }
	 
	 // convert inputstream to String
	    private static String convertInputStreamToString(InputStream inputStream) throws IOException{
	        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
	        String line = "";
	        String result = "";
	        while((line = bufferedReader.readLine()) != null)
	            result += line;
	 
	        inputStream.close();
	        return result;	 
	    }
	   

	@Override
	public void logOut() {
		client = null;
	}

	@Override
	public Account getCurrentAccount() throws NotAuthenticatedException {
		if(currentAccount != null) {
			return currentAccount;
		}
		else {
			throw new NotAuthenticatedException();
		}
		
	}

	@Override
	public void modifyEMailAddress(String emailAddress)
			throws NotAuthenticatedException, IllegalFieldException,
			NetworkServiceException {
		  // We first check the validity of the arguments to create the parameters
        if (FieldVerifier.verifyEMailAddress(emailAddress)){}
        else { throw new IllegalFieldException(IllegalFieldException.EMAIL_ADDRESS, IllegalFieldException.REASON_VALUE_INCORRECT, emailAddress);}      
        InputStream inputStream = null;
        String result = "";
        try {                	
            // make GET request to the given URL
            HttpResponse httpResponse = client.execute(new HttpGet("http://92.222.33.38:8080/app_server/ns/modifyemail?pseudo="+  currentAccount.getPseudo() +"&password="+ currentPassword +"&new_email="+ emailAddress));
            StatusLine statusLine = httpResponse.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if (statusCode == 200) {
            	// receive response as inputStream
            	HttpEntity entity =  httpResponse.getEntity();
            	inputStream = entity.getContent();
	                // convert inputstream to string
	                if(inputStream != null)
	                    {result = convertInputStreamToString(inputStream);	                
		                try {
			                // creation JSON Object
			                JSONObject obj = new JSONObject(result);
			                int returnCode = obj.getInt("returncode");
				                if(returnCode == 0){
				                    currentAccount.setMailAddress(obj.getString("email"));}
				            // Else display error message
				                else if(returnCode ==1){
				                	throw new NetworkServiceException("Problem of access to the DB");
				                }
				                else {
				                	throw new NotAuthenticatedException();
				                }
			            } catch (JSONException e) {
			                // TODO Auto-generated catch block
			                // "Error Occured [Server's JSON response might be invalid]!"
			                e.printStackTrace();
			            	                
		                } catch (Exception e) {
			                e.printStackTrace();
			            }
	                    }
	                else {
	                	throw new NetworkServiceException("Connection issue with ther server, null imput stream");
	                }
            }
	               
 	                
            // When Http response code is '404'
            else if(statusCode == 404){
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
        }catch(Exception e) {
        	// TODO Auto-generated catch block
			e.printStackTrace();
        }
                        	
	}

	@Override
	public void modifyPassword(String newPassword)
			throws NotAuthenticatedException, IllegalFieldException,
			NetworkServiceException {
		  // We first check the validity of the arguments to create the parameters
        if (FieldVerifier.verifyPassword(newPassword)){}
        else { throw new IllegalFieldException(IllegalFieldException.PASSWORD, IllegalFieldException.REASON_VALUE_INCORRECT, newPassword);}      
        InputStream inputStream = null;
        String result = "";
        try {                	
            // make GET request to the given URL
            HttpResponse httpResponse = client.execute(new HttpGet("http://92.222.33.38:8080/app_server/ns/modifypassword?pseudo="+  currentAccount.getPseudo() +"&password="+ currentPassword +"&new_password="+ newPassword));
            StatusLine statusLine = httpResponse.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if (statusCode == 200) {
            	// receive response as inputStream
            	HttpEntity entity =  httpResponse.getEntity();
            	inputStream = entity.getContent();
	                // convert inputstream to string
	                if(inputStream != null)
	                    {result = convertInputStreamToString(inputStream);	                
		                try {
			                // creation JSON Object
			                JSONObject obj = new JSONObject(result);
			                int returnCode = obj.getInt("returncode");
				                if(returnCode == 0){
				                    currentPassword = newPassword;}
				            // Else display error message
				                else if(returnCode ==1){
				                	throw new NetworkServiceException("Problem of access to the DB");
				                }
				                else {
				                	throw new NotAuthenticatedException();
				                }
			            } catch (JSONException e) {
			                // TODO Auto-generated catch block
			                // "Error Occured [Server's JSON response might be invalid]!"
			                e.printStackTrace();
			            	                
		                } catch (Exception e) {
			                e.printStackTrace();
			            }
	                    }
	                else {
	                	throw new NetworkServiceException("Connection issue with ther server, null imput stream");
	                }
            }
	               
 	                
            // When Http response code is '404'
            else if(statusCode == 404){
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
        }catch(Exception e) {
        	// TODO Auto-generated catch block
			e.printStackTrace();
        }
                      

	}

	@Override
	public List<Tag> getTags() throws NotAuthenticatedException,
			NetworkServiceException {
		List<Tag> res = null;
		 // We first check the validity of the arguments to create the parameters
        if (FieldVerifier.verifyName(currentAccount.getPseudo())){}
        else { throw new IllegalFieldException(IllegalFieldException.PSEUDO, IllegalFieldException.REASON_VALUE_INCORRECT, currentAccount.getPseudo());}                
		InputStream inputStream = null;
        String result = "";
        try {                	
            // make GET request to the given URL
            HttpResponse httpResponse = client.execute(new HttpGet("http://92.222.33.38:8080/app_server/ns/retrievetag?pseudo="+  currentAccount.getPseudo() +"&password="+ currentPassword));
            StatusLine statusLine = httpResponse.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if (statusCode == 200) {
            	// receive response as inputStream
            	HttpEntity entity =  httpResponse.getEntity();
            	inputStream = entity.getContent();
	                // convert inputstream to string
	                if(inputStream != null)
	                    {result = convertInputStreamToString(inputStream);	                
		                try {
			                // creation JSON Object
			                JSONObject obj = new JSONObject(result);
			                int returnCode = obj.getInt("returncode");
				                if(returnCode == 0){
				                	org.json.JSONArray arrayOfJsonTag = new org.json.JSONArray(obj.getJSONArray("listTags"));
				                	for(int i =0; i< arrayOfJsonTag.length(); i++ ){
				                		JSONObject tagjson = arrayOfJsonTag.getJSONObject(i);
				                		Tag tag = new Tag(tagjson.getString( "tag_id" ), tagjson.getString( "object_name" ),tagjson.getString( "picture" ));
				                		res.add(tag);
				                	}
				                	}
				            // Else display error message
				               
				                else {
				                	throw new NotAuthenticatedException();
				                }
			            } catch (JSONException e) {
			                // TODO Auto-generated catch block
			                // "Error Occured [Server's JSON response might be invalid]!"
			                e.printStackTrace();
			            	                
		                } catch (Exception e) {
			                e.printStackTrace();
			            }
	                    }
	                else {
	                	throw new NetworkServiceException("Connection issue with ther server, null imput stream");
	                }
            }
	               
 	                
            // When Http response code is '404'
            else if(statusCode == 404){
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
        }catch(Exception e) {
        	// TODO Auto-generated catch block
			e.printStackTrace();
        }
                   
		return res;
	}

	@Override
	public Tag addTag(Tag tag) throws NotAuthenticatedException,
			IllegalFieldException, NetworkServiceException {		
		 // We first check the validity of the arguments to create the parameters
       if (FieldVerifier.verifyTagUID(tag.getUid())){}
       else { throw new IllegalFieldException(IllegalFieldException.TAG_UID, IllegalFieldException.REASON_VALUE_INCORRECT, tag.getUid());} 
       if (FieldVerifier.verifyTagUID(tag.getUid())){}
       else { throw new IllegalFieldException(IllegalFieldException.TAG_OBJECT_NAME, IllegalFieldException.REASON_VALUE_INCORRECT, tag.getObjectName());} 
		InputStream inputStream = null;
       String result = "";
       try {                	
           // make GET request to the given URL
           HttpResponse httpResponse = client.execute(new HttpGet("http://92.222.33.38:8080/app_server/ns/addtag?pseudo="+  currentAccount.getPseudo() +"&password="+ currentPassword +"&id="+ tag.getUid() +"&object_name="+ tag.getObjectName() + "&picture="+ tag.getObjectImageName()));
           StatusLine statusLine = httpResponse.getStatusLine();
           int statusCode = statusLine.getStatusCode();
           if (statusCode == 200) {
           	// receive response as inputStream
           	HttpEntity entity =  httpResponse.getEntity();
           	inputStream = entity.getContent();
	                // convert inputstream to string
	                if(inputStream != null)
	                    {result = convertInputStreamToString(inputStream);	                
		                try {
			                // creation JSON Object
			                JSONObject obj = new JSONObject(result);
			                int returnCode = obj.getInt("returncode");
				                if(returnCode == 0){}
				            // Else display error message
				               
				                else {
				                	throw new NetworkServiceException("Wrong pseudo/password combination or access to the DB");
				                }
			            } catch (JSONException e) {
			                // TODO Auto-generated catch block
			                // "Error Occured [Server's JSON response might be invalid]!"
			                e.printStackTrace();
			            	                
		                } catch (Exception e) {
			                e.printStackTrace();
			            }
	                    }
	                else {
	                	throw new NetworkServiceException("Connection issue with ther server, null imput stream");
	                }
           }
	               
	                
           // When Http response code is '404'
           else if(statusCode == 404){
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
       }catch(Exception e) {
       	// TODO Auto-generated catch block
			e.printStackTrace();
       }
                  
		return tag;
	}

	@Override
	public Tag modifyObjectName(Tag tag, String newObjectName)
			throws NotAuthenticatedException, IllegalFieldException,
			NetworkServiceException {
		  // We first check the validity of the arguments to create the parameters
        if (FieldVerifier.verifyName(newObjectName)){}
        else { throw new IllegalFieldException(IllegalFieldException.TAG_OBJECT_NAME, IllegalFieldException.REASON_VALUE_INCORRECT, newObjectName);}      
        InputStream inputStream = null;
        String result = "";
        try {                	
            // make GET request to the given URL
            HttpResponse httpResponse = client.execute(new HttpGet("http://92.222.33.38:8080/app_server/ns/modifyobjectname?pseudo="+  currentAccount.getPseudo() +"&password="+ currentPassword +"&id="+ tag.getUid() +"&new_object_name="+ newObjectName));
            StatusLine statusLine = httpResponse.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if (statusCode == 200) {
            	// receive response as inputStream
            	HttpEntity entity =  httpResponse.getEntity();
            	inputStream = entity.getContent();
	                // convert inputstream to string
	                if(inputStream != null)
	                    {result = convertInputStreamToString(inputStream);	                
		                try {
			                // creation JSON Object
			                JSONObject obj = new JSONObject(result);
			                int returnCode = obj.getInt("returncode");
				                if(returnCode == 0){
				                    tag.setObjectName(obj.getString("newobjectname"));}
				            // Else display error message
				                else if(returnCode ==1){
				                	throw new NetworkServiceException("Problem of access to the DB");
				                }
				                else {
				                	throw new NotAuthenticatedException();
				                }
			            } catch (JSONException e) {
			                // TODO Auto-generated catch block
			                // "Error Occured [Server's JSON response might be invalid]!"
			                e.printStackTrace();
			            	                
		                } catch (Exception e) {
			                e.printStackTrace();
			            }
	                    }
	                else {
	                	throw new NetworkServiceException("Connection issue with ther server, null imput stream");
	                }
            }
	               
 	                
            // When Http response code is '404'
            else if(statusCode == 404){
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
        }catch(Exception e) {
        	// TODO Auto-generated catch block
			e.printStackTrace();
        }
                  
		return tag;
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
        InputStream inputStream = null;
        String result = "";
        try {                	
            // make GET request to the given URL
            HttpResponse httpResponse = client.execute(new HttpGet("http://92.222.33.38:8080/app_server/ns/deletetag?pseudo="+  currentAccount.getPseudo() +"&password="+ currentPassword +"&id="+ tag.getUid()));
            StatusLine statusLine = httpResponse.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if (statusCode == 200) {
            	// receive response as inputStream
            	HttpEntity entity =  httpResponse.getEntity();
            	inputStream = entity.getContent();
	                // convert inputstream to string
	                if(inputStream != null)
	                    {result = convertInputStreamToString(inputStream);	                
		                try {
			                // creation JSON Object
			                JSONObject obj = new JSONObject(result);
			                int returnCode = obj.getInt("returncode");
				                if(returnCode == 0){
				                    }
				            // Else display error message
				                else if(returnCode ==1){
				                	throw new NetworkServiceException("Problem of access to the DB");
				                }
				                else {
				                	throw new NotAuthenticatedException();
				                }
			            } catch (JSONException e) {
			                // TODO Auto-generated catch block
			                // "Error Occured [Server's JSON response might be invalid]!"
			                e.printStackTrace();
			            	                
		                } catch (Exception e) {
			                e.printStackTrace();
			            }
	                    }
	                else {
	                	throw new NetworkServiceException("Connection issue with ther server, null imput stream");
	                }
            }
	               
 	                
            // When Http response code is '404'
            else if(statusCode == 404){
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
        }catch(Exception e) {
        	// TODO Auto-generated catch block
			e.printStackTrace();
        }

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
