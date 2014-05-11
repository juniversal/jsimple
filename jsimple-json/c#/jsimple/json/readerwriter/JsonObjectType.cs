using System.Collections.Generic;

namespace jsimple.json.readerwriter {


    /// <summary>
    /// @author Bret Johnson
    /// @since 5/19/13 3:10 PM
    /// </summary>
    public class JsonObjectType {
        private JsonObjectType superclass;
        private Dictionary<string, JsonProperty> properties = new Dictionary<string, JsonProperty>();

        public JsonObjectType(JsonObjectType superclass) {
            this.superclass = superclass;
        }

        public JsonObjectType() : this(null) {
        }

        public virtual JsonProperty getProperty(string name) {
            JsonProperty value = properties.GetValueOrNull(name);
            if (value != null)
                return value;
            if (superclass != null)
                return superclass.getProperty(name);
            return null;
        }

        public virtual JsonProperty createProperty(string name, int id) {
            JsonProperty property = properties.GetValueOrNull(name);
            if (property != null)
                throw new JsonException("Property for '{}' has already been added to atoms list", name);

            property = new JsonProperty(name, id);
            properties[name] = property;
            return property;
        }

        private T addNewProperty<T>(T property) where T : JsonProperty {
            string name = property.Name;
            if (properties.GetValueOrNull(name) != null)
                throw new JsonException("Property for '{}' has already been added to atoms list", name);

            properties[name] = property;
            return property;
        }

        public virtual JsonStringProperty createStringProperty(string name, int id) {
            return addNewProperty(new JsonStringProperty(name, id));
        }

        public virtual JsonIntProperty createIntProperty(string name, int id) {
            return addNewProperty(new JsonIntProperty(name, id));
        }

        public virtual JsonLongProperty createLongProperty(string name, int id) {
            return addNewProperty(new JsonLongProperty(name, id));
        }

        public virtual JsonDoubleProperty createDoubleProperty(string name, int id) {
            return addNewProperty(new JsonDoubleProperty(name, id));
        }

        public virtual JsonBooleanProperty createBooleanProperty(string name, int id) {
            return addNewProperty(new JsonBooleanProperty(name, id));
        }

        public virtual JsonObjectProperty createObjectProperty(string name, int id) {
            return addNewProperty(new JsonObjectProperty(name, id));
        }

        public virtual JsonArrayProperty createArrayProperty(string name, int id) {
            return addNewProperty(new JsonArrayProperty(name, id));
        }
    }

}