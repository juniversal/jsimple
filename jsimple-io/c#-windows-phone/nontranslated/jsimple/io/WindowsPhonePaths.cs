using System;
using System.IO.IsolatedStorage;
using Windows.Storage;
using jsimple.util;

namespace jsimple.io
{
    public class WindowsPhonePaths : Paths
    {
        StorageFolderDirectory applicationDataDirectory = null;

        /// <returns> the directory where the application should store its private data. </returns>
        public override Directory ApplicationDataDirectory
        {
            get
            {
                lock (this)
                {
                    if (applicationDataDirectory == null)
                    {
                        if (applicationDataDirectory == null)
                            applicationDataDirectory = new StorageFolderDirectory(null, ApplicationData.Current.LocalFolder);
                    }
                }
                return applicationDataDirectory;
            }
        }

        public override Directory getDirectory(string directoryPathString)
        {
            throw new BasicException("Not implemented");
        }

    }
}