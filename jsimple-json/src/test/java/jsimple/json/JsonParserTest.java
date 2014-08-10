/*
 * Copyright (c) 2012-2014, Microsoft Mobile
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package jsimple.json;

import jsimple.io.StringReader;
import jsimple.json.objectmodel.JsonArray;
import jsimple.json.objectmodel.JsonNull;
import jsimple.json.objectmodel.JsonObject;
import jsimple.json.text.JsonParsingException;
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
        assertEquals(123.45, parseJsonArray("[[123.45]]").getJsonArray(0).get(0));
        assertEquals("", parseJsonArray("[[\"\"]]").getJsonArray(0).getString(0));

        assertEquals(JsonNull.singleton, parseJsonObject("{\"abc\": [null] }").getJsonArray("abc").get(0));

        validateParsingException("Expected , or ] but encountered end of JSON text", "[\"abc\", [\"def\", 42]");
        validateParsingException("Expected , or ] but encountered true", "[\"abc\", [\"def\", 42] true");
    }

    private JsonObject parseJsonObject(String jsonText) {
        return (JsonObject) Json.parse(new StringReader(jsonText));
    }

    private JsonArray parseJsonArray(String jsonText) {
        return (JsonArray) Json.parse(new StringReader(jsonText));
    }

    private void validateParsingException(String exceptionMessage, String jsonText) {
        try {
            Json.parse(new StringReader(jsonText));
            fail();
        } catch (JsonParsingException e) {
            assertEquals(exceptionMessage, e.getMessage());
        }
    }
}
