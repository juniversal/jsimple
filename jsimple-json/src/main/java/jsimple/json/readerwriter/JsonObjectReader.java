package jsimple.json.readerwriter;

import jsimple.json.JsonException;
import jsimple.json.objectmodel.JsonNull;
import jsimple.json.text.JsonParsingException;
import jsimple.json.text.Token;
import jsimple.json.text.TokenType;
import org.jetbrains.annotations.Nullable;

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
    private @Nullable JsonObjectType objectType = null;
    private boolean atBeginning = true;
    private boolean atEnd = false;

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

    public JsonObjectReader readObjectPropertyValue() {
        if (token.getType() != TokenType.LEFT_BRACE)
            throw new JsonParsingException("object as the value, starting with {", token);

        return new JsonObjectReader(token);
    }

    public JsonArrayReader readArrayPropertyValue() {
        if (token.getType() != TokenType.LEFT_BRACKET)
            throw new JsonParsingException("array as the value, starting with [", token);

        return new JsonArrayReader(token);
    }

    public Object readPrimitiveValue() {
        Object value = token.getPrimitiveValue();
        if (value == JsonNull.singleton)
            throw new JsonParsingException("non-null value", token);

        token.advance();
        return value;
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
