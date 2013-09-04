package jsimple.json.text;

import jsimple.io.Writer;
import jsimple.json.JsonException;
import jsimple.json.objectmodel.JsonArray;
import jsimple.json.objectmodel.JsonNull;
import jsimple.json.objectmodel.JsonObject;

/**
 * @author Bret Johnson
 * @since 7/8/12 3:46 AM
 */
public final class Serializer {
    private Writer writer;
    private char[] buffer = new char[BUFFER_SIZE];
    private int currIndex;      // Next character to be processed
    int indent;

    private static final int BUFFER_SIZE = 256;

    public Serializer(Writer writer) {
        this.writer = writer;
        currIndex = 0;
        indent = 0;
    }

    public void writeValue(Object obj) {
        if (obj instanceof String)
            writeString((String) obj);
        else if (obj instanceof Integer || obj instanceof Long)
            write(obj.toString());
        else if (obj instanceof Boolean) {
            boolean booleanValue = (boolean) (Boolean) obj;
            write(booleanValue ? "true" : "false");
        } else if (obj instanceof JsonObject)
            writeJsonObject((JsonObject) obj);
        else if (obj instanceof JsonArray)
            writeJsonArray((JsonArray) obj);
        else if (obj instanceof JsonNull)
            write("null");
        else throw new JsonException("Unexpected JSON object type: {}", obj);
    }

    /**
     * Write a JSON object.  Each name/value pair is output on a separate line, indented by two spaces.  If the object
     * is empty, just "{}" is appended.
     *
     * @param jsonObject object to append
     */
    public void writeJsonObject(JsonObject jsonObject) {
        int size = jsonObject.size();
        if (size == 0)
            write("{}");
        else {
            write("{\n");
            indent += 2;

            for (int i = 0; i < size; ++i) {
                writeIndent();

                writeString(jsonObject.getName(i));
                write(": ");
                writeValue(jsonObject.getValue(i));

                if (i < size - 1)
                    write(",\n");
                else write("\n");
            }

            indent -= 2;
            writeIndent();
            write("}");
        }
    }

    /**
     * Append a JSON array.  If the array is empty or its elements are all simple objects (that is, literals or embedded
     * objects/arrays that are empty), then output it all on one line.  Otherwise, each array element is on a separate
     * line.
     *
     * @param array array to append
     */
    public void writeJsonArray(JsonArray array) {
        int size = array.size();
        if (size == 0)
            write("[]");
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

            write("[");

            if (useMultipleLines) {
                write("\n");
                indent += 2;

                for (int i = 0; i < size; ++i) {
                    writeIndent();

                    writeValue(array.get(i));

                    if (i < size - 1)
                        write(",\n");
                    else write("\n");
                }

                indent -= 2;
                writeIndent();
            } else {
                for (int i = 0; i < size; ++i) {
                    writeValue(array.get(i));

                    if (i < size - 1)
                        write(", ");
                }
            }

            write(']');
        }
    }

    /**
     * Append spaces for the current indent.
     */
    public void writeIndent() {
        for (int i = 0; i < indent; ++i)
            write(' ');
    }

    public void writeString(String string) {
        write('\"');

        int length = string.length();
        for (int i = 0; i < length; ++i) {
            char c = string.charAt(i);

            // Check for characters that need to be escaped
            switch (c) {
                case '\"':
                    write("\\\"");
                    break;

                case '\\':
                    write("\\\\");
                    break;

                case '\b':
                    write("\\b");
                    break;

                case '\f':
                    write("\\f");
                    break;

                case '\n':
                    write("\\n");
                    break;

                case '\r':
                    write("\\r");
                    break;

                case '\t':
                    write("\\t");
                    break;

                default:
                    if (Token.isControlCharacter(c))
                        writeUnicodeEscape(c);
                    else write(c);
            }
        }

        write("\"");
    }

    public void writeUnicodeEscape(char c) {
        write("\\u");

        writeHexDigit((c & 0xF000) >>> 12);
        writeHexDigit((c & 0x0F00) >>> 8);
        writeHexDigit((c & 0x00F0) >>> 4);
        writeHexDigit(c & 0x000F);
    }

    public void writeHexDigit(int hexDigit) {
        if (hexDigit <= 9)
            write((char) ('0' + hexDigit));
        else if (hexDigit <= 15)
            write((char) ('A' + (hexDigit - 10)));
        else throw new JsonException("Hex digit out of range: {}", hexDigit);
    }

    public void write(String s) {
        int length = s.length();
        for (int i = 0; i < length; i++)
            write(s.charAt(i));
    }

    private void write(char c) {
        if (currIndex >= BUFFER_SIZE)
            flush();
        buffer[currIndex++] = c;
    }

    public void flush() {
        writer.write(buffer, 0, currIndex);
        currIndex = 0;
    }

    /**
     * Increment (or decrement if negative) the prevailing indent by the specified amount.
     *
     * @param amount amount to change indent
     */
    public void indent(int amount) {
        indent += amount;
    }
}
