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
        return (boolean) (Boolean) objectReader.readPropertyValue();
    }

    @Override public Object readValueUntyped(JsonObjectReader objectReader) {
        return objectReader.readPropertyValue();
    }

    public void write(JsonObjectWriter objectWriter, boolean value) {
        objectWriter.writeProperty(this, value);
    }
}
