﻿using System;
using System.IO;
using Windows.Storage;
using Windows.Storage.Streams;

namespace jsimple.io
{
    public class StorageFileFile : File
    {
        private readonly StorageFolderDirectory parent;
        private readonly string name;
        private StorageFile storageFile;

        public StorageFileFile(StorageFolderDirectory parent, string name)
        {
            this.parent = parent;
            this.name = name;
        }

        public StorageFileFile(StorageFolderDirectory parent, StorageFile storageFile)
        {
            this.parent = parent;
            this.name = storageFile.Name;
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
                storageFile = parent.StorageFolder.CreateFileAsync(name, CreationCollisionOption.ReplaceExisting).DoSynchronously();

                IRandomAccessStream randomAccessStream = storageFile.OpenAsync(FileAccessMode.ReadWrite).DoSynchronously();
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
                storageFile.RenameAsync(newName).DoSynchronously();
            }
            catch (System.IO.IOException e)
            {
                throw DotNetIOUtils.jSimpleExceptionFromDotNetIOException(e);
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

        public override long LastModifiedTime
        {
            // TODO: Implement an alternative to this (probably store timestamps in the media library instead on WP)
            //set { throw new NotImplementedException("Setting LastModifiedTime isn't supported on Windows Phone"); }
            set {  }
        }
    }
}
