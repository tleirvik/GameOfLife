package model.gameoflife;

/**
 * This class is a Throwable that indicates that an error in interpreting a file or
 * a stream of RLE format
 *
 * @see    java.lang.Error
 */
public class PatternFormatException extends Exception {

	/**
	 *
	 */
	private static final long serialVersionUID = -8709309769970592942L;

	/**
	 *  Constructs a new exception with null as its message.
	 */
	public PatternFormatException() {
		super();
	}

	/**
	 *  Constructs a new exception with the specified message.
	 *
	 * @param message the detail message. Retrieved with the getMessage() method
	 */
	public PatternFormatException(String message) {
		super(message);
	}
	/**
	 *  Constructs a new exception with the specified message and cause.
	 *
	 * @param message the detail message. Retrieved with the getMessage() method
	 * @param cause the cause. Retrieved with the getCause() method
	 */
	public PatternFormatException(String message, Throwable cause) {
        super(message, cause);
    }

	/**
	 *  Constructs a new exception with the specified cause.
	 * @param cause the cause. Retrieved with the getCause() method
	 */
	public PatternFormatException(Throwable cause) {
        super(cause);
    }
}
