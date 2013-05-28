package jsimple.json.readerwriter;

import jsimple.json.text.Serializer;

/**
 * @author Bret Johnson
 * @since 5/21/13 11:10 PM
 */
public class JsonObjectWriter extends jsimple.lang.AutoCloseable {
    Serializer serializer;
    boolean flushWhenDone = false;
    boolean singleLine = false;
    boolean outputSomething = false;

    public JsonObjectWriter(Serializer serializer) {
        this.serializer = serializer;
    }

    public JsonObjectWriter(Serializer serializer, boolean flushWhenDone) {
        this.serializer = serializer;
        this.flushWhenDone = flushWhenDone;
    }

    public boolean isSingleLine() {
        return singleLine;
    }

    public void setSingleLine(boolean singleLine) {
        this.singleLine = singleLine;
    }

    public void writeProperty(JsonProperty property, Object value) {
        writePropertyName(property.getName());
        serializer.writeValue(value);
    }

    public void writeProeprty(String propertyName, Object value) {
        writePropertyName(propertyName);
        serializer.writeValue(value);
    }

    public void writePropertyName(String propertyName) {
        if (singleLine) {
            if (!outputSomething) {
                serializer.write("{ ");
                outputSomething = true;
            } else serializer.write(", ");

            serializer.writeString(propertyName);
            serializer.write(": ");
        } else {
            if (!outputSomething) {
                serializer.write("{\n");
                serializer.indent(2);
                outputSomething = true;
            } else serializer.write(",\n");

            serializer.writeIndent();
            serializer.writeString(propertyName);
            serializer.write(": ");
        }
    }

    public JsonObjectWriter writeObjectProperty(JsonObjectProperty property) {
        writePropertyName(property.getName());
        return new JsonObjectWriter(serializer);
    }

    public JsonArrayWriter writeArrayProperty(JsonArrayProperty property) {
        writePropertyName(property.getName());
        return new JsonArrayWriter(serializer);
    }

    @Override public void close() {
        if (!outputSomething)
            serializer.write("{}");
        else {
            if (singleLine) {
                serializer.write(" }");
            } else {
                serializer.write("\n");
                serializer.indent(-2);
                serializer.writeIndent();
                serializer.write("}");
            }
        }

        if (flushWhenDone)
            serializer.flush();
    }
}
