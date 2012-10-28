package jsimple.net;

import jsimple.io.InputStream;
import jsimple.io.OutputStream;

import java.net.UnknownServiceException;
import java.util.List;
import java.util.Map;

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
public abstract class HttpRequestBase {
    // Common HTTP methods
    public final static String METHOD_GET = "GET";
    public final static String METHOD_POST = "POST";
    public final static String METHOD_PUT = "PUT";
    public final static String METHOD_DELETE = "DELETE";

    /*
    The HttpRequest subclasses should implement at least this constructor:
    public HttpRequest(String url, boolean keepAlive);
    */

    /**
     * Set the method for the request, one of:
     * <p/>
     * GET POST HEAD OPTIONS PUT DELETE TRACE
     * <p/>
     * are legal, subject to protocol restrictions.  The default method is GET.
     *
     * @param method the HTTP method
     */
    public abstract void setMethod(String method);

    /**
     * Sets a specified timeout value, in milliseconds, to be used when opening a communications link to the resource
     * referenced by this URLConnection.  If the timeout expires before the connection can be established, an exception
     * is raised (java.net.SocketTimeoutException for Java version). A timeout of zero is interpreted as an infinite
     * timeout.
     *
     * @param timeoutInMillis an <code>int</code> that specifies the connect timeout value in milliseconds
     * @see #connect()
     */
    public abstract void setConnectTimeout(int timeoutInMillis);

    /**
     * Sets the read timeout to a specified timeout, in milliseconds. A non-zero value specifies the timeout when
     * reading from Input stream when a connection is established to a resource. If the timeout expires before there is
     * data available for read, an exception (java.net.SocketTimeoutException for Java version) is raised. A timeout of
     * zero is interpreted as an infinite timeout.
     *
     * @param timeoutInMillis an <code>int</code> that specifies the timeout value to be used in milliseconds
     * @see java.io.InputStream#read()
     */
    public abstract void setReadTimeout(int timeoutInMillis);

    /**
     * Sets the header. If a header with the key already exists, overwrite its value with the new value.  This must be
     * called before this instance is connected; if called after, an exception is thrown.
     * <p/>
     * NOTE: HTTP requires all request headers which can legally have multiple instances with the same key to use a
     * comma-separated list syntax which enables multiple values to be appended into a single header.
     *
     * @param key   the keyword by which the request is known (e.g., "<code>Accept</code>").
     * @param value the value associated with it.
     */
    public abstract void setHeader(String key, String value);

    /**
     * Returns the value of the named header.  This must be called before this instance is connected; if called after,
     * an exception is thrown.
     *
     * @param key the keyword by which the request is known (e.g., "Accept").
     * @return the value of the header
     */
    public abstract String getHeader(String key);

    /**
     * Sets the value of the <code>doOutput</code> field for this instance to the specified value.  This must be called
     * before this instance is connected; if called after, an exception is thrown.
     * <p/>
     * An HttpUrlConect can be used for input and/or output.  Set the DoOutput flag to true if you intend to use the URL
     * connection for output, false if not.  The default is false.
     *
     * @param dooutput the new value.
     * @throws IllegalStateException if already connected
     */
    public abstract void setDoOutput(boolean dooutput);

    /**
     * Returns an output stream that writes to this connection.
     *
     * @return an output stream that writes to this connection.
     * @throws jsimple.io.IOException if an I/O error occurs while opening the connection.
     */
    public abstract OutputStream getOutputStream();

    /**
     * Opens a communications link to the resource referenced by this URL, if such a connection has not already been
     * established.
     * <p/>
     * If the <code>connect</code> method is called when the connection has already been opened.
     * <p/>
     * HttpRequest objects go through two phases: first they are created, then they are connected.  After being created,
     * and before being connected, various options can be specified (e.g., doInput and UseCaches).  After connecting, it
     * is an error to try to set them.  Operations that depend on being connected, like getContentLength, will
     * implicitly perform the connection, if necessary.
     *
     * @throws jsimple.io.SocketTimeoutException if the timeout expires before the connection can be established
     * @throws jsimple.io.IOException            if an I/O error occurs while opening the connection.
     * @see #setConnectTimeout(int)
     */
    public abstract void connect();

    /**
     * Gets the status code from an HTTP response message (e.g. 200, 401, etc.).  Returns -1 if no code can be discerned
     * from the response (i.e., the response is not valid HTTP).  Throws an exception if could not connect to the
     * server.
     *
     * @return the HTTP Status-Code, or -1
     * @throws jsimple.io.IOException if an error occurred connecting to the server
     */
    public abstract int getResponseCode();

    /**
     * Returns an unmodifiable Map of the header fields. The Map keys are Strings that represent the response-header
     * field names. Each Map value is an unmodifiable List of Strings that represents the corresponding field values.
     *
     * @return a Map of header fields
     * @since 1.4
     */
    public abstract Map<String, List<String>> getHeaderFields();


    /**
     * Returns an input stream that reads from this open connection.
     * <p/>
     * A SocketTimeoutException can be thrown when reading from the returned input stream if the read timeout expires
     * before data is available for read.
     *
     * @return an input stream that reads from this open connection.
     * @throws jsimple.io.IOException if an I/O error occurs while creating the input stream.
     * @see #setReadTimeout(int)
     */
    public abstract InputStream getInputStream();

    /**
     * Returns the error stream if the connection failed but the server sent useful data nonetheless. The typical
     * example is when an HTTP server responds with a 404, which will cause a FileNotFoundException to be thrown in
     * connect, but the server sent an HTML help page with suggestions as to what to do.
     * <p/>
     * <p>This method will not cause a connection to be initiated.  If the connection was not connected, or if the
     * server did not have an error while connecting or if the server had an error but no error data was sent, this
     * method will return null. This is the default.
     *
     * @return an error stream if any, null if there have been no errors, the connection is not connected or the server
     *         sent no useful data.
     */
    public abstract InputStream getErrorStream();
}