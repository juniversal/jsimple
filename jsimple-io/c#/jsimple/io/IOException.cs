using System;

namespace jsimple.io
{

	using BasicException = jsimple.util.BasicException;

	/// <summary>
	/// This is a platform independent class used to represent I/O exceptions, taking the place of java.io.IOException in
	/// JSimple based code.
	/// 
	/// @author Bret Johnson
	/// @since 10/6/12 5:27 PM
	/// </summary>
	public class IOException : BasicException
	{
		public IOException(Exception cause) : base(cause)
		{
		}

		public IOException(string message) : base(message)
		{
		}

		public IOException(string message, object arg1) : base(message, arg1)
		{
		}

		public IOException(string message, object arg1, object arg2) : base(message, arg1, arg2)
		{
		}

		public IOException(string message, params object[] args) : base(message, args)
		{
		}
	}

}