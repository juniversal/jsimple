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

package jsimple.net;

import org.jetbrains.annotations.Nullable;

/**
 * @author Bret Johnson
 * @since 7/24/13 11:07 PM
 */
public abstract class SocketListener {
    private int port;
    private SocketConnectionHandler socketConnectionHandler;
    private static volatile @Nullable SocketListenerFactory factory;

    /**
     * Create TCP SocketListener, using the global factory.  This method is the normal way to create a TPC
     * SocketListener, using the default implementation appropriate for the current platform.
     *
     * @param socketConnectionHandler connection handler, to receive incoming connections
     * @param port                    port number to listen on
     * @return SocketListener
     */
    public static SocketListener createTcpSocketListener(SocketConnectionHandler socketConnectionHandler, int port) {
        if (factory == null)
            throw new RuntimeException("SocketListener factory isn't set; did you forget to call JSimpleIO.init()?");
        return factory.createTcpSocketListener(socketConnectionHandler, port);
    }

    /**
     * Set the global (default) factory used to create SocketListeners.  Clients normally don't call this method
     * directly and just call JSimpleIO.init at app startup instead, which sets the factory to the default
     * implementation appropriate for the current platform.
     *
     * @param socketListenerFactory socket listener factory
     */
    public static void setFactory(@Nullable SocketListenerFactory socketListenerFactory) {
        factory = socketListenerFactory;
    }

    protected SocketListener(SocketConnectionHandler socketConnectionHandler, int port) {
        this.socketConnectionHandler = socketConnectionHandler;
        this.port = port;
    }

    public SocketConnectionHandler getSocketConnectionHandler() {
        return socketConnectionHandler;
    }

    /**
     * Get the port.  The port only applies to TCP sockets.
     *
     * @return port number
     */
    public int getPort() {
        return port;
    }

    /**
     * Set the port, overriding what was previously set.  The port only applies to socket types that have the concept of
     * port, like TCP/UDP sockets.  The port must be set before starting the listener; if changed, the listener must be
     * stopped & restarted.
     *
     * @param port port number
     */
    public void setPort(int port) {
        this.port = port;
    }

    public abstract void start();

    public abstract void stop();

    public static interface SocketListenerFactory {
        SocketListener createTcpSocketListener(SocketConnectionHandler socketConnectionHandler1, int port);
    }
}
