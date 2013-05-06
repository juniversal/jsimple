using System;

namespace jsimple.lang
{
    public abstract class AutoCloseable : IDisposable
    {
        public abstract void close();

        public void Dispose()
        {
            close();
        }
    }
}