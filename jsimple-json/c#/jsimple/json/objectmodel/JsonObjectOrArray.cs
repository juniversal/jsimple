namespace jsimple.json.objectmodel {

    using ByteArrayOutputStream = jsimple.io.ByteArrayOutputStream;
    using StringWriter = jsimple.io.StringWriter;
    using Utf8OutputStreamWriter = jsimple.io.Utf8OutputStreamWriter;
    using Writer = jsimple.io.Writer;
    using Serializer = jsimple.json.text.Serializer;
    using ByteArrayRange = jsimple.util.ByteArrayRange;

    /// <summary>
    /// This class is simply used to represent, in a type safe way, either a JSON object or an array.  According to the spec
    /// a JSON text, at the highest level, is allowed to be either a JSON object or array, so this class captures that.
    /// 
    /// @author Bret Johnson
    /// @since 7/8/12 1:56 PM
    /// </summary>
    public abstract class JsonObjectOrArray {
        public virtual void write(Writer writer) {
            Serializer serializer = new Serializer(writer);
            serializer.writeValue(this);
            serializer.write("\n"); // Terminate the last line
            serializer.flush();
        }

        public override string ToString() {
            StringWriter stringWriter = new StringWriter();
            write(stringWriter);
            return stringWriter.ToString();
        }

        public virtual ByteArrayRange toUtf8Bytes() {
            using (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
                using (Utf8OutputStreamWriter writer = new Utf8OutputStreamWriter(byteArrayOutputStream)) {
                    write(writer);
                    return byteArrayOutputStream.closeAndGetByteArray();
                }
            }
        }
    }

}