using System;
using System.IO.IsolatedStorage;

namespace jsimple.io
{
    public class Paths : PathsBase
    {
        private static readonly object lockObject = new Object();
        static volatile FileSystemDirectory applicationDataDirectory = null;

        /// <returns> the directory where the application should store its private data. </returns>
        public static Directory ApplicationDataDirectory
        {
            get
            {
                if (applicationDataDirectory == null)
                {
                    lock (lockObject)
                    {
                        if (applicationDataDirectory == null)
                            applicationDataDirectory = new FileSystemDirectory("c:\\foo");
                    }
                }
                return applicationDataDirectory;
            }
        }
    }
}