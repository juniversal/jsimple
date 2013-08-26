package jsimple.logging.stdimpl;

import jsimple.io.Writer;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * @author Bret Johnson
 * @since 4/11/13 4:10 PM
 */
public class WriterAppender extends Appender {
    Writer writer;
    boolean flushImmediately = true;

    public WriterAppender(Writer writer) {
        this.writer = writer;
    }

    @Override public void append(LoggingEvent loggingEvent) {
        writer.writeln(loggingEvent.getLevel().getDefaultDisplayName() + " " + loggingEvent.getLoggerName() + " - " +
                loggingEvent.getFormattedMessage());

        @Nullable Throwable throwable = loggingEvent.getThrowable();
        // TODO: Put this BACK (AND MAKE PLATFORM INDEPENDENT)
        /*
        if (throwable != null) {
            String stackTrace = "<error generating stack trace>";

            try (StringWriter stringWriter = new StringWriter()) {
                try (PrintWriter printWriter = new PrintWriter(stringWriter)) {
                    throwable.printStackTrace(printWriter);
                }

                stackTrace = stringWriter.getBuffer().toString();
            }
            catch(IOException e) {
            }

            writer.write(stackTrace);
        }
            */

        if (flushImmediately)
            writer.flush();

        /*
        #logback.classic pattern: %d [%thread] %-5level %logger{36} - %msg%n
        2012-04-26 14:54:38,461 [main] DEBUG com.foo.App - Hello world
        2012-04-26 14:54:38,461 [main] DEBUG com.foo.App - Hi again
        */

        //To change body of implemented methods use File | Settings | File Templates.
    }
}
