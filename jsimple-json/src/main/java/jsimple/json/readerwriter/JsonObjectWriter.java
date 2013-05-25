package jsimple.json.readerwriter;

import jsimple.json.objectmodel.JsonObjectOrArray;
import jsimple.json.text.Serializer;

/**
 * @author Bret Johnson
 * @since 5/21/13 11:10 PM
 */
public class JsonObjectWriter {
    boolean outputSomething = false;
    Serializer serializer;
    boolean flushWhenDone = false;

    public JsonObjectWriter(Serializer serializer) {
        this.serializer = serializer;
    }

    public JsonObjectWriter(Serializer serializer, boolean flushWhenDone) {
        this.serializer = serializer;
        this.flushWhenDone = flushWhenDone;
    }

    public void done() {
        if (!outputSomething)
            serializer.appendRaw("{}");
        else {
            serializer.appendRaw("\n");
            serializer.indent(-2);
            serializer.appendIndent();
            serializer.appendRaw("}");
        }

        if (flushWhenDone)
            serializer.flush();
    }

    public void write(JsonProperty property, Object value) {
        writePropertyName(property.getName());
        serializer.appendPrimitive(value);
    }

    public void write(String propertyName, Object value) {
        writePropertyName(propertyName);
        serializer.appendPrimitive(value);
    }

    public void writePropertyName(String propertyName) {
        if (!outputSomething) {
            serializer.appendRaw("{\n");
            serializer.indent(2);
            outputSomething = true;
        } else serializer.appendRaw(",\n");

        serializer.appendIndent();
        serializer.appendString(propertyName);
        serializer.appendRaw(": ");
    }

    public JsonObjectWriter writeObjectProperty(JsonObjectProperty property) {
        writePropertyName(property.getName());
        return new JsonObjectWriter(serializer);
    }

    public JsonArrayWriter writeArrayProperty(JsonArrayProperty property) {
        writePropertyName(property.getName());
        return new JsonArrayWriter(serializer);
    }

    /**
     * Serialize an entire JSON root object.  If this method is called, generally none of other append methods are used
     * by the caller.
     *
     * @param jsonObjectOrArray JSON root object to serialize
     */
    public void serialize(JsonObjectOrArray jsonObjectOrArray) {
        serializer.append(jsonObjectOrArray);
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
                serializer.appendPrimitive(": ");
                serializer.appendPrimitive(jsonObject.getValue(i));

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

                    serializer.appendPrimitive(array.get(i));

                    if (i < size - 1)
                        text.append(",\n");
                    else text.append("\n");
                }

                indent -= 2;
                appendIndent();
            } else {
                for (int i = 0; i < size; ++i) {
                    serializer.appendPrimitive(array.get(i));

                    if (i < size - 1)
                        text.append(", ");
                }
            }

            text.append(']');
        }
    }
*/
}
