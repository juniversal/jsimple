using System;
using System.Threading;
using Windows.Foundation;

namespace jsimple.io
{
    public static class AsyncOperationExtension
    {
        public static TResult DoSynchronously<TResult>(this IAsyncOperation<TResult> asyncOperation)
        {
            bool complete = false;
            Object monitorLock = new Object();

            asyncOperation.Completed = (operation, status) =>
                {
                    lock (monitorLock)
                    {
                        complete = true;
                        Monitor.Pulse(monitorLock);
                    }
                };

            lock (monitorLock)
            {
                while (!complete)
                    Monitor.Wait(monitorLock);
            }

            return asyncOperation.GetResults();
        }
    }
}