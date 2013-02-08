namespace jsimple.json
{

	/// <summary>
	/// @author Bret Johnson
	/// @since 5/6/12 2:13 AM
	/// </summary>
	public sealed class JsonParsingException : JsonException
	{
		internal JsonParsingException(string expected, string encountered, Token token) : base("Expected " + expected + " but encountered " + encountered)
		{
		}

		internal JsonParsingException(string message, Token token) : base(message)
		{
		}
	}

}