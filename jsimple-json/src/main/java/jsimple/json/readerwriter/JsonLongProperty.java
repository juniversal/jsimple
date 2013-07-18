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
        Object value = objectReader.readPropertyValue();
        if (value instanceof Integer)
            return (long) (int) (Integer) value;
        else return (long) (Long) value;
    }

    @Override public Object readValueUntyped(JsonObjectReader objectReader) {
        return objectReader.readPropertyValue();
    }

    public void write(JsonObjectWriter objectWriter, long value) {
        objectWriter.writeProperty(this, value);
    }
}
