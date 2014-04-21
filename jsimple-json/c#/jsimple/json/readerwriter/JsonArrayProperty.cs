namespace jsimple.json.readerwriter {

    /// <summary>
    /// @author Bret Johnson
    /// @since 5/22/13 9:11 PM
    /// </summary>
    public class JsonArrayProperty : JsonProperty {
        public JsonArrayProperty(string name, int id) : base(name, id) {
        }

        public virtual JsonArrayReader readValue(JsonObjectReader objectReader) {
            return (JsonArrayReader) objectReader.readPropertyValue();
        }

        public virtual JsonArrayWriter write(JsonObjectWriter objectWriter) {
            return objectWriter.writeArrayProperty(this);
        }
    }

}