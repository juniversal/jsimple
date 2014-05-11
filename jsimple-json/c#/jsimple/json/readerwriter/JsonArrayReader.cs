namespace jsimple.json.readerwriter {

    using JsonNull = jsimple.json.objectmodel.JsonNull;
    using JsonParsingException = jsimple.json.text.JsonParsingException;
    using Token = jsimple.json.text.Token;
    using TokenType = jsimple.json.text.TokenType;

    /// <summary>
    /// JsonArrayReader lets the caller parse the items in an array one by one, iterating through them & processing them as
    /// they're parsed.  If the array is very big--often when it's the outermost object in a JSON text--then this method is
    /// more memory efficient than parsing the whole array into memory at once.
    /// 
    /// @author Bret Johnson
    /// @since 7/28/12 11:09 PM
    /// </summary>
    public class JsonArrayReader {
        protected internal Token token;
        private bool atBeginning = true;
        private bool atEnd_Renamed = false;

        public JsonArrayReader(Token token) {
            this.token = token;
            token.checkAndAdvance(TokenType.LEFT_BRACKET);
        }

        protected internal virtual void readElementPrefix() {
            if (atBeginning)
                atBeginning = false;
            else
                token.checkAndAdvance(TokenType.COMMA);
        }

        public virtual bool atEnd() {
            if (!atEnd_Renamed && token.Type == TokenType.RIGHT_BRACKET) {
                token.advance();
                atEnd_Renamed = true;
            }

            return atEnd_Renamed;
        }

        public virtual object readPrimitive() {
            readElementPrefix();

            object value = token.PrimitiveValue;
            if (value == JsonNull.singleton)
                throw new JsonParsingException("non-null value", token);

            token.advance();
            return value;
        }

        public virtual bool readBoolean() {
            return (bool)(bool?) readPrimitive();
        }

        public virtual string readString() {
            return (string) readPrimitive();
        }

        public virtual int readInt() {
            return (int)(int?) readPrimitive();
        }

        // TODO: Automatically convert int to long
        public virtual long readLong() {
            return (long)(long?) readPrimitive();
        }

        public virtual double readDouble() {
            return (double)(double?) readPrimitive();
        }

        public virtual JsonObjectReader readObject() {
            readElementPrefix();

            if (token.Type != TokenType.LEFT_BRACE)
                throw new JsonParsingException("object as the value, starting with {", token);

            return new JsonObjectReader(token);
        }

        public virtual JsonArrayReader readArray() {
            readElementPrefix();

            if (token.Type != TokenType.LEFT_BRACKET)
                throw new JsonParsingException("array as the value, starting with [", token);

            return new JsonArrayReader(token);
        }
    }

}