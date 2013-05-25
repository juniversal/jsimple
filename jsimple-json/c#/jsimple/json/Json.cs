namespace jsimple.json
{

	/// <summary>
	/// @author Bret Johnson
	/// @since 5/24/13 12:37 AM
	/// </summary>

	using Reader = jsimple.io.Reader;
	using StringReader = jsimple.io.StringReader;
	using Writer = jsimple.io.Writer;
	using JsonObjectOrArray = jsimple.json.objectmodel.JsonObjectOrArray;
	using ObjectModelParser = jsimple.json.objectmodel.ObjectModelParser;
	using JsonArrayReader = jsimple.json.readerwriter.JsonArrayReader;
	using JsonArrayWriter = jsimple.json.readerwriter.JsonArrayWriter;
	using JsonObjectReader = jsimple.json.readerwriter.JsonObjectReader;
	using JsonObjectWriter = jsimple.json.readerwriter.JsonObjectWriter;
	using Serializer = jsimple.json.text.Serializer;
	using Token = jsimple.json.text.Token;
	using TokenType = jsimple.json.text.TokenType;

	public sealed class Json
	{
		/// <summary>
		/// Parse the specified JSON text, returning a JsonObject or JsonArray, depending on what the outermost container
		/// type is in the JSON.
		/// <p/>
		/// Per spec, the root element in a JSON text is either an array or an object.
		/// </summary>
		/// <param name="reader"> JSON text </param>
		/// <returns> JSON object tree </returns>
		public static JsonObjectOrArray parse(Reader reader)
		{
			return (new ObjectModelParser(reader)).parseRoot();
		}

		/// <summary>
		/// Return a JsonObjectReader or a JsonArrayReader for the specified JSON text, depending on what the outermost
		/// container type is in the JSON.  The caller can use instanceof to determine which it is.
		/// </summary>
		/// <param name="reader"> JSON text </param>
		/// <returns> JsonObjectReader or JsonArrayReader </returns>
		public static object read(Reader reader)
		{
			Token token = new Token(reader);
			if (token.Type == TokenType.LEFT_BRACKET)
				return new JsonArrayReader(token);
			else
				return new JsonObjectReader(token);
		}

		/// <summary>
		/// Assuming the JSON text is an object, return a JsonObjectReader, to iteratively read through the name/value pairs
		/// in the object one by one, to process each as it's parsed. If the object is big (normally always because it has
		/// descendant objects that can contain big arrays), this can be more efficient than parsing the whole JSON text into
		/// memory at once.  If text isn't a JSON object (e.g. is a JSON array), a class cast exception is thrown.
		/// </summary>
		/// <param name="reader"> JSON text, which should be a JSON array </param>
		/// <returns> array reader, to read through the array </returns>
		public static JsonObjectReader readObject(Reader reader)
		{
			return (JsonObjectReader) read(reader);
		}

		/// <summary>
		/// Assuming the JSON text is an array, return a JsonArrayReader, to iteratively parse through the items in the array
		/// one by one, to process each as it's parsed. If the array is big this can be more efficient than parsing the whole
		/// JSON text into memory at once.  If text isn't a JSON array (e.g. is a JSON object), a class cast exception is
		/// thrown.
		/// </summary>
		/// <param name="reader"> JSON text, which should be a JSON array </param>
		/// <returns> array reader, to read through the array </returns>
		public static JsonArrayReader readArray(Reader reader)
		{
			return (JsonArrayReader) read(reader);
		}

		/// <summary>
		/// Serialize a JSON array, item by item.  When an array is large and the root of a JSON text, this method is more
		/// memory efficient than creating a single JsonArray (including all items inside of it) in memory all at once and
		/// serializing that.
		/// </summary>
		/// <returns> JsonArrayWriter, to serialize each item </returns>
		public static JsonObjectWriter writeObject(Writer writer)
		{
			return new JsonObjectWriter(new Serializer(writer), true);
		}

		/// <summary>
		/// Serialize a JSON array, item by item.  When an array is large and the root of a JSON text, this method is more
		/// memory efficient than creating a single JsonArray (including all items inside of it) in memory all at once and
		/// serializing that.
		/// </summary>
		/// <returns> JsonArrayWriter, to serialize each item </returns>
		public static JsonArrayWriter writeArray(Writer writer)
		{
			return new JsonArrayWriter(new Serializer(writer), true);
		}
	}

}