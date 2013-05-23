package jsimple.json.readerwriter;

/**
 * @author Bret Johnson
 * @since 5/22/13 9:12 PM
 */
public class JsonObjectProperty extends JsonProperty {
    public JsonObjectProperty(String name, int id) {
        super(name, id);
    }

    public JsonObjectReader readValue(JsonObjectReader objectReader) {
        return objectReader.readObjectPropertyValue();
    }

    public JsonObjectWriter write(JsonObjectWriter objectWriter) {
        return objectWriter.writeObjectProperty(this);
    }
}
