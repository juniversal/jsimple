package jsimple.json.readerwriter;

/**
 * @author Bret Johnson
 * @since 5/17/13 12:40 AM
 */
public class JsonProperty {
    protected String name;
    protected int id;

    public JsonProperty(String name) {
        this.name = name;
        this.id = -1;
    }

    public JsonProperty(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public Object readValueUntyped(JsonObjectReader objectReader) {
        return objectReader.readPropertyValue();
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }
}
