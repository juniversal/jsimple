package jsimple.logging.stdimpl;

/**
 * @author Bret Johnson
 * @since 4/8/13 12:39 AM
 */
public abstract class Appender {
    abstract public void append(LoggingEvent loggingEvent);
}
