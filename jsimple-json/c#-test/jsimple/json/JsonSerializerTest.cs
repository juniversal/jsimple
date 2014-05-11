using System.Text;

namespace jsimple.json {

    using JsonArray = jsimple.json.objectmodel.JsonArray;
    using JsonNull = jsimple.json.objectmodel.JsonNull;
    using JsonObject = jsimple.json.objectmodel.JsonObject;
    using JsonObjectOrArray = jsimple.json.objectmodel.JsonObjectOrArray;
    using UnitTest = jsimple.unit.UnitTest;
    using NUnit.Framework;

    /// <summary>
    /// @author Bret Johnson
    /// @since 7/8/12 1:42 PM
    /// </summary>
    public class JsonSerializerTest : UnitTest {
        [Test] public virtual void testSerializeObject()
        {
            assertSerializedJsonIs((new JsonObject()).add("num", 3).add("escape", "\\ \" / \r \n \t \f \b").add("control", "\0 \u0001 \u001F \u0020 \u007F \u009F \u00A0").add("longmax", long.MaxValue).add("longmin", long.MinValue).add("doublemax", double.MaxValue).add("doublemin", double.Epsilon).add("bool", true).add("bool2", false).add("nul", JsonNull.singleton).add("emptyobj", new JsonObject()).add("obj", (new JsonObject()).add("val", "abc")), "{", "  \"num\": 3,", "  \"escape\": \"\\\\ \\\" / \\r \\n \\t \\f \\b\",", "  \"control\": \"\\u0000 \\u0001 \\u001F \u0020 \\u007F \\u009F \u00A0\",", "  \"longmax\": " + long.MaxValue + ",", "  \"longmin\": " + long.MinValue + ",", "  \"doublemax\": " + double.MaxValue + ",", "  \"doublemin\": " + double.Epsilon + ",", "  \"bool\": true,", "  \"bool2\": false,", "  \"nul\": null,", "  \"emptyobj\": {},", "  \"obj\": {", "    \"val\": \"abc\"", "  }", "}");
        }

        [Test] public virtual void testSerializeArray()
        {
            assertSerializedJsonIs(new JsonArray(), "[]");

            assertSerializedJsonIs((new JsonArray()).add("abc").add(3).add(true).add(new JsonObject()), "[\"abc\", 3, true, {}]");

            assertSerializedJsonIs((new JsonArray()).add("abc").add((new JsonObject()).add("val", 3)), "[", "  \"abc\",", "  {", "    \"val\": 3", "  }", "]");
        }

        internal virtual void assertSerializedJsonIs(JsonObjectOrArray json, params string[] lines) {
            StringBuilder jsonTextBuilder = new StringBuilder();
            foreach (string line in lines)
                jsonTextBuilder.Append(line + "\n");
            string jsonText = jsonTextBuilder.ToString();

            assertEquals(jsonText, json.ToString());
        }
    }

}