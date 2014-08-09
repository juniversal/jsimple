/*
    Copyright (c) 2012-2014, Microsoft Mobile

    Permission is hereby granted, free of charge, to any person obtaining a copy
    of this software and associated documentation files (the "Software"), to deal
    in the Software without restriction, including without limitation the rights
    to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
    copies of the Software, and to permit persons to whom the Software is
    furnished to do so, subject to the following conditions:

    The above copyright notice and this permission notice shall be included in
    all copies or substantial portions of the Software.

    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
    IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
    FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
    AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
    LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
    OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
    THE SOFTWARE.
*/

package jsimple.json;

import jsimple.json.objectmodel.JsonArray;
import jsimple.json.objectmodel.JsonNull;
import jsimple.json.objectmodel.JsonObject;
import jsimple.json.objectmodel.JsonObjectOrArray;
import jsimple.unit.UnitTest;
import org.junit.Test;

/**
 * @author Bret Johnson
 * @since 7/8/12 1:42 PM
 */
public class JsonSerializerTest extends UnitTest {
    @Test public void testSerializeObject() {
        assertSerializedJsonIs(
                new JsonObject().
                        add("num", 3).
                        add("escape", "\\ \" / \r \n \t \f \b").
                        add("control", "\0 \u0001 \u001F \u0020 \u007F \u009F \u00A0").
                        add("longmax", Long.MAX_VALUE).
                        add("longmin", Long.MIN_VALUE).
                        add("doublemax", Double.MAX_VALUE).
                        add("doublemin", Double.MIN_VALUE).
                        add("bool", true).
                        add("bool2", false).
                        add("nul", JsonNull.singleton).
                        add("emptyobj", new JsonObject()).
                        add("obj", new JsonObject().add("val", "abc")),
                "{",
                "  \"num\": 3,",
                "  \"escape\": \"\\\\ \\\" / \\r \\n \\t \\f \\b\",",
                "  \"control\": \"\\u0000 \\u0001 \\u001F \u0020 \\u007F \\u009F \u00A0\",",
                "  \"longmax\": " + Long.MAX_VALUE + ",",
                "  \"longmin\": " + Long.MIN_VALUE + ",",
                "  \"doublemax\": " + Double.MAX_VALUE + ",",
                "  \"doublemin\": " + Double.MIN_VALUE + ",",
                "  \"bool\": true,",
                "  \"bool2\": false,",
                "  \"nul\": null,",
                "  \"emptyobj\": {},",
                "  \"obj\": {",
                "    \"val\": \"abc\"",
                "  }",
                "}"
        );
    }

    @Test public void testSerializeArray() {
        assertSerializedJsonIs(
                new JsonArray(),
                "[]"
        );

        assertSerializedJsonIs(
                new JsonArray().
                        add("abc").
                        add(3).
                        add(true).
                        add(new JsonObject()),
                "[\"abc\", 3, true, {}]"
        );

        assertSerializedJsonIs(
                new JsonArray().
                        add("abc").
                        add(new JsonObject().add("val", 3)),
                "[",
                "  \"abc\",",
                "  {",
                "    \"val\": 3",
                "  }",
                "]"
        );
    }

    void assertSerializedJsonIs(JsonObjectOrArray json, String... lines) {
        StringBuilder jsonTextBuilder = new StringBuilder();
        for (String line : lines)
            jsonTextBuilder.append(line + "\n");
        String jsonText = jsonTextBuilder.toString();

        assertEquals(jsonText, json.toString());
    }
}
