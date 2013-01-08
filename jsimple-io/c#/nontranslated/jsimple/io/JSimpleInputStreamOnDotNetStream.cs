using System;
using System.IO;

namespace jsimple.io
{
    public class JSimpleInputStreamOnDotNetStream : InputStream
    {
        private readonly Stream dotNetStream;

        public JSimpleInputStreamOnDotNetStream(Stream dotNetStream)
        {
            this.dotNetStream = dotNetStream;
        }

        public override void close()
        {
            try
            {
                dotNetStream.Dispose();
            }
            catch (System.IO.IOException e)
            {
                throw DotNetIOUtils.jSimpleExceptionFromDotNetIOException(e);
            }
        }

        public override int read()
        {
            try
            {
                return dotNetStream.ReadByte();
            }
            catch (System.IO.IOException e)
            {
                throw DotNetIOUtils.jSimpleExceptionFromDotNetIOException(e);
            }
        }

        public override int read(sbyte[] buffer, int offset, int length)
        {
            try
            {
                int bytesRead = dotNetStream.Read((byte[]) (Array) buffer, offset, length);
                if (bytesRead == 0)
                    return -1; // JSimple input streams return -1 at end of stream, not 0
                else return bytesRead;
            }
            catch (System.IO.IOException e)
            {
                throw DotNetIOUtils.jSimpleExceptionFromDotNetIOException(e);
            }
        }
    }
}