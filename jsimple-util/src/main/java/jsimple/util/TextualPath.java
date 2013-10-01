package jsimple.util;

import java.util.ArrayList;

/**
 * A TextualPath abstracts any path-like structure that can be represented as a list of strings.  It can be used for
 * paths in a file system, a URL path, an XPath path, etc.  Implementation wise, it's simply a list of 0 or move
 * strings.
 * <p/>
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
     * @param singlePathElement
     */
    public TextualPath(String singlePathElement) {
        this.pathElements = new ArrayList<String>(1);
        pathElements.set(0, singlePathElement);
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
        return Utils.arrayListsEqual(pathElements, that.pathElements);
    }

    @Override public int hashCode() {
        return Utils.arrayListHashCode(pathElements);
    }

    public void add(String pathElement) {
        pathElements.add(pathElement);
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
            throw new BasicException("Can't call getParent on an empty path");

        TextualPath newPath = new TextualPath();
        int newSize = pathElements.size() - 1;
        for (int i = 0; i < newSize; i++)
            newPath.add(pathElements.get(i));

        return newPath;
    }

    public ArrayList<String> getPathElements() {
        return pathElements;
    }

    public String getLastElement() {
        int size = pathElements.size();
        if (size == 0)
            throw new BasicException("Can't call getLastElement on an empty path");
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
