/*
 * Portions copyright (c) 2012-2014 Microsoft Mobile.  All Rights Reserved.
 *
 * This file is based on or incorporates material from SLF4J http://www.slf4j.org
 * (collectively, “Third Party Code”). Microsoft Mobile is not the original
 * author of the Third Party Code.  The original copyright notice and the
 * license, under which Microsoft Mobile received such Third Party Code, are set
 * forth below.
 *
 *
 * Copyright (c) 2004-2011 QOS.ch
 * All rights reserved.
 *
 * Permission is hereby granted, free  of charge, to any person obtaining
 * a  copy  of this  software  and  associated  documentation files  (the
 * "Software"), to  deal in  the Software without  restriction, including
 * without limitation  the rights to  use, copy, modify,  merge, publish,
 * distribute,  sublicense, and/or sell  copies of  the Software,  and to
 * permit persons to whom the Software  is furnished to do so, subject to
 * the following conditions:
 *
 * The  above  copyright  notice  and  this permission  notice  shall  be
 * included in all copies or substantial portions of the Software.
 *
 * THE  SOFTWARE IS  PROVIDED  "AS  IS", WITHOUT  WARRANTY  OF ANY  KIND,
 * EXPRESS OR  IMPLIED, INCLUDING  BUT NOT LIMITED  TO THE  WARRANTIES OF
 * MERCHANTABILITY,    FITNESS    FOR    A   PARTICULAR    PURPOSE    AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE,  ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package jsimple.logging;

/**
 * The jsimple.logging.Logger interface is the main user entry point of JSimple logging API. It is expected that logging
 * takes place through concrete implementations of this interface.
 * <p/>
 * <h3>Typical usage pattern:</h3>
 * <pre>
 * import jsimple.logging.Logger;
 * import jsimple.logging.LoggerFactory;
 *
 * public class Wombat {
 *
 *   <span style="color:green">final static Logger logger = LoggerFactory.getLogger("Wombat");</span>
 *   Integer t;
 *   Integer oldT;
 *
 *   public void setTemperature(Integer temperature) {
 *     oldT = t;
 *     t = temperature;
 *     <span style="color:green">logger.debug("Temperature set to {}. Old temperature was {}.", t, oldT);</span>
 *     if(temperature.intValue() > 50) {
 *       <span style="color:green">logger.info("Temperature has risen above 50 degrees.");</span>
 *     }
 *   }
 * }
 * </pre>
 * <p/>
 * This class was adapted from SLF4J, version 1.7.5.  The main change from SLF4J was that Marker support was removed, as
 * it's not (yet) supported in JSimple logging.  Also, generic log methods were added, taking a Level parameter, like in
 * JUL (java util logging) & these methods are used for default implementations.
 *
 * @author Ceki G&uuml;lc&uuml;
 * @author Bret Johnson modified for JSimple
 */
public abstract class Logger {
    /**
     * Case insensitive String constant used to retrieve the name of the root logger.
     *
     * @since 1.3
     */
    final public String ROOT_LOGGER_NAME = "ROOT";

    /**
     * Return the name of this <code>Logger</code> instance.
     *
     * @return name of this logger instance
     */
    public abstract String getName();

    /**
     * Is the logger instance enabled for the TRACE level?
     *
     * @return true if this Logger is enabled for the TRACE level, false otherwise.
     * @since 1.4
     */
    public boolean isTraceEnabled() {
        return isLevelEnabled(Level.TRACE);
    }

    /**
     * Log a message at the TRACE level.
     *
     * @param msg the message string to be logged
     * @since 1.4
     */
    public void trace(String msg) {
        log(Level.TRACE, msg);
    }

    /**
     * Log a message at the TRACE level according to the specified format and argument.
     * <p/>
     * This form avoids superfluous object creation when the logger is disabled for the TRACE level.
     *
     * @param format the format string
     * @param arg    the argument
     * @since 1.4
     */
    public void trace(String format, Object arg) {
        log(Level.TRACE, format, arg);
    }

    /**
     * Log a message at the TRACE level according to the specified format and arguments.
     * <p/>
     * This form avoids superfluous object creation when the logger is disabled for the TRACE level.
     *
     * @param format the format string
     * @param arg1   the first argument
     * @param arg2   the second argument
     * @since 1.4
     */
    public void trace(String format, Object arg1, Object arg2) {
        log(Level.TRACE, format, arg1, arg2);
    }

