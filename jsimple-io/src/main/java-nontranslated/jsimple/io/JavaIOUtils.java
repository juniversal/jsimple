package jsimple.io;

import jsimple.net.SocketTimeoutException;
import jsimple.net.UnknownHostException;

/**
 * @author Bret Johnson
 * @since 10/7/12 12:53 AM
 */
public class JavaIOUtils {

    /**
     * Map a Java IOException to the matching JSimple platform independent exception.  If there's a specific
     * exception type for the subclass (because it's an error we're likely to care about), return that.  Otherwise,
     * just return a JSimple IOException.
     *
     * @param e Java IOException (or a subclass)
     * @return JSimple IOException (or a subclass)
     */
    public static RuntimeException jSimpleExceptionFromJavaIOException(java.io.IOException e) {
        if (e instanceof java.net.SocketTimeoutException)
            return new SocketTimeoutException(e);
        else if (e instanceof java.net.UnknownHostException)
            return new UnknownHostException(e);
        else if (e instanceof java.io.FileNotFoundException)
            return new FileNotFoundException(e);
        else return new IOException(e);
    }
}
