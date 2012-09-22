package jsimple.json;

/**
 * @author Bret Johnson
 * @since 7/8/12 3:55 AM
 */
public final class Json {
    /**
     * Parse the specified JSON text, returning JSON object tree.  Per spec, the root element in a JSON text is either
     * an array or an object.
     *
     * @param text JSON text
     * @return JSON object tree
     */
    public static JsonObjectOrArray parse(String text) {
        return new JsonParser(text).parseRoot();
    }

    /**
     * Assuming the JSON text is an array, parse and iterate through the items in it one by one, to process each as it's
     * parsed. If the array is big this can be more efficient than parsing the whole array into memory at once.  If text
     * isn't a JSON array (e.g. is a JSON object), an exception is thrown.
     *
     * @param text JSON text, which should be a JSON array
     * @return iterator through the array
     */
    public static JsonArrayParser parseArray(String text) {
        return new JsonArrayParser(new JsonParser(text));
    }

    public static String serialize(JsonObjectOrArray object) {
        JsonSerializer serializer = new JsonSerializer();
        serializer.serialize(object);
        return serializer.getResult();
    }

    /**
     * Serialize a JSON array, item by item.  When an array is large and the root of a JSON text, this method is more
     * memory efficient than creating a single JsonArray (including all items inside of it) in memory all at once and
     * serializing that.
     *
     * @return JsonArraySerializer, to serialize each item
     */
    public static JsonArraySerializer serializeArray() {
        return new JsonArraySerializer(new JsonSerializer());
    }
}
