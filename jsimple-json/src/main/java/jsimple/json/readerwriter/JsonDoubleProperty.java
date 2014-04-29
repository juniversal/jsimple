package jsimple.json.readerwriter;

/**
 * @author Bret Johnson
 * @since 5/22/13 9:08 PM
 */
public class JsonDoubleProperty extends JsonProperty {
    public JsonDoubleProperty(String name, int id) {
        super(name, id);
    }

    public double readValue(JsonObjectReader objectReader) {
        return (double) (Double) objectReader.readPropertyValue();
    }

    @Override public Object readValueUntyped(JsonObjectReader objectReader) {
        return objectReader.readPropertyValue();
    }

    public void write(JsonObjectWriter objectWriter, double value) {
        objectWriter.writeProperty(this, value);
    }
}
