/*
 * Copyright (c) 2012-2015, Microsoft Mobile
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
 * @since 11/22/12 12:14 AM
 */
public abstract class Directory extends Path {
    /**
     * Get a file under the directory, which may or may not already exist.
     *
     * @param name file name
     * @return File object, that's a child of this directory
     */
    public abstract File getFile(String name);

    /**
     * Get the child directory, which may or may not already exist.
     *
     * @param name directory name
     * @return Directory object, that's a child of this directory
     */
    public abstract Directory getDirectory(String name);

    /**
     * Get the child directory, creating it if it doesn't already exist.
     *
     * @param name directory name
     * @return Directory object, that's a child of this directory
     */
    public Directory createDirectory(String name) {
        Directory directory = getDirectory(name);
        directory.create();

        return directory;
    }

    /**
     * See if the directory exists.  If there's an error checking if the directory exists, this method throws an
     * exception when possible, though for some platform implementations it'll just return false if platform can't
     * distinguish not existing from there being an error checking.
     *
     * @return true if the file exists
     */
    public abstract boolean exists();

    /**
     * Create the directory, if it doesn't already exist.  All ancestor directories are created as well.  If the
     * directory already exists, this method does nothing.
     */
    public abstract void create();

    /**
     * Rename this directory, giving it a new name in the same parent directory.  If a directory with the specified name
     * already exists, an exception is thrown.  If/when there's the need to move a Directory to a different directory,
     * we'll add separate support for that.
     *
     * @param newName
     */
    public abstract void renameTo(String newName);

    /**
     * Visit the child elements of this path--basically list the files and subdirectories of a directory, calling the
     * visitor for each.  Just direct children are listed, not all descendants; callers can call this method recursively
     * if they want to visit all descendants.
     */
    public abstract void visitChildren(@Nullable FileVisitor fileVisitor, @Nullable DirectoryVisitor directoryVisitor,
                                       @Nullable VisitFailed visitFailed);

    /**
     * Visit the child elements of this path--basically list the files and subdirectories of a directory, calling the
     * visitor for each.  Just direct children are listed, not all descendants; callers can call this method recursively
     * if they want to visit all descendants.
     */
    public void visitChildren(@Nullable FileVisitor fileVisitor, @Nullable DirectoryVisitor directoryVisitor) {
        visitChildren(fileVisitor, directoryVisitor, null);
    }

    /**
     * Delete this directory.  The directory must be empty; if it isn't the results are undefined--for some
     * implementations it will fail and for others delete the directory and its contents.
     */
    public abstract void delete();

    public abstract long getLastModifiedTime();

    /**
     * Set the last modified time for the directory, if supported by the platform (that is, if
     * isSetLastModifiedTimeSupported() returns true).  If not supported by the platform, this method does nothing.  If
     * supported by the platform but an error occurs when changing the last modified time (e.g. the caller doesn't have
     * rights to change it), then this method throws an exception.
     *
     * @param time
     */
    public abstract void setLastModifiedTime(long time);

    public abstract boolean isSetLastModifiedTimeSupported();

    /**
     * Delete the contents of this directory, recursi vely.
     */
    public void deleteContents() {
        visitChildren(
                new FileVisitor() {
                    @Override public boolean visit(File file) {
                        file.delete();
                        return true;
                    }
                },
                new DirectoryVisitor() {
                    public boolean visit(Directory directory) {
                        directory.deleteContents();
                        directory.delete();
                        return true;
                    }
                });
    }

    /**
     * Check if this directory has any contents.
     *
     * @return true if the directory is empty, containing no files or subdirectories
     */
    public boolean isEmpty() {
        final boolean[] foundSomething = new boolean[1];
        foundSomething[0] = false;

        visitChildren(
                new FileVisitor() {
                    @Override public boolean visit(File file) {
                        foundSomething[0] = true;
                        return false;
                    }
                },
                new DirectoryVisitor() {
                    @Override public boolean visit(Directory directory) {
                        foundSomething[0] = true;
                        return false;
                    }
                });

        return !foundSomething[0];
    }
}
