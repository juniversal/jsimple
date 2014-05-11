namespace jsimple.logging.stdimpl {

    using PlatformUtils = jsimple.util.PlatformUtils;

    /// <summary>
    /// @author Bret Johnson
    /// @since 5/27/13 12:27 AM
    /// </summary>
    public class LogEnterLeaveStdImpl : LogEnterLeave {
        private StdLogger logger;
        private Level level;
        private string format;
        private object[] arguments;
        private long startTime = 0;

        internal LogEnterLeaveStdImpl(StdLogger logger, Level level, string format, params object[] arguments) {
            if (logger.isLevelEnabled(level)) {
                logger.log(level, ">>>>> Enter " + format, arguments);
                this.logger = logger;
                this.level = level;
                this.format = format;
                this.arguments = arguments;
                startTime = PlatformUtils.CurrentTimeMillis;
            }
        }

        public override void close() {
            if (startTime != 0) {
                string suffix = "; took " + (PlatformUtils.CurrentTimeMillis - startTime) + "ms";
                logger.log(level, "<<<<< Leave " + format + suffix, arguments);
            }
        }
    }

}