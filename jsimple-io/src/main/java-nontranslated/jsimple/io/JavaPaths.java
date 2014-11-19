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

package jsimple.io;

import org.jetbrains.annotations.Nullable;

/**
 * @author Bret Johnson
 * @since 3/15/13 10:52 PM
 */
public class JavaPaths extends Paths {
    /**
     * This method is defined to return the directory where the application should store its private data.  In Java,
     * there's no standard way to do exactly that so we return user.dir, which is the working directory (the current
     * directory) when the process was started.
     *
     * @return the directory where the application should store its private data
     */
    @Override public Directory getApplicationDataDirectory() {
        @Nullable String workingDirectory = System.getProperty("user.dir");
        if (workingDirectory == null)
            throw new IOException("user.dir is unset for some reason; working directory not specified by the JVM");

        return new FileSystemDirectory(workingDirectory);
    }

    @Override public Directory getFileSystemDirectory(String directoryPathString) {
        return new FileSystemDirectory(directoryPathString);
    }
}
