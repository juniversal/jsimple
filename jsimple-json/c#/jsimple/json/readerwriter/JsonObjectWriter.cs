namespace jsimple.json.readerwriter
{

	using Serializer = jsimple.json.text.Serializer;

	/// <summary>
	/// @author Bret Johnson
	/// @since 5/21/13 11:10 PM
	/// </summary>
	public class JsonObjectWriter : jsimple.lang.AutoCloseable
	{
		internal Serializer serializer;
		internal bool flushWhenDone = false;
		internal bool singleLine = false;
		internal bool outputSomething = false;

		public JsonObjectWriter(Serializer serializer)
		{
			this.serializer = serializer;
		}

		public JsonObjectWriter(Serializer serializer, bool flushWhenDone)
		{
			this.serializer = serializer;
			this.flushWhenDone = flushWhenDone;
		}

		public virtual bool SingleLine
		{
			get
			{
				return singleLine;
			}
			set
			{
				this.singleLine = value;
			}
		}


		public virtual void writeProperty(JsonProperty property, object value)
		{
			writePropertyName(property.Name);
			serializer.writeValue(value);
		}

		public virtual void writeProeprty(string propertyName, object value)
		{
			writePropertyName(propertyName);
			serializer.writeValue(value);
		}

		public virtual void writePropertyName(string propertyName)
		{
			if (singleLine)
			{
				if (!outputSomething)
				{
					serializer.write("{ ");
					outputSomething = true;
				}
				else
					serializer.write(", ");

				serializer.writeString(propertyName);
				serializer.write(": ");
			}
			else
			{
				if (!outputSomething)
				{
					serializer.write("{\n");
					serializer.indent(2);
					outputSomething = true;
				}
				else
					serializer.write(",\n");

				serializer.writeIndent();
				serializer.writeString(propertyName);
				serializer.write(": ");
			}
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

		public override void close()
		{
			if (!outputSomething)
				serializer.write("{}");
			else
			{
				if (singleLine)
					serializer.write(" }");
				else
				{
					serializer.write("\n");
					serializer.indent(-2);
					serializer.writeIndent();
					serializer.write("}");
				}
			}

			if (flushWhenDone)
				serializer.flush();
		}
	}

}