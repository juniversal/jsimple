package jsimple.io;

import java.io.IOException;

/**
 * @author Bret Johnson
 * @since 10/7/12 12:30 AM
 */
public class JSimpleInputStreamOnJavaStream extends InputStream {
    private java.io.InputStream javaInputStream;

    public JSimpleInputStreamOnJavaStream(java.io.InputStream javaInputStream) {
        this.javaInputStream = javaInputStream;
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
        try {
            javaInputStream.close();
        } catch (IOException e) {
            throw JavaIOUtils.jSimpleExceptionFromJavaIOException(e);
        }
    }
}
