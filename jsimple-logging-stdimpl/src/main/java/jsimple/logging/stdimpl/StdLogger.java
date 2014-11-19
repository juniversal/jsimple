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

import jsimple.logging.Level;
import jsimple.logging.LogEnterLeave;
import jsimple.logging.Logger;

import java.util.List;

/**
 * @author Bret Johnson
 * @since 4/8/13 12:10 AM
 */
public class StdLogger extends Logger {
    private StdLoggerFactory factory;
    private String name;
    volatile private Level loggerLevel = null;    // By default, don't override level
    volatile private int effectiveLevel;
    volatile private List<Appender> loggerAppenders = null;   // By default, don't override appenders
    volatile private List<Appender> effectiveAppenders;

    public StdLogger(StdLoggerFactory factory, String name) {
        this.factory = factory;
        this.name = name;

        // By default, don't override level or appenders
        effectiveLevel = factory.getDefaultLevel().getIntValue();
        effectiveAppenders = factory.getDefaultAppenders();
    }

    @Override public String getName() {
        return name;
    }

    /**
     * Override the level for this logger, or remove the override if loggerLevel is null.  When not overridden the
     * logger will get the default level, set on the factory, otherwise the override sets the level.
     *
     * @param loggerLevel level of info to log
     */
    @Override public void setLevel(Level loggerLevel) {
        this.loggerLevel = loggerLevel;
        updateEffectiveLevel();
    }

    @Override public boolean isLevelEnabled(Level level) {
        return level.getIntValue() >= effectiveLevel;
    }

    @Override public void logWithVarargs(Level level, String format, Object... arguments) {
        if (isLevelEnabled(level))
            log(new LoggingEvent(name, level, format, arguments));
    }

    @Override public void log(Level level, String msg, Throwable t) {
        if (isLevelEnabled(level))
            log(new LoggingEvent(name, level, msg, t));
    }

    @Override public LogEnterLeave logStartAndEndWithVarargs(Level level, String format, Object... arguments) {
        if (isLevelEnabled(level))
            return new LogEnterLeaveStdImpl(this, level, format, arguments);
        else return null;
    }

    /**
     * Log the specified logging event to the appenders.  At this point, the isLevelEnabled check is assumed to have
     * already passed.
     *
     * @param loggingEvent event info to log
     */
    private void log(LoggingEvent loggingEvent) {
        // Store the appender list in a local to ensure it doesn't change while using it
        List<Appender> currentEffectiveAppenders = effectiveAppenders;

        for (Appender appender : currentEffectiveAppenders)
            appender.append(loggingEvent);
    }

    void updateEffectiveLevel() {
        synchronized (this) {
            // Store the level in a local to ensure it doesn't change while using it
            Level currentLevel = loggerLevel;

            if (currentLevel != null)
                effectiveLevel = currentLevel.getIntValue();
            else effectiveLevel = factory.getDefaultLevel().getIntValue();
        }
    }

    void updateEffectiveAppenders() {
        synchronized (this) {
            // Store the appender list in a local to ensure it doesn't change while using it
            List<Appender> currentLoggerAppenders = loggerAppenders;

            if (currentLoggerAppenders != null)
                effectiveAppenders = currentLoggerAppenders;
            else effectiveAppenders = factory.getDefaultAppenders();
        }
    }
}
