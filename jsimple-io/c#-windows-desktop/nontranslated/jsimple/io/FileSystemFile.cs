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

using System.IO;
using jsimple.util;

namespace jsimple.io {
    public class FileSystemFile : File {
        private readonly string filePath;
        private readonly FileSystemDirectory parent;

        public FileSystemFile(FileSystemDirectory parent, string filePath) {
            this.parent = parent;
            this.filePath = filePath;
        }

        /*
        public override File AtomicFile
        {
            get
            {
                FileSystemFile atomicFile = new FileSystemFile(this.parent, this.filePath + "-temp");
                return atomicFile;
            }
        }
         */

        public override string getPathString() {
            return filePath;
        }

        public override string getName() {
            return System.IO.Path.GetFileName(filePath);
        }

        public override Directory getParent() {
            return parent;
        }

        public override InputStream openForRead() {
            try {
                FileStream fileStream = System.IO.File.OpenRead(filePath);
                return new DotNetStreamInputStream(fileStream);
            }
            catch (System.IO.IOException e) {
                throw DotNetIOUtils.jSimpleExceptionFromDotNetIOException(e);
            }
        }


        public override OutputStream openForCreate() {
            try {
                FileStream fileStream = System.IO.File.OpenWrite(filePath);
                return new DotNetStreamOutputStream(fileStream);
            }
            catch (System.IO.IOException e) {
                throw DotNetIOUtils.jSimpleExceptionFromDotNetIOException(e);
            }
        }

        public override bool exists() {
            return System.IO.File.Exists(filePath);
        }

        public override void delete() {
            try {
                // Delete the file; if it doesn't exist, nothing happens
                System.IO.File.Delete(filePath);
            }
            catch (System.IO.DirectoryNotFoundException e) {
                // If the directory doesn't exist, ignore the error
            }
            catch (System.IO.IOException e) {
                throw DotNetIOUtils.jSimpleExceptionFromDotNetIOException(e);
            }
        }

        public override void moveTo(File destination)
        {
            string destinationPath = ((FileSystemFile) destination).filePath;

            moveToPath(destinationPath);
        }

        public override void renameTo(string newName) {
            string destinationPath = parent.getChildPath(newName);
            moveToPath(destinationPath);
        }

        private void moveToPath(string destinationPath)
        {
            try
            {
                System.IO.File.Move(filePath, destinationPath);
            }
            catch (System.IO.IOException e)
            {
                // If the destination file already exists (0x800700B7 is "Cannot create a file when that file already
                // exists"), then delete it and retry the move.
                if (e.HResult == unchecked((int) 0x800700B7))
                {
                    try
                    {
                        System.IO.File.Delete(destinationPath);
                        System.IO.File.Move(filePath, destinationPath);
                    }
                    catch (System.IO.IOException e2)
                    {
                        throw DotNetIOUtils.jSimpleExceptionFromDotNetIOException(e2);
                    }
                }
                else
                    throw DotNetIOUtils.jSimpleExceptionFromDotNetIOException(e);
            }
        }

        public override long getLastModifiedTime() {
            try {
                return PlatformUtil.toMillisFromDateTime(System.IO.File.GetLastWriteTimeUtc(filePath));
            }
            catch (System.IO.IOException e) {
                throw DotNetIOUtils.jSimpleExceptionFromDotNetIOException(e);
            }
        }

        public override void setLastModifiedTime(long value)  {
            try {
                System.IO.File.SetLastWriteTimeUtc(filePath, PlatformUtil.toDotNetDateTimeFromMillis(value));
            }
            catch (System.IO.IOException e) {
                throw DotNetIOUtils.jSimpleExceptionFromDotNetIOException(e);
            }
        }

        public override long getSize() {
            try {
                return new FileInfo(filePath).Length;
            }
            catch (System.IO.IOException e) {
                throw DotNetIOUtils.jSimpleExceptionFromDotNetIOException(e);
            }
        }
    }
}