namespace jsimple.json.readerwriter
{

	/// <summary>
	/// @author Bret Johnson
	/// @since 5/22/13 9:12 PM
	/// </summary>
	public class JsonObjectProperty : JsonProperty
	{
		public JsonObjectProperty(string name, int id) : base(name, id)
		{
		}

		public virtual JsonObjectReader readValue(JsonObjectReader objectReader)
		{
			return (JsonObjectReader) objectReader.readPropertyValue();
		}

		public virtual JsonObjectWriter write(JsonObjectWriter objectWriter)
		{
			return objectWriter.writeObjectProperty(this);
		}
	}

}