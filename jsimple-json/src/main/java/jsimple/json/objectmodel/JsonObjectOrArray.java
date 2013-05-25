package jsimple.json.objectmodel;

import jsimple.io.StringWriter;
import jsimple.io.Writer;
import jsimple.json.text.Serializer;

/**
 * This class is simply used to represent, in a type safe way, either a JSON object or an array.  According to the spec
 * a JSON text, at the highest level, is allowed to be either a JSON object or array, so this class captures that.
 *
 * @author Bret Johnson
 * @since 7/8/12 1:56 PM
 */
abstract public class JsonObjectOrArray {
    public void serialize(Writer writer) {
        Serializer serializer = new Serializer(writer);
        serializer.serialize(this);
    }

    @Override public String toString() {
        StringWriter stringWriter = new StringWriter();
        serialize(stringWriter);
        return stringWriter.toString();
    }
}
