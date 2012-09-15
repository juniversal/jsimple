package jsimple.json;

/**
 * JsonArrayParser lets the caller parse the items in an array one by one, iterating through them & processing them as
 * they're parsed.  If the array is very big--often when it's the outermost object in a JSON text--then this method is
 * more memory efficient than parsing the whole array into memory at once.
 *
 * @author Bret Johnson
 * @since 7/28/12 11:09 PM
 */
public class JsonArrayParser {
    JsonParser parser;
    boolean atEnd = false;

    public JsonArrayParser(JsonParser jsonParser) {
        parser = jsonParser;
        parser.checkAndAdvance(TokenType.LEFT_BRACKET);

        if (parser.getTokenType() == TokenType.RIGHT_BRACKET) {
            parser.advance();
            atEnd = true;
        }
    }

    public Object next() {
        if (atEnd)
            throw new JsonException("Can't call next when already at end");

        Object value = parser.parseValue();

        TokenType lookahead = parser.getTokenType();
        if (lookahead == TokenType.COMMA)
            parser.advance();
        else {
            parser.checkAndAdvance(TokenType.RIGHT_BRACKET);
            atEnd = true;
        }

        return value;
    }

    public boolean hasNext() {
        return !atEnd;
    }
}
