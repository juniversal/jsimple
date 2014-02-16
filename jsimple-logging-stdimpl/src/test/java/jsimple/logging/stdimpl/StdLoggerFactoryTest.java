package jsimple.logging.stdimpl;

import jsimple.logging.Level;
import jsimple.logging.Logger;
import jsimple.logging.LoggerFactory;
import org.junit.Test;

/**
 * @author Bret Johnson
 * @since 4/11/13 5:34 PM
 */
public class StdLoggerFactoryTest {
    @Test public void testBasics() {
        LoggerFactory.init(new StdLoggerFactory());

        Logger logger = LoggerFactory.getLogger("mylogger");
        ((StdLogger) logger).setLevel(Level.TRACE);

        logger.trace("trace message");
        logger.debug("debug message");
        logger.info("info message");
        logger.warn("warn message");
        logger.error("error message");
    }
}
