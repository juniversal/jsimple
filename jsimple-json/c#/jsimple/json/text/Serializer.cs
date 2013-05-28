namespace jsimple.json.text
{

	using Writer = jsimple.io.Writer;
	using JsonArray = jsimple.json.objectmodel.JsonArray;
	using JsonNull = jsimple.json.objectmodel.JsonNull;
	using JsonObject = jsimple.json.objectmodel.JsonObject;

	/// <summary>
	/// @author Bret Johnson
	/// @since 7/8/12 3:46 AM
	/// </summary>
	public sealed class Serializer
	{
		private Writer writer;
		private char[] buffer = new char[BUFFER_SIZE];
		private int currIndex; // Next character to be processed
		internal int indent_Renamed;

		private const int BUFFER_SIZE = 256;

		public Serializer(Writer writer)
		{
			this.writer = writer;
			currIndex = 0;
			indent_Renamed = 0;
		}

		public void writeValue(object obj)
		{
			if (obj is string)
				writeString((string) obj);
			else if (obj is int? || obj is long?)
				write(obj.ToString());
			else if (obj is bool?)
			{
				bool booleanValue = (bool)(bool?) obj;
				write(booleanValue ? "true" : "false");
			}
			else if (obj is JsonObject)
				writeJsonObject((JsonObject) obj);
			else if (obj is JsonArray)
				writeJsonArray((JsonArray) obj);
			else if (obj is JsonNull)
				write("null");
			else
				throw new JsonException("Unexpected JSON object type");
		}

		/// <summary>
		/// Write a JSON object.  Each name/value pair is output on a separate line, indented by two spaces.  If the object
		/// is empty, just "{}" is appended.
		/// </summary>
		/// <param name="jsonObject"> object to append </param>
		public void writeJsonObject(JsonObject jsonObject)
		{
			int size = jsonObject.size();
			if (size == 0)
				write("{}");
			else
			{
				write("{\n");
				indent_Renamed += 2;

				for (int i = 0; i < size; ++i)
				{
					writeIndent();

					writeString(jsonObject.getName(i));
					write(": ");
					writeValue(jsonObject.getValue(i));

					if (i < size - 1)
						write(",\n");
					else
						write("\n");
				}

				indent_Renamed -= 2;
				writeIndent();
				write("}");
			}
		}

		/// <summary>
		/// Append a JSON array.  If the array is empty or its elements are all simple objects (that is, literals or embedded
		/// objects/arrays that are empty), then output it all on one line.  Otherwise, each array element is on a separate
		/// line.
		/// </summary>
		/// <param name="array"> array to append </param>
		public void writeJsonArray(JsonArray array)
		{
			int size = array.size();
			if (size == 0)
				write("[]");
			else
			{
				bool allSimpleObjects = true;

				for (int i = 0; i < size; ++i)
				{
					object item = array.get(i);

					if (item is JsonObject && ((JsonObject) item).size() > 0)
					{
						allSimpleObjects = false;
						break;
					}
					else if (item is JsonArray && ((JsonArray) item).size() > 0)
					{
						allSimpleObjects = false;
						break;
					}
				}

				bool useMultipleLines = !allSimpleObjects;

				write("[");

				if (useMultipleLines)
				{
					write("\n");
					indent_Renamed += 2;

					for (int i = 0; i < size; ++i)
					{
						writeIndent();

						writeValue(array.get(i));

						if (i < size - 1)
							write(",\n");
						else
							write("\n");
					}

					indent_Renamed -= 2;
					writeIndent();
				}
				else
				{
					for (int i = 0; i < size; ++i)
					{
						writeValue(array.get(i));

						if (i < size - 1)
							write(", ");
					}
				}

				write(']');
			}
		}

		/// <summary>
		/// Append spaces for the current indent.
		/// </summary>
		public void writeIndent()
		{
			for (int i = 0; i < indent_Renamed; ++i)
				write(' ');
		}

		public void writeString(string @string)
		{
			write('\"');

			int length = @string.Length;
			for (int i = 0; i < length; ++i)
			{
				char c = @string[i];

				// Check for characters that need to be escaped
				switch (c)
				{
					case '\"':
						write("\\\"");
						break;

					case '\\':
						write("\\\\");
						break;

					case '\b':
						write("\\b");
						break;

					case '\f':
						write("\\f");
						break;

					case '\n':
						write("\\n");
						break;

					case '\r':
						write("\\r");
						break;

					case '\t':
						write("\\t");
						break;

					default:
						if (Token.isControlCharacter(c))
							writeUnicodeEscape(c);
						else
							write(c);
						break;
				}
			}

			write("\"");
		}

		public void writeUnicodeEscape(char c)
		{
			write("\\u");

			writeHexDigit((int)((uint)(c & 0xF000) >> 12));
			writeHexDigit((int)((uint)(c & 0x0F00) >> 8));
			writeHexDigit((int)((uint)(c & 0x00F0) >> 4));
			writeHexDigit(c & 0x000F);
		}

		public void writeHexDigit(int hexDigit)
		{
			if (hexDigit <= 9)
				write((char)('0' + hexDigit));
			else if (hexDigit <= 15)
				write((char)('A' + (hexDigit - 10)));
			else
				throw new JsonException("Hex digit out of range: {}", hexDigit);
		}

		public void write(string s)
		{
			int length = s.Length;
			for (int i = 0; i < length; i++)
				write(s[i]);
		}

		private void write(char c)
		{
			if (currIndex >= BUFFER_SIZE)
				flush();
			buffer[currIndex++] = c;
		}

		public void flush()
		{
			writer.write(buffer, 0, currIndex);
			currIndex = 0;
		}

		/// <summary>
		/// Increment (or decrement if negative) the prevailing indent by the specified amount.
		/// </summary>
		/// <param name="amount"> amount to change indent </param>
		public void indent(int amount)
		{
			indent_Renamed += amount;
		}
	}

}