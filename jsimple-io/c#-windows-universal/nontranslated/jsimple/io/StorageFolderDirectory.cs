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

        public override string getName()
        {
            return name;
        }

        public override string getPathString()
        {
            return storageFolder.Path;
        }

        public override long getLastModifiedTime()
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

        public override void setLastModifiedTime(long time)
        {
        }

        public override bool isSetLastModifiedTimeSupported()
        {
            return false;
        }

        public StorageFolder getStorageFolder()
        {
            if (storageFolder == null)
            {
                try
                {
                    storageFolder = parent.getStorageFolder().GetFolderAsync(name).DoSynchronously();
                }
                catch (System.IO.IOException e)
                {
                    throw DotNetIOUtils.jSimpleExceptionFromDotNetIOException(e);
                }
            }

            return storageFolder;
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
                StorageFolder temp = getStorageFolder();
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
                    parent.getStorageFolder().CreateFolderAsync(name, CreationCollisionOption.OpenIfExists).DoSynchronously();
        }

        public override void visitChildren(FileVisitor fileVisitor, DirectoryVisitor directoryVisitor, VisitFailed visitFailed)
        {
            try
            {
                IReadOnlyList<IStorageItem> storageItems = getStorageFolder().GetItemsAsync().DoSynchronously();

                foreach (IStorageItem storageItem in storageItems)
                {
                    StorageItemPathAttributes pathAttributes = new StorageItemPathAttributes(storageItem);

                    if (storageItem is IStorageFolder)
                    {
                        if (directoryVisitor != null)
                            directoryVisitor(new StorageFolderDirectory(this, (StorageFolder) storageItem));
                    }
                    else if (storageItem is IStorageFile)
                    {
                        if (fileVisitor != null)
                            fileVisitor(new StorageFileFile(this, (StorageFile) storageItem));
                    }
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
            getStorageFolder().DeleteAsync(StorageDeleteOption.PermanentDelete).DoSynchronously();
            storageFolder = null;
        }

        public override void renameTo(string newName)
        {
            getStorageFolder().RenameAsync(newName, NameCollisionOption.FailIfExists).DoSynchronously();
        }
    }
}