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

import java.io.IOException;

/**
 * @author Bret Johnson
 * @since 10/7/12 12:30 AM
 */
public class JSimpleInputStreamOnJavaStream extends InputStream {
    private java.io.InputStream javaInputStream;
    private boolean ignoreClose = false;

    public JSimpleInputStreamOnJavaStream(java.io.InputStream javaInputStream) {
        this.javaInputStream = javaInputStream;
    }

    /**
     * Create a JSimple stream that wraps the specified Java InputStream.  If ignoreClose is true then calls to close()
     * don't actually close the underlying stream--they don't do anything.
     *
     * @param javaInputStream Java InputStream
     * @param ignoreClose     whether or not to close the underlying stream when this stream is closed
     */
    public JSimpleInputStreamOnJavaStream(java.io.InputStream javaInputStream, boolean ignoreClose) {
        this.javaInputStream = javaInputStream;
        this.ignoreClose = ignoreClose;
    }

    @Override public int read() {
        try {
            return javaInputStream.read();
        } catch (IOException e) {
            throw JavaIOUtils.jSimpleExceptionFromJavaIOException(e);
        }
    }

    @Override public int read(byte[] buffer) {
        try {
            return javaInputStream.read(buffer);
        } catch (IOException e) {
            throw JavaIOUtils.jSimpleExceptionFromJavaIOException(e);
        }
    }

    @Override public int read(byte[] buffer, int offset, int length) {
        try {
            return javaInputStream.read(buffer, offset, length);
        } catch (IOException e) {
            throw JavaIOUtils.jSimpleExceptionFromJavaIOException(e);
        }
    }

    @Override public void close() {
        if (!ignoreClose) {
            try {
                javaInputStream.close();
            } catch (IOException e) {
                throw JavaIOUtils.jSimpleExceptionFromJavaIOException(e);
            }
        }
    }
}
