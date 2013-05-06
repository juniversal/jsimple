/**
 * Parts of this functionality were adapted from Logback (v. 1.0.120:
 *
 * Logback: the reliable, generic, fast and flexible logging framework.
 * Copyright (C) 1999-2013, QOS.ch. All rights reserved.
 *
 * This program and the accompanying materials are dual-licensed under
 * either the terms of the Eclipse Public License v1.0 as published by
 * the Eclipse Foundation
 *
 *   or (per the licensee's choosing)
 *
 * under the terms of the GNU Lesser General Public License version 2.1
 * as published by the Free Software Foundation.
 */

package jsimple.logging.stdimpl;

import jsimple.logging.Level;
import jsimple.logging.helpers.FormattingTuple;
import jsimple.logging.helpers.MessageFormatter;
import jsimple.util.PlatformUtils;

/**
 * @author Bret Johnson
 * @author Ceki G&uuml;lc&uuml;   (original Logback source)
 * @author S&eacute;bastien Pennec    (original Logback source)
 * @since 4/8/13 12:20 AM
 */
public class LoggingEvent {
    private long timestamp;     // Event time of occurrence, in millis
    private String threadName;
    private String loggerName;
    private Level level;
    private String message = null;
    private Object[] messageArgs = null;
    private String formattedMessage = null;
    private Throwable throwable = null;

    public LoggingEvent(String loggerName, Level level, String message, Object[] argArray) {
        this.timestamp = PlatformUtils.getCurrentTimeMillis();
        this.loggerName = loggerName;
        this.level = level;

        this.message = message;

        FormattingTuple formattingTuple = MessageFormatter.arrayFormat(message, argArray);
        formattedMessage = formattingTuple.getMessage();
        messageArgs = formattingTuple.getArgArray();
        this.throwable = formattingTuple.getThrowable();

        this.threadName = "THREAD";   // TODO: Make this real thread name
    }

    public LoggingEvent(String loggerName, Level level, String message, Throwable t) {
        this.timestamp = PlatformUtils.getCurrentTimeMillis();
        this.loggerName = loggerName;
        this.level = level;

        this.message = message;
        this.formattedMessage = message;
        this.messageArgs = null;
        this.throwable = t;

        this.threadName = "THREAD";   // TODO: Make this real thread name
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

    public String getMessage() {
        return message;
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