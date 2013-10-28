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

                Thread connectionThread = new Thread(() => SocketConnectionHandler.sockedConnected(tcpClientSocket));
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