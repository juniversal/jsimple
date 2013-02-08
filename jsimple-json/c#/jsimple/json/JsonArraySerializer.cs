namespace jsimple.json
{

	/// <summary>
	/// JsonArraySerializer lets the caller serialize the items in an array one by one, iterating through them & serializing
	/// each in turn.  If the array is big--often true when it's the outermost object in a JSON text--then this method is
	/// more memory efficient than creating JSON objects for the entire array in memory at once.
	/// <p/>
	/// The array is also formatted a little differently when serialized this way, optimized for a large array that's at the
	/// outermost level in a file. Here each value in the array is on a separate line, preceded by a blank line to make the
	/// individual values a bit easier to grep.  The normal array serializer will sometimes put values all on one line in
	/// certain simple cases, but that never happens here.
	/// 
	/// @author Bret Johnson
	/// @since 7/29/12 3:08 PM
	/// </summary>
	public class JsonArraySerializer
	{
		internal JsonSerializer serializer;
		internal bool outputItem = false;

		public JsonArraySerializer(JsonSerializer jsonSerializer)
		{
			serializer = jsonSerializer;
			serializer.appendRaw("[\n");
		}

		/// <summary>
		/// Add a value to the array, serializing it.  value can be any valid JSON value class (JsonObject, JsonArray,
		/// String, Integer, Long, Boolean, or JsonNull).
		/// </summary>
		/// <param name="value"> JSON value for array item </param>
		public virtual void append(object value)
		{
			if (outputItem)
				serializer.appendRaw(",\n");

			serializer.appendRaw("\n");
			serializer.append(value);
			outputItem = true;
		}

		/// <summary>
		/// Finish the serialization, outputting the closing bracket and returning the resulting string.
		/// </summary>
		/// <returns> string containing JSON text for entire serialized array </returns>
		public virtual string done()
		{
			serializer.appendRaw("]\n");
			return serializer.Result;
		}
	}

}