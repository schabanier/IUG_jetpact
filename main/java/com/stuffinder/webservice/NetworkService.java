package com.stuffinder.webservice;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.InputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;
import java.io.File;

import org.apache.http.client.methods.HttpUriRequest;
import org.json.JSONException;
import org.json.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;


import com.stuffinder.data.Account;
import com.stuffinder.data.Profile;
import com.stuffinder.data.Tag;
import com.stuffinder.engine.FieldVerifier;
import com.stuffinder.exceptions.AccountNotFoundException;
import com.stuffinder.exceptions.IllegalFieldException;
import com.stuffinder.exceptions.NetworkServiceException;
import com.stuffinder.exceptions.NotAuthenticatedException;
import com.stuffinder.interfaces.NetworkServiceInterface;

import static com.stuffinder.webservice.ConstantsWebService.server_address;
import static com.stuffinder.webservice.ErrorCode.DATABASE_ACCESS_ISSUE;
import static com.stuffinder.webservice.ErrorCode.INFORMATION_INCOMPLETE;
import static com.stuffinder.webservice.ErrorCode.INVALID_PSEUDO_PASSWORD_COMBINATION;
import static com.stuffinder.webservice.ErrorCode.NO_ERROR;
import static com.stuffinder.webservice.ErrorCode.UNKNOWN_ERROR;
import static com.stuffinder.webservice.ErrorCode.USER_ALREADY_REGISTERED;

public class NetworkService implements NetworkServiceInterface {

    private static HttpClient client;
    private Account currentAccount = null;
    private String currentPassword = null;

    private NetworkService() {

    }

    private HttpResponse executeRequest(HttpUriRequest request) throws InterruptedException, IOException {
        HttpExecuter httpExecuter = new HttpExecuter(request);

        httpExecuter.start();
        httpExecuter.join();

        if(httpExecuter.isRequestSuccessful())
            return httpExecuter.getResponse();
        else
            throw httpExecuter.getCatchedIOException();
    }

    class HttpExecuter extends Thread {
        private HttpUriRequest request;

        private HttpResponse response;
        private IOException catchedIOException;

        HttpExecuter(HttpUriRequest request){
            super();
            this.request = request;
        }

        @Override
        public void run(){
            try {
                response = client.execute(request);
            } catch (IOException e) {
                catchedIOException = e;
            }
        }
        boolean isRequestSuccessful(){
            return catchedIOException == null;
        }
        IOException getCatchedIOException(){
            return catchedIOException;
        }
        HttpResponse getResponse(){
            return response;
        }
    }

    @Override
    public void initNetworkService() throws NetworkServiceException {
        NetworkService.client = new DefaultHttpClient();
    }

    @Override
    public void createAccount(Account newAccount, String newPassword)  throws IllegalFieldException, NetworkServiceException {
        // We first check the validity of the arguments to create the parameters
        if (!FieldVerifier.verifyName(newAccount.getPseudo()))
            throw new IllegalFieldException(IllegalFieldException.PSEUDO, IllegalFieldException.REASON_VALUE_INCORRECT, newAccount.getPseudo());

        if (!FieldVerifier.verifyPassword(newPassword))
            throw new IllegalFieldException(IllegalFieldException.PASSWORD, IllegalFieldException.REASON_VALUE_INCORRECT, newPassword);

        if (!FieldVerifier.verifyName(newAccount.getFirstName()))
            throw new IllegalFieldException(IllegalFieldException.FIRSTNAME, IllegalFieldException.REASON_VALUE_INCORRECT, newAccount.getFirstName());

        if (!FieldVerifier.verifyName(newAccount.getLastName()))
            throw new IllegalFieldException(IllegalFieldException.LASTNAME, IllegalFieldException.REASON_VALUE_INCORRECT, newAccount.getLastName());

        if (!FieldVerifier.verifyEMailAddress(newAccount.getEMailAddress()))
            throw new IllegalFieldException(IllegalFieldException.EMAIL_ADDRESS, IllegalFieldException.REASON_VALUE_INCORRECT, newAccount.getEMailAddress());

        InputStream inputStream;
        String result = "";
        try {
            // make GET request to the given URL
            HttpResponse httpResponse = executeRequest(new HttpGet(server_address +"register?pseudo=" + URLEncoder.encode(newAccount.getPseudo(), "UTF-8") + "&password=" + URLEncoder.encode(newPassword, "UTF-8") + "&first_name=" + URLEncoder.encode(newAccount.getFirstName(), "UTF-8") + "&last_name=" + URLEncoder.encode(newAccount.getLastName(), "UTF-8") + "&email=" + URLEncoder.encode(newAccount.getEMailAddress(), "UTF-8")));
            StatusLine statusLine = httpResponse.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if (statusCode == 200) {
                // receive response as inputStream
                HttpEntity entity = httpResponse.getEntity();
                inputStream = entity.getContent();
                // convert inputstream to string
                if (inputStream != null) {
                    result = convertInputStreamToString(inputStream);
                    try {
                        // creation JSON Object
                        JSONObject obj = new JSONObject(result);
                        int returnCode = obj.getInt("returnCode");
                        if (returnCode == NO_ERROR) {
                            /* Everything went fine */
                        }
                        // Else display error message
                        else if (returnCode == USER_ALREADY_REGISTERED) {
                            currentPassword = null;
                            throw new IllegalFieldException(IllegalFieldException.PSEUDO, IllegalFieldException.REASON_VALUE_ALREADY_USED, newAccount.getPseudo());

                        }else if (returnCode == ErrorCode.ILLEGAL_USE_OF_SPECIAL_CHARACTER) {
                             currentPassword = null;
                             throw new NetworkServiceException("Illegal use of special character");

                        } else {
                            currentPassword = null;
                            throw new IllegalFieldException(IllegalFieldException.PSEUDO, IllegalFieldException.REASON_VALUE_INCORRECT, newAccount.getPseudo());
                        }
                    } catch (JSONException e) {
                        throw new NetworkServiceException("Server response might be invalid.");
                    }
                } else {
                    throw new NetworkServiceException("Connection issue with the server, null input stream");
                }
            }
            // When Http response code is '404'
            else if (statusCode == 404) {
                throw new NetworkServiceException("Requested resource not found");
            }
            // When Http response code is '500'
            else if (statusCode == 500) {
                throw new NetworkServiceException("Something went wrong at server end");
            }
            // When Http response code other than 404, 500
            else {
                throw new NetworkServiceException("Unexpected Error occurred! [Most common Error: Device might not be connected to Internet or remote server is not up and running]");
            }
        } catch (IOException | IllegalStateException e) {
            throw new NetworkServiceException("exception of type IOException or IllegalStateException catched.");
        } catch (InterruptedException e) {
            throw new NetworkServiceException("error occurred while executing request.");
        }

    }


