package webservice;

import interfaces.NetworkServiceInterface;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import data.Account;
import data.Profile;
import data.Tag;
import engine.FieldVerifier;
import exceptions.AccountNotFoundException;
import exceptions.IllegalFieldException;
import exceptions.NetworkServiceException;
import exceptions.NotAuthenticatedException;

@SuppressWarnings("deprecation")
public class NetworkService implements NetworkServiceInterface {

	private static HttpClient client;

	private Account currentAccount = null;

	private String currentPassword = null;

	private NetworkService() {

	}

	@Override
	public void initNetworkService() throws NetworkServiceException {

		NetworkService.client = new DefaultHttpClient();
	}

	@Override
	public void createAccount(Account newAccount, String newPassword)

			throws IllegalFieldException, NetworkServiceException { // We first check
		// the validity of
		// the arguments to
		// create the
		// parameters

		if (!FieldVerifier.verifyName(newAccount.getPseudo()))

			throw new IllegalFieldException(IllegalFieldException.PSEUDO,
					IllegalFieldException.REASON_VALUE_INCORRECT,
					newAccount.getPseudo());

		if (!FieldVerifier.verifyPassword(newPassword))

			throw new IllegalFieldException(IllegalFieldException.PASSWORD,
					IllegalFieldException.REASON_VALUE_INCORRECT, newPassword);

		if (!FieldVerifier.verifyName(newAccount.getFirstName()))

			throw new IllegalFieldException(IllegalFieldException.FIRSTNAME,
					IllegalFieldException.REASON_VALUE_INCORRECT,
					newAccount.getFirstName());

		if (!FieldVerifier.verifyName(newAccount.getLastName()))

			throw new IllegalFieldException(IllegalFieldException.LASTNAME,
					IllegalFieldException.REASON_VALUE_INCORRECT,
					newAccount.getLastName());

		if (!FieldVerifier.verifyEMailAddress(newAccount.getEMailAddress()))

			throw new IllegalFieldException(
					IllegalFieldException.EMAIL_ADDRESS,
					IllegalFieldException.REASON_VALUE_INCORRECT,
					newAccount.getEMailAddress());

		InputStream inputStream;

		String result = "";

		try {

			// make GET request to the given URL

			HttpResponse httpResponse = client.execute(new HttpGet(
					"http://92.222.33.38:8080/app_server/ns/register?pseudo="
							+ newAccount.getPseudo() + "&password="
							+ newPassword + "&first_name="
							+ newAccount.getFirstName() + "&last_name="
							+ newAccount.getLastName() + "&email="
							+ newAccount.getEMailAddress()));

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

						int returnCode = obj.getInt("returncode");

						if (returnCode == 0) {

							// currentAccount = new
							// Account(obj.getString("pseudo"),
							// obj.getString("first_name"),
							// obj.getString("last_name"),
							// obj.getString("email"));

						}

						// Else display error message
						else {
							throw new NetworkServiceException(
									"Wrong pseudo/password combination or access to the DB");
						}
					} catch (JSONException e) {
						// "Error Occured [Server's JSON response might be invalid]!"
						throw new NetworkServiceException(
								"Server response might be invalid.");
					}
				} else {
					throw new NetworkServiceException(
							"Connection issue with ther server, null imput stream");
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
						"Unexpected Error occcured! [Most common Error: Device might not be connected to Internet or remote server is not up and running]");
			}
		} catch (IOException | IllegalStateException e) {
			throw new NetworkServiceException(
					"exception of type IOExcption or IllegalStateException catched.");
		}


	}

	@Override
	public Account authenticate(String pseudo, String password)

			throws AccountNotFoundException, NetworkServiceException {

		// We first check the validity of the arguments to create the parameters

		if (!FieldVerifier.verifyName(pseudo))

			throw new IllegalFieldException(IllegalFieldException.PSEUDO,
					IllegalFieldException.REASON_VALUE_INCORRECT, pseudo);

		if (!FieldVerifier.verifyPassword(password))

			throw new IllegalFieldException(IllegalFieldException.PASSWORD,
					IllegalFieldException.REASON_VALUE_INCORRECT, password);

		InputStream inputStream;

		String result = "";

		try {

			// make GET request to the given URL

			HttpResponse httpResponse = client.execute(new HttpGet(
					"http://92.222.33.38:8080/app_server/ns/login?pseudo="
							+ pseudo + "&password=" + password));

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

						int returnCode = obj.getInt("returncode");

						if (returnCode == 0) {

							currentAccount = new Account(
									obj.getString("pseudo"),
									obj.getString("first_name"),
									obj.getString("last_name"),
									obj.getString("email"));

							currentPassword = password; // correction.

						}

						// Else display error message
						else {
							throw new NetworkServiceException(
									"Wrong pseudo/password combination or access to the DB");
						}
					} catch (JSONException e) {
						// "Error Occured [Server's JSON response might be invalid]!"
						throw new NetworkServiceException(
								"Server response might be invalid.");
					}
				} else {
					throw new NetworkServiceException(
							"Connection issue with ther server, null imput stream");
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
						"Unexpected Error occcured! [Most common Error: Device might not be connected to Internet or remote server is not up and running]");
			}
		} catch (IOException | IllegalStateException e) {
			throw new NetworkServiceException(
					"exception of type IOExcption or IllegalStateException catched.");
		}

		return currentAccount;
	}

	// convert inputstream to String

	private static String convertInputStreamToString(InputStream inputStream)
			throws IOException {

		BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader(inputStream));

		String line = "";

		String result = "";

		while ((line = bufferedReader.readLine()) != null)

			result += line;

		inputStream.close();

		return result;
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
	public void modifyEMailAddress(String emailAddress)

			throws NotAuthenticatedException, IllegalFieldException,

			NetworkServiceException {

		if (currentAccount == null)
			throw new NotAuthenticatedException();

		// We first check the validity of the arguments to create the parameters

		if (!FieldVerifier.verifyEMailAddress(emailAddress))

			throw new IllegalFieldException(
					IllegalFieldException.EMAIL_ADDRESS,
					IllegalFieldException.REASON_VALUE_INCORRECT, emailAddress);

		InputStream inputStream;

		String result = "";

		try {

			// make GET request to the given URL

			HttpResponse httpResponse = client.execute(new HttpGet(
					"http://92.222.33.38:8080/app_server/ns/modifyemail?pseudo="
							+ currentAccount.getPseudo() + "&password="
							+ currentPassword + "&new_email=" + emailAddress));

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

						int returnCode = obj.getInt("returncode");

						if (returnCode == 0) {

							currentAccount.setMailAddress(obj
									.getString("email"));

						}

						// Else display error message
						else {
							throw new NetworkServiceException(
									"Wrong pseudo/password combination or access to the DB");
						}
					} catch (JSONException e) {
						// "Error Occured [Server's JSON response might be invalid]!"
						throw new NetworkServiceException(
								"Server response might be invalid.");
					}
				} else {
					throw new NetworkServiceException(
							"Connection issue with ther server, null imput stream");
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
						"Unexpected Error occcured! [Most common Error: Device might not be connected to Internet or remote server is not up and running]");
			}
		} catch (IOException | IllegalStateException e) {
			throw new NetworkServiceException(
					"exception of type IOExcption or IllegalStateException catched.");
		}


	}

	@Override
	public void modifyPassword(String newPassword)

			throws NotAuthenticatedException, IllegalFieldException,

			NetworkServiceException {

		if (currentAccount == null)
			throw new NotAuthenticatedException();

		// We first check the validity of the arguments to create the parameters

		if (!FieldVerifier.verifyPassword(newPassword))

			throw new IllegalFieldException(IllegalFieldException.PASSWORD,
					IllegalFieldException.REASON_VALUE_INCORRECT, newPassword);

		InputStream inputStream;

		String result = "";

		try {

			// make GET request to the given URL

			HttpResponse httpResponse = client
					.execute(new HttpGet(
							"http://92.222.33.38:8080/app_server/ns/modifypassword?pseudo="
									+ currentAccount.getPseudo() + "&password="
									+ currentPassword + "&new_password="
									+ newPassword));

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

						int returnCode = obj.getInt("returncode");

						if (returnCode == 0) {

							currentPassword = newPassword;

						}

						// Else display error message
						else {
							throw new NetworkServiceException(
									"Wrong pseudo/password combination or access to the DB");
						}
					} catch (JSONException e) {
						// "Error Occured [Server's JSON response might be invalid]!"
						throw new NetworkServiceException(
								"Server response might be invalid.");
					}
				} else {
					throw new NetworkServiceException(
							"Connection issue with ther server, null imput stream");
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
						"Unexpected Error occcured! [Most common Error: Device might not be connected to Internet or remote server is not up and running]");
			}
		} catch (IOException | IllegalStateException e) {
			throw new NetworkServiceException(
					"exception of type IOExcption or IllegalStateException catched.");
		}

	}

	@Override
	public List<Tag> getTags() throws NotAuthenticatedException,

	NetworkServiceException {

		if (currentAccount == null)
			throw new NotAuthenticatedException();

		List<Tag> res = new ArrayList<Tag>();

		InputStream inputStream;

		String result = "";

		try {

			// make GET request to the given URL

			HttpResponse httpResponse = client.execute(new HttpGet(
					"http://92.222.33.38:8080/app_server/ns/retrievetag?pseudo="
							+ currentAccount.getPseudo() + "&password="
							+ currentPassword));

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

						int returnCode = obj.getInt("returncode");

						if (returnCode == 0) {

							org.json.JSONArray arrayOfJsonTag = obj
									.getJSONArray("listTags");

							for (int i = 0; i < arrayOfJsonTag.length(); i++) {

								JSONObject tagjson = arrayOfJsonTag
										.getJSONObject(i);

								Tag tag = new Tag(tagjson.getString("tag_id"),
										tagjson.getString("object_name"),
										tagjson.getString("picture"));

								res.add(tag);

							}

						}

						// Else display error message
						else {
							throw new NetworkServiceException(
									"Wrong pseudo/password combination or access to the DB");
						}
					} catch (JSONException e) {
						// "Error Occured [Server's JSON response might be invalid]!"
						throw new NetworkServiceException(
								"Server response might be invalid.");
					}
				} else {
					throw new NetworkServiceException(
							"Connection issue with ther server, null imput stream");
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
						"Unexpected Error occcured! [Most common Error: Device might not be connected to Internet or remote server is not up and running]");
			}
		} catch (IOException | IllegalStateException e) {
			throw new NetworkServiceException(
					"exception of type IOExcption or IllegalStateException catched.");
		}


		return res;
	}

	@Override
	public Tag addTag(Tag tag) throws NotAuthenticatedException,

	IllegalFieldException, NetworkServiceException {

		// We first check the validity of the arguments to create the parameters

		if (currentAccount == null)
			throw new NotAuthenticatedException();

		if (!FieldVerifier.verifyTagUID(tag.getUid()))

			throw new IllegalFieldException(IllegalFieldException.TAG_UID,
					IllegalFieldException.REASON_VALUE_INCORRECT, tag.getUid());

		if (!FieldVerifier.verifyTagUID(tag.getUid()))

			throw new IllegalFieldException(
					IllegalFieldException.TAG_OBJECT_NAME,
					IllegalFieldException.REASON_VALUE_INCORRECT,
					tag.getObjectName());

		InputStream inputStream;

		String result = "";

		try {

			// make GET request to the given URL

			HttpResponse httpResponse = client.execute(new HttpGet(
					"http://92.222.33.38:8080/app_server/ns/addtag?pseudo="
							+ currentAccount.getPseudo() + "&password="
							+ currentPassword + "&id=" + tag.getUid()
							+ "&object_name=" + tag.getObjectName()
							+ "&picture=" + tag.getObjectImageName()));

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

						int returnCode = obj.getInt("returncode");

						if (returnCode == 0) {

						}

						// Else display error message
						else {
							throw new NetworkServiceException(
									"Wrong pseudo/password combination or access to the DB");
						}
					} catch (JSONException e) {
						// "Error Occured [Server's JSON response might be invalid]!"
						throw new NetworkServiceException(
								"Server response might be invalid.");
					}
				} else {
					throw new NetworkServiceException(
							"Connection issue with ther server, null imput stream");
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
						"Unexpected Error occcured! [Most common Error: Device might not be connected to Internet or remote server is not up and running]");
			}
		} catch (IOException | IllegalStateException e) {
			throw new NetworkServiceException(
					"exception of type IOExcption or IllegalStateException catched.");
		}


		return tag;
	}

	@Override
	public Tag modifyObjectName(Tag tag, String newObjectName)

			throws NotAuthenticatedException, IllegalFieldException,

			NetworkServiceException {

		if (currentAccount == null)
			throw new NotAuthenticatedException();

		// We first check the validity of the arguments to create the parameters

		if (!FieldVerifier.verifyName(newObjectName))

			throw new IllegalFieldException(
					IllegalFieldException.TAG_OBJECT_NAME,
					IllegalFieldException.REASON_VALUE_INCORRECT, newObjectName);

		InputStream inputStream = null;

		String result = "";

		try {

			// make GET request to the given URL

			HttpResponse httpResponse = client.execute(new HttpGet(
					"http://92.222.33.38:8080/app_server/ns/modifyobjectname?pseudo="
							+ currentAccount.getPseudo() + "&password="
							+ currentPassword + "&id=" + tag.getUid()
							+ "&new_object_name=" + newObjectName));

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

						int returnCode = obj.getInt("returncode");

						if (returnCode == 0) {

							tag.setObjectName(obj.getString("newobjectname"));

						}

						// Else display error message
						else {
							throw new NetworkServiceException(
									"Wrong pseudo/password combination or access to the DB");
						}
					} catch (JSONException e) {
						// "Error Occured [Server's JSON response might be invalid]!"
						throw new NetworkServiceException(
								"Server response might be invalid.");
					}
				} else {
					throw new NetworkServiceException(
							"Connection issue with ther server, null imput stream");
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
						"Unexpected Error occcured! [Most common Error: Device might not be connected to Internet or remote server is not up and running]");
			}
		} catch (IOException | IllegalStateException e) {
			throw new NetworkServiceException(
					"exception of type IOExcption or IllegalStateException catched.");
		}


		return tag;
	}

	@Override
	public Tag modifyObjectImage(Tag tag, String newImageFileName)

			throws NotAuthenticatedException, IllegalFieldException,

			NetworkServiceException {

		if (currentAccount == null)
			throw new NotAuthenticatedException();

		if (!FieldVerifier.verifyImageFileName(newImageFileName))

			throw new IllegalFieldException(
					IllegalFieldException.TAG_OBJECT_IMAGE,
					IllegalFieldException.REASON_VALUE_INCORRECT,
					newImageFileName);

		InputStream inputStream;

		String result = "";

		try {
			// check URL with your test
			HttpPost httppost = new HttpPost(
					"http://92.222.33.38:8080/app_server/ns/upload");
			// check image with your test
			/* in final version : application\pictures\[pseudo]\[objectName].jpg */
			File imageFile = new File("C:\\test.jpg");

			FileBody bin = new FileBody(imageFile);

			HttpEntity reqEntity = MultipartEntityBuilder
					.create()
					.addPart(
							"pseudo",
							new StringBody(currentAccount.getPseudo(),
									ContentType.TEXT_PLAIN))
									.addPart(
											"password",
											new StringBody(currentPassword,
													ContentType.TEXT_PLAIN))
													.addPart(
															"tagUID",
															new StringBody(tag.getUid(), ContentType.TEXT_PLAIN))
															.addPart(
																	"object_name",
																	new StringBody(tag.getObjectName(),
																			ContentType.TEXT_PLAIN))
																			.addPart("file", bin).build();

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

					result = convertInputStreamToString(inputStream);

					try {

						// creation JSON Object

						JSONObject obj = new JSONObject(result);

						int returnCode = obj.getInt("returncode");

						if (returnCode == 0) {

						}

						// Else display error message
						else {
							throw new NetworkServiceException(
									"Wrong pseudo/password combination or access to the DB");
						}
					} catch (JSONException e) {
						// "Error Occured [Server's JSON response might be invalid]!"
						throw new NetworkServiceException(
								"Server response might be invalid.");
					}
				} else {
					throw new NetworkServiceException(
							"Connection issue with ther server, null imput stream");
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
						"Unexpected Error occcured! [Most common Error: Device might not be connected to Internet or remote server is not up and running]");
			}
		} catch (IOException | IllegalStateException e) {
			throw new NetworkServiceException(
					"exception of type IOExcption or IllegalStateException catched.");
		}


		return null;
	}

	@Override
	public void removeTag(Tag tag) throws NotAuthenticatedException,

	IllegalFieldException, NetworkServiceException {

		if (currentAccount == null)
			throw new NotAuthenticatedException();

		InputStream inputStream;

		String result = "";

		try {

			// make GET request to the given URL

			HttpResponse httpResponse = client.execute(new HttpGet(
					"http://92.222.33.38:8080/app_server/ns/deletetag?pseudo="
							+ currentAccount.getPseudo() + "&password="
							+ currentPassword + "&id=" + tag.getUid()));

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

						int returnCode = obj.getInt("returncode");

						if (returnCode == 0) {

						}

						// Else display error message
						else {
							throw new NetworkServiceException(
									"Wrong pseudo/password combination or access to the DB");
						}
					} catch (JSONException e) {
						// "Error Occured [Server's JSON response might be invalid]!"
						throw new NetworkServiceException(
								"Server response might be invalid.");
					}
				} else {
					throw new NetworkServiceException(
							"Connection issue with ther server, null imput stream");
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
						"Unexpected Error occcured! [Most common Error: Device might not be connected to Internet or remote server is not up and running]");
			}
		} catch (IOException | IllegalStateException e) {
			throw new NetworkServiceException(
					"exception of type IOExcption or IllegalStateException catched.");
		}

	}

	@Override
	public Profile createProfile(String profileName)

			throws NotAuthenticatedException, IllegalFieldException,

			NetworkServiceException {

		if (currentAccount == null)
			throw new NotAuthenticatedException();

		Profile newProfile = new Profile(profileName);

		// We first check the validity of the arguments to create the parameters

		if (!FieldVerifier.verifyName(profileName))

			throw new IllegalFieldException(IllegalFieldException.PROFILE_NAME,
					IllegalFieldException.REASON_VALUE_INCORRECT, profileName);

		InputStream inputStream;

		String result = "";

		try {

			// make GET request to the given URL

			HttpResponse httpResponse = client
					.execute(new HttpGet(
							"http://92.222.33.38:8080/app_server/ns/createprofile?pseudo="
									+ currentAccount.getPseudo() + "&password="
									+ currentPassword + "&profile_name="
									+ profileName));

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

						int returnCode = obj.getInt("returncode");

						if (returnCode == 0) {
						}

						// Else display error message
						else {
							throw new NetworkServiceException(
									"Wrong pseudo/password combination or access to the DB");
						}
					} catch (JSONException e) {
						// "Error Occured [Server's JSON response might be invalid]!"
						throw new NetworkServiceException(
								"Server response might be invalid.");
					}
				} else {
					throw new NetworkServiceException(
							"Connection issue with ther server, null imput stream");
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
						"Unexpected Error occcured! [Most common Error: Device might not be connected to Internet or remote server is not up and running]");
			}
		} catch (IOException | IllegalStateException e) {
			throw new NetworkServiceException(
					"exception of type IOExcption or IllegalStateException catched.");
		}

		return newProfile;
	}

	@Override
	public Profile addTagToProfile(Profile profile, Tag tag)

			throws NotAuthenticatedException, IllegalFieldException,

			NetworkServiceException {

		if (currentAccount == null)
			throw new NotAuthenticatedException();

		String profileName = profile.getName();

		// We first check the validity of the arguments to create the parameters

		if (!FieldVerifier.verifyTagUID(tag.getUid()))

			throw new IllegalFieldException(IllegalFieldException.TAG_UID,
					IllegalFieldException.REASON_VALUE_INCORRECT, tag.getUid());

		if (!FieldVerifier.verifyName(profileName))

			throw new IllegalFieldException(IllegalFieldException.PROFILE_NAME,
					IllegalFieldException.REASON_VALUE_INCORRECT, profileName);

		InputStream inputStream;

		String result = "";

		try {

			// make GET request to the given URL

			HttpResponse httpResponse = client.execute(new HttpGet(
					"http://92.222.33.38:8080/app_server/ns/addtagtoprofile?pseudo="
							+ currentAccount.getPseudo() + "&password="
							+ currentPassword + "&profile_name=" + profileName
							+ "&id=" + tag.getUid()));

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

						int returnCode = obj.getInt("returncode");

						if (returnCode == 0) {

							profile.addTag(tag);
						}

						// Else display error message
						else {
							throw new NetworkServiceException(
									"Wrong pseudo/password combination or access to the DB");
						}
					} catch (JSONException e) {
						// "Error Occured [Server's JSON response might be invalid]!"
						throw new NetworkServiceException(
								"Server response might be invalid.");
					}
				} else {
					throw new NetworkServiceException(
							"Connection issue with ther server, null imput stream");
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
						"Unexpected Error occcured! [Most common Error: Device might not be connected to Internet or remote server is not up and running]");
			}
		} catch (IOException | IllegalStateException e) {
			throw new NetworkServiceException(
					"exception of type IOExcption or IllegalStateException catched.");
		}

		return profile;
	}

	@Override
	public Profile addTagsToProfile(Profile profile, List<Tag> tags)

			throws NotAuthenticatedException, IllegalFieldException,

			NetworkServiceException {

		if (currentAccount == null)
			throw new NotAuthenticatedException();

		String profileName = profile.getName();

		if (!FieldVerifier.verifyName(profileName))

			throw new IllegalFieldException(IllegalFieldException.PROFILE_NAME,
					IllegalFieldException.REASON_VALUE_INCORRECT, profileName);

		for (Tag tag : tags) {
			if (!FieldVerifier.verifyTagUID(tag.getUid()))
				throw new IllegalFieldException(IllegalFieldException.TAG_UID,
						IllegalFieldException.REASON_VALUE_INCORRECT,
						tag.getUid());
		}

		InputStream inputStream;

		String result = "";

		JSONObject jsonUIDs = new JSONObject();

		// on remplit le json avec les couples ("indice", UID)
		for (int i = 0; i < tags.size(); i++) {
			try {
				jsonUIDs.put(Integer.toString(i), tags.get(i).getUid());
			} catch (JSONException e) {
				throw new NetworkServiceException(
						"Problem constructing jsonUIDs for POST");
			}
		}

		try {
			// check URL with your test
			HttpPost httppost = new HttpPost(
					"http://92.222.33.38:8080/app_server/ns/addtagstoprofile");

			HttpEntity reqEntity = MultipartEntityBuilder
					.create()
					.addPart(
							"pseudo",
							new StringBody(currentAccount.getPseudo(),
									ContentType.TEXT_PLAIN))
									.addPart(
											"password",
											new StringBody(currentPassword,
													ContentType.TEXT_PLAIN))
													.addPart("profileName",
															new StringBody(profileName, ContentType.TEXT_PLAIN))
															.addPart(
																	"jsonUIDs",
																	new StringBody(jsonUIDs.toString(),
																			ContentType.TEXT_PLAIN)).build();

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

					result = convertInputStreamToString(inputStream);

					try {

						// creation JSON Object

						JSONObject obj = new JSONObject(result);

						int returnCode = obj.getInt("returncode");

						if (returnCode == 0) {

							for (Tag tag : tags) {

								profile.addTag(tag);
							}
						}

						// Else display error message
						else {
							throw new NetworkServiceException(
									"Wrong pseudo/password combination or access to the DB");
						}
					} catch (JSONException e) {
						// "Error Occured [Server's JSON response might be invalid]!"
						throw new NetworkServiceException(
								"Server response might be invalid.");
					}
				} else {
					throw new NetworkServiceException(
							"Connection issue with ther server, null imput stream");
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
						"Unexpected Error occcured! [Most common Error: Device might not be connected to Internet or remote server is not up and running]");
			}
		} catch (IOException | IllegalStateException e) {
			throw new NetworkServiceException(
					"exception of type IOExcption or IllegalStateException catched.");
		}

		return profile;
	}

	@Override
	public Profile removeTagFromProfile(Profile profile, Tag tag)

			throws NotAuthenticatedException, IllegalFieldException,

			NetworkServiceException {

		if (currentAccount == null)
			throw new NotAuthenticatedException();

		String id = tag.getUid();

		String profileName = profile.getName();

		// We first check the validity of the arguments to create the parameters

		if (!FieldVerifier.verifyTagUID(tag.getUid()))

			throw new IllegalFieldException(IllegalFieldException.TAG_UID,
					IllegalFieldException.REASON_VALUE_INCORRECT, id);

		if (!FieldVerifier.verifyName(profileName))

			throw new IllegalFieldException(IllegalFieldException.PROFILE_NAME,
					IllegalFieldException.REASON_VALUE_INCORRECT, profileName);

		InputStream inputStream;

		String result = "";

		try {

			// make GET request to the given URL

			HttpResponse httpResponse = client.execute(new HttpGet(
					"http://92.222.33.38:8080/app_server/ns/removetagfromprofile?pseudo="
							+ currentAccount.getPseudo() + "&password="
							+ currentPassword + "&id=" + tag.getUid()
							+ "&profile_name=" + profileName));

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

						int returnCode = obj.getInt("returncode");

						if (returnCode == 0) {

							profile.removeTag(tag);

						}

						// Else display error message
						else {
							throw new NetworkServiceException(
									"Wrong pseudo/password combination or access to the DB");
						}
					} catch (JSONException e) {
						// "Error Occured [Server's JSON response might be invalid]!"
						throw new NetworkServiceException(
								"Server response might be invalid.");
					}
				} else {
					throw new NetworkServiceException(
							"Connection issue with ther server, null imput stream");
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
						"Unexpected Error occcured! [Most common Error: Device might not be connected to Internet or remote server is not up and running]");
			}
		} catch (IOException | IllegalStateException e) {
			throw new NetworkServiceException(
					"exception of type IOExcption or IllegalStateException catched.");
		}


		return profile;
	}

	@Override
	public Profile replaceTagListOfProfile(Profile profile, List<Tag> tagList)

			throws NotAuthenticatedException, IllegalFieldException,

			NetworkServiceException {

		if (currentAccount == null)
			throw new NotAuthenticatedException();

		// flemme.
		removeProfile(profile);

		Profile newProfile = createProfile(profile.getName(), tagList);

		return newProfile;
	}

	@Override
	public Profile getProfile(String profileName)

			throws NotAuthenticatedException, NetworkServiceException {

		if (currentAccount == null)
			throw new NotAuthenticatedException();

		Profile newProfile = new Profile(profileName);

		// We first check the validity of the arguments to create the parameters

		if (!FieldVerifier.verifyName(profileName))

			throw new IllegalFieldException(IllegalFieldException.PROFILE_NAME,
					IllegalFieldException.REASON_VALUE_INCORRECT, profileName);

		InputStream inputStream;

		String result = "";

		try {

			// make GET request to the given URL

			HttpResponse httpResponse = client
					.execute(new HttpGet(
							"http://92.222.33.38:8080/app_server/ns/retrieveprofile?pseudo="
									+ currentAccount.getPseudo() + "&password="
									+ currentPassword + "&profile_name="
									+ profileName));

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

						int returnCode = obj.getInt("returncode");

						if (returnCode == 0) {

							org.json.JSONArray arrayOfJsonTag = obj
									.getJSONArray("listTags");

							for (int i = 0; i < arrayOfJsonTag.length(); i++) {

								JSONObject tagjson = arrayOfJsonTag
										.getJSONObject(i);

								Tag tag1 = new Tag(tagjson.getString("tag_id"),
										tagjson.getString("object_name"),
										tagjson.getString("picture"));

								newProfile.addTag(tag1);
							}
						}

						// Else display error message
						else {
							throw new NetworkServiceException(
									"Wrong pseudo/password combination or access to the DB");
						}
					} catch (JSONException e) {
						// "Error Occured [Server's JSON response might be invalid]!"
						throw new NetworkServiceException(
								"Server response might be invalid.");
					}
				} else {
					throw new NetworkServiceException(
							"Connection issue with ther server, null imput stream");
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
						"Unexpected Error occcured! [Most common Error: Device might not be connected to Internet or remote server is not up and running]");
			}
		} catch (IOException | IllegalStateException e) {
			throw new NetworkServiceException(
					"exception of type IOExcption or IllegalStateException catched.");
		}


		return newProfile;

	}

	@Override
	public List<Profile> getProfiles()

			throws NotAuthenticatedException, NetworkServiceException {

		if (currentAccount == null)
			throw new NotAuthenticatedException();

		ArrayList<Profile> profileList = new ArrayList<Profile>();

		InputStream inputStream;

		String result = "";

		try {

			// make GET request to the given URL

			HttpResponse httpResponse = client.execute(new HttpGet(
					"http://92.222.33.38:8080/app_server/ns/getprofiles?pseudo="
							+ currentAccount.getPseudo() + "&password="
							+ currentPassword));

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

						int returnCode = obj.getInt("returncode");

						if (returnCode == 0) {

			// { "intError" = ? ; "listProfiles" = { "profile1" = {"0" = tagID1 ; ...} ; ... } }
			// JSONObject(.. ; "listProfiles" = JSONObject("profile1" = JSONArray( jsonTags) ) ;.. )
							
			// pour ne pas se reposer sur currentAccount, on transmet toutes les coordonnées des tags.

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
						// "Error Occured [Server's JSON response might be invalid]!"
						throw new NetworkServiceException(
								"Server response might be invalid.");
					}
				} else {
					throw new NetworkServiceException(
							"Connection issue with ther server, null imput stream");
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
						"Unexpected Error occcured! [Most common Error: Device might not be connected to Internet or remote server is not up and running]");
			}
		} catch (IOException | IllegalStateException e) {
			throw new NetworkServiceException(
					"exception of type IOExcption or IllegalStateException catched.");
		}


		return profileList;

	}

	// to use singleton design pattern.

	private static NetworkService instance = new NetworkService();

	public static NetworkService getInstance() {

		return instance;
	}

	@Override
	public String downloadObjectImage(Tag tag)
			throws NotAuthenticatedException, IllegalFieldException,
			NetworkServiceException {

		if (currentAccount == null)
			throw new NotAuthenticatedException();

		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Profile createProfile(String profileName, List<Tag> tagList)
			throws NotAuthenticatedException, IllegalFieldException,
			NetworkServiceException {

		if (currentAccount == null)
			throw new NotAuthenticatedException();

		// flemme.
		Profile newProfile = createProfile(profileName);

		Profile newProfile2 = addTagsToProfile(newProfile, tagList);

		return newProfile2;
	}

	@Override
	public Profile modifyProfileName(Profile profile, String newProfileName)
			throws NotAuthenticatedException, IllegalFieldException,
			NetworkServiceException {

		if (currentAccount == null)
			throw new NotAuthenticatedException();

		String profileName = profile.getName();

		// We first check the validity of the arguments to create the parameters

		if (!FieldVerifier.verifyName(profileName))

			throw new IllegalFieldException(IllegalFieldException.PROFILE_NAME,
					IllegalFieldException.REASON_VALUE_INCORRECT, profileName);

		if (!FieldVerifier.verifyName(newProfileName))

			throw new IllegalFieldException(IllegalFieldException.PROFILE_NAME,
					IllegalFieldException.REASON_VALUE_INCORRECT,
					newProfileName);

		InputStream inputStream;

		String result = "";

		try {

			// make GET request to the given URL

			HttpResponse httpResponse = client.execute(new HttpGet(
					"http://92.222.33.38:8080/app_server/ns/modifyprofilename?pseudo="
							+ currentAccount.getPseudo() + "&password="
							+ currentPassword + "&profile_name=" + profileName
							+ "&new_profile_name=" + newProfileName));

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

						int returnCode = obj.getInt("returncode");

						if (returnCode == 0) {

							profile.setName(newProfileName);

						}

						// Else display error message
						else {
							throw new NetworkServiceException(
									"Wrong pseudo/password combination or access to the DB");
						}
					} catch (JSONException e) {
						// "Error Occured [Server's JSON response might be invalid]!"
						throw new NetworkServiceException(
								"Server response might be invalid.");
					}
				} else {
					throw new NetworkServiceException(
							"Connection issue with ther server, null imput stream");
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
						"Unexpected Error occcured! [Most common Error: Device might not be connected to Internet or remote server is not up and running]");
			}
		} catch (IOException | IllegalStateException e) {
			throw new NetworkServiceException(
					"exception of type IOExcption or IllegalStateException catched.");
		}


		return profile;
	}

	@Override
	public Profile removeTagsFromProfile(Profile profile, List<Tag> tagList)
			throws NotAuthenticatedException, IllegalFieldException,
			NetworkServiceException {

		if (currentAccount == null)
			throw new NotAuthenticatedException();

		String profileName = profile.getName();

		if (!FieldVerifier.verifyName(profileName))

			throw new IllegalFieldException(IllegalFieldException.PROFILE_NAME,
					IllegalFieldException.REASON_VALUE_INCORRECT, profileName);

		for (Tag tag : tagList) {
			if (!FieldVerifier.verifyTagUID(tag.getUid()))
				throw new IllegalFieldException(IllegalFieldException.TAG_UID,
						IllegalFieldException.REASON_VALUE_INCORRECT,
						tag.getUid());
		}

		InputStream inputStream;

		String result = "";

		JSONObject jsonUIDs = new JSONObject();

		// on remplit le json avec les couples ("indice", UID)
		for (int i = 0; i < tagList.size(); i++) {
			try {
				jsonUIDs.put(Integer.toString(i), tagList.get(i).getUid());
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		try {
			// check URL with your test
			HttpPost httppost = new HttpPost(
					"http://92.222.33.38:8080/app_server/ns/removetagsfromprofile");

			HttpEntity reqEntity = MultipartEntityBuilder
					.create()
					.addPart(
							"pseudo",
							new StringBody(currentAccount.getPseudo(),
									ContentType.TEXT_PLAIN))
									.addPart(
											"password",
											new StringBody(currentPassword,
													ContentType.TEXT_PLAIN))
													.addPart("profileName",
															new StringBody(profileName, ContentType.TEXT_PLAIN))
															.addPart(
																	"jsonUIDs",
																	new StringBody(jsonUIDs.toString(),
																			ContentType.TEXT_PLAIN)).build();

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

					result = convertInputStreamToString(inputStream);

					try {

						// creation JSON Object

						JSONObject obj = new JSONObject(result);

						int returnCode = obj.getInt("returncode");

						if (returnCode == 0) {

							for (Tag tag : tagList) {
								profile.removeTag(tag);
							}
						}

						// Else display error message
						else {
							throw new NetworkServiceException(
									"Wrong pseudo/password combination or access to the DB");
						}
					} catch (JSONException e) {
						// "Error Occured [Server's JSON response might be invalid]!"
						throw new NetworkServiceException(
								"Server response might be invalid.");
					}
				} else {
					throw new NetworkServiceException(
							"Connection issue with ther server, null imput stream");
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
						"Unexpected Error occcured! [Most common Error: Device might not be connected to Internet or remote server is not up and running]");
			}
		} catch (IOException | IllegalStateException e) {
			throw new NetworkServiceException(
					"exception of type IOExcption or IllegalStateException catched.");
		}


		return profile;

	}

	@Override
	public void removeProfile(Profile profile)
			throws NotAuthenticatedException, IllegalFieldException,
			NetworkServiceException {
		// changer aussi currentAccount ? -> changer visibilité de l'attribut
		// Profils

		if (currentAccount == null)
			throw new NotAuthenticatedException();

		String profileName = profile.getName();

		// We first check the validity of the arguments to create the parameters

		if (!FieldVerifier.verifyName(profileName))

			throw new IllegalFieldException(IllegalFieldException.PROFILE_NAME,
					IllegalFieldException.REASON_VALUE_INCORRECT, profileName);

		InputStream inputStream;

		String result = "";

		try {

			// make GET request to the given URL

			HttpResponse httpResponse = client
					.execute(new HttpGet(
							"http://92.222.33.38:8080/app_server/ns/removeprofile?pseudo="
									+ currentAccount.getPseudo() + "&password="
									+ currentPassword + "&profile_name="
									+ profileName));

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

						int returnCode = obj.getInt("returncode");

						if (returnCode == 0) {

						}

						// Else display error message
						else {
							throw new NetworkServiceException(
									"Wrong pseudo/password combination or access to the DB");
						}
					} catch (JSONException e) {
						// "Error Occured [Server's JSON response might be invalid]!"
						throw new NetworkServiceException(
								"Server response might be invalid.");
					}
				} else {
					throw new NetworkServiceException(
							"Connection issue with ther server, null imput stream");
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
						"Unexpected Error occcured! [Most common Error: Device might not be connected to Internet or remote server is not up and running]");
			}
		} catch (IOException | IllegalStateException e) {
			throw new NetworkServiceException(
					"exception of type IOExcption or IllegalStateException catched.");
		}

	}

	@Override
	public int getLastTagsUpdateTime() throws NetworkServiceException,
	NotAuthenticatedException {

		if (currentAccount == null)
			throw new NotAuthenticatedException();

		return 0;
	}

	@Override
	public int getLastProfilesUpdateTime() throws NetworkServiceException,
	NotAuthenticatedException {

		if (currentAccount == null)
			throw new NotAuthenticatedException();

		return 0;
	}

}
