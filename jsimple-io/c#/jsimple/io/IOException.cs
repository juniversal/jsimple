using System;

namespace jsimple.io
{

	/// <summary>
	/// This is a platform independent class used to represent I/O exceptions, taking the place of java.io.IOException in
	/// JSimple based code.
	/// 
	/// @author Bret Johnson
	/// @since 10/6/12 5:27 PM
	/// </summary>
	public class IOException : Exception
	{
		public IOException(Exception cause) : this(cause.ToString(), cause)
		{
			// TODO: Test to see what cause.toString ends up with in C#
		}

		public IOException(string message) : base(message)
		{
		}

		public IOException(string message, Exception cause) : base(message, cause)
		{
		}
	}

}