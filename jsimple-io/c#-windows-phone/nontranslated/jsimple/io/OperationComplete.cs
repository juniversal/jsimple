using System.Threading;

namespace jsimple.io
{
    public class OperationComplete
    {
        private bool done = false;

        public void wait()
        {
            lock (this)
                while (!done)
                    Monitor.Wait(this);
        }

        public void mark


    }
}