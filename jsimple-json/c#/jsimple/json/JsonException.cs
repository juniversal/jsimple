using System;

namespace jsimple.json
{

	using BasicException = jsimple.util.BasicException;

	/// <summary>
	/// @author Bret Johnson
	/// @since 9/15/12 5:32 PM
	/// </summary>
	public class JsonException : BasicException
	{
		public JsonException(Exception cause) : base(cause)
		{
		}

		public JsonException(string message) : base(message)
		{
		}

		public JsonException(string message, object arg1) : base(message, arg1)
		{
		}

		public JsonException(string message, object arg1, object arg2) : base(message, arg1, arg2)
		{
		}

		public JsonException(string message, params object[] args) : base(message, args)
		{
		}
	}

}