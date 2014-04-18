using System;
using System.IO.IsolatedStorage;

namespace jsimple.io
{
    public class WindowsDesktopPaths : Paths
    {
        private static readonly object lockObject = new Object();
        static volatile FileSystemDirectory applicationDataDirectory = null;

        /// <returns> the directory where the application should store its private data. </returns>
        public override Directory ApplicationDataDirectory
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

        /// <summary>
        /// Return the Directory object corresponding to the string serialized representation.  If the directory doesn't
        /// exist, it's implementation dependent whether this method throws a PathNotFoundException or a
        /// PathNotFoundException is thrown when the returned directory is used--different implementations do different
        /// things there.
        /// <p/>
        /// Not all implementations of Directory support serializing it as a string.  An exception is thrown if it's not
        /// supported.
        /// </summary>
        /// <param name="directoryPathString">
        /// @return </param>
        public override Directory getFileSystemDirectory(string directoryPathString)
        {
            return new FileSystemDirectory(directoryPathString);
        }
    }
}