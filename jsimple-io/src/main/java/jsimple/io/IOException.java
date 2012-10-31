package jsimple.io;

/**
 * This is a platform independent class used to represent I/O exceptions, taking the place of java.io.IOException in
 * JSimple based code.
 *
 * @author Bret Johnson
 * @since 10/6/12 5:27 PM
 */
public class IOException extends RuntimeException {
    public IOException(Throwable cause) {
        // TODO: Test to see what cause.toString ends up with in C#
        this(cause.toString(), cause);
    }

    public IOException(String message) {
        super(message);
    }

    public IOException(String message, Throwable cause) {
        super(message, cause);
    }
}
