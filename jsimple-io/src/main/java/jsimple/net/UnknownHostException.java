package jsimple.net;

import jsimple.io.IOException;

/**
 * This is a platform independent class used to represent I/O exceptions, taking the place of java.io.IOException in
 * JSimple based code.
 *
 * @author Bret Johnson
 * @since 10/6/12 5:27 PM
 */
public class UnknownHostException extends IOException {
    public UnknownHostException(Throwable cause) {
        super(cause);
    }

    public UnknownHostException(String message) {
        super(message);
    }

    public UnknownHostException(String message, Throwable cause) {
        super(message, cause);
    }
}
