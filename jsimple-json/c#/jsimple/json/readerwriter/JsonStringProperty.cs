namespace jsimple.json.readerwriter {

    /// <summary>
    /// @author Bret Johnson
    /// @since 5/22/13 9:07 PM
    /// </summary>
    public class JsonStringProperty : JsonProperty {
        public JsonStringProperty(string name, int id) : base(name, id) {
        }

        public virtual string readValue(JsonObjectReader objectReader) {
            return (string) objectReader.readPropertyValue();
        }

        public override object readValueUntyped(JsonObjectReader objectReader) {
            return objectReader.readPropertyValue();
        }

        public virtual void write(JsonObjectWriter objectWriter, string value) {
            objectWriter.writeProperty(this, value);
        }
    }

}