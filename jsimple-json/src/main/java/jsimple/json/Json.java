package jsimple.json;

/**
 * @author Bret Johnson
 * @since 5/24/13 12:37 AM
 */

import jsimple.io.File;
import jsimple.io.Reader;
import jsimple.io.Utf8InputStreamReader;
import jsimple.io.Writer;
import jsimple.json.objectmodel.JsonArray;
import jsimple.json.objectmodel.JsonObject;
import jsimple.json.objectmodel.JsonObjectOrArray;
import jsimple.json.objectmodel.ObjectModelParser;
import jsimple.json.readerwriter.JsonArrayReader;
import jsimple.json.readerwriter.JsonArrayWriter;
import jsimple.json.readerwriter.JsonObjectReader;
import jsimple.json.readerwriter.JsonObjectWriter;
import jsimple.json.text.Serializer;
import jsimple.json.text.Token;
import jsimple.json.text.TokenType;

public final class Json {
    /**
     * Parse the specified JSON text, returning a JsonObject or JsonArray, depending on what the outermost container
     * type is in the JSON.  Per JSON spec, the root element in a JSON text is either an array or an object.
     *
     * @param reader JSON text
     * @return JSON object tree, either a JsonObject or JsonArray object; use instanceof to distinguish
     */
    public static JsonObjectOrArray parse(Reader reader) {
        return new ObjectModelParser(reader).parseRoot();
    }

    /**
     * Parse the specified JSON text, returning a JsonObject.  If the JSON isn't an object (e.g. is actually an array),
     * then an exception is thrown.
     *
     * @param reader JSON text
     * @return JsonObject object tree
     */
    public static JsonObject parseObject(Reader reader) {
        return (JsonObject) new ObjectModelParser(reader).parseRoot();
    }

    /**
     * Parse the specified JSON text, returning a JsonArray.  If the JSON isn't an array (e.g. is actually a
     * JsonObject), then an exception is thrown.
     *
     * @param reader JSON text
     * @return JsonArray object tree
     */
    public static JsonArray parseArray(Reader reader) {
        return (JsonArray) new ObjectModelParser(reader).parseRoot();
    }

    /**
     * Return a JsonObjectReader or a JsonArrayReader for the specified JSON text, depending on what the outermost
     * container type is in the JSON.  The caller can use instanceof to determine which it is.
     *
     * @param reader JSON text
     * @return JsonObjectReader or JsonArrayReader
     */
    public static Object read(Reader reader) {
        Token token = new Token(reader);
        if (token.getType() == TokenType.LEFT_BRACKET)
            return new JsonArrayReader(token);
        else return new JsonObjectReader(token);
    }

    /**
     * Assuming the JSON text is an object, return a JsonObjectReader, to iteratively read through the name/value pairs
     * in the object one by one, to process each as it's parsed. If the object is big (normally always because it has
     * descendant objects that can contain big arrays), this can be more efficient than parsing the whole JSON text into
     * memory at once.  If text isn't a JSON object (e.g. is a JSON array), a class cast exception is thrown.
     *
     * @param reader JSON text, which should be a JSON array
     * @return array reader, to read through the array
     */
    public static JsonObjectReader readObject(Reader reader) {
        return (JsonObjectReader) read(reader);
    }

    /**
     * Assuming the JSON text is an array, return a JsonArrayReader, to iteratively parse through the items in the array
     * one by one, to process each as it's parsed. If the array is big this can be more efficient than parsing the whole
     * JSON text into memory at once.  If text isn't a JSON array (e.g. is a JSON object), a class cast exception is
     * thrown.
     *
     * @param reader JSON text, which should be a JSON array
     * @return array reader, to read through the array
     */
    public static JsonArrayReader readArray(Reader reader) {
        return (JsonArrayReader) read(reader);
    }

    /**
     * Serialize a JSON array, item by item.  When an array is large and the root of a JSON text, this method is more
     * memory efficient than creating a single JsonArray (including all items inside of it) in memory all at once and
     * serializing that.
     *
     * @return JsonArrayWriter, to serialize each item
     */
    public static JsonObjectWriter writeObject(Writer writer) {
        return new JsonObjectWriter(new Serializer(writer), true);
    }

    /**
     * Serialize a JSON array, item by item.  When an array is large and the root of a JSON text, this method is more
     * memory efficient than creating a single JsonArray (including all items inside of it) in memory all at once and
     * serializing that.
     *
     * @return JsonArrayWriter, to serialize each item
     */
    public static JsonArrayWriter writeArray(Writer writer) {
        return new JsonArrayWriter(new Serializer(writer), true);
    }
}
