using System;

namespace jsimple.io
{

	/// <summary>
	/// This is a platform independent class used to represent character conversion exceptions (e.g. for UTF-8 decoding),
	/// taking the place of java.io.CharConversionException in JSimple based code.
	/// 
	/// @author Bret Johnson
	/// @since 10/6/12 5:27 PM
	/// </summary>
	public class CharConversionException : IOException
	{
		public CharConversionException(Exception cause) : base(cause)
		{
		}

		public CharConversionException(string message) : base(message)
		{
		}

		public CharConversionException(string message, Exception cause) : base(message, cause)
		{
		}
	}

}