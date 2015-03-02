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

using System;
using System.Net;
using System.Net.Sockets;
using System.Threading;

namespace jsimple.net
{
    public class DotNetTcpIpSocketListener : SocketListener
    {
        private int port;
        private System.Net.Sockets.Socket serverSocket;
        private TcpListener tcpListener;
        private bool requestedStop;

        public DotNetTcpIpSocketListener(SocketConnectionHandler connectionHandler, int port)
            : base(connectionHandler, port)
        {
            this.port = port;
        }
        
        public override void start()
        {
/*
            // Establish the local endpoint for the socket.  
            IPEndPoint localEndPoint = new IPEndPoint(IPAddress.Any, port);

            // Create a TCP/IP socket
            serverSocket = new System.Net.Sockets.Socket(AddressFamily.InterNetwork, SocketType.Stream, ProtocolType.Tcp);

            // Bind the socket to the local endpoint and listen for incoming connections.
            serverSocket.Bind(localEndPoint);
            serverSocket.Listen(10);

            Thread serverListenerThread = new Thread(ServerListenerThread);
            serverListenerThread.Start();
*/

            tcpListener = new TcpListener(IPAddress.Any, port);
            tcpListener.Start();

            Thread serverListenerThread = new Thread(ServerListenerThread);
            serverListenerThread.Start();
        }

        public override void stop()
        {
            // TODO: Implement this
        }

        private void ServerListenerThread() {
            // Start listening for connections.
            while (! requestedStop) {
                // Perform a blocking call to accept requests. 
                // You could also user server.AcceptSocket() here.
                TcpClient tcpClient = tcpListener.AcceptTcpClient();

                TcpClientSocket tcpClientSocket = new TcpClientSocket(tcpClient);

                Thread connectionThread = new Thread(() => getSocketConnectionHandler().sockedConnected(tcpClientSocket));
                connectionThread.Start();

/*
                // Program is suspended while waiting for an incoming connection.
                Socket connectionSocket = new DotNetSocket(serverSocket.Accept());

                Thread connectionThread = new Thread(() => SocketConnectionThread(connectionSocket));
                connectionThread.Start();

                //handler.Send(msg);%
                //handler.Shutdown(SocketShutdown.Both);
                //handler.Close();
*/
            }
        }

        public class DotNetSocketListenerFactory : SocketListenerFactory
        {
            public SocketListener createTcpSocketListener(SocketConnectionHandler socketConnectionHandler, int port)
            {
                return new DotNetTcpIpSocketListener(socketConnectionHandler, port);
            }
        };

    }
}