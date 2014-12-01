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

using jsimple.util;
using System;
using System.Collections.Generic;
using System.IO;
using System.Text;

namespace jsimple.io {
    public class FileSystemDirectory : Directory {
        private readonly string directoryPath;

        public FileSystemDirectory(string directoryPath) {
            this.directoryPath = directoryPath;
        }

        public override string PathString {
            get { return directoryPath; }
        }

        public override string Name {
            get { return System.IO.Path.GetFileName(directoryPath); }
        }

        public override File getFile(string name) {
            return new FileSystemFile(this, getChildPath(name));
        }

        public override Directory getDirectory(string name) {
            return new FileSystemDirectory(getChildPath(name));
        }

        public override void create() {
            try {
                DirectoryInfo directoryInfo = new DirectoryInfo(directoryPath);
                if (!directoryInfo.Exists)
                    directoryInfo.Create();
            }
            catch (System.IO.IOException e) {
                throw DotNetIOUtils.jSimpleExceptionFromDotNetIOException(e);
            }
        }

        public override bool exists() {
            try {
                DirectoryInfo directoryInfo = new DirectoryInfo(directoryPath);
                return directoryInfo.Exists;
            }
            catch (System.IO.IOException e) {
                throw DotNetIOUtils.jSimpleExceptionFromDotNetIOException(e);
            }
        }

        public string getChildPath(string name) {
            StringBuilder childPath = new StringBuilder(directoryPath);

            int length = childPath.Length;
            if (length == 0)
                throw new Exception("FileSystemDirectory path is empty string");

            char lastCharacter = directoryPath[length - 1];
            if (lastCharacter != System.IO.Path.DirectorySeparatorChar &&
                lastCharacter != System.IO.Path.AltDirectorySeparatorChar)
                childPath.Append("/"); // JSimple always uses / as the separator

            childPath.Append(name);

            return childPath.ToString();
        }

        public override void visitChildren(DirectoryVisitor visitor) {
            DirectoryInfo directoryInfo = new DirectoryInfo(directoryPath);

            try {
                foreach (FileSystemInfo fileSystemInfo in directoryInfo.EnumerateFileSystemInfos()) {
                    if ((fileSystemInfo.Attributes & FileAttributes.Directory) == FileAttributes.Directory)
                        visitor.visit(new FileSystemDirectory(fileSystemInfo.FullName));
                    else visitor.visit(new FileSystemFile(this, fileSystemInfo.FullName));
                }
            }
            catch (System.IO.IOException e) {
                throw DotNetIOUtils.jSimpleExceptionFromDotNetIOException(e);
            }
        }

        public override void delete() {
            try {
                System.IO.Directory.Delete(directoryPath);
            }
            catch (System.IO.IOException e) {
                throw DotNetIOUtils.jSimpleExceptionFromDotNetIOException(e);
            }
        }

        public override void renameTo(string newName)
        {
            string destinationPath = getChildPath(newName);
            try {
                System.IO.Directory.Move(directoryPath, destinationPath);
            }
            catch (System.IO.IOException e) {
                throw DotNetIOUtils.jSimpleExceptionFromDotNetIOException(e);
            }
        }

        public override long LastModifiedTime {
            get {
                try {
                    return PlatformUtils.toMillisFromDateTime(System.IO.Directory.GetLastWriteTimeUtc(directoryPath));
                }
                catch (System.IO.IOException e) {
                    throw DotNetIOUtils.jSimpleExceptionFromDotNetIOException(e);
                }
            }

            set {
                try {
                    System.IO.Directory.SetLastWriteTimeUtc(directoryPath,
                        PlatformUtils.toDotNetDateTimeFromMillis(value));
                }
                catch (System.IO.IOException e) {
                    throw DotNetIOUtils.jSimpleExceptionFromDotNetIOException(e);
                }
            }
        }

        public override bool SetLastModifiedTimeSupported {
            get { return true; }
        }
    }
}