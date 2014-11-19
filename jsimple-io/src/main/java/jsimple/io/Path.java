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

/**
 * A Path is an abstraction over file system-like data structures.  A Directory path can contain other Directories and
 * Files.  The caller can get an input stream / output stream to read/write a File.
 *
 * @author Bret Johnson
 * @since 11/22/12 12:14 AM
 */
public abstract class Path {
    /**
     * Get the name of this file/directory--the last component of the path.
     *
     * @return name of this file/directory
     */
    public abstract String getName();

    /**
     * Get the string representation of the full path for this file/directory.
     *
     * @return string representation of path
     */
    public abstract String getPathString();

    @Override public String toString() {
        return getPathString();
    }

    /**
     * Get the extension (the text after the period) from the specified file/directory name.  The period itself isn't
     * returned, just the text after.  If there's no extension, the empty string is returned. s
     *
     * @param name file/directory name
     * @return extension part of the name (minus the period) or the empty string if there's no extension
     */
    public static String getNameExtension(String name) {
        int periodIndex = name.lastIndexOf('.');

        if (periodIndex == -1)
            return "";
        else return name.substring(periodIndex + 1);
    }
}
