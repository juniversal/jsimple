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

package jsimple.util;

/**
 * A TextualPath abstracts any path-like structure that can be represented as a list of strings.  It can be used for
 * paths in a file system, a URL path, an XPath path, etc.  Implementation wise, it's simply a list of 0 or move
 * strings.
 * <p>
 * Unlike the standard Java Path class: This isn't just for file system paths and "." and ".." have no special meaning.
 * A TextualPath has no concept of being absolute or relative; it's simplest to think about it as always being absolute
 * and toString represents it that way (with a leading "/"), but if the caller wants to treat it as a relative path it
 * can do that. Also, "/abc" and "/abc/" have the same representation in a TextualPath--they both are a list with one
 * element, "abc", which toString will return as "/abc".
 *
 * @author Bret Johnson
 * @since 8/16/13 9:55 PM
 */
public class TextualPath {
    private ArrayList<String> pathElements;

    /**
     * Create a TextualPath that's empty (that is, points to the root).
     */
    public TextualPath() {
        this.pathElements = new ArrayList<String>();
    }

    /**
     * Create a TextualPath containing a single path element.
     *
     * @param pathElement1
     */
    public TextualPath(String pathElement1) {
        this.pathElements = new ArrayList<String>(1);
        pathElements.add(pathElement1);
    }


    public TextualPath(String pathElement1, String pathElement2) {
        this.pathElements = new ArrayList<String>(2);
        pathElements.add(pathElement1);
        pathElements.add(pathElement2);
    }

    public TextualPath(String pathElement1, String pathElement2, String pathElement3) {
        this.pathElements = new ArrayList<String>(3);
        pathElements.add(pathElement1);
        pathElements.add(pathElement2);
        pathElements.add(pathElement2);
    }

    /**
     * Create a TextualPath that's the same as parent path, except with the new child added at the end.
     *
     * @param parent parent path
     * @param child  child element to add at end
     */
    public TextualPath(TextualPath parent, String child) {
        pathElements = new ArrayList<String>(parent.getLength() + 1);
        for (String pathElement : parent.getPathElements())
            pathElements.add(pathElement);
        pathElements.add(child);
    }

    @Override public boolean equals(Object o) {
        if (!(o instanceof TextualPath))
            return false;
        TextualPath that = (TextualPath) o;
        return pathElements.equals(that.pathElements);
    }

    @Override public int hashCode() {
        return pathElements.hashCode();
    }

    public void add(String pathElement) {
        pathElements.add(pathElement);
    }

    /**
     * See if this path starts with another path.
     *
     * @param prefixPath path that potentially is at beginning of this path
     * @return true if this the beginning elements of this path match prefixPath or if the two paths are the same
     */
    public boolean startsWith(TextualPath prefixPath) {
        int prefixLength = prefixPath.getLength();

        if (prefixLength > pathElements.size())
            return false;

        for (int i = 0; i < prefixLength; i++) {
            String pathElement = pathElements.get(i);
            String prefixPathElement = prefixPath.pathElements.get(i);

            if (!pathElement.equals(prefixPathElement))
                return false;
        }

        return true;
    }

    /**
     * Get the length of the path, the number of path elements contained in it.
     *
     * @return path length, which is >= 0
     */
    public int getLength() {
        return pathElements.size();
    }

    /**
     * Check if the path is empty, having no elements.  This can be thought of as pointing to the root of the tree.
     *
     * @return true if path has no elements
     */
    public boolean isEmpty() {
        return pathElements.size() == 0;
    }

    /**
     * Get the parent of this path, containing all elements except the last.  If the path is empty and thus has no
     * parent, an exception is thrown.
     *
     * @return parent of this path
     */
    public TextualPath getParent() {
        if (isEmpty())
            throw new ProgrammerError("Can't call getParent on an empty path");

        TextualPath newPath = new TextualPath();
        int newSize = pathElements.size() - 1;
        for (int i = 0; i < newSize; i++)
            newPath.add(pathElements.get(i));

        return newPath;
    }

    public ArrayList<String> getPathElements() {
        return pathElements;
    }

    public String getFirstElement() {
        if (pathElements.size() == 0)
            throw new ProgrammerError("Can't call getFirstElement on an empty path");
        return pathElements.get(0);
    }

    public String getLastElement() {
        int size = pathElements.size();
        if (size == 0)
            throw new ProgrammerError("Can't call getLastElement on an empty path");
        else return pathElements.get(size - 1);
    }

    /**
     * Get a human friendly string representation of the path, using / as the delimiter.  Note that no escaping is done
     * of special characters (space, slash, etc.), so this string often isn't appropriate for programmatic use.
     *
     * @return human friendly string representation of the path
     */
    @Override public String toString() {
        if (pathElements.size() == 0)
            return "/";
        else {
            StringBuilder buffer = new StringBuilder();

            for (String pathElement : pathElements) {
                buffer.append("/");
                buffer.append(pathElement);
            }
            return buffer.toString();
        }
    }
}