    @Override
    public Account authenticate(String pseudo, String password)throws AccountNotFoundException, NetworkServiceException {
        // We first check the validity of the arguments to create the parameters
        if(! FieldVerifier.verifyName(pseudo))
            throw new IllegalFieldException(IllegalFieldException.PSEUDO, IllegalFieldException.REASON_VALUE_INCORRECT, pseudo);
        if (! FieldVerifier.verifyPassword(password))
            throw new IllegalFieldException(IllegalFieldException.PASSWORD, IllegalFieldException.REASON_VALUE_INCORRECT, password);
        InputStream inputStream;
        String result;
        try {
            // make GET request to the given URL
            HttpResponse httpResponse = executeRequest(new HttpGet(server_address + "login?pseudo=" + URLEncoder.encode(pseudo, "UTF-8") + "&password=" + URLEncoder.encode(password, "UTF-8")));
            StatusLine statusLine = httpResponse.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if (statusCode == 200) {
                // receive response as inputStream
                HttpEntity entity = httpResponse.getEntity();
                inputStream = entity.getContent();

                // convert inputstream to string
                if (inputStream != null) {
                    result = convertInputStreamToString(inputStream);
                    try {
                        // creation JSON Object
                        JSONObject obj = new JSONObject(result);
                        int returnCode = obj.getInt("returnCode");
                        if (returnCode == NO_ERROR) {
                            currentAccount = new Account(obj.getString("pseudo"), obj.getString("first_name"), obj.getString("last_name"), obj.getString("email"));
                            currentPassword = password; // correction
                        }
                        else if (returnCode == INVALID_PSEUDO_PASSWORD_COMBINATION) {
                            currentPassword = null;
                            throw new AccountNotFoundException();
                        }
                    } catch (JSONException e) {
                        // "Error Occured [Server's JSON response might be invalid]!"
                        throw new NetworkServiceException("Server response might be invalid.");
                    }
                } else {
                    throw new NetworkServiceException("Connection issue with the server, null input stream");
                }
            }
            // When Http response code is '404'
            else if (statusCode == 404) {
                throw new NetworkServiceException("Requested resource not found");
            }

            // When Http response code is '500'
            else if (statusCode == 500) {
                throw new NetworkServiceException("Something went wrong at server end");
            }

            // When Http response code other than 404, 500
            else {
                throw new NetworkServiceException("Unexpected Error occurred! [Most common Error: Device might not be connected to Internet or remote server is not up and running]");
            }
        } catch (IOException | IllegalStateException e) {
            throw new NetworkServiceException("exception of type IOException or IllegalStateException catched.");
        } catch (InterruptedException e) {
            throw new NetworkServiceException("error occurred while executing request.");
        }

        return currentAccount;
    }

    // convert inputstream to String
    private static String convertInputStreamToString(InputStream inputStream) throws IOException, InterruptedException {
        InputStreamConverter inputStreamConverter = new InputStreamConverter(inputStream);

        inputStreamConverter.start();
        inputStreamConverter.join();

        if(inputStreamConverter.isConversionSuccessful())
            return inputStreamConverter.getResult();
        else
            throw inputStreamConverter.getCatchedIOException();
    }

    static class InputStreamConverter extends Thread{
        private InputStream inputStream;
        private String result;

        IOException catchedIOException;

        InputStreamConverter(InputStream inputStream){
            this.inputStream = inputStream;
        }

        public void run(){
            try {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line = "";
                result = "";
                while ((line = bufferedReader.readLine()) != null)
                    result += line;
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
                catchedIOException = e;
            }
        }

        public boolean isConversionSuccessful(){
            return catchedIOException == null;
        }
        IOException getCatchedIOException(){
            return catchedIOException;
        }
        String getResult(){
            return result;
        }
    }

    @Override
    public void logOut() {
        currentAccount = null;
        currentPassword = null;
    }

    @Override
    public Account getCurrentAccount() throws NotAuthenticatedException {
        if (currentAccount != null) {
            return currentAccount;
        } else {
            throw new NotAuthenticatedException();
        }
    }

    @Override
    public void modifyEMailAddress(String emailAddress) throws NotAuthenticatedException, IllegalFieldException,  NetworkServiceException {
        // We first check the validity of the arguments to create the parameters
        if (! FieldVerifier.verifyEMailAddress(emailAddress))
            throw new IllegalFieldException(IllegalFieldException.EMAIL_ADDRESS, IllegalFieldException.REASON_VALUE_INCORRECT, emailAddress);

        InputStream inputStream;
        String result;
        if(currentAccount == null) { throw new NotAuthenticatedException(); }
        try {
            // make GET request to the given URL
            HttpResponse httpResponse = executeRequest(new HttpGet(server_address + "modifyemail?pseudo=" + URLEncoder.encode(currentAccount.getPseudo(), "UTF-8") + "&password=" + URLEncoder.encode(currentPassword, "UTF-8") + "&new_email=" + URLEncoder.encode(emailAddress, "UTF-8")));
            StatusLine statusLine = httpResponse.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if (statusCode == 200) {
                // receive response as inputStream
                HttpEntity entity = httpResponse.getEntity();
                inputStream = entity.getContent();
                // convert inputstream to string
                if (inputStream != null) {
                    result = convertInputStreamToString(inputStream);
                    try {
                        // creation JSON Object
                        JSONObject obj = new JSONObject(result);
                        int returnCode = obj.getInt("returnCode");
                        if (returnCode == NO_ERROR) {
                            currentAccount.setMailAddress(obj.getString("email"));
                        }else if (returnCode == DATABASE_ACCESS_ISSUE) {
                            throw new NetworkServiceException("Problem of access to the DB");
                        }else if (returnCode == INVALID_PSEUDO_PASSWORD_COMBINATION) {
                            throw new NotAuthenticatedException();
                        } else {
                            throw new NetworkServiceException("Unknown Error");
                        }
                    } catch (JSONException e) {
                        throw new NetworkServiceException("Server response might be invalid.");
                    }
                } else {
                    throw new NetworkServiceException("Connection issue with the server, null input stream");
                }
            }
            // When Http response code is '404'
            else if (statusCode == 404) {
                throw new NetworkServiceException("Requested resource not found");
            }
            // When Http response code is '500'
            else if (statusCode == 500) {
                throw new NetworkServiceException("Something went wrong at server end");
            }
            // When Http response code other than 404, 500
            else {
                throw new NetworkServiceException("Unexpected Error occurred! [Most common Error: Device might not be connected to Internet or remote server is not up and running]");
            }
        } catch (IOException | IllegalStateException e) {
            throw new NetworkServiceException("Exception of type IOException or IllegalStateException catched.");
        } catch (InterruptedException e) {
            throw new NetworkServiceException("Error occurred while executing request.");
        }

    }

    @Override
    public void modifyPassword(String newPassword) throws NotAuthenticatedException, IllegalFieldException, NetworkServiceException {
        // We first check the validity of the arguments to create the parameters
        if (! FieldVerifier.verifyPassword(newPassword))
            throw new IllegalFieldException(IllegalFieldException.PASSWORD, IllegalFieldException.REASON_VALUE_INCORRECT, newPassword);

        InputStream inputStream;
        String result;
        try {
            // make GET request to the given URL
            HttpResponse httpResponse = executeRequest(new HttpGet(server_address + "modifypassword?pseudo=" + URLEncoder.encode(currentAccount.getPseudo(), "UTF-8") + "&password=" + URLEncoder.encode(currentPassword, "UTF-8") + "&new_password=" + URLEncoder.encode(newPassword, "UTF-8")));
            StatusLine statusLine = httpResponse.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if (statusCode == 200) {
                // receive response as inputStream
                HttpEntity entity = httpResponse.getEntity();
                inputStream = entity.getContent();
                // convert inputstream to string
                if (inputStream != null) {
                    result = convertInputStreamToString(inputStream);
                    try {
                        // creation JSON Object
                        JSONObject obj = new JSONObject(result);
                        int returnCode = obj.getInt("returnCode");
                        if (returnCode == NO_ERROR) {
                            currentPassword = newPassword;
                        }else if (returnCode == INVALID_PSEUDO_PASSWORD_COMBINATION) {
                            throw new NotAuthenticatedException();
                        }else if (returnCode == DATABASE_ACCESS_ISSUE) {
                            throw new NetworkServiceException("Problem of access to the DB");
                        }else if (returnCode == UNKNOWN_ERROR) {
                            throw new NetworkServiceException("Unknown error");

                        } else {
                            throw new NetworkServiceException("Unknown error");
                        }
                    } catch (JSONException e) {
                        throw new NetworkServiceException("Server response might be invalid.");
                    }
                } else {
                    throw new NetworkServiceException("Connection issue with the server, null input stream");
                }
            }
            // When Http response code is '404'
            else if (statusCode == 404) {
                throw new NetworkServiceException("Requested resource not found");
            }

            // When Http response code is '500'
            else if (statusCode == 500) {
                throw new NetworkServiceException("Something went wrong at server end");
            }

            // When Http response code other than 404, 500
            else {
                throw new NetworkServiceException("Unexpected Error occurred! [Most common Error: Device might not be connected to Internet or remote server is not up and running]");
            }
        } catch (IOException | IllegalStateException e) {
            throw new NetworkServiceException("exception of type IOException or IllegalStateException catched.");
        } catch (InterruptedException e) {
            throw new NetworkServiceException("error occurred while executing request.");
        }
    }

