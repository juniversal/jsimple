using System.IO;
using System.Net.Sockets;
using jsimple.io;

namespace jsimple.net
{
    public class DotNetSocket : Socket
    {
        private System.Net.Sockets.Socket socket;

        public DotNetSocket(System.Net.Sockets.Socket socket)
        {
            this.socket = socket;
        }

        public override InputStream InputStream
        {
            get
            {
                return new DotNetStreamInputStream(new NetworkStream(socket, FileAccess.Read, false));
            }
        }

        public override OutputStream OutputStream
        {
            get { return new DotNetStreamOutputStream(new NetworkStream(socket, FileAccess.Write, false)); }
        }

        public override void close()
        {
            socket.Close();
            throw new System.NotImplementedException();
        }

        public override bool Closed
        {
            get
            {
                return false; /*  socket.Connected; */ }
        }
    }
}