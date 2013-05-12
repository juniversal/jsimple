package jsimple.io;

import jsimple.util.BasicException;

/**
 * This is a platform independent class used to represent I/O exceptions, taking the place of java.io.IOException in
 * JSimple based code.
 *
 * @author Bret Johnson
 * @since 10/6/12 5:27 PM
 */
public class IOException extends BasicException {
    public IOException(Throwable cause) {
        super(cause);
    }

    public IOException(String message) {
        super(message);
    }

    public IOException(String message, Object arg1) {
        super(message, arg1);
    }

    public IOException(String message, Object arg1, Object arg2) {
        super(message, arg1, arg2);
    }

    public IOException(String message, Object... args) {
        super(message, args);
    }
}
