/*
    Copyright (c) 2012-2014, Microsoft Mobile

    Permission is hereby granted, free of charge, to any person obtaining a copy
    of this software and associated documentation files (the "Software"), to deal
    in the Software without restriction, including without limitation the rights
    to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
    copies of the Software, and to permit persons to whom the Software is
    furnished to do so, subject to the following conditions:

    The above copyright notice and this permission notice shall be included in
    all copies or substantial portions of the Software.

    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
    IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
    FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
    AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
    LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
    OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
    THE SOFTWARE.
*/

package jsimple.json.objectmodel;

import jsimple.io.Reader;
import jsimple.json.text.JsonParsingException;
import jsimple.json.text.Token;
import jsimple.json.text.TokenType;

/**
 * @author Bret Johnson
 * @since 5/6/12 12:17 AM
 */
public final class ObjectModelParser {
    private Token token;

    /**
     * Parse the specified JSON text.  getResult() can then be used to get the resulting JSON result tree.
     *
     * @param reader JSON text to parse
     */
    public ObjectModelParser(Reader reader) {
        this.token = new Token(reader);
    }

    public JsonObjectOrArray parseRoot() {
        TokenType lookahead = token.getType();

        JsonObjectOrArray result;
        if (lookahead == TokenType.LEFT_BRACE)
            result = parseObject();
        else if (lookahead == TokenType.LEFT_BRACKET)
            result = parseArray();
        else throw new JsonParsingException("{ or [, starting an object or array", token);

        token.check(TokenType.EOF);

        return result;
    }

    private JsonObject parseObject() {
        JsonObject jsonObject = new JsonObject();

        token.checkAndAdvance(TokenType.LEFT_BRACE);

        // Handle the empty object case here (which makes the code below simpler)
        if (token.getType() == TokenType.RIGHT_BRACE) {
            advance();
            return jsonObject;
        }

        while (true) {
            Object nameObject = token.getPrimitiveValue();

            if (!(nameObject instanceof String))
                throw new JsonParsingException("string for object key", token);

            String name = (String) nameObject;
            advance();

            token.checkAndAdvance(TokenType.COLON);

            Object value = parseValue();

            jsonObject.add(name, value);

            if (token.getType() == TokenType.RIGHT_BRACE) {
                advance();
                break;
            } else if (token.getType() == TokenType.COMMA)
                advance();
            else throw new JsonParsingException(", or }", token);
        }

        return jsonObject;
    }

    public Object parseValue() {
        Object value;

        TokenType lookahead = token.getType();
        if (lookahead == TokenType.PRIMITIVE) {
            value = token.getPrimitiveValue();
            advance();
        } else if (lookahead == TokenType.LEFT_BRACE)
            value = parseObject();
        else if (lookahead == TokenType.LEFT_BRACKET)
            value = parseArray();
        else throw new JsonParsingException("primitive type, object, or array", token);

        return value;
    }

    public JsonArray parseArray() {
        JsonArray jsonArray = new JsonArray();

        token.checkAndAdvance(TokenType.LEFT_BRACKET);

        // Handle the empty array case here (which makes the code below simpler)
        if (token.getType() == TokenType.RIGHT_BRACKET) {
            advance();
            return jsonArray;
        }

        while (true) {
            Object value = parseValue();

            jsonArray.add(value);

            if (token.getType() == TokenType.RIGHT_BRACKET) {
                advance();
                break;
            } else if (token.getType() == TokenType.COMMA)
                advance();
            else throw new JsonParsingException(", or ]", token);
        }

        return jsonArray;
    }

    /**
     * Advance to the next token.
     */
    public void advance() {
        token.advance();
    }

    public TokenType getTokenType() {
        return token.getType();
    }
}
