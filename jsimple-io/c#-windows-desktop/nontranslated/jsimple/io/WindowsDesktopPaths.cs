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

namespace jsimple.io
{
    public class WindowsDesktopPaths : Paths
    {
        private static readonly object lockObject = new Object();
        static volatile FileSystemDirectory applicationDataDirectory = null;

        /// <returns> the directory where the application should store its private data. </returns>
        public override Directory getApplicationDataDirectory()
        {
            if (applicationDataDirectory == null)
            {
                lock (lockObject)
                {
                    if (applicationDataDirectory == null)
                        applicationDataDirectory = new FileSystemDirectory("c:\\foo");
                }
            }
            return applicationDataDirectory;
        }

        /// <summary>
        /// Return the Directory object corresponding to the string serialized representation.  If the directory doesn't
        /// exist, it's implementation dependent whether this method throws a PathNotFoundException or a
        /// PathNotFoundException is thrown when the returned directory is used--different implementations do different
        /// things there.
        /// <p/>
        /// Not all implementations of Directory support serializing it as a string.  An exception is thrown if it's not
        /// supported.
        /// </summary>
        /// <param name="directoryPathString">
        /// @return </param>
        public override Directory getFileSystemDirectory(string directoryPathString)
        {
            return new FileSystemDirectory(directoryPathString);
        }
    }
}