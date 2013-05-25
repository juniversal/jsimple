package jsimple.json.readerwriter;

import jsimple.json.objectmodel.JsonObjectOrArray;
import jsimple.json.text.Serializer;

/**
 * JsonArrayWriter lets the caller serialize the items in an array one by one, iterating through them & serializing each
 * in turn.  If the array is big--often true when it's the outermost object in a JSON text--then this method is more
 * memory efficient than creating JSON objects for the entire array in memory at once.
 * <p/>
 * The array is also formatted a little differently when serialized this way, optimized for a large array that's at the
 * outermost level in a file. Here each value in the array is on a separate line, preceded by a blank line to make the
 * individual values a bit easier to grep.  The normal array serializer will sometimes put values all on one line in
 * certain simple cases, but that never happens here.
 *
 * @author Bret Johnson
 * @since 7/29/12 3:08 PM
 */
public class JsonArrayWriter {
    Serializer serializer;
    boolean outputSomething = false;
    boolean flushWhenDone = false;

    public JsonArrayWriter(Serializer serializer) {
        this.serializer = serializer;
    }

    public JsonArrayWriter(Serializer serializer, boolean flushWhenDone) {
        this.serializer = serializer;
        this.flushWhenDone = flushWhenDone;
    }

    /**
     * Add a value to the array, serializing it.  value can be any valid JSON value class (JsonObject, JsonArray,
     * String, Integer, Long, Boolean, or JsonNull).
     *
     * @param value JSON value for array item
     */
    public void writePrimitive(Object value) {
        writeElementPrefix();
        serializer.appendPrimitive(value);
    }

    public JsonObjectWriter writeObject() {
        writeElementPrefix();
        return new JsonObjectWriter(serializer);
    }

    public JsonArrayWriter writeArray() {
        writeElementPrefix();
        return new JsonArrayWriter(serializer);
    }

    private void writeElementPrefix() {
        if (!outputSomething) {
            serializer.appendRaw("[\n");
            serializer.indent(2);
            outputSomething = true;
        } else serializer.appendRaw(",\n");

        serializer.appendIndent();
    }

    /**
     * Finish the serialization, outputting the closing bracket and returning the resulting string.
     *
     * @return string containing JSON text for entire serialized array
     */
    public void done() {
        if (!outputSomething)
            serializer.appendRaw("[]");
        else {
            serializer.appendRaw("\n");
            serializer.indent(-2);
            serializer.appendIndent();
            serializer.appendRaw("]");
        }

        if (flushWhenDone)
            serializer.flush();
    }

    /**
     * Serialize an entire JSON root object.  If this method is called, generally none of other append methods are used
     * by the caller.
     *
     * @param jsonObjectOrArray JSON root object to serialize
     */
    public void serialize(JsonObjectOrArray jsonObjectOrArray) {
        writePrimitive(jsonObjectOrArray);
        serializer.appendRaw("\n");    // Terminate the last line
    }

/*
    */
/**
 * Append a JSON object.  Each name/value pair is output on a separate line, indented by two spaces.  If the object
 * is empty, just "{}" is appended.
 *
 * @param jsonObject object to append
 *//*

    public void appendJsonObject(JsonObject jsonObject) {
        int size = jsonObject.size();
        if (size == 0)
            text.append("{}");
        else {
            text.append("{\n");
            indent += 2;

            for (int i = 0; i < size; ++i) {
                appendIndent();

                appendString(jsonObject.getName(i));
                writePrimitive(": ");
                writePrimitive(jsonObject.getValue(i));

                if (i < size - 1)
                    text.append(",\n");
                else text.append("\n");
            }

            indent -= 2;
            appendIndent();
            text.append("}");
        }
    }

    */
/**
 * Append a JSON array.  If the array is empty or its elements are all simple objects (that is, literals or embedded
 * objects/arrays that are empty), then output it all on one line.  Otherwise, each array element is on a separate
 * line.
 *
 * @param array array to append
 *//*

    public void appendJsonArray(JsonArray array) {
        int size = array.size();
        if (size == 0)
            text.append("[]");
        else {
            boolean allSimpleObjects = true;

            for (int i = 0; i < size; ++i) {
                Object item = array.get(i);

                if (item instanceof JsonObject && ((JsonObject) item).size() > 0) {
                    allSimpleObjects = false;
                    break;
                } else if (item instanceof JsonArray && ((JsonArray) item).size() > 0) {
                    allSimpleObjects = false;
                    break;
                }
            }

            boolean useMultipleLines = !allSimpleObjects;

            text.append("[");

            if (useMultipleLines) {
                text.append("\n");
                indent += 2;

                for (int i = 0; i < size; ++i) {
                    appendIndent();

                    writePrimitive(array.get(i));

                    if (i < size - 1)
                        text.append(",\n");
                    else text.append("\n");
                }

                indent -= 2;
                appendIndent();
            } else {
                for (int i = 0; i < size; ++i) {
                    writePrimitive(array.get(i));

                    if (i < size - 1)
                        text.append(", ");
                }
            }

            text.append(']');
        }
    }
*/
}
