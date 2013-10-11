package jsimple.net;

import jsimple.io.InputStream;
import jsimple.io.OutputStream;

/**
 * @author Bret Johnson
 * @since 7/24/13 11:07 PM
 */
public abstract class Socket extends jsimple.lang.AutoCloseable {
    public abstract InputStream getInputStream();

    public abstract OutputStream getOutputStream();

    public abstract boolean isClosed();
}
