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

		public static final String TAG_ADDITION_WINDOW_TITLE = "Tag addition";
		public static final String TAG_MODIFICATION_WINDOW_TITLE = "Tag modification";
		
		public static final String TAG_UID_LABEL = "tag UID";
		public static final String TAG_OBJECT_NAME_LABEL = "Object name";
		public static final String TAG_OBJECT_IMAGE_FILENAME_LABEL = "Object image filename (optionnal)";

		public static final String CANCEL_BUTTON_NAME = "Cancel";
		public static final String VALIDATE_TAG_ADDITION_BUTTON_NAME = "Add";
		public static final String VALIDATE_TAG_MODIFICATION_BUTTON_NAME = "Validate";
		
		
		public static final String IMAGE_FILENAME_CHOOSER_BUTTON_TOOLTIP_TEXT = "Select an image";
		public static final String IMAGE_FILENAME_CHOOSER_WINDOW_TITLE = "Image file selection";

		public static final String IMAGE_FILE_NOT_FOUND_MESSAGE = "The specified image file doesn't exist.";
		public static final String IMAGE_FILE_NOT_FOUND_TITLE = "Error on field image filename";

		public static final String getTagUIDAlreadyUsedMessage(String uid)
		{
			return "The tag which has uid \"" + uid + "\" is already used.";
		}

		public static final String getObjectNameAlreadyUsedMessage(String objectName)
		{
			return "The object name \""+ objectName + "\" is already used for another tag.";
		}

		public static final String MODIFICATION_TAG_NOT_FOUND_MESSAGE = "Unable to modify : this tag has been deleted by an user on another application instance.";
		public static final String MODIFICATION_TAG_NOT_FOUND_TITLE = "Tag not found";

		public static final String DELETION_TAG_NOT_FOUND_MESSAGE = "Unable to delete : this tag has been deleted by an user on another application instance.";
		public static final String DELETION_TAG_NOT_FOUND_TITLE = "Tag not found";
		
		
		public static final String CONFIRM_TAG_DELETION_MESSAGE = "Are you sure you want to delete this tag ?";
		public static final String CONFIRM_TAG_DELETION_TITLE = "Delete tag";
		
		public static final String getTagsNumberTitle(int tagsNumber)
		{
			return tagsNumber + " tags";
		}
		public static final String LINKED_PROFILES_LIST_TITLE = "Linked profiles";
	}
	

	public static final class UserInformationsManagement
	{
		public static final String ACCOUNT_CREATION_WINDOW_TITLE = "Account creation";
		public static final String ACCOUNT_MODIFICATION_WINDOW_TITLE = "User informations modification";

		public static final String CANCEL_BUTTON_NAME = "Cancel";
		public static final String MODIFY_BUTTON_NAME = "Modify";
		public static final String VALIDATE_ACCOUNT_CREATION_BUTTON_NAME = "Create";
		public static final String VALIDATE_ACCOUNT_MODIFICATION_BUTTON_NAME = "Validate";

		public static final String PSEUDO_ALREADY_USED_MESSAGE = "This pseudo is already used.";
		public static final String PASSWORD_AND_CONFIRMATION_NOT_EQUAL_MESSAGE = "Please retype correctly your password.";

		public static final String TAGS_NUMBER_LABEL = "Number of tags";
		public static final String PROFILES_NUMBER_LABEL = "Number of profiles";
		

		public static final String NEW_PASSWORD_LABEL = "Enter a new password";
		public static final String NEW_PASSWORD_CONFIRMATION_LABEL = "Retype the new password";
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

		public static final String TAG_UID = "tag UID";
		public static final String TAG_OBJECT_NAME = "object name";
		public static final String TAG_OBJECT_FILE_NAME = "object filename";
	}
	
	
	
	public static final class CommonErrorMessages
	{
		public static final String NETWORK_SERVICE_ERROR_TITLE = "Network error";
		public static final String NETWORK_SERVICE_ERROR_MESSAGE = "A network error has occured. Maybe you're not connected to internet.";

		public static final String ABNORMAL_ERROR_TITLE = "Abnormal error";
		public static final String ABNORMAL_ERROR_MESSAGE = "An abnormal error has occured. Please restart the application to try to solve the problem.";

		public static final String UNKNOWN_ERROR_TITLE = "Unknown error";
		public static final String UNKNOWN_ERROR_MESSAGE = "Unknown error has occured. Please restart the application to try to solve the problem.";
		
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
