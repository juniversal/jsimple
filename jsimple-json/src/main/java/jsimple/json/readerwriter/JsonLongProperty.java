package jsimple.json.readerwriter;

/**
 * @author Bret Johnson
 * @since 5/22/13 9:08 PM
 */
public class JsonLongProperty extends JsonProperty {
    public JsonLongProperty(String name, int id) {
        super(name, id);
    }

    // TODO: Automatically convert int to long
    public long readValue(JsonObjectReader objectReader) {
        return (Long) objectReader.readPrimitiveValue();
    }

    public void write(JsonObjectWriter objectWriter, long value) {
        objectWriter.write(this, value);
    }
}