    @Override
    public List<Tag> getTags() throws NotAuthenticatedException, NetworkServiceException {
        List<Tag> res = new ArrayList<>();
        if(currentAccount == null) { throw new NotAuthenticatedException(); }
        InputStream inputStream;
        String result = "";
        try {
            // make GET request to the given URL
            HttpResponse httpResponse = executeRequest(new HttpGet(server_address + "retrievetag?pseudo=" + URLEncoder.encode(currentAccount.getPseudo(), "UTF-8") + "&password=" + URLEncoder.encode(currentPassword, "UTF-8")));
            StatusLine statusLine = httpResponse.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if (statusCode == 200) {
                // receive response as inputStream
                HttpEntity entity = httpResponse.getEntity();
                inputStream = entity.getContent();
                // convert inputstream to string
                if (inputStream != null) {
                    result = convertInputStreamToString(inputStream);
                    try {
                        // creation JSON Object
                        JSONObject obj = new JSONObject(result);
                        int returnCode = obj.getInt("returnCode");
                        if (returnCode == NO_ERROR) {
                            org.json.JSONArray arrayOfJsonTag = obj.getJSONArray("listTags");
                            for (int i = 0; i < arrayOfJsonTag.length(); i++) {
                                JSONObject tagjson = arrayOfJsonTag.getJSONObject(i);
                                Tag tag = new Tag(tagjson.getString("tag_id"), tagjson.getString("object_name"), tagjson.getString("picture_name"));
                                res.add(tag);
                            }
                        }
                        else {
                            throw new NotAuthenticatedException();
                        }
                    } catch (JSONException e) {
                        Log.e("Content of the catched JSON", result);
                        e.printStackTrace();
                        throw new NetworkServiceException("Server response might be invalid.");
                    }
                } else {
                    throw new NetworkServiceException("Connection issue with the server, null input stream");
                }
            }
            // When Http response code is '404'
            else if (statusCode == 404) {
                throw new NetworkServiceException("Requested resource not found");
            }

            // When Http response code is '500'
            else if (statusCode == 500) {
                throw new NetworkServiceException("Something went wrong at server end");
            }

            // When Http response code other than 404, 500
            else {
                throw new NetworkServiceException("Unexpected Error occurred! [Most common Error: Device might not be connected to Internet or remote server is not up and running]");
            }
        } catch (IOException | IllegalStateException e) {
            e.printStackTrace();
            throw new NetworkServiceException("exception of type IOException or IllegalStateException catched.");
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw new NetworkServiceException("error occurred while executing request.");
        }
        return res;
    }

    @Override
    public Tag addTag(Tag tag) throws NotAuthenticatedException,IllegalFieldException, NetworkServiceException {
        // We first check the validity of the arguments to create the parameters
        if (! FieldVerifier.verifyTagUID(tag.getUid()))
            throw new IllegalFieldException(IllegalFieldException.TAG_UID, IllegalFieldException.REASON_VALUE_INCORRECT, tag.getUid());
        if (! FieldVerifier.verifyTagUID(tag.getUid()))
            throw new IllegalFieldException(IllegalFieldException.TAG_OBJECT_NAME, IllegalFieldException.REASON_VALUE_INCORRECT, tag.getObjectName());
        InputStream inputStream;
        String result = "";
        if(currentAccount == null) { throw new NotAuthenticatedException(); }
        try {
            // make GET request to the given URL
            HttpResponse httpResponse = executeRequest(new HttpGet(server_address + "addtag?pseudo=" + URLEncoder.encode(currentAccount.getPseudo(), "UTF-8") + "&password=" + URLEncoder.encode(currentPassword, "UTF-8") + "&id=" + URLEncoder.encode(tag.getUid(), "UTF-8") + "&object_name=" + URLEncoder.encode(tag.getObjectName(), "UTF-8") + "&picture_name=" + URLEncoder.encode(tag.getObjectImageName(), "UTF-8")));
            StatusLine statusLine = httpResponse.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if (statusCode == 200) {
                // receive response as inputStream
                HttpEntity entity = httpResponse.getEntity();
                inputStream = entity.getContent();
                // convert inputstream to string
                if (inputStream != null) {
                    result = convertInputStreamToString(inputStream);
                    try {
                        // creation JSON Object
                        JSONObject obj = new JSONObject(result);
                        int returnCode = obj.getInt("returnCode");
                        if (returnCode == NO_ERROR) {
                        }else if (returnCode == INVALID_PSEUDO_PASSWORD_COMBINATION) {
                        throw new NotAuthenticatedException();
                         }else if (returnCode == DATABASE_ACCESS_ISSUE) {
                             throw new NetworkServiceException("Problem of access to the DB");
                         }else if (returnCode == UNKNOWN_ERROR) {
                            throw new NetworkServiceException("Unknown error");
                        }else if (returnCode == INFORMATION_INCOMPLETE) {
                            throw new NetworkServiceException("Information incomplete");
                         }else {
                            throw new NetworkServiceException("Unknown error");
                        }
                    } catch (JSONException e) {
                        throw new NetworkServiceException("Server response might be invalid.");
                    }
                } else {
                    throw new NetworkServiceException("Connection issue with the server, null input stream");
                }
            }
            // When Http response code is '404'
            else if (statusCode == 404) {
                throw new NetworkServiceException("Requested resource not found");
            }
            // When Http response code is '500'
            else if (statusCode == 500) {
                throw new NetworkServiceException("Something went wrong at server end");
            }
            // When Http response code other than 404, 500
            else {
                throw new NetworkServiceException("Unexpected Error occurred! [Most common Error: Device might not be connected to Internet or remote server is not up and running]");
            }
        } catch (IOException | IllegalStateException e) {
            throw new NetworkServiceException("exception of type IOException or IllegalStateException catched.");
        } catch (InterruptedException e) {
            throw new NetworkServiceException("error occurred while executing request.");
        }
        return tag;
    }

