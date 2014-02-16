namespace jsimple.json.text
{

	/// <summary>
	/// @author Bret Johnson
	/// @since 5/6/12 2:13 AM
	/// </summary>
	public sealed class JsonParsingException : JsonException
	{
		public JsonParsingException(string message) : base(message)
		{
		}

		public JsonParsingException(string expected, string encountered) : base("Expected {} but encountered {}", expected, encountered)
		{
		}

		public JsonParsingException(string expected, Token token) : this(expected, token.Description)
		{
		}
	}

}