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
        @Override
        public SocketListener createTcpSocketListener(SocketConnectionHandler socketConnectionHandler, int port) {
            return new JavaTcpSocketListener(socketConnectionHandler, port);
        }
    }
}
