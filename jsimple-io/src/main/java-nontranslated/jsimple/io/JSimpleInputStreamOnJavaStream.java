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
