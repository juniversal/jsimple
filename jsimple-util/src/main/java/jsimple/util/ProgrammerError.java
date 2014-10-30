package jsimple.util;


/**
 * A {@code ProgrammerErrorException} is thrown when some check fails indicating that programmer did something they
 * shouldn't have done, perhaps passing in an illegal argument or calling a method at an invalid time.   It's something
 * that should never happen, unless the program has a bug.  Generally, for such errors it's OK to exit the program
 * rather than try to recover gracefully.   For Swift, we may differentiate between programmer errors and runtime errors,
 * only turning the latter into error returns.
 */
public class ProgrammerError extends BasicException {
    public ProgrammerError() {
    }

    public ProgrammerError(String message) {
        super(message);
    }

    public ProgrammerError(String message, Object arg1) {
        super(message, arg1);
    }

    public ProgrammerError(String message, Object arg1, Object arg2) {
        super(message, arg1, arg2);
    }

    public ProgrammerError(String message, Object... args) {
        super(message, args);
    }
}
