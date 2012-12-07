package jsimple.net;

import jsimple.io.InputStream;
import org.jetbrains.annotations.Nullable;

/**
 * This class handles http connectivity.  It's based on a subset of the standard Java java.net.HttpURLConnection class,
 * with some changes (to improve names, get rid of checked exceptions, etc.).
 * <p/>
 * From the doc for java.net.HttpURLConnection:  Each instance of this class is used to make a single request but the
 * underlying network connection to the HTTP server may be transparently shared by other instances. Calling the close()
 * methods on the InputStream or OutputStream of an HttpURLConnection after a request may free network resources
 * associated with this instance but has no effect on any shared persistent connection. Calling the disconnect() method
 * may close the underlying socket if a persistent connection is otherwise idle at that time.
 * <p/>
 * This class is platform independent.  Platforms (Java, C# for .Net, etc.) should define a subclass called
 * "HttpRequest" which is what all the code will instantiate and use.  That subclass should implement the abstract
 * methods below and can implement other, platform specific, methods.  Handling platform specific classes this way has a
 * few advantages:  There are no factory classes to define/implement nor a need to set a factory at app startup. And
 * platform specific code can freely use platform specific methods implemented on the subclass with no casting
 * required.
 *
 * @author Bret Johnson
 * @since 10/6/12 12:58 AM
 */
public abstract class HttpResponseBase {
    /**
     * Gets the status code from an HTTP response message (e.g. 200, 401, etc.).  Returns -1 if no code can be discerned
     * from the response (i.e., the response is not valid HTTP).
     *
     * @return the HTTP Status-Code, or -1
     */
    public abstract int getStatusCode();

    /**
     * Gets the message from the HTTP response status line--the "reason phrase" (which is what the RFC calls it)
     * describing the error that comes after the status code.
     *
     * @return the HTTP reason phrase, from the status line
     */
    public abstract String getStatusMessage();

    /**
     * Returns an input stream that reads from this open connection.
     * <p/>
     * A SocketTimeoutException can be thrown when reading from the returned input stream if the read timeout expires
     * before data is available for read.
     *
     * @return an input stream that reads from this open connection
     * @throws jsimple.io.IOException if an I/O error occurs while creating the input stream
     */
    public abstract InputStream getBodyStream();

    /**
     * Get the value of the specified header or null if the header isn't present.
     * <p/>
     * TODO: Handle multivalued headers by returning, in most cases, the first value--that's at least what the Scribe
     * code does.
     *
     * @param headerName HTTP header name
     * @return header value or null if not present
     */
    public abstract @Nullable String getHeader(String headerName);
}