using System;

namespace jsimple.json
{

	/// <summary>
	/// @author Bret Johnson
	/// @since 9/15/12 5:32 PM
	/// </summary>
	public class JsonException : Exception
	{
		internal JsonException(string message) : base(message)
		{
		}
	}

}