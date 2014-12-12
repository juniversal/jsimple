/*
 * Copyright (c) 2012-2014 Microsoft Mobile.  All Rights Reserved.
 *
 * This file is based on or incorporates material from Logback http://logback.qos.ch
 * (collectively, "Third Party Code"). Microsoft Mobile is not the original
 * author of the Third Party Code.
 *
 * This program and the accompanying materials are made available under the terms
 * of the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html.
 */

package jsimple.logging.stdimpl;

import jsimple.logging.Level;
import jsimple.util.MessageFormatter;
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

        MessageFormatter.FormattingTuple formattingTuple = MessageFormatter.arrayFormat(message, argArray);
        formattedMessage = formattingTuple.getFormattedMessage();
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
