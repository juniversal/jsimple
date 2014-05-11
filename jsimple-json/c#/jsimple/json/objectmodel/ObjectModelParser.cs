namespace jsimple.json.objectmodel {

    using Reader = jsimple.io.Reader;
    using JsonParsingException = jsimple.json.text.JsonParsingException;
    using Token = jsimple.json.text.Token;
    using TokenType = jsimple.json.text.TokenType;

    /// <summary>
    /// @author Bret Johnson
    /// @since 5/6/12 12:17 AM
    /// </summary>
    public sealed class ObjectModelParser {
        private Token token;

        /// <summary>
        /// Parse the specified JSON text.  getResult() can then be used to get the resulting JSON result tree.
        /// </summary>
        /// <param name="reader"> JSON text to parse </param>
        public ObjectModelParser(Reader reader) {
            this.token = new Token(reader);
        }

        public JsonObjectOrArray parseRoot() {
            TokenType lookahead = token.Type;

            JsonObjectOrArray result;
            if (lookahead == TokenType.LEFT_BRACE)
                result = parseObject();
            else if (lookahead == TokenType.LEFT_BRACKET)
                result = parseArray();
            else
                throw new JsonParsingException("{ or [, starting an object or array", token);

            token.check(TokenType.EOF);

            return result;
        }

        private JsonObject parseObject() {
            JsonObject jsonObject = new JsonObject();

            token.checkAndAdvance(TokenType.LEFT_BRACE);

            // Handle the empty object case here (which makes the code below simpler)
            if (token.Type == TokenType.RIGHT_BRACE) {
                advance();
                return jsonObject;
            }

            while (true) {
                object nameObject = token.PrimitiveValue;

                if (!(nameObject is string))
                    throw new JsonParsingException("string for object key", token);

                string name = (string) nameObject;
                advance();

                token.checkAndAdvance(TokenType.COLON);

                object value = parseValue();

                jsonObject.add(name, value);

                if (token.Type == TokenType.RIGHT_BRACE) {
                    advance();
                    break;
                }
                else if (token.Type == TokenType.COMMA)
                    advance();
                else
                    throw new JsonParsingException(", or }", token);
            }

            return jsonObject;
        }

        public object parseValue() {
            object value;

            TokenType lookahead = token.Type;
            if (lookahead == TokenType.PRIMITIVE) {
                value = token.PrimitiveValue;
                advance();
            }
            else if (lookahead == TokenType.LEFT_BRACE)
                value = parseObject();
            else if (lookahead == TokenType.LEFT_BRACKET)
                value = parseArray();
            else
                throw new JsonParsingException("primitive type, object, or array", token);

            return value;
        }

        public JsonArray parseArray() {
            JsonArray jsonArray = new JsonArray();

            token.checkAndAdvance(TokenType.LEFT_BRACKET);

            // Handle the empty array case here (which makes the code below simpler)
            if (token.Type == TokenType.RIGHT_BRACKET) {
                advance();
                return jsonArray;
            }

            while (true) {
                object value = parseValue();

                jsonArray.add(value);

                if (token.Type == TokenType.RIGHT_BRACKET) {
                    advance();
                    break;
                }
                else if (token.Type == TokenType.COMMA)
                    advance();
                else
                    throw new JsonParsingException(", or ]", token);
            }

            return jsonArray;
        }

        /// <summary>
        /// Advance to the next token.
        /// </summary>
        public void advance() {
            token.advance();
        }

        public TokenType TokenType {
            get {
                return token.Type;
            }
        }
    }

}