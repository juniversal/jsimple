package jsimple.json.readerwriter;

/**
 * @author Bret Johnson
 * @since 5/22/13 9:07 PM
 */
public class JsonStringProperty extends JsonProperty {
    public JsonStringProperty(String name, int id) {
        super(name, id);
    }

    public String readValue(JsonObjectReader objectReader) {
        return (String) objectReader.readPropertyValue();
    }

    @Override public Object readValueUntyped(JsonObjectReader objectReader) {
        return objectReader.readPropertyValue();
    }

    public void write(JsonObjectWriter objectWriter, String value) {
        objectWriter.writeProperty(this, value);
    }
}
