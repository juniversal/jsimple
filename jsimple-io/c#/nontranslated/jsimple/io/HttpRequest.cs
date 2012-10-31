using System.Collections.Generic;

namespace jsimple.io
{


    /// <summary>
    /// This class handles http connectivity.  It's based on a subset of the standard Java java.net.HttpURLConnection class,
    /// with some changes (to improve names, get rid of checked exceptions, etc.).
    /// <p/>
    /// From the doc for java.net.HttpURLConnection:  Each instance of this class is used to make a single request but the
    /// underlying network connection to the HTTP server may be transparently shared by other instances. Calling the close()
    /// methods on the InputStream or OutputStream of an HttpURLConnection after a request may free network resources
    /// associated with this instance but has no effect on any shared persistent connection. Calling the disconnect() method
    /// may close the underlying socket if a persistent connection is otherwise idle at that time.
    /// <p/>
    /// This class is platform independent.  Platforms (Java, C# for .Net, etc.) should define a subclass called
    /// "HttpRequest" which is what all the code will instantiate and use.  That subclass should implement the abstract
    /// methods below and can implement other, platform specific, methods.  Handling platform specific classes this way has a
    /// few advantages:  There are no factory classes to define/implement nor a need to set a factory at app startup. And
    /// platform specific code can freely use platform specific methods implemented on the subclass with no casting
    /// required.
    /// 
    /// @author Bret Johnson
    /// @since 10/24/12 2:39 AM
    /// </summary>
    public class HttpRequest : HttpRequestBase
    {
        public HttpRequest(string url)
        {
            // TODO: IMPLEMENT
        }

        /// <summary>
        /// Set the method for the request, one of:
        /// <p/>
        /// GET POST HEAD OPTIONS PUT DELETE TRACE
        /// <p/>
        /// are legal, subject to protocol restrictions.  The default method is GET.
        /// </summary>
        /// <param name="method"> the HTTP method </param>
        public abstract string Method { set; }

        /// <summary>
        /// Sets a specified timeout value, in milliseconds, to be used when opening a communications link to the resource
        /// referenced by this URLConnection.  If the timeout expires before the connection can be established, an exception
        /// is raised (java.net.SocketTimeoutException for Java version). A timeout of zero is interpreted as an infinite
        /// timeout.
        /// </summary>
        /// <param name="timeoutInMillis"> an <code>int</code> that specifies the connect timeout value in milliseconds </param>
        /// <seealso cref= #connect() </seealso>
        public abstract int ConnectTimeout { set; }

        /// <summary>
        /// Sets the read timeout to a specified timeout, in milliseconds. A non-zero value specifies the timeout when
        /// reading from Input stream when a connection is established to a resource. If the timeout expires before there is
        /// data available for read, an exception (java.net.SocketTimeoutException for Java version) is raised. A timeout of
        /// zero is interpreted as an infinite timeout.
        /// </summary>
        /// <param name="timeoutInMillis"> an <code>int</code> that specifies the timeout value to be used in milliseconds </param>
        /// <seealso cref= java.io.InputStream#read() </seealso>
        public abstract int ReadTimeout { set; }

        /// <summary>
        /// Sets the header. If a header with the key already exists, overwrite its value with the new value.  This must be
        /// called before this instance is connected; if called after, an exception is thrown.
        /// <p/>
        /// NOTE: HTTP requires all request headers which can legally have multiple instances with the same key to use a
        /// comma-separated list syntax which enables multiple values to be appended into a single header.
        /// </summary>
        /// <param name="key">   the keyword by which the request is known (e.g., "<code>Accept</code>"). </param>
        /// <param name="value"> the value associated with it. </param>
        public abstract void setHeader(string key, string value);

        /// <summary>
        /// Returns the value of the named header.  This must be called before this instance is connected; if called after,
        /// an exception is thrown.
        /// </summary>
        /// <param name="key"> the keyword by which the request is known (e.g., "Accept"). </param>
        /// <returns> the value of the header </returns>
        public abstract string getHeader(string key);

        /// <summary>
        /// Sets the value of the <code>doOutput</code> field for this instance to the specified value.  This must be called
        /// before this instance is connected; if called after, an exception is thrown.
        /// <p/>
        /// An HttpUrlConect can be used for input and/or output.  Set the DoOutput flag to true if you intend to use the URL
        /// connection for output, false if not.  The default is false.
        /// </summary>
        /// <param name="dooutput"> the new value. </param>
        /// <exception cref="IllegalStateException"> if already connected </exception>
        public abstract bool DoOutput { set; }

        /// <summary>
        /// Returns an output stream that writes to this connection.
        /// </summary>
        /// <returns> an output stream that writes to this connection. </returns>
        /// <exception cref="IOException"> if an I/O error occurs while opening the connection. </exception>
        public abstract OutputStream OutputStream { get; }

        /// <summary>
        /// Opens a communications link to the resource referenced by this URL, if such a connection has not already been
        /// established.
        /// <p/>
        /// If the <code>connect</code> method is called when the connection has already been opened.
        /// <p/>
        /// HttpRequest objects go through two phases: first they are created, then they are connected.  After being created,
        /// and before being connected, various options can be specified (e.g., doInput and UseCaches).  After connecting, it
        /// is an error to try to set them.  Operations that depend on being connected, like getContentLength, will
        /// implicitly perform the connection, if necessary.
        /// </summary>
        /// <exception cref="SocketTimeoutException"> if the timeout expires before the connection can be established </exception>
        /// <exception cref="IOException">            if an I/O error occurs while opening the connection. </exception>
        /// <seealso cref= #setConnectTimeout(int) </seealso>
        public abstract void connect();

        /// <summary>
        /// Gets the status code from an HTTP response message (e.g. 200, 401, etc.).  Returns -1 if no code can be discerned
        /// from the response (i.e., the response is not valid HTTP).  Throws an exception if could not connect to the
        /// server.
        /// </summary>
        /// <returns> the HTTP Status-Code, or -1 </returns>
        /// <exception cref="IOException"> if an error occurred connecting to the server </exception>
        public abstract int ResponseCode { get; }

        /// <summary>
        /// Returns an unmodifiable Map of the header fields. The Map keys are Strings that represent the response-header
        /// field names. Each Map value is an unmodifiable List of Strings that represents the corresponding field values.
        /// </summary>
        /// <returns> a Map of header fields
        /// @since 1.4 </returns>
        public abstract IDictionary<string, IList<string>> HeaderFields { get; }


        /// <summary>
        /// Returns an input stream that reads from this open connection.
        /// <p/>
        /// A SocketTimeoutException can be thrown when reading from the returned input stream if the read timeout expires
        /// before data is available for read.
        /// </summary>
        /// <returns> an input stream that reads from this open connection. </returns>
        /// <exception cref="IOException"> if an I/O error occurs while creating the input stream. </exception>
        /// <seealso cref= #setReadTimeout(int) </seealso>
        public abstract InputStream InputStream { get; }

        /// <summary>
        /// Returns the error stream if the connection failed but the server sent useful data nonetheless. The typical
        /// example is when an HTTP server responds with a 404, which will cause a FileNotFoundException to be thrown in
        /// connect, but the server sent an HTML help page with suggestions as to what to do.
        /// <p/>
        /// <p>This method will not cause a connection to be initiated.  If the connection was not connected, or if the
        /// server did not have an error while connecting or if the server had an error but no error data was sent, this
        /// method will return null. This is the default.
        /// </summary>
        /// <returns> an error stream if any, null if there have been no errors, the connection is not connected or the server
        ///         sent no useful data. </returns>
        public abstract InputStream ErrorStream { get; }
    }
}