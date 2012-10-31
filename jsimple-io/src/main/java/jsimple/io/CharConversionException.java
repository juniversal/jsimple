package jsimple.io;

/**
 * This is a platform independent class used to represent character conversion exceptions (e.g. for UTF-8 decoding),
 * taking the place of java.io.CharConversionException in JSimple based code.
 *
 * @author Bret Johnson
 * @since 10/6/12 5:27 PM
 */
public class CharConversionException extends IOException {
    public CharConversionException(Throwable cause) {
        super(cause);
    }

    public CharConversionException(String message) {
        super(message);
    }

    public CharConversionException(String message, Throwable cause) {
        super(message, cause);
    }
}
