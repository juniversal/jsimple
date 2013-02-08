using System.Text;

namespace jsimple.json
{

	/// <summary>
	/// @author Bret Johnson
	/// @since 5/6/12 12:18 AM
	/// </summary>
	internal sealed class Token
	{
		private string json;
		private int jsonLength;
		private TokenType type;
		private object primitiveValue;
		private int currIndex; // Next character to be processed

		internal Token(string json)
		{
			this.json = json;
			jsonLength = json.Length;
			currIndex = 0;

			// Initialize to avoid warnings
			type = TokenType.PRIMITIVE;
			primitiveValue = JsonNull.singleton;

			advance();
		}

		public TokenType Type
		{
			get
			{
				return type;
			}
		}

		public object PrimitiveValue
		{
			get
			{
				return primitiveValue;
			}
		}

		/// <summary>
		/// Get a user friendly description of the token.  This is typically used for error messages, saying we encountered
		/// such & such, which isn't what we expected.
		/// </summary>
		/// <returns> token description phrase </returns>
		internal string Description
		{
			get
			{
				if (type == TokenType.PRIMITIVE)
				{
					if (primitiveValue == null)
						return "null";
					else if (primitiveValue is int? || primitiveValue is long?)
						return primitiveValue.ToString();
					else if (primitiveValue is bool?)
						return ((bool)(bool?) primitiveValue) ? "true" : "false";
					else if (primitiveValue is string)
						return "\"" + primitiveValue.ToString() + "\"";
					else
						throw new JsonException("Unknown token primitive type");
				}
				else
					return getTokenTypeDescription(type);
			}
		}

		public void advance()
		{
			while (true)
			{
				char lookahead = lookaheadChar();

				switch (lookahead)
				{
					// Skip whitespace
					case ' ':
					case '\t':
					case '\r':
					case '\n':
						++currIndex;
						continue;

					case '{':
						++currIndex;
						type = TokenType.LEFT_BRACE;
						return;

					case '}':
						++currIndex;
						type = TokenType.RIGHT_BRACE;
						return;

					case '[':
						++currIndex;
						type = TokenType.LEFT_BRACKET;
						return;

					case ']':
						++currIndex;
						type = TokenType.RIGHT_BRACKET;
						return;

					case ',':
						++currIndex;
						type = TokenType.COMMA;
						return;

					case ':':
						++currIndex;
						type = TokenType.COLON;
						return;

					case '"':
						primitiveValue = readStringToken();
						type = TokenType.PRIMITIVE;
						return;

					case 't':
						checkAndAdvancePast("true");
						type = TokenType.PRIMITIVE;
						primitiveValue = true;
						return;

					case 'f':
						checkAndAdvancePast("false");
						type = TokenType.PRIMITIVE;
						primitiveValue = false;
						return;

					case 'n':
						checkAndAdvancePast("null");
						type = TokenType.PRIMITIVE;
						primitiveValue = JsonNull.singleton;
						return;

					case '\0':
						type = TokenType.EOF;
						return;
				}

				if (lookahead == '-' || (lookahead >= '0' && lookahead <= '9'))
				{
					primitiveValue = readNumberToken(lookahead);
					type = TokenType.PRIMITIVE;
					return;
				}
				else
					throw new JsonParsingException("Unexpected character '" + lookahead + "' in JSON; if that character should start a string, it must be quoted", this);
			}
		}

		private void checkAndAdvancePast(string expected)
		{
			int length = expected.Length;

			if (currIndex + length > json.Length)
				throw new JsonParsingException(quote(expected), quote(json.Substring(currIndex, json.Length - currIndex)), this);

			string encountered = json.Substring(currIndex, length);

			if (!expected.Equals(encountered))
				throw new JsonParsingException(quote(expected), quote(encountered), this);

			currIndex += length;
		}

		private string readStringToken()
		{
			StringBuilder @string = new StringBuilder();

			++currIndex; // Skip past the leading "

			while (true)
			{
				char c = readChar();

				if (c == '"')
					return @string.ToString();
				else if (c == '\\')
					@string.Append(readEscapedChar());
				else if (JsonUtil.isControlCharacter(c))
					throw new JsonParsingException(charDescription('\"'), charDescription(c), this);
				else
					@string.Append(c);
			}
		}

		private char readEscapedChar()
		{
			char c = readChar();
			switch (c)
			{
				case '\"':
					return '\"';

				case '\\':
					return '\\';

				case '/':
					return '/';

				case 'b':
					return '\b';

				case 'f':
					return '\f';

				case 'n':
					return '\n';

				case 'r':
					return '\r';

				case 't':
					return '\t';

				case 'u':
					return readUnicodeChar();
			}

			if (JsonUtil.isControlCharacter(c))
				throw new JsonParsingException("Invalid escape character code following backslash: " + charDescription(c), this);
			else
				throw new JsonParsingException("Invalid character escape: '\\" + c + "'", this);
		}

		private char lookaheadChar()
		{
			if (currIndex >= jsonLength)
				return '\0';
			return json[currIndex];
		}

		private char readChar()
		{
			if (currIndex >= jsonLength)
				return '\0';
			return json[currIndex++];
		}

		private char readUnicodeChar()
		{
			int value = 0;
			for (int i = 0; i < 4; ++i)
				value = value * 16 + readHexDigit();

			return (char) value;
		}

