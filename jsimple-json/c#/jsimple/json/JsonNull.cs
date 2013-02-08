namespace jsimple.json
{

	/// <summary>
	/// This class is used to represent a "null" literal in JSON.  By having an explicit class, instead of just the regular
	/// Java null, we can differentiate more easily between an object not existing in the JSON and it existing and having the
	/// explicit JSON literal value "null".
	/// 
	/// @author Bret Johnson
	/// @since 5/20/12 9:52 PM
	/// </summary>
	public sealed class JsonNull
	{
		public static JsonNull singleton = new JsonNull();

		private JsonNull()
		{
		}

		public override string ToString()
		{
			return "JSON null primitive";
		}
	}

}