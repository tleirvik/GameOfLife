package GameOfLife;

/**
 * This class is a Throwable that indicates that an error in interpreting a file or
 * a stream of RLE format
 *
 * Denne klasse trenger noe mer funksjonalitet for å forsvare dens eksistens, men
 * siden den er et krav så må vi implementere den.
 *
 * @author tleirvik
 * @see    java.lang.Error
 */
public class PatternFormatException extends Exception {

	/**
	 *
	 */
	private static final long serialVersionUID = -8709309769970592942L;

	public PatternFormatException() {
		super();
	}

	public PatternFormatException(String message) {
		super(message);
	}

	public PatternFormatException(String message, Throwable cause) {
        super(message, cause);
    }

	public PatternFormatException(Throwable cause) {
        super(cause);
    }
}
