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
package jsimple.logging.helpers;


import jsimple.logging.Level;
import jsimple.logging.LogEnterLeave;
import jsimple.logging.Logger;

/**
 * A direct NOP (no operation) implementation of {@link Logger}.
 *
 * @author Bret Johnson modified for JSimple
 */
public class NOPLogger extends Logger {
    /**
     * The unique instance of NOPLogger.
     */
    public static final NOPLogger NOP_LOGGER = new NOPLogger();

    /**
     * There is no point in creating multiple instances of NOPLOgger, except by derived classes, hence the protected
     * access for the constructor.
     */
    protected NOPLogger() {
    }

    /**
     * Always returns the string value "NOP".
     */
    public String getName() {
        return "NOP";
    }

    /**
     * For this logger, don't log anything.
     *
     * @param level log level
     * @return whether logging should be enabled; the NOPLogger always returns false here
     */
    @Override public boolean isLevelEnabled(Level level) {
        return false;
    }

    @Override public void setLevel(Level level) {
    }

    @Override public void logWithVarargs(Level level, String format, Object... arguments) {
    }

    @Override public void log(Level level, String msg, Throwable t) {
    }

    @Override public LogEnterLeave logStartAndEndWithVarargs(Level level, String format, Object... arguments) {
        return null;
    }
}
