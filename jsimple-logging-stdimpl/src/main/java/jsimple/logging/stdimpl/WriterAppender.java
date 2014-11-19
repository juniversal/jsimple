/*
 * Copyright (c) 2012-2014, Microsoft Mobile
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 *
 *
 * Parts of this functionality were adapted from Logback (http://logback.qos.ch).
 * The original Logback copyright is below, which we license under the Eclipse
 * Public License v1.0.
 *
 *   Logback: the reliable, generic, fast and flexible logging framework.
 *   Copyright (C) 1999-2013, QOS.ch. All rights reserved.
 *
 *   This program and the accompanying materials are dual-licensed under
 *   either the terms of the Eclipse Public License v1.0 as published by
 *   the Eclipse Foundation
 *
 *     or (per the licensee's choosing)
 *
 *   under the terms of the GNU Lesser General Public License version 2.1
 *   as published by the Free Software Foundation.
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
