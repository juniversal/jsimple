package jsimple.logging.stdimpl;

import jsimple.logging.Level;
import jsimple.util.PlatformUtils;

/**
 * @author Bret Johnson
 * @since 4/8/13 12:20 AM
 */
public class LoggingEvent {
    private long timestamp;     // Event time of occurrence, in millis
    private String threadName;
    private String loggerName;
    private Level level;
    private String format = null;
    private Object[] messageArgs = null;
    private String formattedMessage = null;
    private Throwable throwable = null;

    public LoggingEvent(String loggerName, Level level, String format, Object[] arguments) {
        this.timestamp = PlatformUtils.getCurrentTimeMillis();
        this.threadName = "THREAD";   // TODO: Make this real thread name
        this.loggerName = loggerName;
        this.level = level;
        this.format = format;
        this.messageArgs = arguments;
    }

    public LoggingEvent(String loggerName, Level level, String message, Throwable t) {
        this.timestamp = PlatformUtils.getCurrentTimeMillis();
        this.threadName = "THREAD";   // TODO: Make this real thread name
        this.loggerName = loggerName;
        this.level = level;
        this.format = message;
        this.messageArgs = null;
        this.throwable = t;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getThreadName() {
        return threadName;
    }

    public String getLoggerName() {
        return loggerName;
    }

    public Level getLevel() {
        return level;
    }

    public String getFormat() {
        return format;
    }

    public Object[] getMessageArgs() {
        return messageArgs;
    }

    public String getFormattedMessage() {
        return formattedMessage;
    }

    public Throwable getThrowable() {
        return throwable;
    }
}
