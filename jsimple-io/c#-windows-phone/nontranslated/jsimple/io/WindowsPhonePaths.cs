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

        public override Directory getFileSystemDirectory(string directoryPathString)
        {
            throw new BasicException("Not implemented");
        }

    }
}