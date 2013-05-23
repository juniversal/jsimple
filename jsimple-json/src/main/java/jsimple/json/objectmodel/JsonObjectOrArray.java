package jsimple.json.objectmodel;

import jsimple.json.Json;
import jsimple.json.readerwriter.JsonArrayWriter;
import jsimple.json.readerwriter.JsonObjectWriter;

/**
 * This class is simply used to represent, in a type safe way, either a JSON object or an array.  According to the spec
 * a JSON text, at the highest level, is allowed to be either a JSON object or array, so this class captures that.
 *
 * @author Bret Johnson
 * @since 7/8/12 1:56 PM
 */
abstract public class JsonObjectOrArray {
    @Override public String toString() {
        return Json.serialize(this);
    }
}
