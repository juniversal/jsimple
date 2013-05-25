namespace jsimple.json
{

	using StringReader = jsimple.io.StringReader;
	using jsimple.json.readerwriter;
	using JsonParsingException = jsimple.json.text.JsonParsingException;
	using UnitTest = jsimple.unit.UnitTest;
	using NUnit.Framework;

	/// <summary>
	/// @author Bret Johnson
	/// @since 5/23/13 1:41 AM
	/// </summary>
	public class ReaderWriterTest : UnitTest
	{
		private const int JSON_STRING_VAL = 1;
		private const int JSON_INT_VAL = 2;
		private const int JSON_LONG_VAL = 3;
		private const int JSON_BOOLEAN_VAL = 4;
		private const int JSON_ARRAY_VAL = 5;
		private const int JSON_OBJECT_VAL = 6;

		private static readonly JsonObjectType jsonObjectType = new JsonObjectType();
		private static readonly JsonStringProperty jsonStringVal = jsonObjectType.createStringProperty("stringVal", JSON_STRING_VAL);
		private static readonly JsonIntProperty jsonIntVal = jsonObjectType.createIntProperty("intVal", JSON_INT_VAL);
		private static readonly JsonLongProperty jsonLongVal = jsonObjectType.createLongProperty("longVal", JSON_LONG_VAL);
		private static readonly JsonBooleanProperty jsonBooleanVal = jsonObjectType.createBooleanProperty("booleanVal", JSON_BOOLEAN_VAL);
		private static readonly JsonArrayProperty jsonArrayVal = jsonObjectType.createArrayProperty("arrayVal", JSON_ARRAY_VAL);
		private static readonly JsonObjectProperty jsonObjectVal = jsonObjectType.createObjectProperty("objectVal", JSON_OBJECT_VAL);

		[Test] public virtual void testParseObject()
		{
			validateParseObject(jsonStringVal, "abc");
			validateParseObject(jsonIntVal, 3);
			validateParseObject(jsonLongVal, 5000000000L);
			validateParseObject(jsonBooleanVal, true);
			validateParseObject(jsonBooleanVal, false);

			// Test array property
			string json = "{ \"arrayVal\": [] }";
			JsonObjectReader objectReader = Json.readObject(new StringReader(json));
			objectReader.ObjectType = jsonObjectType;

			assertEquals(jsonArrayVal, objectReader.readProperty());

			JsonArrayReader subArrayReader = jsonArrayVal.readValue(objectReader);
			assertTrue(subArrayReader.atEnd());

			// Test subobject property
			json = "{ \"objectVal\": {} }";
			objectReader = Json.readObject(new StringReader(json));
			objectReader.ObjectType = jsonObjectType;

			assertEquals(jsonObjectVal, objectReader.readProperty());

			JsonObjectReader subObjectReader = jsonObjectVal.readValue(objectReader);
			assertTrue(subArrayReader.atEnd());
		}

		[Test] public virtual void testParseArray()
		{
			string json = "[\"abc\", 1, -1, 5000000000, true, false, [], {}]";

			JsonArrayReader arrayReader = Json.readArray(new StringReader(json));

			assertEquals("abc", arrayReader.readString());
			assertEquals(1, arrayReader.readInt());
			assertEquals(-1, arrayReader.readInt());
			assertEquals(5000000000L, arrayReader.readLong());
			assertEquals(true, arrayReader.readBoolean());
			assertEquals(false, arrayReader.readBoolean());

			JsonArrayReader subArrayReader = arrayReader.readArray();
			assertTrue(subArrayReader.atEnd());

			JsonObjectReader subObjectReader = arrayReader.readObject();
			assertTrue(subObjectReader.atEnd());
		}

		private void validateParseObject(JsonProperty jsonProperty, object expectedValue)
		{
			string expectedValueJson = expectedValue.ToString();
			if (expectedValue is string)
				expectedValueJson = "\"" + expectedValueJson + "\"";

			string json = "{ \"" + jsonProperty.Name + "\": " + expectedValueJson + " }";

			JsonObjectReader objectReader = Json.readObject(new StringReader(json));
			objectReader.ObjectType = jsonObjectType;

			bool foundProperty = false;
			while (!objectReader.atEnd())
			{
				if (foundProperty)
					fail("Found > 1 property on object");

				JsonProperty readProperty = objectReader.readProperty();
				assertEquals(jsonProperty, readProperty);
				foundProperty = true;

				object actualValue = readProperty.readValueUntyped(objectReader);
				assertEquals(expectedValue, actualValue);
			}
		}

		/*
		assertEquals(null, parseJsonObject("{stringVal = \"abc\"}").getOrNull("abc"));
	
	
		    assertEquals(3, parseJsonObject("{\"abc\": 3}").getOrNull("abc"));
		    assertEquals(true, parseJsonObject("{\"abc\": 3, \"def\": true}").getOrNull("def"));
	
	
		    parseJsonObject("{\"abc\": {\"def\": 42} }").getJsonObject("abc").get("def");
	
		    assertEquals(42, parseJsonObject("{\"abc\": {\"def\": 42} }").getJsonObject("abc").get("def"));
	
		    assertEquals(JsonNull.singleton,
		            parseJsonObject("{\"abc\": {\"def\": null} }").getJsonObject("abc").get("def"));
	
		    validateParsingException("Expected , or } but encountered end of JSON text", "{\"abc\": {\"def\": 42 }");
		    validateParsingException("Expected { or [, starting an object or array but encountered 42", "42");
		    validateParsingException("Expected string for object key but encountered true", "{true}");
		    validateParsingException("Expected string for object key but encountered ','", "{\"abc\": 10,,}");
		    validateParsingException("Expected string for object key but encountered false", "{\"abc\": 10,false}");
		    validateParsingException("Expected ':' but encountered 10", "{\"abc\" 10}");
		}
	
		[Test] public void testParseArray() {
		    assertEquals(0, parseJsonArray("[]").size());
		    assertEquals(3, parseJsonArray("[3]").get(0));
		    assertEquals(true, parseJsonArray("[3, true]").getBoolean(1));
		    assertEquals(42, parseJsonArray("[[42]]").getJsonArray(0).getInt(0));
		    assertEquals(42000000000L, parseJsonArray("[[42000000000]]").getJsonArray(0).getLong(0));
		    assertEquals("", parseJsonArray("[[\"\"]]").getJsonArray(0).getString(0));
	
		    assertEquals(JsonNull.singleton, parseJsonObject("{\"abc\": [null] }").getJsonArray("abc").get(0));
	
		    validateParsingException("Expected , or ] but encountered end of JSON text", "[\"abc\", [\"def\", 42]");
		    validateParsingException("Expected , or ] but encountered true", "[\"abc\", [\"def\", 42] true");
		}
	
		private JsonObject parseJsonObject(String jsonText) {
		    return (JsonObject) JsonObjectModel.parse(jsonText);
		}
	
		private JsonArray parseJsonArray(String jsonText) {
		    return (JsonArray) JsonObjectModel.parse(jsonText);
		}
	*/

		private void validateParsingException(string exceptionMessage, string jsonText)
		{
			try
			{
				Json.parse(new StringReader(jsonText));
				fail();
			}
			catch (JsonParsingException e)
			{
				assertEquals(exceptionMessage, e.Message);
			}
		}
	}
}