    @Override
    public String downloadObjectImage(Tag tag) throws NotAuthenticatedException, IllegalFieldException, NetworkServiceException {
        // We first check the validity of the arguments to create the parameters
        if (! FieldVerifier.verifyTagUID(tag.getUid()))
            throw new IllegalFieldException(IllegalFieldException.TAG_UID, IllegalFieldException.REASON_VALUE_INCORRECT, tag.getUid());
        if(currentAccount == null) { throw new NotAuthenticatedException(); }
        InputStream inputStream = null;
        String destinationFile = tag.getObjectImageName()+".jpg";
        String result = "";
        try {
            // make GET request to the given URL
            HttpResponse httpResponse = executeRequest(new HttpGet(server_address + "modifyobjectname?pseudo=" + URLEncoder.encode(currentAccount.getPseudo(), "UTF-8") + "&password=" + URLEncoder.encode(currentPassword, "UTF-8") + "&id=" + URLEncoder.encode(tag.getUid(), "UTF-8")));
            StatusLine statusLine = httpResponse.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if (statusCode == 200) {
                // receive response as inputStream
                HttpEntity entity = httpResponse.getEntity();
                inputStream = entity.getContent();
                // convert inputstream to string
                if (inputStream != null) {
                    OutputStream os = new FileOutputStream(destinationFile);
                    byte[] b = new byte[2048];
                    int length;
                    while ((length = inputStream.read(b)) != -1) {
                        os.write(b, 0, length);
                    }
                    inputStream.close();
                    os.close();
                } else {
                    throw new NetworkServiceException("Connection issue with the server, null input stream");
                }
            }
            // When Http response code is '404'
            else if (statusCode == 404) {
                throw new NetworkServiceException("Requested resource not found");
            }
            // When Http response code is '500'
            else if (statusCode == 500) {
                throw new NetworkServiceException("Something went wrong at server end");
            }
            // When Http response code other than 404, 500
            else {
                throw new NetworkServiceException("Unexpected Error occurred! [Most common Error: Device might not be connected to Internet or remote server is not up and running]");
            }
        } catch (IOException | IllegalStateException e) {
            throw new NetworkServiceException("exception of type IOException or IllegalStateException catched.");
        } catch (InterruptedException e) {
            throw new NetworkServiceException("error occurred while executing request.");
        }
        return destinationFile;
    }

    @Override
    public Tag modifyObjectName(Tag tag, String newObjectName) throws NotAuthenticatedException, IllegalFieldException, NetworkServiceException {
        // We first check the validity of the arguments to create the parameters
        if (! FieldVerifier.verifyName(newObjectName))
            throw new IllegalFieldException(IllegalFieldException.TAG_OBJECT_NAME, IllegalFieldException.REASON_VALUE_INCORRECT, newObjectName);
        if(currentAccount == null) { throw new NotAuthenticatedException(); }
        InputStream inputStream = null;
        String result = "";
        try {
            // make GET request to the given URL
            HttpResponse httpResponse = executeRequest(new HttpGet(server_address + "modifyobjectname?pseudo=" + URLEncoder.encode(currentAccount.getPseudo(), "UTF-8") + "&password=" + URLEncoder.encode(currentPassword, "UTF-8") + "&id=" + URLEncoder.encode(tag.getUid(), "UTF-8") + "&new_object_name=" + URLEncoder.encode(newObjectName, "UTF-8")));
            StatusLine statusLine = httpResponse.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if (statusCode == 200) {
                // receive response as inputStream
                HttpEntity entity = httpResponse.getEntity();
                inputStream = entity.getContent();
                // convert inputstream to string
                if (inputStream != null) {
                    result = convertInputStreamToString(inputStream);
                    try {
                        // creation JSON Object
                        JSONObject obj = new JSONObject(result);
                        int returnCode = obj.getInt("returnCode");
                        if (returnCode == NO_ERROR) {
                            tag.setObjectName(obj.getString("newobjectname"));
                         }else if (returnCode == INVALID_PSEUDO_PASSWORD_COMBINATION) {
                                throw new NotAuthenticatedException();
                         }else if (returnCode == DATABASE_ACCESS_ISSUE) {
                                throw new NetworkServiceException("Problem of access to the DB");
                         }else if (returnCode == UNKNOWN_ERROR) {
                             throw new NetworkServiceException("Unknown error");
                         } else {
                            throw new NetworkServiceException("Unknown error");
                        }
                    } catch (JSONException e) {
                        throw new NetworkServiceException("Server response might be invalid.");
                    }
                } else {
                    throw new NetworkServiceException("Connection issue with the server, null input stream");
                }
            }
            // When Http response code is '404'
            else if (statusCode == 404) {
                throw new NetworkServiceException("Requested resource not found");
            }
            // When Http response code is '500'
            else if (statusCode == 500) {
                throw new NetworkServiceException("Something went wrong at server end");
            }
            // When Http response code other than 404, 500
            else {
                throw new NetworkServiceException("Unexpected Error occurred! [Most common Error: Device might not be connected to Internet or remote server is not up and running]");
            }
        } catch (IOException | IllegalStateException e) {
            throw new NetworkServiceException("exception of type IOException or IllegalStateException catched.");
        } catch (InterruptedException e) {
            throw new NetworkServiceException("error occurred while executing request.");
        }
        return tag;
    }

