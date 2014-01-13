package jsimple.json.objectmodel;

import jsimple.io.ByteArrayOutputStream;
import jsimple.io.StringWriter;
import jsimple.io.Utf8OutputStreamWriter;
import jsimple.io.Writer;
import jsimple.json.text.Serializer;
import jsimple.util.ByteArrayRange;

/**
 * This class is simply used to represent, in a type safe way, either a JSON object or an array.  According to the spec
 * a JSON text, at the highest level, is allowed to be either a JSON object or array, so this class captures that.
 *
 * @author Bret Johnson
 * @since 7/8/12 1:56 PM
 */
abstract public class JsonObjectOrArray {
    public void write(Writer writer) {
        Serializer serializer = new Serializer(writer);
        serializer.writeValue(this);
        serializer.write("\n");    // Terminate the last line
        serializer.flush();
    }

    @Override public String toString() {
        StringWriter stringWriter = new StringWriter();
        write(stringWriter);
        return stringWriter.toString();
    }

    public ByteArrayRange toUtf8Bytes() {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            try (Utf8OutputStreamWriter writer = new Utf8OutputStreamWriter(byteArrayOutputStream)) {
                write(writer);
                return byteArrayOutputStream.closeAndGetByteArray();
            }
        }
    }
}
