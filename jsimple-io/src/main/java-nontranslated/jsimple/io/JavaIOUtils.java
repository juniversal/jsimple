package jsimple.io;

import jsimple.net.SocketTimeoutException;
import jsimple.net.UnknownHostException;

/**
 * @author Bret Johnson
 * @since 10/7/12 12:53 AM
 */
public class JavaIOUtils {

    /**
     * Map a Java IOException to the matching JSimple platform independent exception.  If there's a specific exception
     * type for the subclass (because it's an error we're likely to care about), return that.  Otherwise, just return a
     * JSimple IOException.
     *
     * @param e Java IOException (or a subclass)
     * @return JSimple IOException (or a subclass)
     */
    public static IOException jSimpleExceptionFromJavaIOException(java.io.IOException e) {
        IOException jSimpleIOException;
        String message = e.getMessage();
        if (message == null)
            message = "";

        if (e instanceof java.net.SocketTimeoutException)
            jSimpleIOException = new SocketTimeoutException(message, e);
        else if (e instanceof java.net.UnknownHostException)
            jSimpleIOException = new UnknownHostException(message, e);
        else if (e instanceof java.io.FileNotFoundException)
            jSimpleIOException = new PathNotFoundException(message, e);
        else jSimpleIOException = new IOException(message, e);

        // Replace the stack trace with the original one, so it looks like the original code threw the exception.
        // In some ways that's not technically correct, but it generally makes the stack traces easier to read
        // and more standard Java-like, since the nested exception wrapping doesn't add any real info in error output
        // and in practice tends to make error output a bit harder to understand, not easier
        StackTraceElement[] stackTrace = e.getStackTrace();
        jSimpleIOException.setStackTrace(stackTrace);

        return jSimpleIOException;
    }
}
