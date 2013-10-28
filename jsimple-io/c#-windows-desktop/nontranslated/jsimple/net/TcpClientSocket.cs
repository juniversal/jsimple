using System.IO;
using System.Net.Sockets;
using jsimple.io;

namespace jsimple.net
{
    public class TcpClientSocket : Socket
    {
        private TcpClient tcpClient;

        public TcpClientSocket(TcpClient tcpClient)
        {
            this.tcpClient = tcpClient;
        }

        public override InputStream InputStream
        {
            get { return new DotNetStreamInputStream(tcpClient.GetStream()); }
        }

        public override OutputStream OutputStream
        {
            get { return new DotNetStreamOutputStream(tcpClient.GetStream()); }
        }

        public override void close()
        {
            tcpClient.Close();
        }

        public override bool Closed
        {
            get
            {
                return false /* !socket.Connected */;
            }
        }
    }
}