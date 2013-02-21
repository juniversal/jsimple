using System;

namespace jsimple.net
{

	using IOException = jsimple.io.IOException;

	/// <summary>
	/// This is a platform independent class used to represent I/O exceptions, taking the place of java.io.IOException in
	/// JSimple based code.
	/// 
	/// @author Bret Johnson
	/// @since 10/6/12 5:27 PM
	/// </summary>
	public class UnknownHostException : IOException
	{
		public UnknownHostException(Exception cause) : base(cause)
		{
		}

		public UnknownHostException(string message) : base(message)
		{
		}

		public UnknownHostException(string message, Exception cause) : base(message, cause)
		{
		}
	}

}