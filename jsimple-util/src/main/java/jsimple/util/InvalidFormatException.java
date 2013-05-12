package jsimple.util;

/**
 * This is a generic exception class used to indicate the format of something isn't what it's excepted to be.
 *
 * @author Bret Johnson
 * @since 5/5/13 4:48 AM
 */
public class InvalidFormatException extends BasicException {
    public InvalidFormatException(String message) {
        super(message);
    }

    public InvalidFormatException(String message, Object arg1) {
        super(message, arg1);
    }

    public InvalidFormatException(String message, Object arg1, Object arg2) {
        super(message, arg1, arg2);
    }

    public InvalidFormatException(String message, Object... args) {
        super(message, args);
    }
}
