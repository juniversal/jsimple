/*
 * Copyright (c) 2012-2014 Microsoft Mobile.  All Rights Reserved.
 *
 * This file is based on or incorporates material from Logback http://logback.qos.ch
 * (collectively, “Third Party Code”). Microsoft Mobile is not the original
 * author of the Third Party Code.
 *
 * This program and the accompanying materials are made available under the terms
 * of the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html.
 */

package jsimple.logging.stdimpl;

import jsimple.io.Writer;
import jsimple.util.PlatformUtils;
import org.jetbrains.annotations.Nullable;

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
        synchronized (this) {
            writer.writeln(loggingEvent.getLevel().getDefaultDisplayName() + " " + loggingEvent.getLoggerName() + " - " +
                    loggingEvent.getFormattedMessage());

            @Nullable Throwable throwable = loggingEvent.getThrowable();
            if (throwable != null)
                writer.writeln(PlatformUtils.getExceptionDescription(throwable));

            if (flushImmediately)
                writer.flush();
        }

        /*
        #logback.classic pattern: %d [%thread] %-5level %logger{36} - %msg%n
        2012-04-26 14:54:38,461 [main] DEBUG com.foo.App - Hello world
        2012-04-26 14:54:38,461 [main] DEBUG com.foo.App - Hi again
        */

        //To change body of implemented methods use File | Settings | File Templates.
    }
}
