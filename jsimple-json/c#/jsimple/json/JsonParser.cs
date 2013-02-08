namespace jsimple.json
{

	/// <summary>
	/// @author Bret Johnson
	/// @since 5/6/12 12:17 AM
	/// </summary>
	public sealed class JsonParser
	{
		private Token token;

		/// <summary>
		/// Parse the specified JSON text.  getResult() can then be used to get the resulting JSON result tree. </summary>
		/// <param name="text"> JSON text to parse </param>
		public JsonParser(string text)
		{
			this.token = new Token(text);
		}

		public JsonObjectOrArray parseRoot()
		{
			TokenType lookahead = token.Type;

			JsonObjectOrArray result;
			if (lookahead == TokenType.LEFT_BRACE)
				result = parseObject();
			else if (lookahead == TokenType.LEFT_BRACKET)
				result = parseArray();
			else
				throw new JsonParsingException("{ or [, starting an object or array", token.Description, token);

			check(TokenType.EOF);

			return result;
		}

		public JsonObject parseObject()
		{
			JsonObject jsonObject = new JsonObject();

			checkAndAdvance(TokenType.LEFT_BRACE);

			// Handle the empty object case here (which makes the code below simpler)
			if (token.Type == TokenType.RIGHT_BRACE)
			{
				advance();
				return jsonObject;
			}

			while (true)
			{
				object nameObject = token.PrimitiveValue;

				if (!(nameObject is string))
					throw new JsonParsingException("string for object key", token.Description, token);

				string name = (string) nameObject;
				advance();

				checkAndAdvance(TokenType.COLON);

				object value = parseValue();

				jsonObject.add(name, value);

				if (token.Type == TokenType.RIGHT_BRACE)
				{
					advance();
					break;
				}
				else if (token.Type == TokenType.COMMA)
					advance();
				else
					throw new JsonParsingException(", or }", token.Description, token);
			}

			return jsonObject;
		}

		public object parseValue()
		{
			object value;

			TokenType lookahead = token.Type;
			if (lookahead == TokenType.PRIMITIVE)
			{
				value = token.PrimitiveValue;
				advance();
			}
			else if (lookahead == TokenType.LEFT_BRACE)
				value = parseObject();
			else if (lookahead == TokenType.LEFT_BRACKET)
				value = parseArray();
			else
				throw new JsonParsingException("primitive type, object, or array", token.Description, token);

			return value;
		}

		public JsonArray parseArray()
		{
			JsonArray jsonArray = new JsonArray();

			checkAndAdvance(TokenType.LEFT_BRACKET);

			// Handle the empty array case here (which makes the code below simpler)
			if (token.Type == TokenType.RIGHT_BRACKET)
			{
				advance();
				return jsonArray;
			}

			while (true)
			{
				object value = parseValue();

				jsonArray.add(value);

				if (token.Type == TokenType.RIGHT_BRACKET)
				{
					advance();
					break;
				}
				else if (token.Type == TokenType.COMMA)
					advance();
				else
					throw new JsonParsingException(", or ]", token.Description, token);
			}

			return jsonArray;
		}

		/// <summary>
		/// Validate that the token is the expected type (throwing an exception if it isn't), then advance to the next
		/// token.
		/// </summary>
		/// <param name="expectedType"> expected token type </param>
		public void checkAndAdvance(TokenType expectedType)
		{
			check(expectedType);
			token.advance();
		}

		/// <summary>
		/// Validate that the token is the expected type (throwing an exception if it isn't).
		/// </summary>
		/// <param name="expectedType"> expected token type </param>
		public void check(TokenType expectedType)
		{
			if (token.Type != expectedType)
				throw new JsonParsingException(Token.getTokenTypeDescription(expectedType), token.Description, token);
		}

		/// <summary>
		/// Advance to the next token.
		/// </summary>
		public void advance()
		{
			token.advance();
		}

		public TokenType TokenType
		{
			get
			{
				return token.Type;
			}
		}
	}

}