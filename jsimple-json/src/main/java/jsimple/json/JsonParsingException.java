package jsimple.json;

/**
 * @author Bret Johnson
 * @since 5/6/12 2:13 AM
 */
public final class JsonParsingException extends JsonException {
    JsonParsingException(String expected, String encountered, Token token) {
        super("Expected " + expected + " but encountered " + encountered);
    }

    JsonParsingException(String message, Token token) {
        super(message);
    }
}
