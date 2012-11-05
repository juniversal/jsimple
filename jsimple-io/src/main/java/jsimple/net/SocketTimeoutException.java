package jsimple.net;

import jsimple.io.IOException;

/**
 * This is a platform independent class used to represent network timeout exceptions, taking the place of
 * java.net.SocketTimeoutException in JSimple based code.
 *
 * @author Bret Johnson
 * @since 10/6/12 5:27 PM
 */
public class SocketTimeoutException extends IOException {
    public SocketTimeoutException(Throwable cause) {
        // TODO: Test to see what cause.toString ends up with in C#
        this(cause.toString(), cause);
    }

    public SocketTimeoutException(String message) {
        super(message);
    }

    public SocketTimeoutException(String message, Throwable cause) {
        super(message, cause);
    }
}
