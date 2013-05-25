namespace jsimple.json.readerwriter
{

	/// <summary>
	/// @author Bret Johnson
	/// @since 5/17/13 12:40 AM
	/// </summary>
	public class JsonIntProperty : JsonProperty
	{
		public JsonIntProperty(string name, int id) : base(name, id)
		{
		}

		public virtual int readValue(JsonObjectReader objectReader)
		{
			return (int?) objectReader.readPropertyValue();
		}

		public override object readValueUntyped(JsonObjectReader objectReader)
		{
			return objectReader.readPropertyValue();
		}

		public virtual void write(JsonObjectWriter objectWriter, int value)
		{
			objectWriter.write(this, value);
		}
	}

}