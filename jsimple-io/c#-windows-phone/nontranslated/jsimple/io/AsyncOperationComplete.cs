using System.Threading;

namespace jsimple.io
{
    public class AsyncOperationComplete
    {
        private bool complete = false;

        public void wait()
        {
            lock (this)
            {
                while (!complete)
                    Monitor.Wait(this);
            }
        }

        public void setCompleted()
        {
            lock (this)
            {
                complete = true;
                Monitor.Pulse(this);
            }
        }
    }
}