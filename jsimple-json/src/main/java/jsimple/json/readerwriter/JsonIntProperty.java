package jsimple.json.readerwriter;

/**
 * @author Bret Johnson
 * @since 5/17/13 12:40 AM
 */
public class JsonIntProperty extends JsonProperty {
    public JsonIntProperty(String name, int id) {
        super(name, id);
    }

    public int readValue(JsonObjectReader objectReader) {
        return (Integer) objectReader.readPrimitiveValue();
    }

    public void write(JsonObjectWriter objectWriter, int value) {
        objectWriter.write(this, value);
    }
}
