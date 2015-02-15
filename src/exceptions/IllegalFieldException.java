package exceptions;

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

	
	// This constructor will be removed.
	public IllegalFieldException(int fieldId, String message)
	{
		this(fieldId, REASON_VALUE_INCORRECT, message);
	}

	public IllegalFieldException(int fieldId, int reason)
	{
		this(fieldId, reason, "");
	}
	
	
	public IllegalFieldException(int fieldId, int reason, String message)
	{
		super(message);
		this.reason = reason;
		this.fieldId = fieldId;
	}
	
	public int getFieldId()
	{
		return fieldId;
	}

	public int getReason()
	{
		return reason;
	}
	
}
