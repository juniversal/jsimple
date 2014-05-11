namespace jsimple.logging.stdimpl {

    using NUnit.Framework;

    /// <summary>
    /// @author Bret Johnson
    /// @since 4/11/13 5:34 PM
    /// </summary>
    public class StdLoggerFactoryTest {
        [Test] public virtual void testBasics()
        {
            LoggerFactory.init(new StdLoggerFactory());

            Logger logger = LoggerFactory.getLogger("mylogger");
            ((StdLogger) logger).Level = Level.TRACE;

            logger.trace("trace message");
            logger.debug("debug message");
            logger.info("info message");
            logger.warn("warn message");
            logger.error("error message");
        }
    }

}