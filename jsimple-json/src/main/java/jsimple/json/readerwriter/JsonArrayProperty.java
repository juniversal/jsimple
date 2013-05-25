package jsimple.json.readerwriter;

/**
 * @author Bret Johnson
 * @since 5/22/13 9:11 PM
 */
public class JsonArrayProperty extends JsonProperty {
    public JsonArrayProperty(String name, int id) {
        super(name, id);
    }

    public JsonArrayReader readValue(JsonObjectReader objectReader) {
        return (JsonArrayReader) objectReader.readPropertyValue();
    }

    public JsonArrayWriter write(JsonObjectWriter objectWriter) {
        return objectWriter.writeArrayProperty(this);
    }
}
