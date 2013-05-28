namespace jsimple.json.readerwriter
{

	/// <summary>
	/// @author Bret Johnson
	/// @since 5/22/13 9:09 PM
	/// </summary>
	public class JsonBooleanProperty : JsonProperty
	{
		public JsonBooleanProperty(string name, int id) : base(name, id)
		{
		}

		public virtual bool readValue(JsonObjectReader objectReader)
		{
			return (bool)(bool?) objectReader.readPropertyValue();
		}

		public override object readValueUntyped(JsonObjectReader objectReader)
		{
			return objectReader.readPropertyValue();
		}

		public virtual void write(JsonObjectWriter objectWriter, bool value)
		{
			objectWriter.writeProperty(this, value);
		}
	}

}