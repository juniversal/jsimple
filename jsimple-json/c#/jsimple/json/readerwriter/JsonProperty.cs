namespace jsimple.json.readerwriter
{

	/// <summary>
	/// @author Bret Johnson
	/// @since 5/17/13 12:40 AM
	/// </summary>
	public class JsonProperty
	{
		protected internal string name;
		protected internal int id;

		public JsonProperty(string name)
		{
			this.name = name;
			this.id = -1;
		}

		public JsonProperty(string name, int id)
		{
			this.name = name;
			this.id = id;
		}

		public virtual object readValueUntyped(JsonObjectReader objectReader)
		{
			return objectReader.readPropertyValue();
		}

		public virtual string Name
		{
			get
			{
				return name;
			}
		}

		public virtual int Id
		{
			get
			{
				return id;
			}
		}
	}

}