package gui;

public class Constants
{
	public static final class MainFrame
	{
		public static final String WINDOW_TITLE = "Stuffinder";
		
		// elements of the left panel when authenticated..
		public static final String USER_INFORMATIONS_PANEL_NAME = "User informations";
		public static final String TAGS_PANEL_NAME = "Tags management";
		public static final String PROFILES_PANEL_NAME = "Profiles management";

		public static final String LOGOUT_BUTTON_NAME = "Logout";

		public static final String ADD_TAG_BUTTON_NAME = "Add new tag";
	}
	
	
	public static final class Authentication
	{
		public static final String CREATE_ACCOUNT_BUTTON_NAME = "Create account";
		public static final String DO_AUTHENTICATE_BUTTON_NAME = "Log in";

		public static final String ACCOUNT_NOT_FOUND_MESSAGE = "The account is not found.";
		public static final String ACCOUNT_NOT_FOUND_TITLE = "Account not found";
		
	}

	
	public static final class TagsManagement
	{
		public static final String NETWORK_SERVICE_OCCURED = "";
	}
	

	public static final class UserInformationsManagement
	{
		public static final String ACCOUNT_CREATION_WINDOW_TITLE = "Account creation";
		
		public static final String CANCEL_BUTTON_NAME = "Cancel";
		public static final String VALIDATE_ACCOUNT_CREATION_BUTTON_NAME = "Create";

		public static final String PSEUDO_ALREADY_USED_MESSAGE = "This pseudo is already used.";
		public static final String PASSWORD_AND_CONFIRMATION_NOT_EQUAL_MESSAGE = "Please retype correctly your password.";
	}
	

	public static final class Fields
	{
		public static final String PSEUDO = "Pseudo";
		
		public static final String FIRSTNAME = "Firstname";
		public static final String LASTNAME = "Lastname";
		public static final String EMAIL_ADDRESS = "Email address";
		
		public static final String PASSWORD = "Password";
		public static final String PASSWORD_LABEL = "Enter a password";
		public static final String PASSWORD_CONFIRMATION = "Password confirmation";
		public static final String PASSWORD_CONFIRMATION_LABEL = "Retype the password";
	}
	
	
	
	public static final class CommonErrorMessages
	{
		public static final String NETWORK_SERVICE_ERROR_TITLE = "Network error";
		public static final String NETWORK_SERVICE_ERROR_MESSAGE = "A network error has occured. Maybe you're not connected to internet.";

		public static final String ABNORMAL_ERROR_TITLE = "Abnormal error";
		public static final String ABNORMAL_ERROR_MESSAGE = "An abnormal error has occured. Please restart the application to try to solve the problem.";
		
		public static final String FIELD_ERROR_TITLE = "ERROR on field ";

		public static final String getDefaultFieldErrorMessage(String field)
		{
			return "The field " + field + " is incorrect.";
		}

		public static final String getEmptyFieldErrorMessage(String field)
		{
			return "The field " + field + " can't be empty.";
		}
		
		public static final String getFieldErrorTitle(String field)
		{
			return "Error on field " + field;
		}
		
		

//		public static final String _ERROR_TITLE = "";
//		public static final String _ERROR_MESSAGE = "";
	}
}
