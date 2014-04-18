using System;

namespace jsimple.net {



    /// <summary>
    /// @author Bret Johnson
    /// @since 7/24/13 11:07 PM
    /// </summary>
    public abstract class SocketListener {
        private int port;
        private SocketConnectionHandler socketConnectionHandler;
        private static volatile SocketListenerFactory factory;

        /// <summary>
        /// Create TCP SocketListener, using the global factory.  This method is the normal way to create a TPC
        /// SocketListener, using the default implementation appropriate for the current platform.
        /// </summary>
        /// <param name="socketConnectionHandler"> connection handler, to receive incoming connections </param>
        /// <param name="port">                    port number to listen on </param>
        /// <returns> SocketListener </returns>
        public static SocketListener createTcpSocketListener(SocketConnectionHandler socketConnectionHandler, int port) {
            if (factory == null)
                throw new Exception("SocketListener factory isn't set; did you forget to call JSimpleIO.init()?");
            return factory.createTcpSocketListener(socketConnectionHandler, port);
        }

        /// <summary>
        /// Set the global (default) factory used to create SocketListeners.  Clients normally don't call this method
        /// directly and just call JSimpleIO.init at app startup instead, which sets the factory to the default
        /// implementation appropriate for the current platform.
        /// </summary>
        /// <param name="socketListenerFactory"> socket listener factory </param>
        public static SocketListenerFactory Factory {
            set {
                factory = value;
            }
        }

        protected internal SocketListener(SocketConnectionHandler socketConnectionHandler, int port) {
            this.socketConnectionHandler = socketConnectionHandler;
            this.port = port;
        }

        public virtual SocketConnectionHandler SocketConnectionHandler {
            get {
                return socketConnectionHandler;
            }
        }

        /// <summary>
        /// Get the port.  The port only applies to TCP sockets.
        /// </summary>
        /// <returns> port number </returns>
        public virtual int Port {
            get {
                return port;
            }
            set {
                this.port = value;
            }
        }


        public abstract void start();

        public abstract void stop();

        public interface SocketListenerFactory {
            SocketListener createTcpSocketListener(SocketConnectionHandler socketConnectionHandler1, int port);
        }
    }

}