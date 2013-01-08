using System;
using System.Net;
using System.Threading;
using Windows.Foundation;
using Windows.Storage;
using jsimple.net;

namespace jsimple.io
{
    public class StorageFolderDirectory : Directory
    {
        private readonly StorageFolder storageFolder;

        public StorageFolderDirectory(StorageFolder storageFolder)
        {
            this.storageFolder = storageFolder;
        }

        public override string Name
        {
            get
            {
                return storageFolder.Name;
            }
        }

        public override Directory Parent
        {
            get { throw new System.NotImplementedException(); }
        }

        public override File getChildFile(string name)
        {
            AsyncOperationComplete complete = new AsyncOperationComplete();
            
            IAsyncOperation<StorageFile> asyncOperation = storageFolder.GetFileAsync(name);
            StorageFile storageFile = null;
            asyncOperation.Completed = delegate(IAsyncOperation<StorageFile> info, AsyncStatus status)
                {
                    storageFile = info.GetResults();
                    complete.setCompleted();
                };

            complete.wait();

            return new StorageFileFile(storageFile);
        }

        public override Directory getChildDirectory(string name)
        {
            throw new System.NotImplementedException();
        }

        public override void visitChildren(DirectoryVisitor visitor)
        {
            throw new System.NotImplementedException();
        }
    }


}