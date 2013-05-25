namespace jsimple.json.readerwriter
{

	using JsonObjectOrArray = jsimple.json.objectmodel.JsonObjectOrArray;
	using Serializer = jsimple.json.text.Serializer;

	/// <summary>
	/// @author Bret Johnson
	/// @since 5/21/13 11:10 PM
	/// </summary>
	public class JsonObjectWriter
	{
		internal bool outputSomething = false;
		internal Serializer serializer;
		internal bool flushWhenDone = false;

		public JsonObjectWriter(Serializer serializer)
		{
			this.serializer = serializer;
		}

		public JsonObjectWriter(Serializer serializer, bool flushWhenDone)
		{
			this.serializer = serializer;
			this.flushWhenDone = flushWhenDone;
		}

		public virtual void done()
		{
			if (!outputSomething)
				serializer.appendRaw("{}");
			else
			{
				serializer.appendRaw("\n");
				serializer.indent(-2);
				serializer.appendIndent();
				serializer.appendRaw("}");
			}

			if (flushWhenDone)
				serializer.flush();
		}

		public virtual void write(JsonProperty property, object value)
		{
			writePropertyName(property.Name);
			serializer.appendPrimitive(value);
		}

		public virtual void write(string propertyName, object value)
		{
			writePropertyName(propertyName);
			serializer.appendPrimitive(value);
		}

		public virtual void writePropertyName(string propertyName)
		{
			if (!outputSomething)
			{
				serializer.appendRaw("{\n");
				serializer.indent(2);
				outputSomething = true;
			}
			else
				serializer.appendRaw(",\n");

			serializer.appendIndent();
			serializer.appendString(propertyName);
			serializer.appendRaw(": ");
		}

		public virtual JsonObjectWriter writeObjectProperty(JsonObjectProperty property)
		{
			writePropertyName(property.Name);
			return new JsonObjectWriter(serializer);
		}

		public virtual JsonArrayWriter writeArrayProperty(JsonArrayProperty property)
		{
			writePropertyName(property.Name);
			return new JsonArrayWriter(serializer);
		}

		/// <summary>
		/// Serialize an entire JSON root object.  If this method is called, generally none of other append methods are used
		/// by the caller.
		/// </summary>
		/// <param name="jsonObjectOrArray"> JSON root object to serialize </param>
		public virtual void serialize(JsonObjectOrArray jsonObjectOrArray)
		{
			serializer.append(jsonObjectOrArray);
			serializer.appendRaw("\n"); // Terminate the last line
		}

	/*
	    */
	/// <summary>
	/// Append a JSON object.  Each name/value pair is output on a separate line, indented by two spaces.  If the object
	/// is empty, just "{}" is appended.
	/// </summary>
	/// <param name="jsonObject"> object to append </param>
	//JAVA TO C# CONVERTER NOTE: Beginning of line comments are not maintained by Java to C# Converter:
	//ORIGINAL LINE: 
	 internal * int size = jsonObject.size();
			if (size == 0)
				text.append("{}");
			else
			{
				text.append("{\n");
				indent += 2;

				for (int i = 0; i < size; ++i)
				{
					appendIndent();

					appendString(jsonObject.getName(i));
					serializer.appendPrimitive(": ");
					serializer.appendPrimitive(jsonObject.getValue(i));

					if (i < size - 1)
						text.append(",\n");
					else
						text.append("\n");
				}

				indent -= 2;
				appendIndent();
				text.append("}");
			}
	}

		*/ * int size = array.size();
	/// <summary>
	/// Append a JSON array.  If the array is empty or its elements are all simple objects (that is, literals or embedded
	/// objects/arrays that are empty), then output it all on one line.  Otherwise, each array element is on a separate
	/// line.
	/// </summary>
	/// <param name="array"> array to append </param>
	//JAVA TO C# CONVERTER NOTE: Beginning of line comments are not maintained by Java to C# Converter:
	//ORIGINAL LINE: 
			if (size == 0)
				text.append("[]");
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

				text.append("[");

				if (useMultipleLines)
				{
					text.append("\n");
					indent += 2;

					for (int i = 0; i < size; ++i)
					{
						appendIndent();

						serializer.appendPrimitive(array.get(i));

						if (i < size - 1)
							text.append(",\n");
						else
							text.append("\n");
					}

					indent -= 2;
					appendIndent();
				}
				else
				{
					for (int i = 0; i < size; ++i)
					{
						serializer.appendPrimitive(array.get(i));

						if (i < size - 1)
							text.append(", ");
					}
				}

				text.append(']');
			}
}
	*/
	}

}