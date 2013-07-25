package jsimple.json.readerwriter;

import jsimple.json.JsonException;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

/**
 * @author Bret Johnson
 * @since 5/19/13 3:10 PM
 */
public class JsonObjectType {
    private @Nullable JsonObjectType superclass;
    private HashMap<String, JsonProperty> properties = new HashMap<String, JsonProperty>();

    public JsonObjectType(@Nullable JsonObjectType superclass) {
        this.superclass = superclass;
    }

    public JsonObjectType() {
        this(null);
    }

    public @Nullable JsonProperty getProperty(String name) {
        @Nullable JsonProperty value = properties.get(name);
        if (value != null)
            return value;
        if (superclass != null)
            return superclass.getProperty(name);
        return null;
    }

    public JsonProperty createProperty(String name, int id) {
        JsonProperty property = properties.get(name);
        if (property != null)
            throw new JsonException("Property for '{}' has already been added to atoms list", name);

        property = new JsonProperty(name, id);
        properties.put(name, property);
        return property;
    }

    private <T extends JsonProperty> T addNewProperty(T property) {
        String name = property.getName();
        if (properties.get(name) != null)
            throw new JsonException("Property for '{}' has already been added to atoms list", name);

        properties.put(name, property);
        return property;
    }

    public JsonStringProperty createStringProperty(String name, int id) {
        return addNewProperty(new JsonStringProperty(name, id));
    }

    public JsonIntProperty createIntProperty(String name, int id) {
        return addNewProperty(new JsonIntProperty(name, id));
    }

    public JsonLongProperty createLongProperty(String name, int id) {
        return addNewProperty(new JsonLongProperty(name, id));
    }

    public JsonBooleanProperty createBooleanProperty(String name, int id) {
        return addNewProperty(new JsonBooleanProperty(name, id));
    }

    public JsonObjectProperty createObjectProperty(String name, int id) {
        return addNewProperty(new JsonObjectProperty(name, id));
    }

    public JsonArrayProperty createArrayProperty(String name, int id) {
        return addNewProperty(new JsonArrayProperty(name, id));
    }
}
