package jsimple.json.readerwriter;

/**
 * @author Bret Johnson
 * @since 5/22/13 9:09 PM
 */
public class JsonBooleanProperty extends JsonProperty {
    public JsonBooleanProperty(String name, int id) {
        super(name, id);
    }

    public boolean readValue(JsonObjectReader objectReader) {
        return (Boolean) objectReader.readPrimitiveValue();
    }

    public void write(JsonObjectWriter objectWriter, boolean value) {
        objectWriter.write(this, value);
    }
}
