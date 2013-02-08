using System.Text;

namespace jsimple.json
{

	/// <summary>
	/// @author Bret Johnson
	/// @since 7/8/12 3:46 AM
	/// </summary>
	public sealed class JsonSerializer
	{
		internal StringBuilder text = new StringBuilder();
		internal int indent = 0;

		/// <summary>
		/// Serialize an entire JSON root object.  If this method is called, generally none of other append methods are used
		/// by the caller.
		/// </summary>
		/// <param name="jsonObjectOrArray"> JSON root object to serialize </param>
		internal void serialize(JsonObjectOrArray jsonObjectOrArray)
		{
			append(jsonObjectOrArray);
			text.Append("\n"); // Terminate the last line
		}

		internal string Result
		{
			get
			{
				return text.ToString();
			}
		}

		public void append(object obj)
		{
			if (obj is string)
				appendString((string) obj);
			else if (obj is int?)
				text.Append((int)((int?) obj));
			else if (obj is long?)
				text.Append((long)((long?) obj));
			else if (obj is JsonObject)
				appendJsonObject((JsonObject) obj);
			else if (obj is JsonArray)
				appendJsonArray((JsonArray) obj);
			else if (obj is bool?)
			{
				bool booleanValue = (bool)(bool?) obj;
				text.Append(booleanValue ? "true" : "false");
			}
			else if (obj is JsonNull)
				text.Append("null");
			else
				throw new JsonException("Unexpected JSON object type");
		}

		/// <summary>
		/// Append a JSON object.  Each name/value pair is output on a separate line, indented by two spaces.  If the object
		/// is empty, just "{}" is appended.
		/// </summary>
		/// <param name="jsonObject"> object to append </param>
		public void appendJsonObject(JsonObject jsonObject)
		{
			int size = jsonObject.size();
			if (size == 0)
				text.Append("{}");
			else
			{
				text.Append("{\n");
				indent += 2;

				for (int i = 0; i < size; ++i)
				{
					appendIndent();

					text.Append("\"");
					text.Append(jsonObject.getName(i));
					text.Append("\": ");

					append(jsonObject.getValue(i));

					if (i < size - 1)
						text.Append(",\n");
					else
						text.Append("\n");
				}

				indent -= 2;
				appendIndent();
				text.Append("}");
			}
		}

		/// <summary>
		/// Append a JSON array.  If the array is empty or its elements are all simple objects (that is, literals or embedded
		/// objects/arrays that are empty), then output it all on one line.  Otherwise, each array element is on a separate
		/// line.
		/// </summary>
		/// <param name="array"> array to append </param>
		public void appendJsonArray(JsonArray array)
		{
			int size = array.size();
			if (size == 0)
				text.Append("[]");
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

				text.Append("[");

				if (useMultipleLines)
				{
					text.Append("\n");
					indent += 2;

					for (int i = 0; i < size; ++i)
					{
						appendIndent();

						append(array.get(i));

						if (i < size - 1)
							text.Append(",\n");
						else
							text.Append("\n");
					}

					indent -= 2;
					appendIndent();
				}
				else
				{
					for (int i = 0; i < size; ++i)
					{
						append(array.get(i));

						if (i < size - 1)
							text.Append(", ");
					}
				}

				text.Append(']');
			}
		}

		/// <summary>
		/// Append spaces for the current indent.
		/// </summary>
		public void appendIndent()
		{
			for (int i = 0; i < indent; ++i)
				text.Append(' ');
		}

		public void appendString(string @string)
		{
			text.Append("\"");

			int length = @string.Length;
			for (int i = 0; i < length; ++i)
			{
				char c = @string[i];

				// Check for characters that need to be escaped
				switch (c)
				{
					case '\"':
						text.Append("\\\"");
						break;

					case '\\':
						text.Append("\\\\");
						break;

					case '\b':
						text.Append("\\b");
						break;

					case '\f':
						text.Append("\\f");
						break;

					case '\n':
						text.Append("\\n");
						break;

					case '\r':
						text.Append("\\r");
						break;

					case '\t':
						text.Append("\\t");
						break;

					default:
						if (JsonUtil.isControlCharacter(c))
							appendUnicodeEscape(c);
						else
							text.Append(c);
						break;
				}
			}

			text.Append("\"");
		}

		public void appendUnicodeEscape(char c)
		{
			text.Append("\\u");

			appendHexDigit((int)((uint)(c & 0xF000) >> 12));
			appendHexDigit((int)((uint)(c & 0x0F00) >> 8));
			appendHexDigit((int)((uint)(c & 0x00F0) >> 4));
			appendHexDigit(c & 0x000F);
		}

		public void appendHexDigit(int hexDigit)
		{
			if (hexDigit <= 9)
				text.Append((char)('0' + hexDigit));
			else if (hexDigit <= 15)
				text.Append((char)('A' + (hexDigit - 10)));
			else
				throw new JsonException("Hex digit out of range: " + hexDigit);
		}

		public void appendRaw(string rawText)
		{
			text.Append(rawText);
		}
	}

}