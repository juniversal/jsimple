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

package jsimple.io;

import jsimple.net.SocketTimeoutException;
import jsimple.net.UnknownHostException;

/**
 * @author Bret Johnson
 * @since 10/7/12 12:53 AM
 */
public class JavaIOUtils {

    /**
     * Map a Java IOException to the matching JSimple platform independent exception.  If there's a specific exception
     * type for the subclass (because it's an error we're likely to care about), return that.  Otherwise, just return a
     * JSimple IOException.
     *
     * @param e Java IOException (or a subclass)
     * @return JSimple IOException (or a subclass)
     */
    public static IOException jSimpleExceptionFromJavaIOException(java.io.IOException e) {
        IOException jSimpleIOException;
        String message = e.getMessage();
        if (message == null)
            message = "";

        if (e instanceof java.net.SocketTimeoutException)
            jSimpleIOException = new SocketTimeoutException(message, e);
        else if (e instanceof java.net.UnknownHostException)
            jSimpleIOException = new UnknownHostException(message, e);
        else if (e instanceof java.io.FileNotFoundException)
            jSimpleIOException = new PathNotFoundException(message, e);
        else jSimpleIOException = new IOException(message, e);

        // Replace the stack trace with the original one, so it looks like the original code threw the exception.
        // In some ways that's not technically correct, but it generally makes the stack traces easier to read
        // and more standard Java-like, since the nested exception wrapping doesn't add any real info in error output
        // and in practice tends to make error output a bit harder to understand, not easier
        StackTraceElement[] stackTrace = e.getStackTrace();
        jSimpleIOException.setStackTrace(stackTrace);

        return jSimpleIOException;
    }
}
