package jsimple.json.text;

import jsimple.json.JsonException;

/**
 * @author Bret Johnson
 * @since 5/6/12 2:13 AM
 */
public final class JsonParsingException extends JsonException {
    public JsonParsingException(String message) {
        super(message);
    }

    public JsonParsingException(String expected, String encountered) {
        super("Expected {} but encountered {}", expected, encountered);
    }

    public JsonParsingException(String expected, Token token) {
        this(expected, token.getDescription());
    }
}
