/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2012-2015 Microsoft Mobile.  All Rights Reserved.
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
 * This file is based on or incorporates material from SLF4J http://www.slf4j.org
 * (collectively, "Third Party Code"). Microsoft Mobile is not the original
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

import jsimple.logging.helpers.NOPLogger;
import jsimple.util.BasicException;

/**
 * The <code>LoggerFactory</code> is a utility class producing Loggers for various logging APIs, most notably for log4j,
 * logback JDK 1.4 logging, and JSimple logging.
 * <p/>
 * <code>LoggerFactory</code> is essentially a wrapper around an {@link ILoggerFactory} instance bound with
 * <code>LoggerFactory</code> at compile time.
 * <p/>
 * Please note that all methods in <code>LoggerFactory</code> are static.
 *
 * @author Ceki G&uuml;lc&uuml;
 * @author Robert Elliot
 * @author Bret Johnson modified for JSimple
 */
public final class LoggerFactory {
    static ILoggerFactory concreteLoggerFactory = null;

    // private constructor prevents instantiation
    private LoggerFactory() {
    }

    public static void init(ILoggerFactory loggerFactory) {
        if (concreteLoggerFactory != null)
            throw new BasicException("LoggerFactory is already initialized");
        concreteLoggerFactory = loggerFactory;
    }

    /**
     * Return a logger named according to the name parameter using the statically bound {@link ILoggerFactory} instance.
     * If the LoggerFactory hasn't been initialized yet (init hasn't been called), the NOPLogger is returned, where
     * logged messages don't go anywhere.
     *
     * @param name The name of the logger.
     * @return logger
     */
    public static Logger getLogger(String name) {
        if (concreteLoggerFactory != null)
            return concreteLoggerFactory.getLogger(name);
        else return NOPLogger.NOP_LOGGER;
    }
}
