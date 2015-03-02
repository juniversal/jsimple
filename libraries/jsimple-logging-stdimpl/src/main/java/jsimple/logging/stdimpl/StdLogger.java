/*
 * Copyright (c) 2012-2015 Microsoft Mobile.  All Rights Reserved.
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
import jsimple.logging.LogEnterLeave;
import jsimple.logging.Logger;
import jsimple.util.List;

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
