namespace jsimple.json.readerwriter {

    using JsonParsingException = jsimple.json.text.JsonParsingException;
    using Token = jsimple.json.text.Token;
    using TokenType = jsimple.json.text.TokenType;

    /// <summary>
    /// JsonObjectReader lets the caller parse the items in an array one by one, iterating through them & processing them as
    /// they're parsed.  If the array is very big--often when it's the outermost object in a JSON text--then this method is
    /// more memory efficient than parsing the whole array into memory at once.
    /// 
    /// @author Bret Johnson
    /// @since 7/28/12 11:09 PM
    /// </summary>
    public class JsonObjectReader {
        protected internal Token token;
        private JsonObjectType objectType = emptyObjectType; // This is the default, if no explicit ObjectType is set
        private bool atBeginning = true;
        private bool atEnd_Renamed = false;

        private static readonly JsonObjectType emptyObjectType = new JsonObjectType();

        public JsonObjectReader(Token token) {
            this.token = token;
            token.checkAndAdvance(TokenType.LEFT_BRACE);
        }

        public virtual string readPropertyName() {
            if (atEnd_Renamed)
                throw new JsonException("JSON Object has no more name/value pairs");

            if (atBeginning)
                atBeginning = false;
            else
                token.checkAndAdvance(TokenType.COMMA);

            token.check(TokenType.PRIMITIVE);
            object nameObject = token.PrimitiveValue;
            if (!(nameObject is string))
                throw new JsonParsingException("string for object key", token);
            token.advance();

            token.checkAndAdvance(TokenType.COLON);

            return (string) nameObject;
        }

        public virtual JsonProperty readProperty() {
            string name = readPropertyName();
            JsonProperty property = objectType.getProperty(name);
            if (property == null)
                property = new JsonProperty(name);
            return property;
        }

        public virtual object readPropertyValue() {
            TokenType type = token.Type;
            switch (type) {
                case TokenType.PRIMITIVE:
                    object value = token.PrimitiveValue;
                    token.advance();
                    return value;
                case TokenType.LEFT_BRACKET:
                    return new JsonArrayReader(token);
                case TokenType.LEFT_BRACE:
                    return new JsonObjectReader(token);
                default:
                    if (atEnd())
                        throw new JsonException("No more object properties");
                    else
                        throw new JsonParsingException("start of property value", token);
            }
        }

        public virtual bool atEnd() {
            if (!atEnd_Renamed && token.Type == TokenType.RIGHT_BRACE) {
                token.advance();
                atEnd_Renamed = true;
            }

            return atEnd_Renamed;
        }

        public virtual JsonObjectType ObjectType {
            set {
                this.objectType = value;
            }
        }
    }

}