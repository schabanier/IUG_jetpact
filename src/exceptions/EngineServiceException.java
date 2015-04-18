package exceptions;

/**
 * Created by propriétaire on 15/03/2015.
 */
public class EngineServiceException extends Exception {
	
	private static final long serialVersionUID = 3668425089554446718L;

	/**
     * Constructs a new {@code Exception} that includes the current stack trace.
     */
    public EngineServiceException() {
    }

    /**
     * Constructs a new {@code Exception} with the current stack trace and the
     * specified detail message.
     *
     * @param detailMessage the detail message for this exception.
     */
    public EngineServiceException(String detailMessage) {
        super(detailMessage);
    }
}