		private int readHexDigit()
		{
			char digitChar = readChar();

			if (digitChar >= '0' && digitChar <= '9')
				return digitChar - '0';
			else if (digitChar >= 'a' && digitChar <= 'f')
				return 10 + (digitChar - 'a');
			else if (digitChar >= 'A' && digitChar <= 'F')
				return 10 + (digitChar - 'A');
			else
				throw new JsonParsingException("valid hex digit for unicode escape", charDescription(digitChar), this);
		}

		private string charDescription(char c)
		{
			if (c == '\0')
				return "end of JSON text";
			else if (c == '\r')
				return "carriage return";
			else if (c == '\n')
				return "newline";
			else if (c == '\t')
				return "tab";
			else if (JsonUtil.isControlCharacter(c))
			{
				int remaining = c;

				int digit4 = remaining % 16;
				remaining /= 16;

				int digit3 = remaining % 16;
				remaining /= 16;

				int digit2 = remaining % 16;
				remaining /= 16;

				int digit1 = remaining;

				StringBuilder description = new StringBuilder("\\u");
				description.Append(numToHexDigitChar(digit1));
				description.Append(numToHexDigitChar(digit2));
				description.Append(numToHexDigitChar(digit3));
				description.Append(numToHexDigitChar(digit4));
				return description.ToString();
			}
			else
				return "'" + c + "'";
		}

		private string quote(string s)
		{
			// Escape any characters that won't print well and should be escaped
			s = s.Replace("\\", "\\\\");
			s = s.Replace("\r", "\\r");
			s = s.Replace("\n", "\\n");
			s = s.Replace("\t", "\\t");
			s = s.Replace("\f", "\\f");
			s = s.Replace("\b", "\\b");

			// Use single quotes as they seem a little clearer in error messages since JSON never has single quoted strings
			// in content
			return "'" + s + "'";
		}

		private char numToHexDigitChar(int num)
		{
			if (num >= 0 && num <= 9)
				return (char)((int) '0' + num);
			else if (num >= 10 && num <= 15)
				return (char)((int) 'a' + (num - 10));
			else
				throw new JsonException("Digit " + num + " should be < 16 (and > 0) in call to numToHexDigitChar");
		}

		private object readNumberToken(char lookahead)
		{
			bool negative = false;
			if (lookahead == '-')
			{
				negative = true;
				readChar(); // Skip past the minus sign
				lookahead = lookaheadChar();

				if (!(lookahead >= '0' && lookahead <= '9'))
					throw new JsonParsingException("Expected a digit to follow a minus sign", this);
			}

			long value = 0;

			while (true)
			{
				lookahead = lookaheadChar();
				if (lookahead >= '0' && lookahead <= '9')
				{
					int digit = lookahead - '0';

					if (negative)
					{
						if (-1 * value < (long.MinValue + digit) / 10)
							throw new JsonParsingException("Negative number is too big, overflowing the size of a long", this);
					}
					else if (value > (long.MaxValue - digit) / 10)
						throw new JsonParsingException("Number is too big, overflowing the size of a long", this);

					value = 10 * value + digit;
					++currIndex;
				}
				else if (lookahead == '.')
				{
					double doubleValue = (double) value + readFractionalPartOfDouble();
					if (negative)
						doubleValue = -doubleValue;
					return doubleValue;
				}
				else if (lookahead == 'e' || lookahead == 'E')
					throw new JsonParsingException("Numbers in scientific notation aren't currently supported", this);
				else
					break;
			}

			if (negative)
				value = -1 * value;

			if (value <= int.MaxValue && value >= int.MinValue)
				return (int?)((int) value);
			else
				return (long?) value;
		}

		/// <summary>
		/// Parse and return the fractional part of a floating point number--the decimal point and what's after it.  Note
		/// that our implementation is somewhat more forgiving than the JSON standard in that "123." is treated as valid-- it
		/// doesn't require a digit to follow the decimal point.   Scientific notation is currently not supported--if that's
		/// ever needed, we can implement it.
		/// </summary>
		/// <returns> double representing what follows the decimal point; the returned value is >= 0 and < 1 </returns>
		private double readFractionalPartOfDouble()
		{
			if (lookaheadChar() != '.')
				throw new JsonParsingException("Expected fraction to start with a '.'", this);
			++currIndex;

			double value = 0;
			double place = 0.1;

			while (true)
			{
				char lookahead = lookaheadChar();
				if (lookahead >= '0' && lookahead <= '9')
				{
					int digit = lookahead - '0';
					++currIndex;

					value += digit * place;
					place /= 10;
				}
				else if (lookahead == 'e' || lookahead == 'E')
					throw new JsonParsingException("Numbers in scientific notation aren't currently supported", this);
				else
					break;
			}

			return value;
		}

		internal static string getTokenTypeDescription(TokenType type)
		{
			switch (type)
			{
				case jsimple.json.TokenType.LEFT_BRACE:
					return "'{'";
				case jsimple.json.TokenType.RIGHT_BRACE:
					return "'}'";
				case jsimple.json.TokenType.LEFT_BRACKET:
					return "'['";
				case jsimple.json.TokenType.RIGHT_BRACKET:
					return "']'";
				case jsimple.json.TokenType.COMMA:
					return "','";
				case jsimple.json.TokenType.COLON:
					return "':'";
				case jsimple.json.TokenType.PRIMITIVE:
					return "primitive (string/number/true/false/null)";
				case jsimple.json.TokenType.EOF:
					return "end of JSON text";
				default:
					throw new JsonException("Unknown TokenType: " + type.ToString());
			}
		}
	}

}