package jsimple.logging.stdimpl;

import jsimple.logging.Level;
import jsimple.logging.LogEnterLeave;
import jsimple.util.PlatformUtils;

/**
 * @author Bret Johnson
 * @since 5/27/13 12:27 AM
 */
public class LogEnterLeaveStdImpl extends LogEnterLeave {
    private StdLogger logger;
    private Level level;
    private String format;
    private Object[] arguments;
    private long startTime = 0;

    LogEnterLeaveStdImpl(StdLogger logger, Level level, String format, Object... arguments) {
        if (logger.isLevelEnabled(level)) {
            logger.log(level, ">>>>> Enter " + format, arguments);
            this.logger = logger;
            this.level = level;
            this.format = format;
            this.arguments = arguments;
            startTime = PlatformUtils.getCurrentTimeMillis();
        }
    }

    @Override public void close() {
        if (startTime != 0) {
            String suffix = "; took " + (PlatformUtils.getCurrentTimeMillis() - startTime) + "ms";
            logger.log(level, "<<<<< Leave " + format + suffix, arguments);
        }
    }
}
