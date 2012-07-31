package jsimple.json;

/**
 * JsonArraySerializer lets the caller serialize the items in an array one by one, iterating through them & serializing
 * each in turn.  If the array is big--often true when it's the outermost object in a JSON text--then this method is
 * more memory efficient than creating JSON objects for the entire array in memory at once.
 * <p/>
 * The array is also formatted a little differently when serialized this way, optimized for a large array that's at the
 * outermost level in a file. Here each value in the array is on a separate line, preceded by a blank line to make the
 * individual values a bit easier to grep.  The normal array serializer will sometimes put values all on one line in
 * certain simple cases, but that never happens here.
 *
 * @author Bret Johnson
 * @since 7/29/12 3:08 PM
 */
public class JsonArraySerializer {
    JsonSerializer serializer;
    boolean outputItem = false;

    public JsonArraySerializer(JsonSerializer jsonSerializer) {
        serializer = jsonSerializer;
        serializer.appendRaw("[\n");
    }

    /**
     * Add a value to the array, serializing it.  value can be any valid JSON value class (JsonObject, JsonArray,
     * String, Integer, Long, Boolean, or JsonNull).
     *
     * @param value JSON value for array item
     */
    public void append(Object value) {
        if (outputItem)
            serializer.appendRaw(",\n");

        serializer.appendRaw("\n");
        serializer.append(value);
        outputItem = true;
    }

    /**
     * Finish the serialization, outputting the closing bracket and returning the resulting string.
     *
     * @return string containing JSON text for entire serialized array
     */
    public String done() {
        serializer.appendRaw("]\n");
        return serializer.getResult();
    }
}
