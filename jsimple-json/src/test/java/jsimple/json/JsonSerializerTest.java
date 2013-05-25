package jsimple.json;

import jsimple.json.objectmodel.*;
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