    @Override
    public Tag modifyObjectImage(Tag tag, String newImageFileName)throws NotAuthenticatedException, IllegalFieldException, NetworkServiceException {
        if (! FieldVerifier.verifyImageFileName(newImageFileName))
            throw new IllegalFieldException(IllegalFieldException.TAG_OBJECT_IMAGE, IllegalFieldException.REASON_VALUE_INCORRECT, newImageFileName);
        if(currentAccount == null) { throw new NotAuthenticatedException(); }
        InputStream inputStream;
        String result = "";
        try {
            HttpPost httppost=new HttpPost(server_address+"modifyobjectimage");
            // check image with your test
        	/* in final version : application\pictures\[pseudo]\[objectName].jpg */
            File imageFile = new File(newImageFileName);
            FileBody bin = new FileBody(imageFile);
            HttpEntity reqEntity = MultipartEntityBuilder.create()
                    .addPart("pseudo", new StringBody(currentAccount.getPseudo(), ContentType.TEXT_PLAIN))
                    .addPart("password", new StringBody(currentPassword, ContentType.TEXT_PLAIN))
                    .addPart("objectName", new StringBody(tag.getObjectName(), ContentType.TEXT_PLAIN))
                    .addPart("file", bin)
                    .build();
            httppost.setEntity(reqEntity);
            HttpResponse httpResponse = client.execute(httppost);
            StatusLine statusLine = httpResponse.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if (statusCode == 200) {
                // receive response as inputStream
                HttpEntity entity = httpResponse.getEntity();
                inputStream = entity.getContent();
                // convert inputstream to string
                if (inputStream != null) {
                    try {
                        result = convertInputStreamToString(inputStream);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    try {
                        // creation JSON Object
                        JSONObject obj = new JSONObject(result);
                        int returnCode = obj.getInt("returnCode");
                        if (returnCode == NO_ERROR) {

                        }
                        // Else display error message
                        else if (returnCode == DATABASE_ACCESS_ISSUE) {
                            throw new NetworkServiceException("Problem of access to the DB");
                        } else {
                            throw new NotAuthenticatedException();
                        }
                    } catch (JSONException e) {
                        // "Error Occurred [Server's JSON response might be invalid]!"
                        throw new NetworkServiceException("Server response might be invalid.");
                    }
                } else {
                    throw new NetworkServiceException("Connection issue with the server, null input stream");
                }
            }
            // When Http response code is '404'
            else if (statusCode == 404) {
                throw new NetworkServiceException("Requested resource not found");
            }
            // When Http response code is '500'
            else if (statusCode == 500) {
                throw new NetworkServiceException("Something went wrong at server end");
            }
            // When Http response code other than 404, 500
            else {
                throw new NetworkServiceException("Unexpected Error occurred! [Most common Error: Device might not be connected to Internet or remote server is not up and running]");
            }
        } catch (IOException | IllegalStateException e) {
            throw new NetworkServiceException("exception of type IOException or IllegalStateException catched.");
        }
        return null;
    }

    @Override
    public void removeTag(Tag tag) throws NotAuthenticatedException, IllegalFieldException, NetworkServiceException {
        if(currentAccount == null) { throw new NotAuthenticatedException(); }
        InputStream inputStream;
        String result = "";
        try {
            // make GET request to the given URL
            HttpResponse httpResponse = executeRequest(new HttpGet(server_address + "deletetag?pseudo=" + URLEncoder.encode(currentAccount.getPseudo(), "UTF-8") + "&password=" + URLEncoder.encode(currentPassword, "UTF-8") + "&id=" + URLEncoder.encode(tag.getUid(), "UTF-8")));
            StatusLine statusLine = httpResponse.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if (statusCode == 200) {
                // receive response as inputStream
                HttpEntity entity = httpResponse.getEntity();
                inputStream = entity.getContent();
                // convert inputstream to string
                if (inputStream != null) {
                    result = convertInputStreamToString(inputStream);
                    try {
                        // creation JSON Object
                        JSONObject obj = new JSONObject(result);
                        int returnCode = obj.getInt("returnCode");
                        if (returnCode == NO_ERROR) {
                        }else if (returnCode == INVALID_PSEUDO_PASSWORD_COMBINATION) {
                            throw new NotAuthenticatedException();
                        }else if (returnCode == DATABASE_ACCESS_ISSUE) {
                            throw new NetworkServiceException("Problem of access to the DB");
                        }else if (returnCode == UNKNOWN_ERROR) {
                            throw new NetworkServiceException("Unknown error");
                        }else if (returnCode == INFORMATION_INCOMPLETE) {
                            throw new NetworkServiceException("Information incomplete");
                        }else {
                            throw new NetworkServiceException("Unknown error");
                        }
                    } catch (JSONException e) {
                        // "Error Occurred [Server's JSON response might be invalid]!"
                        throw new NetworkServiceException("Server response might be invalid.");
                    }
                } else {
                    throw new NetworkServiceException("Connection issue with the server, null input stream");
                }
            }
            // When Http response code is '404'
            else if (statusCode == 404) {
                throw new NetworkServiceException("Requested resource not found");
            }
            // When Http response code is '500'
            else if (statusCode == 500) {
                throw new NetworkServiceException("Something went wrong at server end");
            }
            // When Http response code other than 404, 500
            else {
                throw new NetworkServiceException("Unexpected Error occurred! [Most common Error: Device might not be connected to Internet or remote server is not up and running]");
            }
        } catch (IOException | IllegalStateException e) {
            throw new NetworkServiceException("exception of type IOException or IllegalStateException catched.");
        } catch (InterruptedException e) {
            throw new NetworkServiceException("error occurred while executing request.");
        }

    }

    @Override
    public Profile createProfile(String profileName)throws NotAuthenticatedException, IllegalFieldException, NetworkServiceException {
        if (!FieldVerifier.verifyName(profileName))
            throw new IllegalFieldException(IllegalFieldException.PROFILE_NAME, IllegalFieldException.REASON_VALUE_INCORRECT, profileName);
        if(currentAccount == null) { throw new NotAuthenticatedException(); }
        Profile newprofile = new Profile(profileName);
        InputStream inputStream;
        String result = "";
        try {
            // make GET request to the given URL
            HttpResponse httpResponse = executeRequest(new HttpGet(server_address +"createprofile?pseudo=" + URLEncoder.encode(currentAccount.getPseudo(), "UTF-8") + "&password=" + URLEncoder.encode(currentPassword, "UTF-8") + "&profile_name=" + URLEncoder.encode(profileName, "UTF-8")));
            StatusLine statusLine = httpResponse.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if (statusCode == 200) {
                // receive response as inputStream
                HttpEntity entity = httpResponse.getEntity();
                inputStream = entity.getContent();
                // convert inputstream to string
                if (inputStream != null) {
                    result = convertInputStreamToString(inputStream);
                    try {
                        // creation JSON Object
                        JSONObject obj = new JSONObject(result);
                        int returnCode = obj.getInt("returnCode");
                        if (returnCode == NO_ERROR) {
                            /* Everything went fine */
                        }
                        // Else display error message

                        else if (returnCode == ErrorCode.ILLEGAL_USE_OF_SPECIAL_CHARACTER) {
                            throw new NetworkServiceException("Illegal use of special character");

                        } else {
                            throw new IllegalFieldException(IllegalFieldException.PSEUDO, IllegalFieldException.REASON_VALUE_INCORRECT, profileName);
                        }
                    } catch (JSONException e) {
                        throw new NetworkServiceException("Server response might be invalid.");
                    }
                } else {
                    throw new NetworkServiceException("Connection issue with the server, null input stream");
                }
            }

            // When Http response code is '404'
            else if (statusCode == 404) {
                throw new NetworkServiceException("Requested resource not found");
            }

            // When Http response code is '500'
            else if (statusCode == 500) {
                throw new NetworkServiceException("Something went wrong at server end");
            }

            // When Http response code other than 404, 500
            else {
                throw new NetworkServiceException("Unexpected Error occurred! [Most common Error: Device might not be connected to Internet or remote server is not up and running]");
            }
        } catch (IOException | IllegalStateException e) {
            throw new NetworkServiceException("exception of type IOException or IllegalStateException catched.");
        } catch (InterruptedException e) {
            throw new NetworkServiceException("error occurred while executing request.");
        }
        return newprofile;
    }

    @Override
    public Profile createProfile(String profileName, List<Tag> tagList) throws NotAuthenticatedException, IllegalFieldException, NetworkServiceException {
    if(currentAccount == null) { throw new NotAuthenticatedException(); }
    Profile profile = createProfile(profileName);
    profile = addTagsToProfile(profile, tagList);
    return profile;
}

    @Override
    public Profile modifyProfileName(Profile profile, String newProfileName) throws NotAuthenticatedException, IllegalFieldException,NetworkServiceException {
        if(currentAccount == null) { throw new NotAuthenticatedException(); }
        String profileName = profile.getName();
        // We first check the validity of the arguments to create the parameters
        if (! FieldVerifier.verifyName(profileName))
            throw new IllegalFieldException(IllegalFieldException.PROFILE_NAME, IllegalFieldException.REASON_VALUE_INCORRECT, profileName);
        if (! FieldVerifier.verifyName(newProfileName))
            throw new IllegalFieldException(IllegalFieldException.PROFILE_NAME, IllegalFieldException.REASON_VALUE_INCORRECT, newProfileName);
        InputStream inputStream;
        String result = "";
        try {
            // make GET request to the given URL
            HttpResponse httpResponse = client.execute(new HttpGet(server_address + "modifyprofilename?pseudo=" + URLEncoder.encode(currentAccount.getPseudo(), "UTF-8") + "&password=" + URLEncoder.encode(currentPassword, "UTF-8") + "&profile_name=" + URLEncoder.encode(profileName, "UTF-8") + "&new_profile_name=" + URLEncoder.encode(newProfileName, "UTF-8")));
            StatusLine statusLine = httpResponse.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if (statusCode == 200) {
                // receive response as inputStream
                HttpEntity entity = httpResponse.getEntity();
                inputStream = entity.getContent();
                // convert inputstream to string
                if (inputStream != null) {
                    try {
                        result = convertInputStreamToString(inputStream);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    try {
                        // creation JSON Object
                        JSONObject obj = new JSONObject(result);
                        int returnCode = obj.getInt("returnCode");
                        if (returnCode == NO_ERROR) {
                            profile.setName(newProfileName);
                        }
                        // Else display error message
                        else {
                            throw new NetworkServiceException("Wrong pseudo/password combination or access to the DB");
                        }
                    } catch (JSONException e) {
                        // "Error Occured [Server's JSON response might be invalid]!"
                        throw new NetworkServiceException("Server response might be invalid.");
                    }
                } else {
                    throw new NetworkServiceException("Connection issue with ther server, null imput stream");
                }
            }
            // When Http response code is '404'
            else if (statusCode == 404) {
                throw new NetworkServiceException("Requested resource not found");
            }
            // When Http response code is '500'
            else if (statusCode == 500) {
                throw new NetworkServiceException("Something went wrong at server end");
            }
            // When Http response code other than 404, 500
            else {
                throw new NetworkServiceException("Unexpected Error occcured! [Most common Error: Device might not be connected to Internet or remote server is not up and running]");
            }
        } catch (IOException | IllegalStateException e) {
            throw new NetworkServiceException("exception of type IOExcption or IllegalStateException catched.");
        }
        return profile;
    }

    @Override
    public Profile addTagToProfile(Profile profile, Tag tag) throws NotAuthenticatedException, IllegalFieldException, NetworkServiceException {
        if(currentAccount == null) { throw new NotAuthenticatedException(); }
        String profileName = profile.getName();
        // We first check the validity of the arguments to create the parameters
        if (! FieldVerifier.verifyTagUID(tag.getUid()))
            throw new IllegalFieldException(IllegalFieldException.TAG_UID, IllegalFieldException.REASON_VALUE_INCORRECT, tag.getUid());
        if (! FieldVerifier.verifyName(profileName))
            throw new IllegalFieldException(IllegalFieldException.PROFILE_NAME, IllegalFieldException.REASON_VALUE_INCORRECT, profileName);
        InputStream inputStream;
        String result = "";
        try {
            // make GET request to the given URL
            HttpResponse httpResponse = client.execute(new HttpGet(server_address + "addtagtoprofile?pseudo=" + URLEncoder.encode(currentAccount.getPseudo(), "UTF-8") + "&password=" + URLEncoder.encode(currentPassword, "UTF-8") + "&profile_name=" + URLEncoder.encode(profileName, "UTF-8")+ "&id=" + URLEncoder.encode(tag.getUid(), "UTF-8")));
            StatusLine statusLine = httpResponse.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if (statusCode == 200) {
                // receive response as inputStream
                HttpEntity entity = httpResponse.getEntity();
                inputStream = entity.getContent();
                // convert inputstream to string
                if (inputStream != null) {
                    try {
                        result = convertInputStreamToString(inputStream);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    try {
                        // creation JSON Object
                        JSONObject obj = new JSONObject(result);
                        int returnCode = obj.getInt("returnCode");
                        if (returnCode == NO_ERROR) {
                           profile.addTag(tag);
                        }
                        // Else display error message
                        else {
                            throw new NetworkServiceException("Wrong pseudo/password combination or access to the DB");
                        }
                    } catch (JSONException e) {
                        // "Error Occurred [Server's JSON response might be invalid]!"
                        throw new NetworkServiceException("Server response might be invalid.");
                    }
                } else {
                    throw new NetworkServiceException("Connection issue with the server, null input stream");
                }
            }
            // When Http response code is '404'
            else if (statusCode == 404) {
                throw new NetworkServiceException("Requested resource not found");
            }
            // When Http response code is '500'
            else if (statusCode == 500) {
                throw new NetworkServiceException("Something went wrong at server end");
            }
            // When Http response code other than 404, 500
            else {
                throw new NetworkServiceException("Unexpected Error occurred! [Most common Error: Device might not be connected to Internet or remote server is not up and running]");
            }
        } catch (IOException | IllegalStateException e) {
            throw new NetworkServiceException("exception of type IOException or IllegalStateException catched.");
        }
        return profile;
    }

    @Override
    public Profile addTagsToProfile(Profile profile, List<Tag> tags) throws NotAuthenticatedException, IllegalFieldException, NetworkServiceException {
        if(currentAccount == null) { throw new NotAuthenticatedException(); }
        String profileName = profile.getName();
        if (! FieldVerifier.verifyName(profileName))
            throw new IllegalFieldException(IllegalFieldException.PROFILE_NAME, IllegalFieldException.REASON_VALUE_INCORRECT, profileName);
        for(Tag tag : tags) {
            if (! FieldVerifier.verifyTagUID(tag.getUid()))
                throw new IllegalFieldException(IllegalFieldException.TAG_UID, IllegalFieldException.REASON_VALUE_INCORRECT, tag.getUid());
        }
        InputStream inputStream;
        String result = "";
        JSONObject jsonUIDs = new JSONObject();
        //on remplit le json avec les couples ("indice", UID)
        for(int i=0; i<tags.size(); i++) {
            try {
                jsonUIDs.put(Integer.toString(i), tags.get(i).getUid());
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        try {
            // check URL with your test
            HttpPost httppost = new HttpPost(server_address + "addtagstoprofile");
            HttpEntity reqEntity = MultipartEntityBuilder.create()
                    .addPart("pseudo", new StringBody(currentAccount.getPseudo(), ContentType.TEXT_PLAIN))
                    .addPart("password", new StringBody(currentPassword, ContentType.TEXT_PLAIN))
                    .addPart("profileName", new StringBody(profileName, ContentType.TEXT_PLAIN))
                    .addPart("jsonUIDs", new StringBody(jsonUIDs.toString(), ContentType.TEXT_PLAIN))
                    .build();
            httppost.setEntity(reqEntity);
            HttpResponse httpResponse = client.execute(httppost);
            StatusLine statusLine = httpResponse.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if (statusCode == 200) {
                // receive response as inputStream
                HttpEntity entity = httpResponse.getEntity();
                inputStream = entity.getContent();
                // convert inputstream to string
                if (inputStream != null) {
                    try {
                        result = convertInputStreamToString(inputStream);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    try {
                        // creation JSON Object
                        JSONObject obj = new JSONObject(result);
                        int returnCode = obj.getInt("returnCode");
                        if (returnCode == NO_ERROR) {
                            for (Tag tag : tags) {
                               profile.addTag(tag);
                            }
                        }
                        // Else display error message
                        else if (returnCode == DATABASE_ACCESS_ISSUE) {
                            throw new NetworkServiceException("Problem of access to the DB");
                        } else {
                            throw new NotAuthenticatedException();
                        }
                    } catch (JSONException e) {
                        // "Error Occurred [Server's JSON response might be invalid]!"
                        throw new NetworkServiceException("Server response might be invalid.");
                    }
                } else {
                    throw new NetworkServiceException("Connection issue with the server, null input stream");
                }
            }
            // When Http response code is '404'
            else if (statusCode == 404) {
                throw new NetworkServiceException("Requested resource not found");
            }
            // When Http response code is '500'
            else if (statusCode == 500) {
                throw new NetworkServiceException("Something went wrong at server end");
            }
            // When Http response code other than 404, 500
            else {
                throw new NetworkServiceException("Unexpected Error occurred! [Most common Error: Device might not be connected to Internet or remote server is not up and running]");
            }
        } catch (IOException | IllegalStateException e) {
            throw new NetworkServiceException("exception of type IOException or IllegalStateException catched.");
        }
        return profile;
    }

    @Override
    public Profile removeTagFromProfile(Profile profile, Tag tag) throws NotAuthenticatedException, IllegalFieldException, NetworkServiceException {
        if(currentAccount == null) { throw new NotAuthenticatedException(); }
        String id = tag.getUid();
        String profileName = profile.getName();
        // We first check the validity of the arguments to create the parameters
        if (! FieldVerifier.verifyTagUID(tag.getUid()))
            throw new IllegalFieldException(IllegalFieldException.TAG_UID, IllegalFieldException.REASON_VALUE_INCORRECT, id);
        if (! FieldVerifier.verifyName(profileName))
            throw new IllegalFieldException(IllegalFieldException.PROFILE_NAME, IllegalFieldException.REASON_VALUE_INCORRECT, profileName);
        InputStream inputStream;
        String result = "";
        try {
            // make GET request to the given URL
            HttpResponse httpResponse = client.execute(new HttpGet(server_address + "removetagfromprofile?pseudo=" + URLEncoder.encode(currentAccount.getPseudo(), "UTF-8") + "&password=" + URLEncoder.encode(currentPassword, "UTF-8") + "&id=" + URLEncoder.encode(tag.getUid(), "UTF-8") + "&profile_name=" + URLEncoder.encode(profileName,"UTF-8")));
            StatusLine statusLine = httpResponse.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if (statusCode == 200) {
                // receive response as inputStream
                HttpEntity entity = httpResponse.getEntity();
                inputStream = entity.getContent();
                // convert inputstream to string
                if (inputStream != null) {
                    try {
                        result = convertInputStreamToString(inputStream);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    try {
                        // creation JSON Object
                        JSONObject obj = new JSONObject(result);
                        int returnCode = obj.getInt("returnCode");
                        if (returnCode == NO_ERROR) {
                           profile.removeTag(tag);
                        }
                        // Else display error message
                        else {
                            throw new NetworkServiceException("Wrong pseudo/password combination or access to the DB");
                        }
                    } catch (JSONException e) {
                        // "Error Occurred [Server's JSON response might be invalid]!"
                        throw new NetworkServiceException("Server response might be invalid.");
                    }
                } else {
                    throw new NetworkServiceException("Connection issue with the server, null input stream");
                }
            }
            // When Http response code is '404'
            else if (statusCode == 404) {
                throw new NetworkServiceException("Requested resource not found");
            }
            // When Http response code is '500'
            else if (statusCode == 500) {
                throw new NetworkServiceException("Something went wrong at server end");
            }
            // When Http response code other than 404, 500
            else {
                throw new NetworkServiceException("Unexpected Error occurred! [Most common Error: Device might not be connected to Internet or remote server is not up and running]");
            }
        } catch (IOException | IllegalStateException e) {
            throw new NetworkServiceException("exception of type IOException or IllegalStateException catched.");
        }
        return profile;
    }

    @Override
    public Profile removeTagsFromProfile(Profile profile, List<Tag> tagList) throws NotAuthenticatedException, IllegalFieldException, NetworkServiceException {
        if(currentAccount == null) { throw new NotAuthenticatedException(); }
        String profileName = profile.getName();
        if (! FieldVerifier.verifyName(profileName))
            throw new IllegalFieldException(IllegalFieldException.PROFILE_NAME, IllegalFieldException.REASON_VALUE_INCORRECT, profileName);
        for(Tag tag : tagList) {
            if (! FieldVerifier.verifyTagUID(tag.getUid()))
                throw new IllegalFieldException(IllegalFieldException.TAG_UID, IllegalFieldException.REASON_VALUE_INCORRECT, tag.getUid());
        }
        InputStream inputStream;
        String result = "";
        JSONObject jsonUIDs = new JSONObject();
        //on remplit le json avec les couples ("indice", UID)
        for(int i=0; i<tagList.size(); i++) {
            try {
                jsonUIDs.put(Integer.toString(i), tagList.get(i).getUid());
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        try {
            // check URL with your test
            HttpPost httppost=new HttpPost(server_address + "removetagsfromprofile");
            HttpEntity reqEntity = MultipartEntityBuilder.create()
                    .addPart("pseudo", new StringBody(currentAccount.getPseudo(), ContentType.TEXT_PLAIN))
                    .addPart("password", new StringBody(currentPassword, ContentType.TEXT_PLAIN))
                    .addPart("profileName", new StringBody(profileName, ContentType.TEXT_PLAIN))
                    .addPart("jsonUIDs", new StringBody(jsonUIDs.toString(), ContentType.TEXT_PLAIN))
                    .build();
            httppost.setEntity(reqEntity);
            HttpResponse httpResponse = client.execute(httppost);
            StatusLine statusLine = httpResponse.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if (statusCode == 200) {
                // receive response as inputStream
                HttpEntity entity = httpResponse.getEntity();
                inputStream = entity.getContent();
                // convert inputstream to string
                if (inputStream != null) {
                    try {
                        result = convertInputStreamToString(inputStream);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    try {
                        // creation JSON Object
                        JSONObject obj = new JSONObject(result);
                        int returnCode = obj.getInt("returnCode");
                        if (returnCode == NO_ERROR) {
                            org.json.JSONArray arrayOfJsonTag = obj.getJSONArray("listTags");
                            for (Tag tag :tagList) {
                               profile.removeTag(tag);
                            }
                        }
                        // Else display error message
                        else if (returnCode == 1) {
                            throw new NetworkServiceException("Problem of access to the DB");
                        } else {
                            throw new NotAuthenticatedException();
                        }
                    } catch (JSONException e) {
                        // "Error Occured [Server's JSON response might be invalid]!"
                        throw new NetworkServiceException("Server response might be invalid.");
                    }
                } else {
                    throw new NetworkServiceException("Connection issue with the server, null input stream");
                }
            }
            // When Http response code is '404'
            else if (statusCode == 404) {
                throw new NetworkServiceException("Requested resource not found");
            }
            // When Http response code is '500'
            else if (statusCode == 500) {
                throw new NetworkServiceException("Something went wrong at server end");
            }
            // When Http response code other than 404, 500
            else {
                throw new NetworkServiceException("Unexpected Error occurred! [Most common Error: Device might not be connected to Internet or remote server is not up and running]");
            }
        } catch (IOException | IllegalStateException e) {
            throw new NetworkServiceException("exception of type IOException or IllegalStateException catched.");
        }
        return profile;
    }

    @Override
    public Profile removeAllFromProfile(Profile profile) throws NotAuthenticatedException, IllegalFieldException, NetworkServiceException {
        return null;
    }

    @Override
    public Profile replaceTagListOfProfile(Profile profile, List<Tag> tagList) throws NotAuthenticatedException, IllegalFieldException, NetworkServiceException {
        if(currentAccount == null) { throw new NotAuthenticatedException(); }
        //flemme.
        removeProfile(profile);
        Profile newProfile = createProfile(profile.getName(), tagList);
        return newProfile;
    }

    @Override
    public void removeProfile(Profile profile) throws NotAuthenticatedException, IllegalFieldException, NetworkServiceException {
        if(currentAccount == null) { throw new NotAuthenticatedException(); }
        // changer aussi currentAccount ? -> changer visibilitÈ de l'attribut Profils
        String profileName = profile.getName();
        // We first check the validity of the arguments to create the parameters
        if (! FieldVerifier.verifyName(profileName))
            throw new IllegalFieldException(IllegalFieldException.PROFILE_NAME, IllegalFieldException.REASON_VALUE_INCORRECT, profileName);
        InputStream inputStream;
        String result = "";
        try {
            // make GET request to the given URL
            HttpResponse httpResponse = client.execute(new HttpGet(server_address+"removeprofile?pseudo=" + URLEncoder.encode(currentAccount.getPseudo(), "UTF-8") + "&password=" + URLEncoder.encode(currentPassword, "UTF-8") + "&profile_name=" + URLEncoder.encode(profileName, "UTF-8")));
            StatusLine statusLine = httpResponse.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if (statusCode == 200) {
                // receive response as inputStream
                HttpEntity entity = httpResponse.getEntity();
                inputStream = entity.getContent();
                // convert inputstream to string
                if (inputStream != null) {
                    try {
                        result = convertInputStreamToString(inputStream);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    try {
                        // creation JSON Object
                        JSONObject obj = new JSONObject(result);
                        int returnCode = obj.getInt("returnCode");
                        if (returnCode == NO_ERROR) {
                        }
                        // Else display error message
                        else {
                            throw new NetworkServiceException("Wrong pseudo/password combination or access to the DB");
                        }
                    } catch (JSONException e) {
                        // "Error Occurred [Server's JSON response might be invalid]!"
                        throw new NetworkServiceException("Server response might be invalid.");
                    }

                } else {
                    throw new NetworkServiceException("Connection issue with the server, null input stream");
                }
            }
            // When Http response code is '404'
            else if (statusCode == 404) {
                throw new NetworkServiceException("Requested resource not found");
            }
            // When Http response code is '500'
            else if (statusCode == 500) {
                throw new NetworkServiceException("Something went wrong at server end");
            }
            // When Http response code other than 404, 500
            else {
                throw new NetworkServiceException("Unexpected Error occurred! [Most common Error: Device might not be connected to Internet or remote server is not up and running]");
            }
        } catch (IOException | IllegalStateException e) {
            throw new NetworkServiceException("exception of type IOException or IllegalStateException catched.");
        }

    }

    @Override
    public Profile getProfile(String profileName) throws NotAuthenticatedException, NetworkServiceException {
        if(currentAccount == null) { throw new NotAuthenticatedException(); }
        Profile newProfile = new Profile(profileName);
        // We first check the validity of the arguments to create the parameters
        if (! FieldVerifier.verifyName(profileName))
            throw new IllegalFieldException(IllegalFieldException.PROFILE_NAME, IllegalFieldException.REASON_VALUE_INCORRECT, profileName);
        InputStream inputStream;
        String result = "";
        try {
            // make GET request to the given URL
            HttpResponse httpResponse = client.execute(new HttpGet(server_address+"retrieveprofile?pseudo=" + URLEncoder.encode(currentAccount.getPseudo(), "UTF-8") + "&password=" + URLEncoder.encode(currentPassword, "UTF-8") + "&profile_name=" + URLEncoder.encode(profileName, "UTF-8")));
            StatusLine statusLine = httpResponse.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if (statusCode == 200) {
                // receive response as inputStream
                HttpEntity entity = httpResponse.getEntity();
                inputStream = entity.getContent();
                // convert inputstream to string
                if (inputStream != null) {
                    try {
                        result = convertInputStreamToString(inputStream);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    try {
                        // creation JSON Object
                        JSONObject obj = new JSONObject(result);
                        int returnCode = obj.getInt("returnCode");
                        if (returnCode == NO_ERROR) {
                            org.json.JSONArray arrayOfJsonTag = obj.getJSONArray("listTags");
                            for (int i = 0; i < arrayOfJsonTag.length(); i++) {
                                JSONObject tagjson = arrayOfJsonTag.getJSONObject(i);
                                Tag tag1 = new Tag(tagjson.getString("tag_id"), tagjson.getString("object_name"), tagjson.getString("picture"));
                                newProfile.addTag(tag1);
                            }
                        }
                        // Else display error message
                        else {
                            throw new NetworkServiceException("Wrong pseudo/password combination or access to the DB");
                        }
                    } catch (JSONException e) {
                        // "Error Occurred [Server's JSON response might be invalid]!"
                        throw new NetworkServiceException("Server response might be invalid.");
                    }
                } else {
                    throw new NetworkServiceException("Connection issue with the server, null input stream");
                }
            }
            // When Http response code is '404'
            else if (statusCode == 404) {
                throw new NetworkServiceException("Requested resource not found");
            }
            // When Http response code is '500'
            else if (statusCode == 500) {
                throw new NetworkServiceException("Something went wrong at server end");
            }
            // When Http response code other than 404, 500
            else {
                throw new NetworkServiceException("Unexpected Error occurred! [Most common Error: Device might not be connected to Internet or remote server is not up and running]");
            }
        } catch (IOException | IllegalStateException e) {
            throw new NetworkServiceException("exception of type IOException or IllegalStateException catched.");
        }
        return newProfile;
    }

    @Override
    public List<Profile> getProfiles() throws NotAuthenticatedException, NetworkServiceException {
        if(currentAccount == null) { throw new NotAuthenticatedException(); }
        ArrayList<Profile> profileList = new ArrayList<Profile>();
        InputStream inputStream;
        String result = "";
        try {
            // make GET request to the given URL
            HttpResponse httpResponse = client.execute(new HttpGet(server_address + "getprofiles?pseudo="+ URLEncoder.encode(currentAccount.getPseudo(), "UTF_8") + "&password=" + URLEncoder.encode(currentPassword, "UTF_8")));
            StatusLine statusLine = httpResponse.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if (statusCode == 200) {
                // receive response as inputStream
                HttpEntity entity = httpResponse.getEntity();
                inputStream = entity.getContent();
                // convert inputstream to string
                if (inputStream != null) {
                    try {
                        result = convertInputStreamToString(inputStream);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    try {
                        // creation JSON Object
                        JSONObject obj = new JSONObject(result);
                        int returnCode = obj.getInt("returnCode");
                        if (returnCode == NO_ERROR) {
                            // { "intError" = ? ; "listProfiles" = { "profile1" = {"0" = tagID1 ; ...} ; ... } }
                            // JSONObject(.. ; "listProfiles" = JSONObject("profile1" = JSONArray( jsonTags) ) ;.. )
                            // pour ne pas se reposer sur currentAccount, on transmet toutes les coordonnÈes des tags.
                            org.json.JSONObject jsonProfiles = obj
                                    .getJSONObject("listProfiles");
                            for (Iterator<?> iterator = jsonProfiles.keys(); iterator.hasNext();) {
                                String profileName = (String) iterator.next();
                                Profile profile = new Profile(profileName);
                                org.json.JSONArray arrayOfJsonTag = jsonProfiles
                                        .getJSONArray(profileName);
                                for (int i = 0; i < arrayOfJsonTag.length(); i++) {
                                    JSONObject tagjson = arrayOfJsonTag
                                            .getJSONObject(i);
                                    Tag tag = new Tag(tagjson.getString("tag_id"),
                                            tagjson.getString("object_name"),
                                            tagjson.getString("picture"));
                                    profile.addTag(tag);
                                }
                                profileList.add(profile);
                            }
                        }
                        // Else display error message
                        else {
                            throw new NetworkServiceException(
                                    "Wrong pseudo/password combination or access to the DB");
                        }
                    } catch (JSONException e) {
                        // "Error Occurred [Server's JSON response might be invalid]!"
                        throw new NetworkServiceException(
                                "Server response might be invalid.");
                    }
                } else {
                    throw new NetworkServiceException(
                            "Connection issue with the server, null input stream");
                }
            }
            // When Http response code is '404'
            else if (statusCode == 404) {
                throw new NetworkServiceException(
                        "Requested resource not found");
            }
            // When Http response code is '500'
            else if (statusCode == 500) {
                throw new NetworkServiceException(
                        "Something went wrong at server end");
            }
            // When Http response code other than 404, 500
            else {
                throw new NetworkServiceException(
                        "Unexpected Error occurred! [Most common Error: Device might not be connected to Internet or remote server is not up and running]");
            }
        } catch (IOException | IllegalStateException e) {
            throw new NetworkServiceException(
                    "exception of type IOException or IllegalStateException catched.");
        }


        return profileList;
    }

    @Override
    public int getLastTagsUpdateTime() throws NetworkServiceException {
        return 0;
    }

    @Override
    public int getLastProfilesUpdateTime() throws NetworkServiceException {
        return 0;
    }

    // to use singleton design pattern.

    private static NetworkService instance = new NetworkService();

    public static NetworkService getInstance() {
        return instance;
    }
}
