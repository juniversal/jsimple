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

    public interface SocketListenerFactory {
        SocketListener createTcpSocketListener(SocketConnectionHandler socketConnectionHandler1, int port);
    }
}
