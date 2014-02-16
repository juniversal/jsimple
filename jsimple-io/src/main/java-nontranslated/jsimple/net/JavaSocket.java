package jsimple.net;

import jsimple.io.*;

/**
 * @author Bret Johnson
 * @since 8/5/13 1:40 AM
 */
public class JavaSocket extends Socket {
    private java.net.Socket socket;

    public JavaSocket(java.net.Socket socket) {
        this.socket = socket;
    }

    @Override public InputStream getInputStream() {
        try {
            return new JSimpleInputStreamOnJavaStream(socket.getInputStream(), true);
        } catch (java.io.IOException e) {
            throw JavaIOUtils.jSimpleExceptionFromJavaIOException(e);
        }
    }

    @Override public OutputStream getOutputStream() {
        try {
            return new JSimpleOutputStreamOnJavaStream(socket.getOutputStream(), true);
        } catch (java.io.IOException e) {
            throw JavaIOUtils.jSimpleExceptionFromJavaIOException(e);
        }
    }

    @Override public boolean isClosed() {
        return socket.isClosed();
    }

    @Override public void close() {
        try {
            socket.close();
        } catch (java.io.IOException e) {
            throw JavaIOUtils.jSimpleExceptionFromJavaIOException(e);
        }
    }
}
