using System;

namespace jsimple.net
{

	using OutputStream = jsimple.io.OutputStream;


	/// <summary>
	/// This class handles HTTP connectivity.  It's based on a subset of the standard Java java.net.HttpURLConnection class,
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
	/// @since 10/6/12 12:58 AM
	/// </summary>
	public abstract class HttpRequest
	{
		// Common HTTP methods
		public const string METHOD_GET = "GET";
		public const string METHOD_POST = "POST";
		public const string METHOD_PUT = "PUT";
		public const string METHOD_DELETE = "DELETE";

		// Common HTTP request headers
		public const string HEADER_CONTENT_LENGTH = "Content-Length";
		public const string HEADER_CONTENT_TYPE = "Content-Type";
		public const string HEADER_USER_AGENT = "User-Agent";
		public const string HEADER_ACCEPT = "Accept";

		private static volatile HttpRequestFactory factory;

		/// <summary>
		/// Create an HttpRequest, using the global factory.  This method is the normal way to create an HttpRequest, using
		/// the default implementation appropriate for the current platform.
		/// </summary>
		/// <param name="url"> URL </param>
		/// <returns> HttpRequest object </returns>
		public static HttpRequest create(string url)
		{
			if (factory == null)
				throw new Exception("HttpRequest factory isn't set; did you forget to call JSimpleIO.init()?");
			return factory.createHttpRequest(url);
		}

		/// <summary>
		/// Set the global (default) factory used to create http requests.  Clients normally don't call this method directly
		/// and just call JSimpleIO.init at app startup instead, which sets the factory to the default implementation
		/// appropriate for the current platform.
		/// </summary>
		/// <param name="httpRequestFactory"> factory </param>
		public static HttpRequestFactory Factory
		{
			set
			{
				factory = value;
			}
		}

		/// <summary>
		/// Set the method for the request, one of:
		/// <p/>
		/// GET POST HEAD OPTIONS PUT DELETE TRACE
		/// <p/>
		/// are legal, subject to protocol restrictions.  The default method is GET.
		/// </summary>
		/// <param name="method"> the HTTP method </param>
		public abstract string Method {set;}

		/// <summary>
		/// Sets a specified timeout value, in milliseconds, to be used when doing connecting/reading/writing to the server.
		/// A timeout of zero is interpreted as an infinite timeout.  If the timeout expires before the operation is
		/// complete, an exception is raised (e.g. java.net.SocketTimeoutException for the Java version for connect
		/// timeouts).
		/// <p/>
		/// For now, we just have one timeout value rather than multiple settings per operation type, as that makes cross
		/// platform support easier and is generally good enough for clients.
		/// </summary>
		/// <param name="timeoutInMillis"> an <code>int</code> that specifies the connect timeout value in milliseconds </param>
		public abstract int Timeout {set;}

		/// <summary>
		/// Sets the header. If a header with the key already exists, overwrite its value with the new value.
		/// <p/>
		/// NOTE: HTTP requires all request headers which can legally have multiple instances with the same key to use a
		/// comma-separated list syntax which enables multiple values to be appended into a single header.  This method
		/// replaces an existing header with the specified name; get the existing header and append to it if you wish to make
		/// it multi-valued.
		/// </summary>
		/// <param name="name">  the header name (e.g., "<code>Accept</code>") </param>
		/// <param name="value"> the value associated with it </param>
		public abstract void setHeader(string name, string value);

		/// <summary>
		/// Returns the value of the named header or null if it's not set.
		/// </summary>
		/// <param name="name"> the header name (e.g., "Accept"). </param>
		/// <returns> the value of the header </returns>
		public abstract string getHeader(string name);

		/// <summary>
		/// Returns an output stream that writes to this connection.  If your app needs to set the value of the ContentLength
		/// header, then this must be done before retrieving the stream (which, for one thing, is enforced on .NET).  You
		/// must close the stream after you finish writing to it; failing to do that may make the system run out of
		/// connections (another .NET warning & good practice in general).
		/// </summary>
		/// <returns> an output stream that writes to this connection. </returns>
		/// <exception cref="jsimple.io.IOException"> if an I/O error occurs while opening the connection. </exception>
		public abstract OutputStream createRequestBodyStream();

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
		/// <exception cref="jsimple.net.SocketTimeoutException"> if the timeout expires before the connection can be established </exception>
		/// <exception cref="jsimple.io.IOException">             if an I/O error occurs while opening the connection. </exception>
		/// <seealso cref= #setTimeout </seealso>
		public abstract HttpResponse send();

		public interface HttpRequestFactory
		{
			HttpRequest createHttpRequest(string url);
		}
	}
}