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

import jsimple.io.StdIO;
import jsimple.logging.ILoggerFactory;
import jsimple.logging.Level;
import jsimple.logging.Logger;
import jsimple.logging.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author Bret Johnson
 * @since 4/8/13 12:57 AM
 */
public class StdLoggerFactory implements ILoggerFactory {
    private HashMap<String, StdLogger> loggers = new HashMap<String, StdLogger>();
    private Level defaultLevel = Level.DEBUG;
    private List<Appender> defaultAppenders = new ArrayList<Appender>();

    public static StdLoggerFactory init(Level defaultLevel) {
        StdLoggerFactory stdLoggerFactory = new StdLoggerFactory();
        stdLoggerFactory.setDefaultLevel(defaultLevel);

        LoggerFactory.init(stdLoggerFactory);
        return stdLoggerFactory;
    }

    public StdLoggerFactory() {
        defaultAppenders.add(new WriterAppender(StdIO.stdout));
    }

    /**
     * Return an appropriate {@link jsimple.logging.Logger} instance as specified by the <code>name</code> parameter.
     * <p/>
     * If the name parameter is equal to {@link jsimple.logging.Logger#ROOT_LOGGER_NAME}, that is the string value
     * "ROOT" (case insensitive), then the root logger of the underlying logging system is returned.
     * <p/>
     * Null-valued name arguments are considered invalid.
     * <p/>
     * Certain extremely simple logging systems, e.g. NOP, may always return the same logger instance regardless of the
     * requested name.
     *
     * @param name the name of the Logger to return
     * @return a Logger instance
     */
    public Logger getLogger(String name) {
        return getStdLogger(name);
    }

    public StdLogger getStdLogger(String name) {
        synchronized (this) {
            StdLogger logger = loggers.get(name);
            if (logger == null) {
                logger = new StdLogger(this, name);
                loggers.put(name, logger);
            }

            return logger;
        }
    }

    public Level getDefaultLevel() {
        return defaultLevel;
    }

    public void setDefaultLevel(Level defaultLevel) {
        synchronized (this) {
            this.defaultLevel = defaultLevel;

            for (StdLogger logger : loggers.values())
                logger.updateEffectiveLevel();
        }
    }

    /**
     * Return the current default list of appenders that are configured.  The returned list should NOT be modified; it
     * must be treated as immutable.
     *
     * @return configured appenders
     */
    public List<Appender> getDefaultAppenders() {
        return defaultAppenders;
    }

    public void setDefaultAppenders(List<Appender> defaultAppenders) {
        synchronized (this) {
            this.defaultAppenders = defaultAppenders;

            for (StdLogger logger : loggers.values())
                logger.updateEffectiveAppenders();
        }
    }
}
