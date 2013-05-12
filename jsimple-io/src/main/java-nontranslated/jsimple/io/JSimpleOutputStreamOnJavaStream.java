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

    public JSimpleOutputStreamOnJavaStream(java.io.OutputStream javaOutputStream) {
        this.javaOutputStream = javaOutputStream;
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
        try {
            javaOutputStream.close();
        } catch (java.io.IOException e) {
            throw JavaIOUtils.jSimpleExceptionFromJavaIOException(e);
        }
    }
}
