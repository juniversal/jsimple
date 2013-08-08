using System;
using System.IO;
using Windows.Storage;
using Windows.Storage.Streams;
using jsimple.util;

namespace jsimple.io
{
    public class StorageFileFile : File
    {
        private readonly string name;
        private readonly StorageFolderDirectory parent;
        private StorageFile storageFile;

        public StorageFileFile(StorageFolderDirectory parent, string name)
        {
            this.parent = parent;
            this.name = name;
        }

        public StorageFileFile(StorageFolderDirectory parent, StorageFile storageFile)
        {
            this.parent = parent;
            name = storageFile.Name;
            this.storageFile = storageFile;
        }

        public override string Name
        {
            get { return name; }
        }

        public override Directory Parent
        {
            get { return parent; }
        }

        public override InputStream openForRead()
        {
            ensureGotStorageFile();

            try
            {
                IInputStream inputStream = storageFile.OpenSequentialReadAsync().DoSynchronously();

                // TODO: Need to close the inputStream itself; add a closedListener to InputStream, i think
                // Use the default 16K buffer for AsStreamForRead()
                return new DotNetStreamInputStream(inputStream.AsStreamForRead());
            }
            catch (System.IO.IOException e)
            {
                throw DotNetIOUtils.jSimpleExceptionFromDotNetIOException(e);
            }
        }

        public override OutputStream openForCreate()
        {
            try
            {
                storageFile =
                    parent.StorageFolder.CreateFileAsync(name, CreationCollisionOption.ReplaceExisting)
                          .DoSynchronously();

                IRandomAccessStream randomAccessStream =
                    storageFile.OpenAsync(FileAccessMode.ReadWrite).DoSynchronously();
                IOutputStream outputStream = randomAccessStream.GetOutputStreamAt(0);

                // Use the default buffer for AsStreamForWrite()
                return new DotNetStreamOutputStream(outputStream.AsStreamForWrite());
            }
            catch (System.IO.IOException e)
            {
                throw DotNetIOUtils.jSimpleExceptionFromDotNetIOException(e);
            }
        }

        public override void delete()
        {
            ensureGotStorageFile();
            storageFile.DeleteAsync(StorageDeleteOption.PermanentDelete).DoSynchronously();
        }

        public override bool exists()
        {
            try
            {
                ensureGotStorageFile();
            }
            catch (PathNotFoundException)
            {
                return false;
            }

            return true;
        }

        public override void rename(string newName)
        {
            ensureGotStorageFile();

            try
            {
                storageFile.RenameAsync(newName, NameCollisionOption.ReplaceExisting).DoSynchronously();
            }
            catch (System.IO.IOException e)
            {
                throw DotNetIOUtils.jSimpleExceptionFromDotNetIOException(e);
            }
        }

        public override long LastModifiedTime
        {
            get
            {
                ensureGotStorageFile();

                try
                {
                    return PlatformUtils.toMillisFromDateTimeOffset(storageFile.GetBasicPropertiesAsync().DoSynchronously().DateModified);
                }
                catch (System.IO.IOException e)
                {
                    throw DotNetIOUtils.jSimpleExceptionFromDotNetIOException(e);
                }
            }

            set { }
        }

        public override long Size
        {
            get
            {
                ensureGotStorageFile();

                try
                {
                    return (long) storageFile.GetBasicPropertiesAsync().DoSynchronously().Size;
                }
                catch (System.IO.IOException e)
                {
                    throw DotNetIOUtils.jSimpleExceptionFromDotNetIOException(e);
                }
            }
        }

        private void ensureGotStorageFile()
        {
            if (storageFile == null)
            {
                try
                {
                    storageFile = parent.StorageFolder.GetFileAsync(name).DoSynchronously();
                }
                catch (System.IO.IOException e)
                {
                    throw DotNetIOUtils.jSimpleExceptionFromDotNetIOException(e);
                }
            }
        }
    }
}