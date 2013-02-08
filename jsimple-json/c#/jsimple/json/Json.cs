namespace jsimple.json
{

	/// <summary>
	/// @author Bret Johnson
	/// @since 7/8/12 3:55 AM
	/// </summary>
	public sealed class Json
	{
		/// <summary>
		/// Parse the specified JSON text, returning JSON object tree.  Per spec, the root element in a JSON text is either
		/// an array or an object.
		/// </summary>
		/// <param name="text"> JSON text </param>
		/// <returns> JSON object tree </returns>
		public static JsonObjectOrArray parse(string text)
		{
			return (new JsonParser(text)).parseRoot();
		}

		/// <summary>
		/// Assuming the JSON text is an array, parse and iterate through the items in it one by one, to process each as it's
		/// parsed. If the array is big this can be more efficient than parsing the whole array into memory at once.  If text
		/// isn't a JSON array (e.g. is a JSON object), an exception is thrown.
		/// </summary>
		/// <param name="text"> JSON text, which should be a JSON array </param>
		/// <returns> iterator through the array </returns>
		public static JsonArrayParser parseArray(string text)
		{
			return new JsonArrayParser(new JsonParser(text));
		}

		public static string serialize(JsonObjectOrArray @object)
		{
			JsonSerializer serializer = new JsonSerializer();
			serializer.serialize(@object);
			return serializer.Result;
		}

		/// <summary>
		/// Serialize a JSON array, item by item.  When an array is large and the root of a JSON text, this method is more
		/// memory efficient than creating a single JsonArray (including all items inside of it) in memory all at once and
		/// serializing that.
		/// </summary>
		/// <returns> JsonArraySerializer, to serialize each item </returns>
		public static JsonArraySerializer serializeArray()
		{
			return new JsonArraySerializer(new JsonSerializer());
		}
	}

}