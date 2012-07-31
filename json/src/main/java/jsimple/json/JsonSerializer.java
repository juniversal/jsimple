package jsimple.json;

/**
 * @author Bret Johnson
 * @since 7/8/12 3:46 AM
 */
public final class JsonSerializer {
    StringBuilder text = new StringBuilder();
    int indent = 0;

    /**
     * Serialize an entire JSON root object.  If this method is called, generally none of other append methods are used
     * by the caller.
     *
     * @param jsonObjectOrArray JSON root object to serialize
     */
    void serialize(JsonObjectOrArray jsonObjectOrArray) {
        append(jsonObjectOrArray);
        text.append("\n");    // Terminate the last line
    }

    String getResult() {
        return text.toString();
    }

    public void append(Object obj) {
        if (obj instanceof String)
            appendString((String) obj);
        else if (obj instanceof Integer)
            text.append(((Integer) obj).intValue());
        else if (obj instanceof Long)
            text.append(((Long) obj).longValue());
        else if (obj instanceof JsonObject)
            appendJsonObject((JsonObject) obj);
        else if (obj instanceof JsonArray)
            appendJsonArray((JsonArray) obj);
        else if (obj instanceof Boolean) {
            boolean booleanValue = (boolean) (Boolean) obj;
            text.append(booleanValue ? "true" : "false");
        } else if (obj instanceof JsonNull)
            text.append("null");
        else throw new RuntimeException("Unexpected JSON object type");
    }

    /**
     * Append a JSON object.  Each name/value pair is output on a separate line, indented by two spaces.  If the object
     * is empty, just "{}" is appended.
     *
     * @param jsonObject object to append
     */
    public void appendJsonObject(JsonObject jsonObject) {
        int size = jsonObject.size();
        if (size == 0)
            text.append("{}");
        else {
            text.append("{\n");
            indent += 2;

            for (int i = 0; i < size; ++i) {
                appendIndent();

                text.append("\"");
                text.append(jsonObject.getName(i));
                text.append("\": ");

                append(jsonObject.getValue(i));

                if (i < size - 1)
                    text.append(",\n");
                else text.append("\n");
            }

            indent -= 2;
            appendIndent();
            text.append("}");
        }
    }

    /**
     * Append a JSON array.  If the array is empty or its elements are all simple objects (that is, literals or embedded
     * objects/arrays that are empty), then output it all on one line.  Otherwise, each array element is on a separate
     * line.
     *
     * @param array array to append
     */
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

                    append(array.get(i));

                    if (i < size - 1)
                        text.append(",\n");
                    else text.append("\n");
                }

                indent -= 2;
                appendIndent();
            } else {
                for (int i = 0; i < size; ++i) {
                    append(array.get(i));

                    if (i < size - 1)
                        text.append(", ");
                }
            }

            text.append(']');
        }
    }

    /**
     * Append spaces for the current indent.
     */
    public void appendIndent() {
        for (int i = 0; i < indent; ++i)
            text.append(' ');
    }

    public void appendString(String string) {
        text.append("\"");

        int length = string.length();
        for (int i = 0; i < length; ++i) {
            char c = string.charAt(i);

            // Check for characters that need to be escaped
            switch (c) {
                case '\"':
                    text.append("\\\"");
                    break;

                case '\\':
                    text.append("\\\\");
                    break;

                case '\b':
                    text.append("\\b");
                    break;

                case '\f':
                    text.append("\\f");
                    break;

                case '\n':
                    text.append("\\n");
                    break;

                case '\r':
                    text.append("\\r");
                    break;

                case '\t':
                    text.append("\\t");
                    break;

                default:
                    if (JsonUtil.isControlCharacter(c))
                        appendUnicodeEscape(c);
                    else text.append(c);
            }
        }

        text.append("\"");
    }

    public void appendUnicodeEscape(char c) {
        text.append("\\u");

        appendHexDigit((c & 0xF000) >>> 12);
        appendHexDigit((c & 0x0F00) >>> 8);
        appendHexDigit((c & 0x00F0) >>> 4);
        appendHexDigit(c & 0x000F);
    }

    public void appendHexDigit(int hexDigit) {
        if (hexDigit <= 9)
            text.append((char) ('0' + hexDigit));
        else if (hexDigit <= 15)
            text.append((char) ('A' + (hexDigit - 10)));
        else throw new RuntimeException("Hex digit out of range: " + hexDigit);
    }

    public void appendRaw(String rawText) {
        text.append(rawText);
    }
}
