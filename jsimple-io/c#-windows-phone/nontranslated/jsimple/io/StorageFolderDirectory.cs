using System;
using System.Collections.Generic;
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

        public override File getFile(string name)
        {
            try
            {
                return new StorageFileFile(storageFolder.GetFileAsync(name).DoSynchronously());
            }
            catch (System.IO.IOException e)
            {
                throw DotNetIOUtils.jSimpleExceptionFromDotNetIOException(e);
            }
        }

        public override File createFile(string name)
        {
            try
            {
                return new StorageFileFile(storageFolder.CreateFileAsync(name, CreationCollisionOption.ReplaceExisting).DoSynchronously());
            }
            catch (System.IO.IOException e)
            {
                throw DotNetIOUtils.jSimpleExceptionFromDotNetIOException(e);
            }
        }

        public override Directory getDirectory(string name)
        {
            try
            {
                return new StorageFolderDirectory(storageFolder.GetFolderAsync(name).DoSynchronously());
            }
            catch (System.IO.IOException e)
            {
                throw DotNetIOUtils.jSimpleExceptionFromDotNetIOException(e);
            }
        }

        public override Directory getOrCreateDirectory(string name)
        {
            try
            {
                return
                    new StorageFolderDirectory(
                        storageFolder.CreateFolderAsync(name, CreationCollisionOption.OpenIfExists).DoSynchronously());
            }
            catch (System.IO.IOException e)
            {
                throw DotNetIOUtils.jSimpleExceptionFromDotNetIOException(e);
            }
        }

        public override void visitChildren(DirectoryVisitor visitor)
        {
            IReadOnlyList<IStorageItem> storageItems = storageFolder.GetItemsAsync().DoSynchronously();

            foreach (IStorageItem storageItem in storageItems)
            {
                StorageItemPathAttributes pathAttributes = new StorageItemPathAttributes(storageItem);

                if (storageItem is IStorageFolder)
                    visitor.visit(new StorageFolderDirectory((StorageFolder)storageItem), pathAttributes);
                else if (storageItem is IStorageFile)
                    visitor.visit(new StorageFileFile((StorageFile)storageItem), pathAttributes);
                else throw new Exception("Unknown type of StorageItem: " + storageItem);
            }
        }
    }
}