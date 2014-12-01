/*
 * Copyright (c) 2012-2014, Microsoft Mobile
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

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

        public override string PathString
        {
            get
            {
                ensureGotStorageFile();
                return storageFile.Path;
            }
        }

        public override Directory Parent
        {
            get { return parent; }
        }

        public StorageFolderDirectory ParentStorageFolderDirectory
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

        public override void renameTo(string newName)
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

        public override void moveTo(File destination) {
            ensureGotStorageFile();

            StorageFileFile destinationStorageFile = (StorageFileFile) destination;

            try {
                storageFile.MoveAsync(destinationStorageFile.ParentStorageFolderDirectory.StorageFolder, destinationStorageFile.Name, NameCollisionOption.ReplaceExisting);
            }
            catch (System.IO.IOException e) {
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