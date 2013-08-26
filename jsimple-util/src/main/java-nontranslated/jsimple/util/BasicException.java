package jsimple.util;

/**
 * @author Bret Johnson
 * @since 5/7/13 2:19 AM
 */
public class BasicException extends RuntimeException {
    public BasicException(Throwable cause) {
        super(cause);
    }

    public BasicException(String message) {
        super(message);
    }

    public BasicException(String message, Object arg1) {
        this(MessageFormatter.format(message, arg1));
    }

    public BasicException(String message, Object arg1, Object arg2) {
        this(MessageFormatter.format(message, arg1, arg2));
    }

    public BasicException(String message, Object... args) {
        this(MessageFormatter.arrayFormat(message, args));
    }

    public BasicException(MessageFormatter.FormattingTuple formattingTuple) {
        super(formattingTuple.getFormattedMessage());

        Throwable throwable = formattingTuple.getThrowable();
        if (throwable != null)
            initCause(throwable);
    }
}