    /**
     * Log a message at the TRACE level according to the specified format and arguments.
     * <p/>
     * This form avoids superfluous string concatenation when the logger is disabled for the TRACE level. However, this
     * variant incurs the hidden (and relatively small) cost of creating an <code>Object[]</code> before invoking the
     * method, even if this logger is disabled for TRACE. The variants taking {@link #trace(String, Object) one} and
     * {@link #trace(String, Object, Object) two} arguments exist solely in order to avoid this hidden cost.
     *
     * @param format    the format string
     * @param arguments a list of 3 or more arguments
     * @since 1.4
     */
    public void trace(String format, Object... arguments) {
        log(Level.TRACE, format, arguments);
    }

    /**
     * Log an exception (throwable) at the TRACE level with an accompanying message.
     *
     * @param msg the message accompanying the exception
     * @param t   the exception (throwable) to log
     * @since 1.4
     */
    public void trace(String msg, Throwable t) {
        log(Level.TRACE, msg, t);
    }

    /**
     * Is the logger instance enabled for the DEBUG level?
     *
     * @return true if this Logger is enabled for the DEBUG level, false otherwise.
     */
    public boolean isDebugEnabled() {
        return isLevelEnabled(Level.DEBUG);
    }

    /**
     * Log a message at the DEBUG level.
     *
     * @param msg the message string to be logged
     */
    public void debug(String msg) {
        log(Level.DEBUG, msg);
    }

    /**
     * Log a message at the DEBUG level according to the specified format and argument.
     * <p/>
     * This form avoids superfluous object creation when the logger is disabled for the DEBUG level.
     *
     * @param format the format string
     * @param arg    the argument
     */
    public void debug(String format, Object arg) {
        log(Level.DEBUG, format, arg);
    }

    /**
     * Log a message at the DEBUG level according to the specified format and arguments.
     * <p/>
     * This form avoids superfluous object creation when the logger is disabled for the DEBUG level.
     *
     * @param format the format string
     * @param arg1   the first argument
     * @param arg2   the second argument
     */
    public void debug(String format, Object arg1, Object arg2) {
        log(Level.DEBUG, format, arg1, arg2);
    }

    /**
     * Log a message at the DEBUG level according to the specified format and arguments.
     * <p/>
     * This form avoids superfluous string concatenation when the logger is disabled for the DEBUG level. However, this
     * variant incurs the hidden (and relatively small) cost of creating an <code>Object[]</code> before invoking the
     * method, even if this logger is disabled for DEBUG. The variants taking {@link #debug(String, Object) one} and
     * {@link #debug(String, Object, Object) two} arguments exist solely in order to avoid this hidden cost.
     *
     * @param format    the format string
     * @param arguments a list of 3 or more arguments
     */
    public void debug(String format, Object... arguments) {
        log(Level.DEBUG, format, arguments);
    }

    /**
     * Log an exception (throwable) at the DEBUG level with an accompanying message.
     *
     * @param msg the message accompanying the exception
     * @param t   the exception (throwable) to log
     */
    public void debug(String msg, Throwable t) {
        log(Level.DEBUG, msg, t);
    }

    /**
     * Is the logger instance enabled for the INFO level?
     *
     * @return true if this Logger is enabled for the INFO level, false otherwise.
     */
    public boolean isInfoEnabled() {
        return isLevelEnabled(Level.INFO);
    }

    /**
     * Log a message at the INFO level.
     *
     * @param msg the message string to be logged
     */
    public void info(String msg) {
        log(Level.INFO, msg);
    }

    /**
     * Log a message at the INFO level according to the specified format and argument.
     * <p/>
     * This form avoids superfluous object creation when the logger is disabled for the INFO level.
     *
     * @param format the format string
     * @param arg    the argument
     */
    public void info(String format, Object arg) {
        log(Level.INFO, format, arg);
    }

    /**
     * Log a message at the INFO level according to the specified format and arguments.
     * <p/>
     * This form avoids superfluous object creation when the logger is disabled for the INFO level.
     *
     * @param format the format string
     * @param arg1   the first argument
     * @param arg2   the second argument
     */
    public void info(String format, Object arg1, Object arg2) {
        log(Level.INFO, format, arg1, arg2);
    }

