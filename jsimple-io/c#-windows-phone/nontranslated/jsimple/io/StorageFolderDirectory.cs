using System;
using System.Collections.Generic;
using System.Net;
using System.Threading;
using Windows.Foundation;
using Windows.Storage;
using jsimple.net;
using jsimple.util;

namespace jsimple.io
{
    public class StorageFolderDirectory : Directory
    {
        private readonly StorageFolderDirectory parent;
        private readonly string name;
        private StorageFolder storageFolder;

        public StorageFolderDirectory(StorageFolderDirectory parent, string name)
        {
            this.parent = parent;
            this.name = name;
            this.storageFolder = null;
        }
         
        public StorageFolderDirectory(StorageFolderDirectory parent, StorageFolder storageFolder)
        {
            this.parent = parent;
            this.name = storageFolder.Name;
            this.storageFolder = storageFolder;
        }

        public override string Name
        {
            get { return name; }
        }

        public override string PathString
        {
            get { return storageFolder.Path; }
        }

        public override long LastModifiedTime
        {
            get
            {
                try
                {
                    return PlatformUtils.toMillisFromDateTimeOffset(storageFolder.GetBasicPropertiesAsync().DoSynchronously().DateModified);
                }
                catch (System.IO.IOException e)
                {
                    throw DotNetIOUtils.jSimpleExceptionFromDotNetIOException(e);
                }
            }

            set { }
        }

        public override bool SetLastModifiedTimeSupported
        {
            get { return false; }
        }


        public StorageFolder StorageFolder
        {
            get
            {
                if (storageFolder == null)
                {
                    try
                    {
                        storageFolder = parent.StorageFolder.GetFolderAsync(name).DoSynchronously();
                    }
                    catch (System.IO.IOException e)
                    {
                        throw DotNetIOUtils.jSimpleExceptionFromDotNetIOException(e);
                    }
                }

                return storageFolder;
            }
        }

        public override File getFile(string name)
        {
            return new StorageFileFile(this, name);
        }

        public override Directory getDirectory(string name)
        {
            return new StorageFolderDirectory(this, name);
        }

        public override bool exists()
        {
            try
            {
                StorageFolder temp = StorageFolder;
            }
            catch (PathNotFoundException)
            {
                return false;
            }

            return true;
        }

        public override void create()
        {
            if (storageFolder == null)
                storageFolder =
                    parent.StorageFolder.CreateFolderAsync(name, CreationCollisionOption.OpenIfExists).DoSynchronously();
        }

        public override void visitChildren(DirectoryVisitor visitor)
        {
            try
            {
                IReadOnlyList<IStorageItem> storageItems = StorageFolder.GetItemsAsync().DoSynchronously();

                foreach (IStorageItem storageItem in storageItems)
                {
                    StorageItemPathAttributes pathAttributes = new StorageItemPathAttributes(storageItem);

                    if (storageItem is IStorageFolder)
                        visitor.visit(new StorageFolderDirectory(this, (StorageFolder)storageItem));
                    else if (storageItem is IStorageFile)
                        visitor.visit(new StorageFileFile(this, (StorageFile)storageItem));
                    else throw new Exception("Unknown type of StorageItem: " + storageItem);
                }
            }
            catch (System.IO.IOException e)
            {
                throw DotNetIOUtils.jSimpleExceptionFromDotNetIOException(e);
            }
        }

        public override void delete()
        {
            StorageFolder.DeleteAsync(StorageDeleteOption.PermanentDelete).DoSynchronously();
            storageFolder = null;
        }

        public override void rename(string newName)
        {
            StorageFolder.RenameAsync(newName, NameCollisionOption.FailIfExists).DoSynchronously();
        }
    }
}