package jsimple.json;

import jsimple.util.BasicException;

/**
 * @author Bret Johnson
 * @since 9/15/12 5:32 PM
 */
public class JsonException extends BasicException {
    public JsonException(Throwable cause) {
        super(cause);
    }

    public JsonException(String message) {
        super(message);
    }

    public JsonException(String message, Object arg1) {
        super(message, arg1);
    }

    public JsonException(String message, Object arg1, Object arg2) {
        super(message, arg1, arg2);
    }

    public JsonException(String message, Object... args) {
        super(message, args);
    }
}
