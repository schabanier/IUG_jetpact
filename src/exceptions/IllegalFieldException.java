package exceptions;

import interfaces.NetworkServiceInterface;

/**
 * Thrown to indicate that an illegal field has been passed to a method. The field can be incorrect (syntactically) or already used.<br/>
 * To know the concerned field, get the field id by using the method <code> {@link IllegalFieldException#getFieldId() getFieldId()} </code> and compare with the different constants of this class.<br/>
 * The get the reason why this field is illegal, get the reason code (an integer)  by using the method <code> {@link IllegalFieldException#getReason() getReason()} </code> and compare with the different constants (prefixed by <code>REASON_</code> ) of this class.<br/>
 * If the field is incorrect, there can be an additional message. To get it, use the method {@link IllegalFieldException#getMessage() getMessage()}<br/><br/>
 * For example, if an exception of this type is thrown during a call to the method {@link NetworkServiceInterface#addTag(data.Tag) addTag(Tag tag)}, the code inside the catch clause can be : <br/>
 * <code><pre>
 * catch (IllegalFieldException e) {
 * 		switch(e.fieldId)
 * 		{
 * 			case IllegalFieldException.TAG_UID :
 * 				if(e.getReason() == IllegalFieldException.REASON_VALUE_INCORRECT)
 * 					system.err.println("The tag id is incorrect : " + e.getMessage());
 * 				else
 * 					system.err.println("The tag id is already used for another tag");
 * 			break;
 * 			case IllegalFieldException.TAG_OBJECT_NAME :
 * 				if(e.getReason() == IllegalFieldException.REASON_VALUE_INCORRECT)
 * 					system.err.println("The object name is incorrect : " + e.getMessage());
 * 				else
 * 					system.err.println("The object name is already used for another tag.");
 * 			break;
 * 			case IllegalFieldException.TAG_OBJECT_IMAGE :
 * 				system.err.println("The image filename is incorrect : " + e.getMessage());
 * 			break;
 * 			default:
 * 				system.err.println("Unknown error has occured");
 * 			break;
 * 		}
 * }
 * </pre></code>
 * 
 * @author Nicolas Thierce
 *
 */
public class IllegalFieldException extends IllegalArgumentException
{
	private static final long serialVersionUID = 6349027407947995384L;
	
	private final int fieldId;
	private final int reason;

	
	public static final int PSEUDO = 0;
	public static final int FIRSTNAME = 1;
	public static final int LASTNAME = 2;
	public static final int EMAIL_ADDRESS = 3;
	public static final int BIRTH_DATE = 4;
	public static final int PASSWORD = 5;

	public static final int TAG_UID = 10;
	public static final int TAG_OBJECT_NAME = 11;
	public static final int TAG_OBJECT_IMAGE = 12;
	

	
	public static final int REASON_VALUE_INCORRECT = 0;
	public static final int REASON_VALUE_ALREADY_USED = 1;

	
	@Deprecated
	/**
	 * Will be removed. Use another constructor.
	 * @param fieldId
	 * @param message
	 */
	public IllegalFieldException(int fieldId, String message)
	{
		this(fieldId, REASON_VALUE_INCORRECT, message);
	}

	/**
	 * is equivalent to <code>new IllegalFieldException(fieldId, reason, "");</code>
	 * @param fieldId
	 * @param reason
	 * @see IllegalFieldException#IllegalFieldException(int, int, String) IllegalFieldException(int fieldId, int reason, String message)
	 */
	public IllegalFieldException(int fieldId, int reason)
	{
		this(fieldId, reason, "");
	}
	
	/**
	 * To create an exception to indicate a field is illegal.
	 * @param fieldId the constant of this class matching with the concerned field.
	 * @param reason The constant of this class (prefixed by <code>REASON_</code>)
	 * @param message an additionnal message if the reason is incorrect field (can be an empty string).
	 */
	public IllegalFieldException(int fieldId, int reason, String message)
	{
		super(message != null ? message : "");
		this.reason = reason;
		this.fieldId = fieldId;
	}
	
	/**
	 * To get the id of the illegal field. One of the constants defined in this class
	 * @return
	 */
	public int getFieldId()
	{
		return fieldId;
	}

	/**
	 * To get the reason why the field is illegal. One of the constants (prefixed by REASON_) defined in this class.
	 * @return
	 */
	public int getReason()
	{
		return reason;
	}
	
}
