package jsimple.json;

import jsimple.unit.UnitTest;
import org.junit.Test;

/**
 * @author Bret Johnson
 * @since 5/6/12 12:33 AM
 */
public class JsonParserTest extends UnitTest {
    @Test public void testParseObject() {
        assertEquals(null, parseJsonObject("{}").getOrNull("abc"));
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

    @Test public void testParseArray() {
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
        return (JsonObject) Json.parse(jsonText);
    }

    private JsonArray parseJsonArray(String jsonText) {
        return (JsonArray) Json.parse(jsonText);
    }

    private void validateParsingException(String exceptionMessage, String jsonText) {
        try {
            Json.parse(jsonText);
            fail();
        } catch (JsonParsingException e) {
            assertEquals(exceptionMessage, e.getMessage());
        }
    }
}
