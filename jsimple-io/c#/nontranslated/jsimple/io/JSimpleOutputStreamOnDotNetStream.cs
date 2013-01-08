using System;
using System.IO;

namespace jsimple.io
{
    public class JSimpleOutputStreamOnDotNetStream : OutputStream
    {
        private readonly Stream dotNetStream;

        public JSimpleOutputStreamOnDotNetStream(Stream dotNetStream)
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

        public override void flush()
        {
            try
            {
                dotNetStream.Flush();
            }
            catch (System.IO.IOException e)
            {
                throw DotNetIOUtils.jSimpleExceptionFromDotNetIOException(e);
            }
        }

        public override void write(sbyte[] buffer, int offset, int length)
        {
            try
            {
                dotNetStream.Write((byte[]) (Array) buffer, offset, length);
            }
            catch (System.IO.IOException e)
            {
                throw DotNetIOUtils.jSimpleExceptionFromDotNetIOException(e);
            }
        }

        public override void write(int oneByte)
        {
            try
            {
                dotNetStream.WriteByte((byte) oneByte);
            }
            catch (System.IO.IOException e)
            {
                throw DotNetIOUtils.jSimpleExceptionFromDotNetIOException(e);
            }
        }
    }
}