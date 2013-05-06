using System;

namespace jsimple.util
{

	/// <summary>
	/// This is a generic exception class used to indicate the format of something isn't what it's excepted to be.
	/// 
	/// @author Bret Johnson
	/// @since 5/5/13 4:48 AM
	/// </summary>
	public class InvalidFormatException : Exception
	{
		public InvalidFormatException(string message) : base(message)
		{
		}

		public InvalidFormatException(string message, Exception cause) : base(message, cause)
		{
		}
	}

}