package jsimple.net;

import jsimple.io.JavaIOUtils;

import java.io.IOException;
import java.net.ServerSocket;

/**
 * @author Bret Johnson
 * @since 8/5/13 12:29 AM
 */
public class JavaTcpSocketListener extends SocketListener {
    private ServerSocket serverSocket;
    private Thread serverThread;

    public JavaTcpSocketListener(SocketConnectionHandler socketConnectionHandler, int port) {
        super(socketConnectionHandler, port);
    }

    public void start() {
        try {
            serverSocket = new ServerSocket(getPort());
        } catch (IOException e) {
            throw JavaIOUtils.jSimpleExceptionFromJavaIOException(e);
        }

        serverThread = new ServerListenerThread();
        serverThread.start();
    }

    public void stop() {
        // TODO: Implement this
    }

    private class ServerListenerThread extends Thread {
        private boolean stop = false;

        @Override public void run() {
            while (!stop) {
                try {
                    java.net.Socket clientSocket = serverSocket.accept();

                    SocketConnectionThread socketConnectionThread =
                            new SocketConnectionThread(new JavaSocket(clientSocket));
                    socketConnectionThread.start();
                } catch (IOException e) {
                    // TODO: Handle error better
                    System.out.println("Accept failed: 8000");
                }
            }
        }
    }

    private class SocketConnectionThread extends Thread {
        private Socket socket;

        private SocketConnectionThread(Socket socket) {
            this.socket = socket;
        }

        @Override public void run() {
            getSocketConnectionHandler().sockedConnected(socket);
        }
    }

    public static class JavaSocketListenerFactory implements SocketListenerFactory {
        @Override public SocketListener createTcpSocketListener(SocketConnectionHandler socketConnectionHandler, int port) {
            return new JavaTcpSocketListener(socketConnectionHandler, port);
        }
    }
}
