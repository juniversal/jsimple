namespace jsimple.json.readerwriter
{

	/// <summary>
	/// @author Bret Johnson
	/// @since 5/22/13 9:08 PM
	/// </summary>
	public class JsonLongProperty : JsonProperty
	{
		public JsonLongProperty(string name, int id) : base(name, id)
		{
		}

		// TODO: Automatically convert int to long
		public virtual long readValue(JsonObjectReader objectReader)
		{
			return (long)(long?) objectReader.readPropertyValue();
		}

		public override object readValueUntyped(JsonObjectReader objectReader)
		{
			return objectReader.readPropertyValue();
		}

		public virtual void write(JsonObjectWriter objectWriter, long value)
		{
			objectWriter.writeProperty(this, value);
		}
	}

}