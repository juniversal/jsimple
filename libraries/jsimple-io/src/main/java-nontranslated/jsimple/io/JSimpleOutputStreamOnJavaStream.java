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

/**
 * This class turns a java.io.OutputStream into a JSimple OutputStream.  It's a straight pass through, except that
 * checked exceptions are turned into similar platform independent unchecked exceptions.
 *
 * @author Bret Johnson
 * @since 10/7/12 12:12 AM
 */
public class JSimpleOutputStreamOnJavaStream extends OutputStream {
    private java.io.OutputStream javaOutputStream;
    private boolean ignoreClose;

    public JSimpleOutputStreamOnJavaStream(java.io.OutputStream javaOutputStream) {
        this.javaOutputStream = javaOutputStream;
    }

    /**
     * Create a JSimple stream that wraps the specified Java OutputStream.  If ignoreClose is true then calls to close()
     * don't actually close the underlying stream--they don't do anything.
     *
     * @param javaOutputStream Java OutputStream
     * @param ignoreClose      whether or not to close the underlying stream when this stream is closed
     */
    public JSimpleOutputStreamOnJavaStream(java.io.OutputStream javaOutputStream, boolean ignoreClose) {
        this.javaOutputStream = javaOutputStream;
        this.ignoreClose = ignoreClose;
    }

    @Override public void write(int oneByte) {
        try {
            javaOutputStream.write(oneByte);
        } catch (java.io.IOException e) {
            throw JavaIOUtils.jSimpleExceptionFromJavaIOException(e);
        }
    }

    @Override public void write(byte[] b) {
        try {
            javaOutputStream.write(b);
        } catch (java.io.IOException e) {
            throw JavaIOUtils.jSimpleExceptionFromJavaIOException(e);
        }
    }

    @Override public void write(byte[] b, int offset, int length) {
        try {
            javaOutputStream.write(b, offset, length);
        } catch (java.io.IOException e) {
            throw JavaIOUtils.jSimpleExceptionFromJavaIOException(e);
        }
    }

    @Override public void flush() {
        try {
            javaOutputStream.flush();
        } catch (java.io.IOException e) {
            throw JavaIOUtils.jSimpleExceptionFromJavaIOException(e);
        }
    }

    @Override protected void doClose() {
        if (!ignoreClose) {
            try {
                javaOutputStream.close();
            } catch (java.io.IOException e) {
                throw JavaIOUtils.jSimpleExceptionFromJavaIOException(e);
            }
        }
    }
}
