package jsimple.util;

/**
 * This is a generic exception class used to indicate the format of something isn't what it's excepted to be.
 *
 * @author Bret Johnson
 * @since 5/5/13 4:48 AM
 */
public class InvalidFormatException extends RuntimeException {
    public InvalidFormatException(String message) {
        super(message);
    }

    public InvalidFormatException(String message, Throwable cause) {
        super(message, cause);
    }
}