    /**
     * Log a message at the INFO level according to the specified format and arguments.
     * <p/>
     * This form avoids superfluous string concatenation when the logger is disabled for the INFO level. However, this
     * variant incurs the hidden (and relatively small) cost of creating an <code>Object[]</code> before invoking the
     * method, even if this logger is disabled for INFO. The variants taking {@link #info(String, Object) one} and
     * {@link #info(String, Object, Object) two} arguments exist solely in order to avoid this hidden cost.
     *
     * @param format    the format string
     * @param arguments a list of 3 or more arguments
     */
    public void info(String format, Object... arguments) {
        log(Level.INFO, format, arguments);
    }

    /**
     * Log an exception (throwable) at the INFO level with an accompanying message.
     *
     * @param msg the message accompanying the exception
     * @param t   the exception (throwable) to log
     */
    public void info(String msg, Throwable t) {
        log(Level.INFO, msg, t);
    }

    /**
     * Is the logger instance enabled for the WARN level?
     *
     * @return true if this Logger is enabled for the WARN level, false otherwise.
     */
    public boolean isWarnEnabled() {
        return isLevelEnabled(Level.WARN);
    }

    /**
     * Log a message at the WARN level.
     *
     * @param msg the message string to be logged
     */
    public void warn(String msg) {
        log(Level.WARN, msg);
    }

    /**
     * Log a message at the WARN level according to the specified format and argument.
     * <p/>
     * This form avoids superfluous object creation when the logger is disabled for the WARN level.
     *
     * @param format the format string
     * @param arg    the argument
     */
    public void warn(String format, Object arg) {
        log(Level.WARN, format, arg);
    }

    /**
     * Log a message at the WARN level according to the specified format and arguments.
     * <p/>
     * This form avoids superfluous string concatenation when the logger is disabled for the WARN level. However, this
     * variant incurs the hidden (and relatively small) cost of creating an <code>Object[]</code> before invoking the
     * method, even if this logger is disabled for WARN. The variants taking {@link #warn(String, Object) one} and
     * {@link #warn(String, Object, Object) two} arguments exist solely in order to avoid this hidden cost.
     *
     * @param format    the format string
     * @param arguments a list of 3 or more arguments
     */
    public void warn(String format, Object... arguments) {
        log(Level.WARN, format, arguments);
    }

    /**
     * Log a message at the WARN level according to the specified format and arguments.
     * <p/>
     * This form avoids superfluous object creation when the logger is disabled for the WARN level.
     *
     * @param format the format string
     * @param arg1   the first argument
     * @param arg2   the second argument
     */
    public void warn(String format, Object arg1, Object arg2) {
        log(Level.WARN, format, arg1, arg2);
    }

    /**
     * Log an exception (throwable) at the WARN level with an accompanying message.
     *
     * @param msg the message accompanying the exception
     * @param t   the exception (throwable) to log
     */
    public void warn(String msg, Throwable t) {
        log(Level.WARN, msg, t);
    }

    /**
     * Is the logger instance enabled for the ERROR level?
     *
     * @return true if this Logger is enabled for the ERROR level, false otherwise.
     */
    public boolean isErrorEnabled() {
        return isLevelEnabled(Level.ERROR);
    }

    /**
     * Log a message at the ERROR level.
     *
     * @param msg the message string to be logged
     */
    public void error(String msg) {
        log(Level.ERROR, msg);
    }

    /**
     * Log a message at the ERROR level according to the specified format and argument.
     * <p/>
     * This form avoids superfluous object creation when the logger is disabled for the ERROR level.
     *
     * @param format the format string
     * @param arg    the argument
     */
    public void error(String format, Object arg) {
        log(Level.ERROR, format, arg);
    }

    /**
     * Log a message at the ERROR level according to the specified format and arguments.
     * <p/>
     * This form avoids superfluous object creation when the logger is disabled for the ERROR level.
     *
     * @param format the format string
     * @param arg1   the first argument
     * @param arg2   the second argument
     */
    public void error(String format, Object arg1, Object arg2) {
        log(Level.ERROR, format, arg1, arg2);
    }

    /**
     * Log a message at the ERROR level according to the specified format and arguments.
     * <p/>
     * This form avoids superfluous string concatenation when the logger is disabled for the ERROR level. However, this
     * variant incurs the hidden (and relatively small) cost of creating an <code>Object[]</code> before invoking the
     * method, even if this logger is disabled for ERROR. The variants taking {@link #error(String, Object) one} and
     * {@link #error(String, Object, Object) two} arguments exist solely in order to avoid this hidden cost.
     *
     * @param format    the format string
     * @param arguments a list of 3 or more arguments
     */
    public void error(String format, Object... arguments) {
        log(Level.ERROR, format, arguments);
    }

