/*
 * Copyright (c) 2012-2015, Microsoft Mobile
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

package jsimple.util;


/**
 * A {@code ProgrammerError} is thrown when some check fails indicating that programmer did something they shouldn't
 * have done, perhaps passing in an illegal argument or calling a method at an invalid time.   It's something that
 * should never happen, unless the program has a bug.  Generally, for such errors it's OK to exit the program rather
 * than try to recover gracefully.   For Swift (which doesn't support exceptions at all--at least not yet), we may
 * differentiate between programmer errors and runtime errors, only turning the latter into error returns.
 */
public class ProgrammerError extends BasicException {
    public ProgrammerError() {
    }

    public ProgrammerError(String message) {
        super(message);
    }

    public ProgrammerError(String message, Object arg1) {
        super(message, arg1);
    }

    public ProgrammerError(String message, Object arg1, Object arg2) {
        super(message, arg1, arg2);
    }

    public ProgrammerError(String message, Object... args) {
        super(message, args);
    }
}
