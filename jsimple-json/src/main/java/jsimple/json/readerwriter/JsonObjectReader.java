package jsimple.json.readerwriter;

import jsimple.json.JsonException;
import jsimple.json.text.JsonParsingException;
import jsimple.json.text.Token;
import jsimple.json.text.TokenType;

/**
 * JsonObjectReader lets the caller parse the items in an array one by one, iterating through them & processing them as
 * they're parsed.  If the array is very big--often when it's the outermost object in a JSON text--then this method is
 * more memory efficient than parsing the whole array into memory at once.
 *
 * @author Bret Johnson
 * @since 7/28/12 11:09 PM
 */
public class JsonObjectReader {
    protected Token token;
    private JsonObjectType objectType = emptyObjectType;   // This is the default, if no explicit ObjectType is set
    private boolean atBeginning = true;
    private boolean atEnd = false;

    private static final JsonObjectType emptyObjectType = new JsonObjectType();

    public JsonObjectReader(Token token) {
        this.token = token;
        token.checkAndAdvance(TokenType.LEFT_BRACE);
    }

    public String readPropertyName() {
        if (atEnd)
            throw new JsonException("JSON Object has no more name/value pairs");

        if (atBeginning)
            atBeginning = false;
        else token.checkAndAdvance(TokenType.COMMA);

        token.check(TokenType.PRIMITIVE);
        Object nameObject = token.getPrimitiveValue();
        if (!(nameObject instanceof String))
            throw new JsonParsingException("string for object key", token);
        token.advance();

        token.checkAndAdvance(TokenType.COLON);

        return (String) nameObject;
    }

    public JsonProperty readProperty() {
        String name = readPropertyName();
        JsonProperty property = objectType.getProperty(name);
        if (property == null)
            property = new JsonProperty(name);
        return property;
    }

    public Object readPropertyValue() {
        TokenType type = token.getType();
        switch (type) {
            case PRIMITIVE:
                Object value = token.getPrimitiveValue();
                token.advance();
                return value;
            case LEFT_BRACKET:
                return new JsonArrayReader(token);
            case LEFT_BRACE:
                return new JsonObjectReader(token);
            default:
                if (atEnd())
                    throw new JsonException("No more object properties");
                else throw new JsonParsingException("start of property value", token);
        }
    }

    public boolean atEnd() {
        if (!atEnd && token.getType() == TokenType.RIGHT_BRACE) {
            token.advance();
            atEnd = true;
        }

        return atEnd;
    }

    public void setObjectType(JsonObjectType objectType) {
        this.objectType = objectType;
    }
}
