package jsimple.io;

/**
 * This is a platform independent class used to represent file not found exceptions, taking the place of
 * java.io.FileNotFoundException in JSimple based code.
 *
 * @author Bret Johnson
 * @since 10/9/12 12:15 PM
 */
public class PathNotFoundException extends IOException {
    public PathNotFoundException(Throwable cause) {
        super(cause);
    }

    public PathNotFoundException(String message) {
        super(message);
    }

    public PathNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