    /**
     * Log an exception (throwable) at the ERROR level with an accompanying message.
     *
     * @param msg the message accompanying the exception
     * @param t   the exception (throwable) to log
     */
    public void error(String msg, Throwable t) {
        log(Level.ERROR, msg, t);
    }

    /**
     * Is the logger instance enabled for the specified level?
     *
     * @return true if this Logger is enabled for the specified level, false otherwise.
     */
    public abstract boolean isLevelEnabled(Level level);

    /**
     * Set the level for this logger.  The logger implementation is free to define the exact semantics of changing the
     * level--what other loggers may be affected (if there's some kind of hierarchy), etc.
     *
     * @param level new level
     */
    public abstract void setLevel(Level level);

    /**
     * Log a message at the specified level.
     *
     * @param level logging level to log at
     * @param msg   the message string to be logged
     */
    public void log(Level level, String msg) {
        if (isLevelEnabled(level))
            logWithVarargs(level, msg);
    }

    /**
     * Log a message at the specified level according to the specified format and argument.
     * <p/>
     * This form avoids superfluous object creation when the logger is disabled for the level.
     *
     * @param level  logging level to log at
     * @param format the format string
     * @param arg    the argument
     */
    public void log(Level level, String format, Object arg) {
        if (isLevelEnabled(level))
            logWithVarargs(level, format, arg);
    }

    /**
     * Log a message at the specified level according to the specified format and arguments.
     * <p/>
     * This form avoids superfluous object creation when the logger is disabled for the level.
     *
     * @param level  logging level to log at
     * @param format the format string
     * @param arg1   the first argument
     * @param arg2   the second argument
     */
    public void log(Level level, String format, Object arg1, Object arg2) {
        if (isLevelEnabled(level))
            logWithVarargs(level, format, arg1, arg2);
    }

    /**
     * Log a message at the specified level according to the specified format and arguments.
     * <p/>
     * This form avoids superfluous string concatenation when the logger is disabled for the level. However, this
     * variant incurs the hidden (and relatively small) cost of creating an <code>Object[]</code> before invoking the
     * method, even if this logger is disabled for ERROR. The variants taking {@link #error(String, Object) one} and
     * {@link #error(String, Object, Object) two} arguments exist solely in order to avoid this hidden cost.
     *
     * @param level     logging level to log at
     * @param format    the format string
     * @param arguments a list of 3 or more arguments
     */
    public void log(Level level, String format, Object... arguments) {
        if (isLevelEnabled(level))
            logWithVarargs(level, format, arguments);
    }

    /**
     * Log a message at the specified level, with the specified message format and arguments.  This method is the same
     * as the log varargs method, except that it has a different name, with no overloads, to force use of the varargs
     * version no matter how few arguments are passed.
     *
     * @param level     logging level to log at
     * @param format    the format string
     * @param arguments a list of 0 or more arguments
     */
    public abstract void logWithVarargs(Level level, String format, Object... arguments);

    /**
     * Log an exception (throwable) at the specified level with an accompanying message.
     *
     * @param level logging level to log at
     * @param msg   the message accompanying the exception
     * @param t     the exception (throwable) to log
     */
    public abstract void log(Level level, String msg, Throwable t);

    public LogEnterLeave debugEnterLeave(String msg) {
        return logEnterLeave(Level.DEBUG, msg);
    }

    public LogEnterLeave debugEnterLeave(String format, Object... arguments) {
        return logEnterLeave(Level.DEBUG, format, arguments);
    }

    public LogEnterLeave traceEnterLeave(String msg) {
        return logEnterLeave(Level.TRACE, msg);
    }

    public LogEnterLeave traceEnterLeave(String format, Object... arguments) {
        return logEnterLeave(Level.TRACE, format, arguments);
    }

    public LogEnterLeave logEnterLeave(Level level, String format, Object... arguments) {
        if (isLevelEnabled(level))
            return logStartAndEndWithVarargs(level, format, arguments);
        else return null;
    }

    /**
     * Log start and end messages at the specified level, with the specified message format and arguments.  This method
     * is the same as the logEnterLeave varargs method, except that it has a different name, with no overloads, to force
     * use of the varargs version no matter how few arguments are passed.
     *
     * @param level     logging level to log at
     * @param format    the format string
     * @param arguments a list of 0 or more arguments
     */
    public abstract LogEnterLeave logStartAndEndWithVarargs(Level level, String format, Object... arguments);
}
