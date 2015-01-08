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
 */

package jsimple.logging.stdimpl;

import jsimple.logging.Level;
import jsimple.logging.LogEnterLeave;
import jsimple.util.PlatformUtil;

/**
 * @author Bret Johnson
 * @since 5/27/13 12:27 AM
 */
public class LogEnterLeaveStdImpl extends LogEnterLeave {
    private StdLogger logger;
    private Level level;
    private String format;
    private Object[] arguments;
    private long startTime = 0;

    LogEnterLeaveStdImpl(StdLogger logger, Level level, String format, Object... arguments) {
        if (logger.isLevelEnabled(level)) {
            logger.log(level, ">>>>> Enter " + format, arguments);
            this.logger = logger;
            this.level = level;
            this.format = format;
            this.arguments = arguments;
            startTime = PlatformUtil.getCurrentTimeMillis();
        }
    }

    @Override public void close() {
        if (startTime != 0) {
            String suffix = "; took " + (PlatformUtil.getCurrentTimeMillis() - startTime) + "ms";
            logger.log(level, "<<<<< Leave " + format + suffix, arguments);
        }
    }
}
