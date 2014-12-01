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

namespace jsimple.io
{
    public class DotNetIOUtils
    {
        /// <summary>
        ///     Map a .Net IOException to the matching JSimple platform independent exception.  If there's a specific exception
        ///     type for the subclass (because it's an error we're likely to care about), return that.  Otherwise, just return a
        ///     JSimple IOException.
        /// </summary>
        public static IOException jSimpleExceptionFromDotNetIOException(System.IO.IOException e)
        {
            IOException jSimpleIOException;
            string message = e.Message ?? "";

            // TODO: Map the other exceptions
            /*
            if (e is java.net.SocketTimeoutException)
                jSimpleIOException = new SocketTimeoutException(message);
            else if (e is UnknownHostException)
                jSimpleIOException = new UnknownHostException(message);
            */
            // TODO: Include DirectoryNotFoundException for .Net apps
            if (e is System.IO.FileNotFoundException)
                jSimpleIOException = new PathNotFoundException(message, e);
            else jSimpleIOException = new IOException(message, e);

            /*
            // Replace the stack trace with the original one, so it looks like the original code threw the exception.
            // In some ways that's not technically correct, but it generally makes the stack traces easier to read
            // and more standard Java-like, since the nested exception wrapping doesn't add any real info in error output
            // and in practice tends to make error output a bit harder to understand, not easier
            StackTraceElement[] stackTrace = e.getStackTrace();
            jSimpleIOException.setStackTrace(stackTrace);
             */

            return jSimpleIOException;
        }
    }
}