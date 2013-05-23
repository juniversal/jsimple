package jsimple.json;

import jsimple.json.objectmodel.ObjectModelParser;
import jsimple.json.objectmodel.JsonObjectOrArray;
import jsimple.json.readerwriter.JsonArrayWriter;
import jsimple.json.text.Serializer;
import jsimple.json.readerwriter.JsonArrayReader;
import jsimple.json.readerwriter.JsonObjectReader;
import jsimple.json.text.Token;

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
        return new ObjectModelParser(text).parseRoot();
    }

    /**
     * Assuming the JSON text is an object, return a JsonObjectReader, to iteratively read through the name/value pairs
     * in the object one by one, to process each as it's parsed. If the object is big (normally always because it has
     * descendant objects that can contain big arrays), this can be more efficient than parsing the whole JSON text into
     * memory at once.  If text isn't a JSON object (e.g. is a JSON array), an exception is thrown.
     *
     * @param text JSON text, which should be a JSON array
     * @return array reader, to read through the array
     */
    public static JsonObjectReader readObject(String text) {
        return new JsonObjectReader(new Token(text));
    }

    /**
     * Assuming the JSON text is an array, return a JsonArrayReader, to iteratively parse through the items in the array
     * one by one, to process each as it's parsed. If the array is big this can be more efficient than parsing the whole
     * JSON text into memory at once.  If text isn't a JSON array (e.g. is a JSON object), an exception is thrown.
     *
     * @param text JSON text, which should be a JSON array
     * @return array reader, to read through the array
     */
    public static JsonArrayReader readArray(String text) {
        return new JsonArrayReader(new Token(text));
    }

    public static String serialize(JsonObjectOrArray object) {
        Serializer serializer = new Serializer();
        serializer.serialize(object);
        return serializer.getResult();
    }

    /**
     * Serialize a JSON array, item by item.  When an array is large and the root of a JSON text, this method is more
     * memory efficient than creating a single JsonArray (including all items inside of it) in memory all at once and
     * serializing that.
     *
     * @return JsonArrayWriter, to serialize each item
     */
    public static JsonArrayWriter serializeArray() {
        return new JsonArrayWriter(new Serializer());
    }
}
